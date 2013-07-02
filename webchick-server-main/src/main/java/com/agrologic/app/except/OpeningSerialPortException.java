
/*
* To change this template, choose Tools | Templates
* and open the template in the editor.
 */
package com.agrologic.app.except;

public class OpeningSerialPortException extends Exception {

    public OpeningSerialPortException() {
        super("Error opening comport.\nTry enother port.");
    }

    public OpeningSerialPortException(String message) {
        super(message);
    }
}



