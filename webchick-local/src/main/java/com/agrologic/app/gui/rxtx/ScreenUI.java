/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.agrologic.app.gui.rxtx;

/**
 * @author Administrator
 */
public interface ScreenUI {

    static final int ONE_SECOND = 1000;
    static final int COL_NUMBERS = 4;
    static final int SCREEN_WIDTH = 1024;
    static final int SCREEN_HEIGHT = 768;
    static final int REFRESH_RATE = ONE_SECOND;

    void executeUpdate();

    void initLoadedControllerData();

    void initScreenComponents();
}
