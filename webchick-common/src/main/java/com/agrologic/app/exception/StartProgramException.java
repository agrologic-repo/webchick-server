package com.agrologic.app.exception;

/**
 * Throws while starting application .
 *
 * @author Valery Manakhimov
 */
public class StartProgramException extends GeneralException {

    public StartProgramException(String message) {
        super("Fatal error \n" + message);
    }
}



