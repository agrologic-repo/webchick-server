package com.agrologic.app.model.rxtx;

import com.agrologic.app.gui.rxtx.DataImage;

/**
 * A "DataChangeEvent" event gets delivered whenever a bean changes a "bound"
 * or "constrained" property.  A PropertyChangeEvent object is sent as an
 * argument to the PropertyChangeListener and VetoableChangeListener methods.
 * <p/>
 * Normally PropertyChangeEvents are accompanied by the name and the old
 * and new value of the changed property.  If the new value is a primitive
 * type (such as int or boolean) it must be wrapped as the
 * corresponding java.lang.* Object type (such as Integer or Boolean).
 * <p/>
 * Null values may be provided for the old and the new values if their
 * true values are not known.
 * <p/>
 * An event source may send a null object as the name to indicate that an
 * arbitrary set of if its properties have changed.  In this case the
 * old and new values should also be null.
 */
public class DataChangeEvent {
    private Integer bitNumber;
    private String newString;
    private DataImage.Type newType;

    public DataChangeEvent(String newString) {
        this.newString = newString;
    }

    public DataChangeEvent(DataImage.Type newType, Integer bitNumber) {
        this.newType = newType;
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
