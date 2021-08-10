package com.wuxp.codegen.core;

import java.util.concurrent.CompletableFuture;

/**
 * @author wuxp
 */
public interface CodeGenerateAsyncTaskFuture {

    CompletableFuture<Void> future();
}
