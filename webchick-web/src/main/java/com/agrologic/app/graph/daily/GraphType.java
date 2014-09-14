package com.agrologic.app.graph.daily;

import java.util.HashMap;
import java.util.Map;

public class GraphType {
    public static final int BLANK_GRAPH = 0;
    public static final int IN_FEED_WATER_GRAPH = 2;
    public static final int IN_OUT_TEMP_HUMID_GRAPH = 1;

    /**
     * Map of Strings to GraphType objects.
     */
    private static final Map graphTypeMap = new HashMap();

    /**
     * The in/out temperature and humidity graph.
     */
    public static final GraphType IN_OUT_TEMP_HUMID = new GraphType(IN_OUT_TEMP_HUMID_GRAPH);

    /**
     * The in temperature water and consumption graph
     */
    public static final GraphType IN_FEED_WATER = new GraphType(IN_FEED_WATER_GRAPH);

    /**
     * The in temperature water and consumption graph
     */
    public static final GraphType BLANK = new GraphType(BLANK_GRAPH);

    // initialize a String -> GraphType map
    static {
        graphTypeMap.put(BLANK_GRAPH, "Blank Graph (24 hours)");
        graphTypeMap.put(IN_OUT_TEMP_HUMID_GRAPH, "In Temperature Water and Consumption Graph (24 hours)");
        graphTypeMap.put(IN_FEED_WATER_GRAPH, "In Temperature Water and Consumption Graph (24 hours)");
    }

    private int value;

    public GraphType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    /**
     * Convert a GraphType to a String object.
     *
     * @param type The GraphType
     * @return The String or null
     */
    public static String typeToString(int type) {
        return (String) graphTypeMap.get(type);
    }

    @Override
    public String toString() {
        return typeToString(value);
    }
}



