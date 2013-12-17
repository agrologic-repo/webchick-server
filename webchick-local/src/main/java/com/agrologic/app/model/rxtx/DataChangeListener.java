package com.agrologic.app.model.rxtx;

/**
 * A "DataChangeEvent" event gets fired whenever a bean changes a "bound"
 * property.  You can register a PropertyChangeListener with a source
 * bean so as to be notified of any bound property updates.
 */
public interface DataChangeListener {

    /**
     * This method gets called when a bound property is changed.
     *
     * @param event A DataChangeEvent object describing the event source and the property that has changed.
     */
    public void dataChanged(DataChangeEvent event);
}
