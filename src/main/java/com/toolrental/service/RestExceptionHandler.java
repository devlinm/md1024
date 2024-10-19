package com.toolrental.service;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Map;

/**
 * Make {@link ValidationException} rendered as a json error message with a http status code of {@link HttpStatus#PRECONDITION_FAILED}
 */
@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = {ValidationException.class, MethodArgumentTypeMismatchException.class})
    public ResponseEntity<Object> handleConflict(Exception ex, WebRequest request) {
        String msg = ex.getMessage();
        if (ex instanceof MethodArgumentTypeMismatchException) {
            msg = "Bad value for '" + ((MethodArgumentTypeMismatchException) ex).getName() + "': " + msg;
        };
        Map<String, String> jsonBody = Map.of("error", msg);
        return handleExceptionInternal(ex, jsonBody, new HttpHeaders(), HttpStatus.PRECONDITION_FAILED, request);
    }
}
