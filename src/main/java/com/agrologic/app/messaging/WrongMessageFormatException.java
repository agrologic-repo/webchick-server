package com.agrologic.app.messaging;

public class WrongMessageFormatException extends Exception {
    public WrongMessageFormatException(String message) {
        super(message);
    }
}
