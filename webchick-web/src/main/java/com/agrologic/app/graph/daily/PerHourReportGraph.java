package com.agrologic.app.graph.daily;

import org.jfree.chart.JFreeChart;

import java.util.*;

/**
 * Created by Valery on 1/13/14.
 */
public abstract class PerHourReportGraph {
    public static final int DAY_HOURS = 24;
    public static final int IN_TEMP_INDEX = 0;
    public static final int OUT_TEMP_INDEX = 24;
    public static final int HUMIDITY_INDEX = 48;
    public static final int FEED_INDEX = 72;
    public static final int WATER_INDEX = 96;

    protected JFreeChart chart;
    protected Long currentTime;
    protected String[] datasetString;
    protected Map<String, String> dictinary;
    protected boolean empty;
    protected Locale locale;
    protected int maxY;
    protected int minY;

    public PerHourReportGraph(String values) {
        this.datasetString = values.split(" ", -1);
        this.minY = Integer.MAX_VALUE;
        this.maxY = Integer.MIN_VALUE;
        setEmpty();
    }

    protected void initLanguage() {
        dictinary = new HashMap<String, String>();
        ResourceBundle bundle = ResourceBundle.getBundle("labels", locale);
        for (Enumeration<String> e = bundle.getKeys(); e.hasMoreElements(); ) {
            String key = e.nextElement();
            if (key.startsWith("graph")) {
                String value = bundle.getString(key);
                dictinary.put(key, value);
            }
        }
    }

    protected void resetMinMaxY() {
        this.minY = Integer.MAX_VALUE;
        this.maxY = Integer.MIN_VALUE;
    }

    private void setEmpty() {
        if (datasetString.length > 0) {
            empty = false;
        } else {
            empty = true;
        }
    }

    protected boolean isEmpty() {
        return empty;
    }

    public abstract JFreeChart createChart();
}
