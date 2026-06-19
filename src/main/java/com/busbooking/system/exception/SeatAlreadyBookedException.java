package com.busbooking.system.exception;

public class SeatAlreadyBookedException
        extends RuntimeException {

    public SeatAlreadyBookedException(
            String message
    ) {
        super(message);
    }
}