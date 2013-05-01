package com.agrologic.app.model.rxtx;

import com.agrologic.app.gui.rxtx.DataImage;

public class DataChangeEvent {
    private Integer bitNumber;
    private String  newString;
    private DataImage.Type newType;

    public DataChangeEvent(String newString) {
        this.newString = newString;
    }

    public DataChangeEvent(DataImage.Type newType, Integer bitNumber) {
        this.newType   = newType;
        this.bitNumber = bitNumber;
    }

    public DataImage.Type getType() {
        return newType;
    }

    public Integer getBitNumber() {
        return bitNumber;
    }

    public String getNewString() {
        return newString;
    }
}
