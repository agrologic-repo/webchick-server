
/*
* To change this template, choose Tools | Templates
* and open the template in the editor.
 */
package com.agrologic.app.model;


import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Title: CellinkState <br>
 * Decription: <br>
 * Copyright:   Copyright (c) 2009 <br>
 * Company:     Agro Logic LTD. <br>
 * @author      Valery Manakhimov <br>
 * @version     1.1 <br>
 */
public class CellinkState {
    public static final int STATE_OFFLINE = 0;
    public static final int STATE_ONLINE  = 1;
    public static final int STATE_RESTART = 6;
    public static final int STATE_RUNNING = 3;
    public static final int STATE_START   = 2;
    public static final int STATE_STOP    = 4;
    public static final int STATE_UNKNOWN = 5;

    /** Data transferring state requested . */
    public static final CellinkState START = new CellinkState(STATE_START);

    /** Cellink  running in data transferring state. */
    public static final CellinkState RUNNING = new CellinkState(STATE_RUNNING);

    /** Cellink connected in keep alive mode */
    public static final CellinkState ONLINE = new CellinkState(STATE_ONLINE);

    /** The cellink not connected. */
    public static final CellinkState OFFLINE = new CellinkState(STATE_OFFLINE);

    /** Map of Strings to CellinkState objects. */
    private static final Map CELLINK_STATES_MAP = new HashMap();

    /** The cellink is stopped. */
    public static final CellinkState STOP = new CellinkState(STATE_STOP);

    /** The unkown state */
    public static final CellinkState UNKNOWN = new CellinkState(STATE_UNKNOWN);

    /** The restart state */
    public static final CellinkState RESTART = new CellinkState(STATE_RESTART);

    // initialize a String -> CellinkState map
    static {
        CELLINK_STATES_MAP.put("Offline", STATE_OFFLINE);
        CELLINK_STATES_MAP.put("Online", STATE_ONLINE);
        CELLINK_STATES_MAP.put("Started", STATE_START);
        CELLINK_STATES_MAP.put("Running", STATE_RUNNING);
        CELLINK_STATES_MAP.put("Stoped", STATE_STOP);
        CELLINK_STATES_MAP.put("Unknown", STATE_UNKNOWN);
        CELLINK_STATES_MAP.put("Restart", STATE_RESTART);
    }

    private int value;

    public CellinkState(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    /**
     * Convert a CellinkState to a String object.
     * @param state The CellinkState
     * @return The String or null
     */
    public static String stateToString(int state) {
        return (String) CELLINK_STATES_MAP.get(state);
    }

    /**
     * Convert a CellinkState to a String object.
     * @param state The CellinkState
     * @return The String or null
     */
    public static String stateToString(CellinkState state) {
        Iterator keys = CELLINK_STATES_MAP.keySet().iterator();

        while (keys.hasNext()) {
            String key = (String) keys.next();

            if (((CellinkState) CELLINK_STATES_MAP.get(key)).equals(state)) {
                return key;
            }
        }

        return null;
    }

    /**
     * Convert a String to a CellinkState object.
     * @param str The String
     * @return The CellinkState or null
     */
    public static CellinkState stringToState(String str) {
        return (CellinkState) CELLINK_STATES_MAP.get(str);
    }

    /**
     * Convert a int to a CellinkState object.
     * @param str The String
     * @return The CellinkState or null
     */
    public static CellinkState intToState(int i) {
        if (i == STATE_ONLINE) {
            return ONLINE;
        } else if (i == STATE_OFFLINE) {
            return OFFLINE;
        } else if (i == STATE_RUNNING) {
            return RUNNING;
        } else if (i == STATE_START) {
            return START;
        } else if (i == STATE_STOP) {
            return STOP;
        } else if (i == STATE_RESTART) {
            return RESTART;
        } else {
            return UNKNOWN;
        }
    }

    @Override
    public String toString() {
        switch (value) {
        case STATE_OFFLINE :
            return "Offline";

        case STATE_ONLINE :
            return "Online";

        case STATE_START :
            return "Started";

        case STATE_RUNNING :
            return "Running";

        case STATE_STOP :
            return "Stoped";

        case STATE_RESTART :
            return "Restart";

        default :
            return "Unknown";
        }
    }
}


