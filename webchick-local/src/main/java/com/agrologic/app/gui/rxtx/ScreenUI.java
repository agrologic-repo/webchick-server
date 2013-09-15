package com.agrologic.app.gui.rxtx;

public interface ScreenUI {

    public static final int ONE_SECOND = 1000;
    public static final int MAIN_SCREEN_COL_NUMBERS = 4;
    public static final int OTHER_SCREEN_COL_NUMBERS = 4;
    public static final int REFRESH_RATE = ONE_SECOND;

    void executeUpdate();

    void initLoadedControllerData();

    void initScreenComponents();
}
