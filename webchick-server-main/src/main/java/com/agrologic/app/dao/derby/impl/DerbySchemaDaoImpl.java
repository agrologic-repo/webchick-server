
/*
* To change this template, choose Tools | Templates
* and open the template in the editor.
 */
package com.agrologic.app.dao.derby.impl;

//~--- non-JDK imports --------------------------------------------------------

import com.agrologic.app.dao.DaoType;
import com.agrologic.app.dao.mysql.impl.SchemaDaoImpl;

/**
 *
 * @author Administrator
 */
public class DerbySchemaDaoImpl extends SchemaDaoImpl {

    public DerbySchemaDaoImpl() {
        this(DaoType.DERBY);
    }

    public DerbySchemaDaoImpl(DaoType daoType) {
        super(daoType);
    }
}


//~ Formatted by Jindent --- http://www.jindent.com
