
/*
* To change this template, choose Tools | Templates
* and open the template in the editor.
 */
package com.agrologic.app.except;

/**
 * {Insert class description here}
 *
 * @version $Revision: 1.1.1.1 $
 * @since Build {insert version here} (MM YYYY)
 * @author Valery Manakhimov
 * @author $Author: nbweb $, (this version)
 */
public class ObjectDoesNotExist extends Exception {
    private static final long serialVersionUID = 1L;

    public ObjectDoesNotExist(String message) {
        super(message);
    }

    @Override
    public String toString() {
        return super.getMessage();
    }
}


