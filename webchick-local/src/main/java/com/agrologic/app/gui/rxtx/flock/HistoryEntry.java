package com.agrologic.app.gui.rxtx.flock;

import com.agrologic.app.model.DataFormat;

import java.util.Map;

public class HistoryEntry {

    private Object id;
    private Integer format;
    private String title;
    private Map<Integer, Double> values;
    private Map<Integer, String> values24Map;

    public HistoryEntry() {
    }

    public HistoryEntry(Object id, String title) {
        this(id, title, DataFormat.DEC_1);
    }

    public HistoryEntry(Object id, String title, Integer format) {
        this.id = id;
        this.format = format;
        this.title = title;
    }

    public void setId(Object id){this.id = id;}

    public void setFormat(Integer format){this.format = format;}

    public void setTitle(String title) {
        this.title = title;
    }

    public void setValues(Map<Integer, Double> values) {
        this.values = values;
    }

    public Object getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public Map<Integer, Double> getValues() {
        return values;
    }

    public Integer getFormat() {
        return format;
    }

    public void setValues24Map(Map<Integer, String> values24Map) {
        this.values24Map = values24Map;
    }

    public String getValues24ByGrowDay(Integer growday) {
        return values24Map.get(growday);
    }

    @Override
    public String toString() {
        return title;
    }
}
