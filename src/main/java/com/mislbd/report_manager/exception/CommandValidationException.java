package com.mislbd.report_manager.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class CommandValidationException extends RuntimeException {
    public CommandValidationException(String message) {
        super(message);
    }
}