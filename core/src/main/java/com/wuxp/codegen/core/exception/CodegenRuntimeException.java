package com.wuxp.codegen.core.exception;

/**
 * @author wuxp
 */
public class CodegenRuntimeException extends RuntimeException {

    public CodegenRuntimeException() {
    }

    public CodegenRuntimeException(String message) {
        super(message);
    }

    public CodegenRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    public CodegenRuntimeException(Throwable cause) {
        super(cause);
    }

    public CodegenRuntimeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
