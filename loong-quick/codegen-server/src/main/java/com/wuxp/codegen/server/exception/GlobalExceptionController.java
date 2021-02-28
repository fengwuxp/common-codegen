package com.wuxp.codegen.server.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理
 *
 * @author wuxp
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionController {


    @ExceptionHandler(Throwable.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public void exception(Exception exception) {
        log.error("请求发生异常：{}", exception.getMessage(), exception);
    }
}
