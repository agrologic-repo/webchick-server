package com.agrologic.app.exception;

/**
 * Throws if can not open serial port
 */
public class SerialPortControlFailure extends GeneralException {

    public SerialPortControlFailure(String message, Exception e) {
        super(message);
        setStackTrace(e.getStackTrace());
    }
}



