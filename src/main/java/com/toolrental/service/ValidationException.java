package com.toolrental.service;

public class ValidationException extends Exception {

    public ValidationException(String errorMessage){
        super(errorMessage);
    }
}
