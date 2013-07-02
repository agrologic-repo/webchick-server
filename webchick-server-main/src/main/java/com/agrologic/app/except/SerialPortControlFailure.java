
/*
* To change this template, choose Tools | Templates
* and open the template in the editor.
 */
package com.agrologic.app.except;

public class SerialPortControlFailure extends Exception {

    public SerialPortControlFailure(String message, Exception e) {
        super(message);
        setStackTrace(e.getStackTrace());
    }
}



