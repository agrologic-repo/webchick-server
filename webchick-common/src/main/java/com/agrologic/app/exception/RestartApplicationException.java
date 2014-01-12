package com.agrologic.app.exception;

/**
 * Throws if application cannot be restarted .
 *
 * @author Valery Manakhimov
 */
public class RestartApplicationException extends Exception {

    public static final String CAN_NOT_RESTART_APPPLCIATION = "Can not restart appliciation \n. " +
            "%s was not found in classpath .";

    public RestartApplicationException(String s) {
        super(String.format(CAN_NOT_RESTART_APPPLCIATION, s));
    }
}
