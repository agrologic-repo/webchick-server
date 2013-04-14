
/*
* To change this template, choose Tools | Templates
* and open the template in the editor.
 */
package com.agrologic.app.model;

/**
 * If class is dedicated to listen events from Cellink , then it should implement this interface.
 * @author Administrator
 */
public interface CellinkListener {

    /**
     * This event fires if the cellink state of cellink was changed.
     * @param event the object that carries information about event
     */
    void cellinkChanged(CellinkEvent event);
}


//~ Formatted by Jindent --- http://www.jindent.com
