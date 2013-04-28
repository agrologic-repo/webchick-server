
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.agrologic.app.graph;


import com.agrologic.app.model.DataDto;

//~--- JDK imports ------------------------------------------------------------

import java.util.Map;
import java.util.Map.Entry;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.TreeMap;

/**
 * Title: DataForGraphCreator.java <br> Description: <br> Copyright: Copyright
 * 2010 <br> Company: Agro Logic Ltd.
 * <br>
 *
 * @author Valery Manakhimov <br>
 * @version 0.1.1 <br>
 */
public class DataGraphCreator {

    /**
     * Create history data by grow day.
     *
     * @param history the history map per grow day.
     * @param data the searching data .
     * @return histDataByGrowDay the map with searched history data per grow day
     */
    public static Map<Integer, DataDto> createHistoryDataByGrowDay(final Map<Integer, String> history,
            final DataDto data) {
        Map<Integer, DataDto> histDataByGrowDay = new TreeMap<Integer, DataDto>();
        Set<Entry<Integer, String>> entries = history.entrySet();
        for (Entry entry : entries) {
            DataDto dataFromHist = getDataFromHistory(data, (String) entry.getValue());
            if (dataFromHist != null) {
                histDataByGrowDay.put((Integer) entry.getKey(), dataFromHist);
                if (entry.getKey() == null) {
                    System.out.println(entry);
                }
            }
        }
        return histDataByGrowDay;
    }

    /**
     * Return data object with value from history string.
     *
     * @param searchData the data that encapsulate all field except value.
     * @param histString the string which all history pairs data and value .
     * @return foundData the data object with value from history string, or null.
     */
    private static DataDto getDataFromHistory(DataDto searchData, String histString) {
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
                    DataDto foundData = (DataDto) searchData.clone();
                    return foundData;
                }
            } catch (NoSuchElementException e) {
                System.err.println(e.getMessage());
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
