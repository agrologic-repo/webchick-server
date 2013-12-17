package com.agrologic.app.exception;

public class TimeoutException extends Exception {

    public static final String TIME_OUT_ERROR = "Receiving the response timed out ";

    public TimeoutException(String message) {
        super(message);
    }
}



