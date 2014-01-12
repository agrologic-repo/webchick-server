package com.agrologic.app.model.history;

import java.util.Map;

/**
 * Created by Valery on 12/30/13.
 */
public class PerDayHistoryData implements HistoryData {
    private String title;
    private Map<Integer, Integer> values;

    public PerDayHistoryData(String title, Map<Integer, Integer> values) {
        this.title = title;
        this.values = values;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public Map<Integer, Integer> getValues() {
        return values;
    }
}


