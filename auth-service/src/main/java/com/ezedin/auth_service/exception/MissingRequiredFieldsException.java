package com.ezedin.auth_service.exception;

public class MissingRequiredFieldsException extends Exception{
    public MissingRequiredFieldsException(String message) {
        super(message);
    }
}
