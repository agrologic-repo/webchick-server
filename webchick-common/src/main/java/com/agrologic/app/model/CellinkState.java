package com.agrologic.app.model;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class CellinkState {
    public static final int STATE_OFFLINE = 0;
    public static final int STATE_ONLINE = 1;
    public static final int STATE_START = 2;
    public static final int STATE_RUNNING = 3;
    public static final int STATE_STOP = 4;
    public static final int STATE_UNKNOWN = 5;
    //    public static final int STATE_RESTART = 6;
    public static final int CONNECT_TIMEOUT = 15000 * 60;    // 15 minute
    /**
     * Map of Strings to CellinkState objects.
     */
    private static final Map CELLINK_STATES_MAP = new HashMap();

    /**
     * Data transferring state requested .
     */
    public static final CellinkState START = new CellinkState(STATE_START);

    /**
     * Cellink  running in data transferring state.
     */
    public static final CellinkState RUNNING = new CellinkState(STATE_RUNNING);

    /**
     * Cellink connected in keep alive mode
     */
    public static final CellinkState ONLINE = new CellinkState(STATE_ONLINE);

    /**
     * The cellink not connected.
     */
    public static final CellinkState OFFLINE = new CellinkState(STATE_OFFLINE);

    /**
     * The cellink is stopped.
     */
    public static final CellinkState STOP = new CellinkState(STATE_STOP);

    /**
     * The unkown state
     */
    public static final CellinkState UNKNOWN = new CellinkState(STATE_UNKNOWN);

    /**
     * The restart state
     */
//    public static final CellinkState RESTART = new CellinkState(STATE_RESTART);

    // initialize a String -> CellinkState map
    static {
        CELLINK_STATES_MAP.put("offline", STATE_OFFLINE);
        CELLINK_STATES_MAP.put("online", STATE_ONLINE);
        CELLINK_STATES_MAP.put("started", STATE_START);
        CELLINK_STATES_MAP.put("running", STATE_RUNNING);
        CELLINK_STATES_MAP.put("stopped", STATE_STOP);
        CELLINK_STATES_MAP.put("unknown", STATE_UNKNOWN);
//        CELLINK_STATES_MAP.put("restart", STATE_RESTART);
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
     *
     * @param state The CellinkState
     * @return The String or null
     */
    public static String stateToString(int state) {
        return (String) CELLINK_STATES_MAP.get(state);
    }

    /**
     * Convert a CellinkState to a String object.
     *
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
     *
     * @param str The String
     * @return The CellinkState or null
     */
    public static CellinkState stringToState(String str) {
        return (CellinkState) CELLINK_STATES_MAP.get(str);
    }

    /**
     * Convert a int to a CellinkState object.
     *
     * @param i the state
     * @return cellink state
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
//        } else if (i == STATE_RESTART) {
//            return RESTART;
        } else {
            return UNKNOWN;
        }
    }

    public static Map listState() {
        return CELLINK_STATES_MAP;
    }

    public static CellinkState getCellinkState(int state) {
        return intToState(state);
    }

    public static String getCellinkStateColor(int state) {
        String color = "white";

        switch (state) {
            case CellinkState.STATE_ONLINE:
                color = "#000000";

                break;

            case CellinkState.STATE_RUNNING:
                color = "#FFFFFF";

                break;

            case CellinkState.STATE_START:
                color = "#000000";

                break;

            case CellinkState.STATE_STOP:
                color = "#000000";

                break;

            case CellinkState.STATE_OFFLINE:
                color = "#000000";

                break;

            case CellinkState.STATE_UNKNOWN:
                color = "#FFFFFF";

                break;

            default:
                color = "white";

                break;
        }

        return color;
    }

    public static String getCellinkStateBGColor(int state) {
        String color = "white";

        switch (state) {
            case CellinkState.STATE_ONLINE:
                color = "#00FF00";

                break;

            case CellinkState.STATE_RUNNING:
                color = "#0000FF";

                break;

            case CellinkState.STATE_START:
                color = "#FF9933";

                break;

            case CellinkState.STATE_STOP:
                color = "#9999FF";

                break;

            case CellinkState.STATE_OFFLINE:
                color = "#FF0000";

                break;

            case CellinkState.STATE_UNKNOWN:
                color = "#808080";

                break;

            default:
                color = "#000000";

                break;
        }

        return color;
    }

    @Override
    public String toString() {
        switch (value) {
            case STATE_OFFLINE:
                return "offline";

            case STATE_ONLINE:
                return "online";

            case STATE_START:
                return "started";

            case STATE_RUNNING:
                return "running";

            case STATE_STOP:
                return "stopped";

//            case STATE_RESTART:
//                return "restart";

            default:
                return "unknown";
        }
    }
}


