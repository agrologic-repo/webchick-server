package com.agrologic.app.service.history;

import com.agrologic.app.model.Data;
import org.apache.commons.lang.StringEscapeUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by Valery on 1/7/14.
 */
public class HistoryContent {
    protected Integer growDay;
    protected Map<Data, List<String>> historyContentPerHour;
    protected Map<Integer, List<Data>> historyContentPerDay;

    public HistoryContent() {
        historyContentPerHour = new TreeMap<Data, List<String>>();
        historyContentPerDay = new TreeMap<Integer, List<Data>>();
    }

    public Integer getGrowDay() {
        return growDay;
    }

    public void setGrowDay(Integer growDay) {
        this.growDay = growDay;
    }

    public List<String> getTitlesForExcel() {
        int index = 1, maxSizeIndex = 1;
        int maxSize = 0;
        for (List<Data> dataList : historyContentPerDay.values()) {
            if (dataList.size() > maxSize) {
                maxSize = dataList.size();
                maxSizeIndex = index;
            }
            index++;
        }
        List<String> titles = new ArrayList<String>();
        for (Data data : historyContentPerDay.get(maxSizeIndex)) {
            titles.add(StringEscapeUtils.unescapeHtml(data.getUnicodeLabel()));
        }
        return titles;
    }

    public List<String> getTitlesForExcelPerHour() {
        List<String> titles = new ArrayList<String>();
        titles.add(String.format("Grow day %s Hour(24) ", growDay));
        for (Data data : historyContentPerHour.keySet()) {
            titles.add(StringEscapeUtils.unescapeHtml(data.getUnicodeLabel()));
        }
        return titles;
    }

    public List<Data> getTitlesForHtml() {
        int index = 1, maxSizeIndex = 1;
        int maxSize = 0;
        for (List<Data> dataList : historyContentPerDay.values()) {
            if (dataList.size() > maxSize) {
                maxSize = dataList.size();
                maxSizeIndex = index;
            }
            index++;
        }
        return historyContentPerDay.get(maxSizeIndex);
    }

    public List<String> getTitlesPerHourForHtml() {
        List<String> titles = new ArrayList<String>();
        titles.add(String.format("Grow day %s Hour(24) ", growDay));
        for (Data data : historyContentPerHour.keySet()) {
            titles.add(StringEscapeUtils.unescapeHtml(data.getUnicodeLabel()));
        }
        return titles;
    }

    public void addHistoryContentPerDay(Integer day, List<Data> historyContentPerDay) {
        this.historyContentPerDay.put(day, historyContentPerDay);
    }

    public void addHistoryContentPerHour(Data data, List<String> historyContentPerHour) {
        this.historyContentPerHour.put(data, historyContentPerHour);
    }

    public Map<Integer, List<Data>> getHistoryContentPerDay() {
        return historyContentPerDay;
    }

    public List<List<String>> getHistoryContentPerHour() {
        List<List<String>> lists = new ArrayList<List<String>>();
        for (int i = 0; i < 24; i++) {
            List<String> dataList = new ArrayList<String>();
            dataList.add("" + i);
            for (Map.Entry<Data, List<String>> entry : historyContentPerHour.entrySet()) {
                if (entry.getValue().size() <= i) {
                    dataList.add("0");
                } else {
                    dataList.add(entry.getValue().get(i));
                }
            }
            lists.add(dataList);
        }
        return lists;
    }

    public List<List<String>> getExcelDataColumns() {
        List<List<String>> lists = new ArrayList<List<String>>();
        for (int i = 0; i < getTitlesForExcel().size(); i++) {
            List<String> dataList = new ArrayList<String>();
            for (Map.Entry<Integer, List<Data>> entry : historyContentPerDay.entrySet()) {
                if (entry.getValue().size() <= i) {
                    dataList.add("-1");
                } else {
                    Data data = entry.getValue().get(i);
                    dataList.add(data.getFormattedValue());
                }
            }
            lists.add(dataList);
        }
        return lists;
    }

    public List<List<String>> getExcelDataColumnsPerHour() {
        List<List<String>> lists = new ArrayList<List<String>>();
        List<String> hours = new ArrayList<String>();
        for (int i = 0; i < 24; i++) {
            hours.add("" + i);
        }
        lists.add(hours);
        for (List<String> historyDataList : historyContentPerHour.values()) {
            lists.add(historyDataList);
        }
        return lists;
    }
}
