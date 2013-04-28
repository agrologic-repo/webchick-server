
/*
* To change this template, choose Tools | Templates
* and open the template in the editor.
 */
package com.agrologic.app.except;

/**
 * Title: TimeoutException <br>
 * Description: <br>
 * Copyright:   Copyright (c) 2009 <br>
 * @version     1.0 <br>
 */
public class TimeoutException extends Exception {
    String message;

    public TimeoutException(String method) {
        message = "Method timed out: " + method;
    }

    @Override
    public String toString() {
        return message;
    }
}



