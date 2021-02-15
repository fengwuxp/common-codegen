package com.wuxp.codegen.format;

import com.wuxp.codegen.core.CodeFormatter;
import com.wuxp.codegen.core.config.CodegenConfig;
import com.wuxp.codegen.core.config.CodegenConfigHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.concurrent.CustomizableThreadFactory;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * 通过调用命令行去格式化代码
 *
 * @author wuxp
 */
@Slf4j
public abstract class AbstractCommandCodeFormatter implements CodeFormatter {

    /**
     * 是否支持 prettier命令行工具
     */
    protected final boolean support;

    /**
     * 用于执行命令的 Executor
     */
    protected ThreadPoolExecutor executor;

    private final List<Future<?>> futures;

    protected AbstractCommandCodeFormatter() {
        this.support = this.preCheckEnv();
        this.init();
        this.futures = new ArrayList<>(512);
    }

    @Override
    public void format(String filepath) {
        if (!support) {
            return;
        }
        if (executor == null) {
            runCommand(genFormatCommand(filepath));
        } else {
            CodegenConfig codegenConfig = CodegenConfigHolder.getConfig();
            Future<?> future = executor.submit(() -> {
                // keep config
                CodegenConfigHolder.setConfig(codegenConfig);
                runCommand(genFormatCommand(filepath));
                CodegenConfigHolder.clear();
            });
            this.futures.add(future);
        }
    }

    @Override
    public String format(String sourcecode, Charset charsetName) {
        return sourcecode;
    }

    /**
     * 等待所有的异步任务结束
     */
    public void park() {
        if (futures == null) {
            return;
        }
        int count = 0;
        try {
            for (Future<?> future : futures) {
                future.get();
                count++;
            }
        } catch (InterruptedException exception) {
            log.error("调用命令行格式代码失败：{}", exception.getMessage(), exception);
            Thread.currentThread().interrupt();
        } catch (ExecutionException exception) {
            log.error("调用命令行格式代码发生线程中断异常：{}", exception.getMessage(), exception);
        }
        if (log.isInfoEnabled()) {
            log.info("共执行的异步任务数量：{}", count);
        }

    }

    /**
     * 生成用于format code style的命令
     *
     * @param filepath 文件路径
     * @return 命令
     */
    protected abstract String genFormatCommand(String filepath);

    protected boolean runCommand(String cmd) {
        return runCommand(cmd, null, null);
    }

    protected boolean runCommand(String cmd, String[] args, String delimiter) {
        String command = genCommand(cmd, args, delimiter);
        if (log.isDebugEnabled()) {
            log.debug("执行命令：{}", command);
        }
        Process exec;
        try {
            exec = Runtime.getRuntime().exec(command);
            int i = exec.waitFor();
            if (log.isTraceEnabled()) {
                log.trace("调用命令行格式代码：{}", i);
            }
            exec.destroy();
            return true;
        } catch (IOException exception) {
            log.error("调用命令行格式代码失败：{}", exception.getMessage(), exception);
        } catch (InterruptedException exception) {
            log.error("调用命令行格式代码发生线程中断异常：{}", exception.getMessage(), exception);
            Thread.currentThread().interrupt();
        }
        return false;
    }

    protected String genCommand(String cmd, String[] args, String delimiter) {
        if (args != null && args.length > 0) {
            cmd += " " + String.join(delimiter, args);
        }
        return cmd;
    }

    protected boolean preCheckEnv() {
        return false;
    }

    protected void init() {
        this.executor = new ThreadPoolExecutor(1, 2, 1, TimeUnit.MINUTES,
                new ArrayBlockingQueue<>(1024),
                new CustomizableThreadFactory("command_code_formatter"),
                new ThreadPoolExecutor.CallerRunsPolicy());
    }


}
