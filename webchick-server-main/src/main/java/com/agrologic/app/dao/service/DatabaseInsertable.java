
/*
* To change this template, choose Tools | Templates
* and open the template in the editor.
 */
package com.agrologic.app.dao.service;

import com.agrologic.app.model.Cellink;
import com.agrologic.app.model.Controller;

public interface DatabaseInsertable {
    
    public void insertLoadedData();

    public void insertNewCellink(Cellink cellink);

    public void insertNewController(Controller c);
}



