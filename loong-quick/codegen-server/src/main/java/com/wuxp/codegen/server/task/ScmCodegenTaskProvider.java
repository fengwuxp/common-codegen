package com.wuxp.codegen.server.task;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.RemovalListener;
import com.wuxp.codegen.server.plugins.CodegenPluginExecuteStrategy;
import com.wuxp.codegen.server.scope.CodegenTaskContextHolder;
import com.wuxp.codegen.server.vcs.SourcecodeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.scheduling.annotation.Async;

import java.util.Optional;
import java.util.concurrent.TimeUnit;


/**
 * @author wuxp
 */
@Slf4j
public class ScmCodegenTaskProvider implements CodegenTaskProvider {


    private final Cache<String, CodegenTaskProgressInfo> taskProgressCache;

    private final SourcecodeRepository sourcecodeRepository;

    private final CodegenPluginExecuteStrategy codegenPluginExecuteStrategy;

    private final AsyncTaskExecutor taskExecutor;

    public ScmCodegenTaskProvider(SourcecodeRepository sourcecodeRepository,
                                  CodegenPluginExecuteStrategy codegenPluginExecuteStrategy,
                                  @Qualifier(value = "codegenTaskExecutor") AsyncTaskExecutor taskExecutor) {
        this.sourcecodeRepository = sourcecodeRepository;
        this.codegenPluginExecuteStrategy = codegenPluginExecuteStrategy;
        this.taskProgressCache = Caffeine.newBuilder()
                .weakKeys()
                .weakValues()
                // 设置最后一次写入或访问后经过固定时间过期
                .expireAfterAccess(10, TimeUnit.MINUTES)
                // 初始的缓存空间大小
                .initialCapacity(64)
                // 缓存的最大条数
                .maximumSize(256)
                .removalListener((RemovalListener<String, CodegenTaskProgressInfo>) (key, value, cause) -> {
                    if (log.isInfoEnabled()) {
                        log.info("remove task={},value={},cause={}", key, value, cause);
                    }
                    //  尝试删除任务目录
                    tryDeleteLocalRepository(value);
                })
                .build();
        this.taskExecutor = taskExecutor;
    }

    @Async
    @Override
    public String create(String projectName, String branch) {
        Optional<String> optional = CodegenTaskContextHolder.getScmCode();
        if (!optional.isPresent()) {
            throw new CodegenTaskException("获取scm code 失败");
        }
        String repositoryCode = optional.get();
        String taskId = String.format("%s/%s/%s", repositoryCode, projectName, branch);
        CodegenTaskProgressInfo codegenTaskProgressInfo = taskProgressCache.get(taskId, key -> {
            CodegenTaskProgressInfo taskProgressInfo = new CodegenTaskProgressInfo(taskId, projectName, branch, repositoryCode);
            this.submitTask(projectName, branch, taskProgressInfo);
            return taskProgressInfo;
        });
        codegenTaskProgressInfo.increase();
        return codegenTaskProgressInfo.getTaskId();
    }

    @Override
    public Optional<CodegenTaskProgressInfo> getTaskProgress(String taskId) {
        CodegenTaskProgressInfo codegenTaskProgressInfo = taskProgressCache.get(taskId, key -> null);
        if (codegenTaskProgressInfo == null) {
            // 任务不存在或已失效
            return Optional.empty();
        }
        return Optional.of(codegenTaskProgressInfo);
    }

    @Override
    public void release(String taskId) {
        Optional<CodegenTaskProgressInfo> optional = getTaskProgress(taskId);
        if (!optional.isPresent()) {
            return;
        }
        CodegenTaskProgressInfo codegenTaskProgressInfo = optional.get();
        synchronized (codegenTaskProgressInfo.getTaskId()) {
            codegenTaskProgressInfo.release();
        }
        int taskReferenceCount = codegenTaskProgressInfo.getTaskReferenceCount();
        if (taskReferenceCount == 0) {
            // 移除任务信息
            taskProgressCache.invalidate(taskId);
        }
    }

    private void submitTask(String projectName, String branch, CodegenTaskProgressInfo taskProgressInfo) {
        if (taskProgressInfo.getRetries() >= 5) {
            log.warn("项目：{}，分支：{}任务：{}已达到最大重试次数", projectName, branch, taskProgressInfo);
            taskProgressInfo.setStatus(CodegenTaskStatus.FAILURE);
            return;
        }
        taskExecutor.submit(() -> {
            String projectFilepath = null;
            if (CodegenTaskStatus.CLONE_CODE.equals(taskProgressInfo.getStatus())) {
                try {
                    projectFilepath = sourcecodeRepository.download(projectName, branch);
                } catch (Exception exception) {
                    taskProgressInfo.setLastException(exception);
                    submitTask(projectName, branch, taskProgressInfo);
                    return;
                }
            }
            taskProgressInfo.setStatus(CodegenTaskStatus.CODEGEN_PROCESSING);
            if (CodegenTaskStatus.CODEGEN_PROCESSING.equals(taskProgressInfo.getStatus())) {
                try {
                    codegenPluginExecuteStrategy.executeCodegenPlugin(projectFilepath, null, null);
                } catch (Exception exception) {
                    taskProgressInfo.setLastException(exception);
                    submitTask(projectName, branch, taskProgressInfo);
                    return;
                }
            }
            taskProgressInfo.setStatus(CodegenTaskStatus.SUCCESS);
        });
    }


    /**
     * 尝试删除本地仓库
     *
     * @param info 任务信息
     */
    private void tryDeleteLocalRepository(CodegenTaskProgressInfo info) {

    }
}
