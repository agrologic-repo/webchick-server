package com.agrologic.app.model;

public interface CellinkListener {

    /**
     * This event fires if the cellink state of cellink was changed.
     * @param event the object that carries information about event
     */
    void cellinkChanged(CellinkEvent event);
}


