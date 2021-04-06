package com.jdxiang.shareMusicApi.handle;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.jdxiang.shareMusicApi.exception.NotAuthenticationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
public class ExceptionHandle {
    @ExceptionHandler(value = {DataIntegrityViolationException.class})
    public ResponseEntity<JsonErrorResult> associateDeleteExceptionHandler(final HttpServletRequest httpServletRequest, final Exception exception) {
        return new ResponseEntity<>(new JsonErrorResult(exception.getMessage()), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(value = {NotAuthenticationException.class})
    public ResponseEntity<JsonErrorResult> notAuthenticationExceptionHandler(final HttpServletRequest httpServletRequest, final Exception exception) {
        return new ResponseEntity<>(new JsonErrorResult(exception.getMessage()), HttpStatus.UNAUTHORIZED);
    }
}

class JsonErrorResult {
    @JsonProperty
    String errorMessage;

    JsonErrorResult(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}

