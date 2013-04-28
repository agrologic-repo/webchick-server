
/*
* To change this template, choose Tools | Templates
* and open the template in the editor.
 */
package com.agrologic.app.except;

/**
 * Title: OpeningSerialPortException <br>
 * Description: <br>
 * Copyright:   Copyright (c) 2009 <br>
 * Company:     AgroLogic LTD. <br>
 * @author      Valery Manakhimov <br>
 * @version     1.1 <br>
 */
public class OpeningSerialPortException extends Exception {

    public OpeningSerialPortException() {
        super("Error opening comport.\nTry enother port.");
    }

    public OpeningSerialPortException(String message) {
        super(message);
    }
}



