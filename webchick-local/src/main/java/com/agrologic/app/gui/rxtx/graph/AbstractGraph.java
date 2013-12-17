package com.agrologic.app.gui.rxtx.graph;

import com.agrologic.app.i18n.LocaleManager;
import org.jfree.chart.JFreeChart;

import java.util.Locale;
import java.util.Map;

public abstract class AbstractGraph implements Graph {

    public static final int DAY_HOURS = 24;
    public static final int FEED_INDEX = 72;
    public static final int HUMIDITY_INDEX = 48;
    public static final int IN_TEMP_INDEX = 0;
    public static final int OUT_TEMP_INDEX = 24;
    public static final int WATER_INDEX = 96;
    public static final int LENGHT = 240;
    protected JFreeChart chart;
    protected Long currentTime;
    protected String[] datasetString;
    protected Map<String, String> dictionary;
    protected boolean empty;
    protected Locale locale;
    protected int maxY;
    protected int minY;
    protected GraphType type;

    public AbstractGraph(GraphType type, String values) {
        this.type = type;
        this.datasetString = values.split(" ", -1);
        this.minY = Integer.MAX_VALUE;
        this.maxY = Integer.MIN_VALUE;
        setEmpty();
    }

    protected void initLanguage() {
        LocaleManager localeManager = new LocaleManager();
        dictionary = localeManager.getDictionary(LocaleManager.GRAPH_RESOURCE, locale);
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
}



