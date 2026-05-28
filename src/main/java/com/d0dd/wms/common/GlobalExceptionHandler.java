package com.d0dd.wms.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(Exception.class)
    public Result<String> handleException(Exception e) {
        log.error(e.getMessage(), e);
        String msg = e.getMessage() != null ? e.getMessage() : e.getClass().getSimpleName();
        return Result.error(msg);
    }

    @ExceptionHandler(RuntimeException.class)
    public Result<String> handleRuntimeException(RuntimeException e) {
        log.error(e.getMessage(), e);
        String msg = e.getMessage() != null ? e.getMessage() : e.getClass().getSimpleName();
        return Result.error(msg);
    }
}
