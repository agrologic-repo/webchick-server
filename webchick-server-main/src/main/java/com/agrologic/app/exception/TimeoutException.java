
/*
* To change this template, choose Tools | Templates
* and open the template in the editor.
 */
package com.agrologic.app.exception;

public class TimeoutException extends Exception {

    static final String TIME_OUT_ERROR = "Receiving the response timed out ";

    public TimeoutException(String message) {
        super(message);
    }

    @Override
    public String toString() {
        return super.toString();
    }
}



