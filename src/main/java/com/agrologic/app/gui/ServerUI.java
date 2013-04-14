
/*
* To change this template, choose Tools | Templates
* and open the template in the editor.
 */
package com.agrologic.app.gui;

//~--- non-JDK imports --------------------------------------------------------

import com.agrologic.app.model.Cellink;

//~--- JDK imports ------------------------------------------------------------

import java.util.Iterator;

/**
 *
 * @author Administrator
 */
public interface ServerUI {
    CellinkTable getCellinkTable();

    Iterator<Cellink> iterator();
}


//~ Formatted by Jindent --- http://www.jindent.com
