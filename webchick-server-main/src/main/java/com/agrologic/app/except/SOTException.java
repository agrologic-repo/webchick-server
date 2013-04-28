
/*
* To change this template, choose Tools | Templates
* and open the template in the editor.
 */
package com.agrologic.app.except;

/**
 *
 * @author Administrator
 */
public class SOTException extends Exception {
    public SOTException(String method) {
        super("SOT error  : " + method);
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }
}



