package com.agrologic.app.gui.rxtx;

public interface ScreenUI {

    static final int ONE_SECOND = 1000;
    static final int COL_NUMBERS = 4;
    static final int REFRESH_RATE = ONE_SECOND;

    void executeUpdate();

    void initLoadedControllerData();

    void initScreenComponents();
}
