
/*
* To change this template, choose Tools | Templates
* and open the template in the editor.
 */
package com.agrologic.app.exception;

/**
 * Title: WebException <br>
 * Decription: A Web Exception to notify any error that might occur on the web tier.<br>
 * Copyright:   Copyright (c) 2009 <br>
 * Company:     Agro Logic LTD. <br>
 * @author      Valery Manakhimov <br>
 * @version     1.0 <br>
 */
public class WebException extends Exception {

    /**
     *
     */
    private static final long serialVersionUID = 8562340289131757681L;

    /**
     * The constructor calles the Excpetion constructor to populate the message
     * @param message  A message of the error that occured
     */
    public WebException(String message) {
        super(message);
    }
}



