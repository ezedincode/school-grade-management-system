package com.ezedin.auth_service.exception;

public class UserNameExistException extends Exception{

    public UserNameExistException(String message) {
        super(message);
    }
}
