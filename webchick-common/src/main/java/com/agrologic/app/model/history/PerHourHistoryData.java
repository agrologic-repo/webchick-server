package com.agrologic.app.model.history;

import java.util.Map;

/**
 * Created by Valery on 12/30/13.
 */
public class PerHourHistoryData implements HistoryData {
    private String title;
    private Integer growDay;
    private Map<Integer, Integer> values;

    public PerHourHistoryData(String title, Integer growDay, Map<Integer, Integer> values) {
        this.title = title;
        this.growDay = growDay;
        this.values = values;
    }

    @Override
    public String getTitle() {
        return title;
    }

    public Integer getGrowDay() {
        return growDay;
    }

    @Override
    public Map<Integer, Integer> getValues() {
        return values;
    }

}
