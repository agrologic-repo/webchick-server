package com.agrologic.app.exception;

public class TimeoutException extends GeneralException {

    public static final String TIME_OUT_ERROR = "Receiving the response timed out ";

    public TimeoutException() {
        this(TIME_OUT_ERROR);
    }

    public TimeoutException(String message) {
        super(message);
    }
}



