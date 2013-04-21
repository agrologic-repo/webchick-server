
/*
* To change this template, choose Tools | Templates
* and open the template in the editor.
 */
package com.agrologic.app.dao.service;

//~--- non-JDK imports --------------------------------------------------------

import com.agrologic.app.model.Cellink;
import com.agrologic.app.model.Controller;

/**
 *
 * @author Administrator
 */
public interface DatabaseInsertable {
    
    public void insertLoadedData();

    public void insertNewUser();

    public void insertNewCellink();

    public void insertNewCellinks();

    public void insertNewCellink(Cellink cellink);

    public void insertNewControllers();

    public void insertNewController(Controller c);
}


//~ Formatted by Jindent --- http://www.jindent.com
