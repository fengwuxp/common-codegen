package com.wuxp.codegen.server.task;


import com.wuxp.codegen.server.config.CodegenConfig;
import com.wuxp.codegen.server.config.SourcecodeRepositoryPropertiesConfig;
import com.wuxp.codegen.server.scope.CodegenTaskContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;

import java.io.*;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


@DataJpaTest(properties = {"spring.profiles.active=test"})
@ContextConfiguration(classes = {ScmCodegenTaskProviderTest.TaskConfig.class, SourcecodeRepositoryPropertiesConfig.class, CodegenConfig.class})
@EnableJpaRepositories(basePackages = {"com.wuxp.codegen.server.repositories"})
@EntityScan("com.wuxp.codegen.server.entities")
@EnableJpaAuditing(auditorAwareRef = "codegenJpaAuditorAware")
@TestPropertySource("classpath:application-test.properties")
@ConfigurationPropertiesScan("com.wuxp.codegen")
@Slf4j
class ScmCodegenTaskProviderTest {

    @Autowired
    private CodegenTaskProvider scmCodegenTaskProvider;

    /**
     * 该测试用例仅支持手动调用
     * @throws Exception
     */
    @Test
    @Disabled
    void testCreate()throws Exception {

        CodegenTaskContextHolder.setScmCode("default");
        String mainBranchName ="feature/codegen-restful-support"; //SourcecodeRepositoryType.GIT.getMainBranchName();
        String taskId = scmCodegenTaskProvider.create("common-codegen", mainBranchName);
        ScheduledExecutorService executorService = new ScheduledThreadPoolExecutor(1);
        CountDownLatch countDownLatch=new CountDownLatch(1);
        executorService.scheduleAtFixedRate(() -> {
            Optional<CodegenTaskProgressInfo> optional = scmCodegenTaskProvider.getTaskProgress(taskId);
            if (optional.isPresent()){
                CodegenTaskProgressInfo codegenTaskProgressInfo = optional.get();
                if (CodegenTaskStatus.SUCCESS.equals(codegenTaskProgressInfo.getStatus())){
                    log.info("任务执行成功：{}",codegenTaskProgressInfo);
                    countDownLatch.countDown();
                }else if (CodegenTaskStatus.FAILURE.equals(codegenTaskProgressInfo.getStatus())){
                    log.error("任务执行失败：{}",codegenTaskProgressInfo);
                    countDownLatch.countDown();
                }
            }else {
                log.error("任务不存在：{}",taskId);
                countDownLatch.countDown();
            }
        },200,3000, TimeUnit.MILLISECONDS);
        countDownLatch.await();
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
            log.info("命令执行结果：{}",result);
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
