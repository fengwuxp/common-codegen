package com.wuxp.codegen.server.task;


import com.wuxp.codegen.server.config.LoongCodegenConfig;
import com.wuxp.codegen.server.config.LoongCodegenProperties;
import com.wuxp.codegen.server.scope.CodegenTaskContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;

import java.io.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static com.wuxp.codegen.server.constant.VcsConstants.DEFAULT_REPOSITORY_NANE;


@ContextConfiguration(classes = {SourceCodeManagerTaskServiceTest.TaskConfig.class, LoongCodegenProperties.class, LoongCodegenConfig.class})
@EnableJpaRepositories(basePackages = {"com.wuxp.codegen.server.repositories"})
@EntityScan("com.wuxp.codegen.server.entities")
@EnableJpaAuditing(auditorAwareRef = "codegenJpaAuditorAware")
@TestPropertySource("classpath:application-test.properties")
@ConfigurationPropertiesScan("com.wuxp.codegen")
@Slf4j
class SourceCodeManagerTaskServiceTest {

    @Autowired
    private CodegenTaskService codegenTaskService;

    /**
     * 该测试用例仅支持手动调用
     */
    @Test
//    @EnabledOnOs({OS.MAC})
    @Disabled
    void testCreate() throws Exception {
        CodegenTaskContextHolder.setSourceCodeRepositoryName(DEFAULT_REPOSITORY_NANE);
        String mainBranchName = "master";
        String taskId = codegenTaskService.create("common-codegen", mainBranchName);
        Assertions.assertNotNull(taskId);
        ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        CodegenTaskStatus status = waitTaskStatus(scheduledExecutorService, taskId);
        Assertions.assertNotNull(status);
        codegenTaskService.release(taskId);

    }

    private CodegenTaskStatus waitTaskStatus(ScheduledExecutorService scheduledExecutorService, String taskId) throws Exception {
        return scheduledExecutorService
                .schedule(() -> {
                    CodegenTaskInfo task = codegenTaskService.getTask(taskId);
                    CodegenTaskStatus taskStatus = task.getStatus();
                    log.info("任务执行状态：{}", taskStatus);
                    if (CodegenTaskStatus.SUCCESS.equals(taskStatus) || CodegenTaskStatus.FAILURE.equals(taskStatus)) {
                        return taskStatus;
                    }
                    return waitTaskStatus(scheduledExecutorService, taskId);
                }, 1000, TimeUnit.MILLISECONDS)
                .get();
    }

    @Configuration
    public static class TaskConfig {

    }

    private static void runCmd(String cmd) {
        java.lang.Process process = null;
        try {
            process = Runtime.getRuntime().exec(cmd, null, new File(System.getProperty("user.dir")));
            ByteArrayOutputStream resultOutStream = new ByteArrayOutputStream();
            InputStream errorInStream = new BufferedInputStream(process.getErrorStream());
            InputStream processInStream = new BufferedInputStream(process.getInputStream());
            int num = 0;
            byte[] bs = new byte[1024];
            while ((num = errorInStream.read(bs)) != -1) {
                resultOutStream.write(bs, 0, num);
            }
            while ((num = processInStream.read(bs)) != -1) {
                resultOutStream.write(bs, 0, num);
            }
            String result = resultOutStream.toString();
            log.info("命令执行结果：{}", result);
            errorInStream.close();
            processInStream.close();
            resultOutStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (process != null) process.destroy();
        }
    }
}
