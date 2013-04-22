
/*
* To change this template, choose Tools | Templates
* and open the template in the editor.
 */
package com.agrologic.app.network.rxtx;

/**
 *
 * @author Administrator
 */
public interface Network {

    /**
     * Acknowledge
     */
    public static final byte ACK = 6;    // ACKNOWLEDGE

    /**
     * bell
     */
    public static final byte BELL = 7;    // BELL TO TERMINAL

    /**
     * computer packet
     */
    public static final byte COMPUTER = 18;    // COMPUTER INDICATOR

    /**
     * '\r'
     */
    public static final byte CR = 13;    // CARRIAGE RETURN

    /**
     * End of transmission
     */
    public static final byte EOT = 4;    // END OF TRANSMISSION

    /**
     * Error indicator
     */
    public static final byte ERR = 24;    // ERR INDICATOR

    /**
     * Negative ack
     */
    public static final byte ERROR = 21;    // NEGATIVE ACKNOWLEDGE

    /**
     * Indicate error received buffer
     */
    public static final byte ERROR_BUF = 3;    // ERROR BUFFER

    /**
     * Exit indicator
     */
    public static final byte ESC = 27;    // EXIT INDICATOR

    /**
     * End of text
     */
    public static final byte ETX = 3;    // END OF TEXT

    /**
     * Indicate last received buffer
     */
    public static final byte LAST_BUF = 1;    // LAST BUFFER

    /**
     * Indicate last received buffer
     */
    public static final byte LAST_COMPRESSED = 2;    // LAST BUFFER

    /**
     * Indicate middle received buffer
     */
    public static final byte MIDDLE_BUF = 0;    // MIDDLE BUFFER

    /**
     * Record separator
     */
    public static final byte RS = 30;    // RECORD SEPARATOR

    // communication protocol static fields

    /**
     * Start of header
     */
    public static final byte SOH = 1;    // START OF HEADER

    /**
     * Start of index
     */
    public static final byte SOINDX = 24;    // START OF INDEX

    /**
     * Start of transmission
     */
    public static final byte SOT = 22;    // START OF TRANSMISSION

    /**
     * White space
     */
    public static final byte SPACE = 32;    // WHITE SPACE

    /**
     * Start of text
     */
    public static final byte STX = 2;    // START OF TEXT

    /**
     * terminal packet
     */
    public static final byte TERMINAL = 20;    // TERMINAL INDICATOR

    public void setThreadState(NetworkState networkState);

    public NetworkState getNetworkState();
}


//~ Formatted by Jindent --- http://www.jindent.com
