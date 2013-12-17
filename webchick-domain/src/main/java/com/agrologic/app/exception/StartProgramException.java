package com.agrologic.app.exception;

/**
 *
 */
public class StartProgramException extends Exception {
    public StartProgramException(String message) {
        super("Fatal error \n" + message);
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }
}



