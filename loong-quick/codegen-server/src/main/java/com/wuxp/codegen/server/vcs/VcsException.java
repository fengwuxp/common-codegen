package com.wuxp.codegen.server.vcs;

/**
 * @author wuxp
 */
public class VcsException extends RuntimeException {

    public VcsException() {
    }

    public VcsException(String message) {
        super(message);
    }

    public VcsException(String message, Throwable cause) {
        super(message, cause);
    }

    public VcsException(Throwable cause) {
        super(cause);
    }

    public VcsException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
