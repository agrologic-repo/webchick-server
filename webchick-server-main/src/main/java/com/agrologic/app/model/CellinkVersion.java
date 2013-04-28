
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.agrologic.app.model;


import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Administrator
 */
public class CellinkVersion {

    public static final int DEFAULT_HEAD_BYTES = 4;
    public static final Map<String, Integer> VERSIONS_MAP = new HashMap<String, Integer>();

    static {
        VERSIONS_MAP.put("N/A", DEFAULT_HEAD_BYTES - 1);
        VERSIONS_MAP.put("1",   DEFAULT_HEAD_BYTES - 1);
    }

    public static int headerBytesByVers(String version) {
        String vers = version.trim();
        for (Map.Entry<String, Integer> entry : VERSIONS_MAP.entrySet()) {
            if (vers.startsWith(entry.getKey()) == true) {
                return entry.getValue();
            }
        }
        return DEFAULT_HEAD_BYTES;
    }

    public static int dataBytesByVers(String version) {
        String vers = version.trim();
        for (Map.Entry<String, Integer> entry : VERSIONS_MAP.entrySet()) {
            if (vers.startsWith(entry.getKey()) == true) {
                if (entry.getValue() == DEFAULT_HEAD_BYTES - 1) {
                    return 1;
                } else {
                    return 2;
                }
            }
        }
        return 2;
    }
}



