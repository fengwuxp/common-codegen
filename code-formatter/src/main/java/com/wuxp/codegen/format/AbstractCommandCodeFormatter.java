package com.wuxp.codegen.format;

import com.wuxp.codegen.core.CodeFormatter;
import com.wuxp.codegen.core.config.CodegenConfig;
import com.wuxp.codegen.core.config.CodegenConfigHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.concurrent.CustomizableThreadFactory;
import org.springframework.util.CollectionUtils;

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
     * 执行命令的最大超时时间，单位毫秒
     */
    private static final int MAX_EXECUTE_COMMAND_TIMEOUT_MILLISECONDS = 3500;

    /**
     * 是否启用 formatter
     */
    protected final boolean enabled;

    /**
     * 用于执行命令的 Executor
     */
    protected ThreadPoolExecutor executor;

    private final List<CompletableFuture<?>> futureTasks;

    protected AbstractCommandCodeFormatter() {
        this.enabled = this.preCheckEnv();
        this.futureTasks = new ArrayList<>(512);
        this.init();
    }

    @Override
    public CompletableFuture<Void> asyncFormat(String filepath) {
        if (!enabled) {
            return CompletableFuture.completedFuture(null);
        }
        if (executor == null) {
            runCommand(genFormatCommand(filepath));
            return CompletableFuture.completedFuture(null);
        } else {
            Future<?> future = asyncRunCommand(filepath, CodegenConfigHolder.getConfig());
            return CompletableFuture.runAsync(waitFutureResult(future));
        }
    }

    private Future<?> asyncRunCommand(String filepath, CodegenConfig codegenConfig) {
        return executor.submit(() -> {
            // keep config
            CodegenConfigHolder.setConfig(codegenConfig);
            runCommand(genFormatCommand(filepath));
            CodegenConfigHolder.clear();
        });
    }

    private Runnable waitFutureResult(Future<?> future) {
        return () -> {
            try {
                future.get();
            } catch (InterruptedException exception) {
                log.error("调用命令行格式代码发生线程中断异常：{}", exception.getMessage(), exception);
                Thread.currentThread().interrupt();
            } catch (ExecutionException exception) {
                log.error("调用命令行格式代码失败：{}", exception.getMessage(), exception);
            }
        };
    }

    @Override
    public void format(String filepath) {
        futureTasks.add(asyncFormat(filepath));
    }

    @Override
    public String format(String sourcecode, Charset charsetName) {
        return sourcecode;
    }

    /**
     * 等待所有的异步任务结束
     */
    @Override
    public void waitTaskCompleted() {
        if (CollectionUtils.isEmpty(futureTasks)) {
            return;
        }
        int taskTotal = syncCountFutureTaskTotal();
        if (log.isInfoEnabled()) {
            log.info("共执行的异步任务数量：{}", taskTotal);
        }
        futureTasks.clear();

    }

    private int syncCountFutureTaskTotal() {
        int count = 0;
        for (CompletableFuture<?> future : futureTasks) {
            waitFutureResult(future).run();
            count++;
        }
        return count;
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
        try {
            executeCommand(command);
            return true;
        } catch (IOException exception) {
            log.error("调用命令行格式代码失败：{}", exception.getMessage(), exception);
        } catch (InterruptedException exception) {
            log.error("调用命令行格式代码发生线程中断异常：{}", exception.getMessage(), exception);
            Thread.currentThread().interrupt();
        }
        return false;
    }

    private void executeCommand(String command) throws IOException, InterruptedException {
        Process exec = Runtime.getRuntime().exec(command);
        // 最多等待5秒钟
        boolean success = exec.waitFor(MAX_EXECUTE_COMMAND_TIMEOUT_MILLISECONDS, TimeUnit.MILLISECONDS);
        if (log.isTraceEnabled()) {
            log.trace("调用命令行格式代码：{}", success);
        }
        exec.destroy();
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
