
/*
* To change this template, choose Tools | Templates
* and open the template in the editor.
 */
package com.agrologic.app.except;

/**
 *
 * @author Administrator
 */
public class EOTException extends Exception {
    private static final long serialVersionUID = 1L;

    public EOTException(String method) {
        super("EOT error  : " + method);
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }
}



