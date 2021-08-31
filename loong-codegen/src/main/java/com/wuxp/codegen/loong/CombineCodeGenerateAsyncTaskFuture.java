package com.wuxp.codegen.loong;

import com.wuxp.codegen.core.CodeGenerateAsyncTaskFuture;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@Slf4j
public class CombineCodeGenerateAsyncTaskFuture implements CodeGenerateAsyncTaskFuture {

    private static final CombineCodeGenerateAsyncTaskFuture INSTANCE = new CombineCodeGenerateAsyncTaskFuture();

    private final List<CodeGenerateAsyncTaskFuture> taskFutures;

    public CombineCodeGenerateAsyncTaskFuture() {
        this.taskFutures = new ArrayList<>();
    }

    public static CombineCodeGenerateAsyncTaskFuture getInstance() {
        return INSTANCE;
    }

    @Override
    public CompletableFuture<Void> future() {
        List<CompletableFuture<Void>> futures = this.taskFutures.stream().map(CodeGenerateAsyncTaskFuture::future).collect(Collectors.toList());
        return CompletableFuture.runAsync(() -> waitCompleted(futures));
    }

    private void waitCompleted(List<CompletableFuture<Void>> futures) {
        for (CompletableFuture<Void> future : futures) {
            try {
                future.get();
            } catch (InterruptedException exception) {
                log.error("代码生成后置处理发生线程中断异常：{}", exception.getMessage(), exception);
                Thread.currentThread().interrupt();
            } catch (ExecutionException exception) {
                log.error("代码生成后置处理失败：{}", exception.getMessage(), exception);
            }
        }
    }

    public void addFuture(CodeGenerateAsyncTaskFuture taskFuture) {
        this.taskFutures.add(taskFuture);
    }
}
