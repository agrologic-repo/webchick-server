
/*
* To change this template, choose Tools | Templates
* and open the template in the editor.
 */
package com.agrologic.app.except;

/**
 * Title: TimeoutException <br>
 * Description: <br>
 * Copyright:   Copyright (c) 2009 <br>
 *
 * @version 1.0 <br>
 */
public class TimeoutException extends Exception {

    final static String TIME_OUT_ERROR = "Receiving the response timed out ";

    public TimeoutException() {
        this(TIME_OUT_ERROR);
    }

    public TimeoutException(String message) {
        super(message);
    }

    @Override
    public String toString() {
        return super.toString();
    }
}



