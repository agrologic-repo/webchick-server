
/*
* To change this template, choose Tools | Templates
* and open the template in the editor.
 */
package com.agrologic.app.except;

public class EOTException extends Exception {
    final static String EOT_ERROR = "End of transmission character was not received during reading response bytes.";

    public EOTException() {
        this(EOT_ERROR);
    }

    public EOTException(String message) {
        super(message);
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }
}



