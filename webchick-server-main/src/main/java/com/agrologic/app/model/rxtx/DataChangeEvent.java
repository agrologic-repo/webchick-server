
/*
* To change this template, choose Tools | Templates
* and open the template in the editor.
 */
package com.agrologic.app.model.rxtx;

//~--- non-JDK imports --------------------------------------------------------

import com.agrologic.app.gui.rxtx.DataImage.Type;

/**
 * {Insert class description here}
 *
 * @version $Revision: 1.1.1.1 $
 * @since Build {insert version here} (MM YYYY)
 * @author Valery Manakhimov
 * @author $Author: nbweb $, (this version)
 */
public class DataChangeEvent {
    private Integer bitNumber;
    private String  newString;
    private Type    newType;
    private Long    newValue;

    public DataChangeEvent(Long newValue) {
        this.newValue = newValue;
    }

    public DataChangeEvent(String newString) {
        this.newString = newString;
    }

    public DataChangeEvent(Type newType) {
        this.newType = newType;
    }

    public DataChangeEvent(Type newType, Integer bitNumber) {
        this.newType   = newType;
        this.bitNumber = bitNumber;
    }

    public long getValue() {
        return newValue;
    }

    public Type getType() {
        return newType;
    }

    public Integer getBitNumber() {
        return bitNumber;
    }

    public void setBitNumber(Integer bitNumber) {
        this.bitNumber = bitNumber;
    }

    public String getNewString() {
        return newString;
    }

    public void setNewString(String newString) {
        this.newString = newString;
    }
}


//~ Formatted by Jindent --- http://www.jindent.com
