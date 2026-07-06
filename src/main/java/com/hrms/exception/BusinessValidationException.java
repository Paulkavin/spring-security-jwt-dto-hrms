package com.hrms.exception;

public class BusinessValidationException extends RuntimeException{

    public BusinessValidationException(String message){
        super(message);
    }
    
}

/**
 * By explicitly writing super(message);, you are telling Java: *"Don't use the default blank parent constructor. Use the specific parent constructor (RuntimeException(String message))
 * that knows how to store this error string inside the exception's memory
 */