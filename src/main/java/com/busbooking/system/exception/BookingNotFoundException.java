package com.busbooking.system.exception;

public class BookingNotFoundException
        extends RuntimeException {

    public BookingNotFoundException(
            String message
    ) {
        super(message);
    }
}