
/*
* To change this template, choose Tools | Templates
* and open the template in the editor.
 */
package com.agrologic.app.except;

/**
 *
 * @author Administrator
 */
public class StartProgramException extends Exception {
    public StartProgramException(String message) {
        super("Fatal error \n" + message);
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }
}



