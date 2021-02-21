package com.wuxp.codegen.server.task;

/**
 * @author wuxp
 */
public class CodegenTaskException extends RuntimeException{

    public CodegenTaskException() {
    }

    public CodegenTaskException(String message) {
        super(message);
    }

    public CodegenTaskException(String message, Throwable cause) {
        super(message, cause);
    }

    public CodegenTaskException(Throwable cause) {
        super(cause);
    }

    public CodegenTaskException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
