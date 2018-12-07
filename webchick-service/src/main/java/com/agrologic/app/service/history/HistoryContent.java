package com.agrologic.app.service.history;

import com.agrologic.app.model.Data;
import org.apache.commons.lang.StringEscapeUtils;

import java.util.*;

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

        List<Data> maxListData = new ArrayList<Data>();

        for (List<Data> dataList : historyContentPerDay.values()){
            if(maxListData.size() == 0 || dataList.size() > maxListData.size()){
                maxListData = new ArrayList<Data>(dataList);
            }
        }

        List<String> titles = new ArrayList<String>();

        for (Data data : maxListData) {
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
        int maxSizeIndex = 1;
        int maxSize = 0;
        for (Map.Entry<Integer, List<Data>> entry : historyContentPerDay.entrySet()) {
            if (entry.getValue().size() > maxSize) {
                maxSize = entry.getValue().size();
                maxSizeIndex = entry.getKey();
            }
        }
        return historyContentPerDay.get(maxSizeIndex);
    }

    public List<String> getTitlesPerHourForHtml() {
        List<String> titles = new ArrayList<String>();
//        titles.add("Date"); ////
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

    public List<List<String>> getExcelDataColumns(int size) {

        List<List<String>> lists = new ArrayList<List<String>>();

        for (int i = 0; i < size; i++) {

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
        List<List<String>> lists;
        List<String> hours;
        lists = new ArrayList<List<String>>();
        hours = new ArrayList<String>();
        for (int i = 1; i < 25; i++) {
            hours.add("" + i);
        }
        lists.add(hours);
        for (List<String> historyDataList : historyContentPerHour.values()) {
            lists.add(historyDataList);
        }
        return lists;
    }
}
