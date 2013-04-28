
/*
* To change this template, choose Tools | Templates
* and open the template in the editor.
 */
package com.agrologic.app.web;

//~--- JDK imports ------------------------------------------------------------

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Title: CellinkState <br>
 * Description: <br>
 * Copyright:   Copyright (c) 2009 <br>
 * Company:     AgroLogic LTD. <br>
 * @author      Valery Manakhimov <br>
 * @version     1.1 <br>
 */
public class CellinkState {
    public static final int CONNECT_TIMEOUT = 15000 * 60;    // 15 minute
    public static final int STATE_OFFLINE   = 0;
    public static final int STATE_ONLINE    = 1;
    public static final int STATE_RUNNING   = 3;
    public static final int STATE_START     = 2;
    public static final int STATE_STOP      = 4;
    public static final int STATE_UNKNOWN   = 5;

    /** Map of Strings to CellinkState objects. */
    private static final Map<String, Integer> stateMap = new HashMap();

    /** The unkown state */
    public static final CellinkState UNKNOWN = new CellinkState(STATE_UNKNOWN);

    /** The cellink is stopped. */
    public static final CellinkState STOP = new CellinkState(STATE_STOP);

    /** Data transferring state requested . */
    public static final CellinkState START = new CellinkState(STATE_START);

    /** Cellink  running in data transferring state. */
    public static final CellinkState RUNNING = new CellinkState(STATE_RUNNING);

    /** The cellink not connected. */
    public static final CellinkState OFFLINE = new CellinkState(STATE_OFFLINE);

    /** Cellink connected in keep alive mode */
    public static final CellinkState ALIVE = new CellinkState(STATE_ONLINE);

    // initialize a String -> CellinkState map
    static {
        stateMap.put("offline", STATE_OFFLINE);
        stateMap.put("online", STATE_ONLINE);
        stateMap.put("started", STATE_START);
        stateMap.put("running", STATE_RUNNING);
        stateMap.put("stopped", STATE_STOP);
        stateMap.put("unknown", STATE_UNKNOWN);
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
        CellinkState cs = new CellinkState(state);

        return stateToString(cs);
    }

    /**
     * Convert a CellinkState to a String object.
     * @param state The CellinkState
     * @return The String or null
     */
    public static String stateToString(CellinkState state) {
        Iterator keys = stateMap.keySet().iterator();

        while (keys.hasNext()) {
            String key = (String) keys.next();

            if (stateMap.get(key).equals(state.getValue())) {
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
        return new CellinkState(stateMap.get(str));
    }

    /**
     * Convert a int to a CellinkState object.
     * @param str The String
     * @return The CellinkState or null
     */
    public static CellinkState intToState(int i) {
        if (i == STATE_ONLINE) {
            return ALIVE;
        } else if (i == STATE_OFFLINE) {
            return OFFLINE;
        } else if (i == STATE_RUNNING) {
            return RUNNING;
        } else if (i == STATE_START) {
            return START;
        } else if (i == STATE_STOP) {
            return STOP;
        } else {
            return UNKNOWN;
        }
    }

    public static Map listState() {
        return stateMap;
    }

    public static CellinkState getCellinkState(int state) {
        return intToState(state);
    }

    public static String getCellinkStateColor(int state) {
        String color = "white";

        switch (state) {
        case CellinkState.STATE_ONLINE :
            color = "#000000";

            break;

        case CellinkState.STATE_RUNNING :
            color = "#FFFFFF";

            break;

        case CellinkState.STATE_START :
            color = "#000000";

            break;

        case CellinkState.STATE_STOP :
            color = "#000000";

            break;

        case CellinkState.STATE_OFFLINE :
            color = "#000000";

            break;

        case CellinkState.STATE_UNKNOWN :
            color = "#FFFFFF";

            break;

        default :
            color = "white";

            break;
        }

        return color;
    }

    public static String getCellinkStateBGColor(int state) {
        String color = "white";

        switch (state) {
        case CellinkState.STATE_ONLINE :
            color = "#00FF00";

            break;

        case CellinkState.STATE_RUNNING :
            color = "#0000FF";

            break;

        case CellinkState.STATE_START :
            color = "#FF9933";

            break;

        case CellinkState.STATE_STOP :
            color = "#9999FF";

            break;

        case CellinkState.STATE_OFFLINE :
            color = "#FF0000";

            break;

        case CellinkState.STATE_UNKNOWN :
            color = "#808080";

            break;

        default :
            color = "white";

            break;
        }

        return color;
    }

    @Override
    public String toString() {
        switch (value) {
        case STATE_OFFLINE :
            return "offline";

        case STATE_ONLINE :
            return "online";

        case STATE_START :
            return "started";

        case STATE_RUNNING :
            return "running";

        case STATE_STOP :
            return "stopped";

        default :
            return "unknown";
        }
    }
}



