package com.busbooking.system.exception;

public class UserNotFoundException
        extends RuntimeException {

    public UserNotFoundException(
            String message
    ) {
        super(message);
    }
}