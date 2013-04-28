
/*
* To change this template, choose Tools | Templates
* and open the template in the editor.
 */
package com.agrologic.app.except;

/**
 * Title: SerialPortControlException.java <br>
 * Description: <br>
 * Copyright:   Copyright © 2010 <br>
 * Company:     AgroLogic Ltd. ®<br>
 * @author      Valery Manakhimov <br>
 * @version     0.1.1 <br>
 */
public class SerialPortControlFailure extends Exception {
    public SerialPortControlFailure() {
        super();
    }

    public SerialPortControlFailure(String message) {
        super(message);
    }

    public SerialPortControlFailure(String message, Exception e) {
        super(message);
        setStackTrace(e.getStackTrace());
    }
}



