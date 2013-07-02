
/*
* To change this template, choose Tools | Templates
* and open the template in the editor.
 */
package com.agrologic.app.except;


public class SOTException extends Exception {
    static final String START_OF_TRANSMISSION_ERROR = "Start of transmission character was not received during reading " +
            "response bytes ";

    public SOTException() {
        this(START_OF_TRANSMISSION_ERROR);
    }

    public SOTException(String message) {
        super(message);
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }
}



