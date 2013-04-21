/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.agrologic.app.gui.flock;

import com.agrologic.app.model.DataFormat;
import java.util.Map;

/**
 * {Insert class description here}
 *
 * @version $Revision: 1.1.1.1 $
 * @since Build {insert version here} (MM YYYY)
 * @author Valery Manakhimov
 * @author $Author: nbweb $, (this version)
 */
public class HistoryEntry {

    private Object id;
    private Integer format;
    private String title;
    private Map<Integer, Double> values;
    private Map<Integer, String> values24Map;
    private String values24;


    public HistoryEntry(Object id, String title) {
        this(id, title, DataFormat.DEC_1);
    }

    public HistoryEntry(Object id, String title, Integer format) {
        this.id = id;
        this.format = format;
        this.title = title;
    }

    public void setId(Object id) {
        this.id = id;
    }

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

    public String getValues24() {
        return values24;
    }

    public void setValues24(String values24) {
        this.values24 = values24;
    }

    public Map<Integer, String> getValues24Map() {
        return values24Map;
    }

    public void setValues24Map(Map<Integer, String> values24Map) {
        this.values24Map = values24Map;
    }

    public String getValues24ByGrowDay(Integer growday) {
        return values24Map.get(growday);
    }

    public void setFormat(Integer format) {
        this.format = format;
    }

    @Override
    public String toString() {
        return title;
    }
}
