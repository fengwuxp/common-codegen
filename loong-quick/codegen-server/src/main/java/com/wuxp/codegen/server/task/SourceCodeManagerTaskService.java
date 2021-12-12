package com.wuxp.codegen.server.task;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.RemovalListener;
import com.wuxp.codegen.core.util.CodegenFileUtils;
import com.wuxp.codegen.server.plugins.CodegenPluginExecuteStrategy;
import com.wuxp.codegen.server.scope.CodegenTaskContextHolder;
import com.wuxp.codegen.server.vcs.SourcecodeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.util.concurrent.TimeUnit;


/**
 * @author wuxp
 */
@Slf4j
public class SourceCodeManagerTaskService implements CodegenTaskService {

    /**
     * 最大重试次数
     */
    private static final int MAX_RETRIES = 5;

    private final Cache<String, CodegenTaskInfo> taskProgressCaches;

    private final SourcecodeRepository sourcecodeRepository;

    private final CodegenPluginExecuteStrategy codegenPluginExecuteStrategy;

    private final AsyncTaskExecutor taskExecutor;

    public SourceCodeManagerTaskService(SourcecodeRepository sourcecodeRepository,
                                        CodegenPluginExecuteStrategy codegenPluginExecuteStrategy, AsyncTaskExecutor taskExecutor) {
        this.sourcecodeRepository = sourcecodeRepository;
        this.codegenPluginExecuteStrategy = codegenPluginExecuteStrategy;
        this.taskProgressCaches = Caffeine.newBuilder()
                .weakKeys()
                .weakValues()
                // 设置最后一次写入或访问后经过固定时间过期
                .expireAfterAccess(10, TimeUnit.MINUTES)
                // 初始的缓存空间大小
                .initialCapacity(64)
                // 缓存的最大条数
                .maximumSize(256)
                .removalListener((RemovalListener<String, CodegenTaskInfo>) (key, value, cause) -> {
                    if (log.isInfoEnabled()) {
                        log.info("remove task={},value={},cause={}", key, value, cause);
                    }
                    //  尝试删除任务目录
                    tryDeleteLocalRepository(value);
                })
                .build();
        this.taskExecutor = taskExecutor;
    }

    @Override
    public String create(String projectName, String branch) {
        String projectBranch = getBranchOrDefault(branch);
        String repositoryCode = CodegenTaskContextHolder.getSourceCodeRepositoryName();
        String taskId = generateTaskId(projectName, projectBranch, repositoryCode);
        CodegenTaskInfo taskInfo = taskProgressCaches.get(taskId, key -> createCodegenTask(projectName, projectBranch, repositoryCode, key));
        Assert.notNull(taskInfo, "create codegen task failure,taskId = " + taskId);
        taskInfo.increase();
        return taskInfo.getTaskId();
    }

    private String getBranchOrDefault(String branch) {
        if (StringUtils.hasText(branch)) {
            return branch;
        }
        return sourcecodeRepository.getMasterBranchName();
    }

    private String generateTaskId(String projectName, String branch, String repositoryCode) {
        return String.format("%s/%s/%s", repositoryCode, projectName, branch);
    }

    private CodegenTaskInfo createCodegenTask(String projectName, String branch, String repositoryCode, String taskId) {
        CodegenTaskInfo taskProgressInfo = new CodegenTaskInfo(taskId, projectName, branch, repositoryCode);
        this.submitTask(projectName, branch, taskProgressInfo);
        return taskProgressInfo;
    }

    @Override
    public CodegenTaskInfo getTask(String taskId) {
        CodegenTaskInfo taskInfo = taskProgressCaches.getIfPresent(taskId);
        Assert.notNull(taskInfo, "get codegen task failure,taskId=" + taskId);
        return taskInfo;
    }

    @Override
    public void release(String taskId) {
        CodegenTaskInfo taskInfo = taskProgressCaches.getIfPresent(taskId);
        if (taskInfo == null) {
            return;
        }
        taskInfo.release();
        int taskReferenceCount = taskInfo.getTaskReferenceCount();
        if (taskReferenceCount <= 0) {
            // 移除任务信息
            taskProgressCaches.invalidate(taskId);
            // 删除代码仓库
            sourcecodeRepository.deleteLocalRepository(taskInfo.getProjectName(), taskInfo.getBranch());
        }
    }

    private void submitTask(String projectName, String branch, CodegenTaskInfo taskProgressInfo) {
        if (taskProgressInfo.getRetries() >= MAX_RETRIES) {
            log.error("项目：{}，分支：{} 任务：{} 已达到最大重试次数，任务失败原因：{}", projectName, branch, taskProgressInfo.getTaskId(),
                    taskProgressInfo.getExceptionCause());
            taskProgressInfo.setStatus(CodegenTaskStatus.FAILURE);
            return;
        }
        taskExecutor.submit(() -> {
            taskProgressInfo.setStatus(downloadCode(projectName, branch, taskProgressInfo));
            String projectFilepath = sourcecodeRepository.getLocalDirectory(projectName, branch);
            taskProgressInfo.setStatus(executeCodegenPlugin(taskProgressInfo, projectFilepath));
            if (!taskProgressInfo.getStatus().isCompleted()) {
                // 任务为完成
                submitTask(projectName, branch, taskProgressInfo);
            }
        });
    }

    private CodegenTaskStatus downloadCode(String projectName, String branch, CodegenTaskInfo taskProgressInfo) {
        if (CodegenTaskStatus.CLONE_CODE.equals(taskProgressInfo.getStatus())) {
            try {
                sourcecodeRepository.checkout(projectName, branch);
                return CodegenTaskStatus.CODEGEN_PROCESSING;
            } catch (Exception exception) {
                taskProgressInfo.setLastException(exception);
            }
        }
        return taskProgressInfo.getStatus();
    }

    private CodegenTaskStatus executeCodegenPlugin(CodegenTaskInfo taskProgressInfo, String projectFilepath) {
        if (CodegenTaskStatus.CODEGEN_PROCESSING.equals(taskProgressInfo.getStatus())) {
            try {
                codegenPluginExecuteStrategy.executeCodegenPlugin(projectFilepath, null, null);
                return CodegenTaskStatus.SUCCESS;
            } catch (Exception exception) {
                taskProgressInfo.setLastException(exception);
                // 插件执行失败，不做重试
                taskProgressInfo.setStatus(CodegenTaskStatus.FAILURE);
            }
        }
        return taskProgressInfo.getStatus();
    }


    /**
     * 尝试删除本地仓库
     *
     * @param info 任务信息
     */
    private void tryDeleteLocalRepository(CodegenTaskInfo info) {
        log.info("try delete local repository task = {}", info);
        String localDirectory = sourcecodeRepository.getLocalDirectory(info.getProjectName(), info.getBranch());
        CodegenFileUtils.deleteDirectory(localDirectory);
    }
}
