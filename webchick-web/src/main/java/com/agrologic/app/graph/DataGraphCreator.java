
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.agrologic.app.graph;

import com.agrologic.app.model.Data;

import java.util.*;
import java.util.Map.Entry;


/**
 * Title: DataForGraphCreator.java <br> Description: <br> Copyright: Copyright
 * 2010 <br> Company: Agro Logic Ltd. <br>
 *
 * @author Valery Manakhimov <br>
 * @version 0.1.1 <br>
 */
public class DataGraphCreator {

    /**
     * Create management data by grow day.
     *
     * @param history the management map per grow day.
     * @param data    the searching data .
     * @return histDataByGrowDay the map with searched management data per grow day
     */
    public static Map<Integer, Data> createHistoryDataByGrowDay(final Map<Integer, String> history,
                                                                final Data data) {
        Map<Integer, Data> histDataByGrowDay = new TreeMap<Integer, Data>();
        Set<Entry<Integer, String>> entries = history.entrySet();
        for (Entry entry : entries) {
            if (entry.getValue().equals("-1")) {
                Data tempData = (Data) data.clone();
                tempData.setValue(0L);
                histDataByGrowDay.put((Integer) entry.getKey(), tempData);
            } else {
                Data dataFromHist = getDataFromHistory(data, (String) entry.getValue());
                if (dataFromHist != null) {
                    histDataByGrowDay.put((Integer) entry.getKey(), dataFromHist);
                }
            }
        }
        return histDataByGrowDay;
    }

    /**
     * Return data object with value from management string.
     *
     * @param searchData the data that encapsulate all field exception value.
     * @param histString the string which all management pairs data and value .
     * @return foundData the data object with value from management string, or
     *         null.
     */
    private static Data getDataFromHistory(Data searchData, String histString) {
        StringTokenizer token = new StringTokenizer(histString, " ");

        while (token.hasMoreElements()) {
            try {
                String dataElem = (String) token.nextElement();
                String valElem = (String) token.nextElement();
                if (isNegative(valElem)) {
                    valElem = valElem.replace("-", "");
                }
                String dataType = searchData.getType().toString();
                if (dataElem.equals(dataType)) {
                    Long value = Long.parseLong(valElem);
                    searchData.setValue(value);
                    Data foundData = (Data) searchData.clone();
                    return foundData;
                }
            } catch (NoSuchElementException e) {
                //System.err.println(e.getMessage());
            }
        }
        return null;
    }

    /**
     * Return true if value have an '-' char
     *
     * @param value the value
     * @return true if value is negative
     */
    private static boolean isNegative(String value) {
        if (value.indexOf("-") != -1) {
            return true;
        }
        return false;
    }
}
