
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.agrologic.app.except;

//~--- JDK imports ------------------------------------------------------------
import java.sql.SQLException;

/**
 * Title: DaoException <br> Description: <br> Copyright: Copyright (c) 2009 <br>
 *
 * @version 1.0 <br>
 */
public class DaoException extends SQLException {

    private static final long serialVersionUID = 1L;
    private String message;

    public DaoException(String method) {
        message = "Access to database error : " + method;
    }

    @Override
    public String toString() {
        return message;
    }
}


//~ Formatted by Jindent --- http://www.jindent.com
