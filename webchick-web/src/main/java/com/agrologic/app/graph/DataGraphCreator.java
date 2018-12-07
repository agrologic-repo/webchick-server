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
     * Create history data by grow day.
     *
     * @param history the history map per grow day.
     * @param data    the searching data .
     * @return histDataByGrowDay the map with searched history data per grow day
     */
    public static Map<Integer, Data> createHistoryDataByGrowDay(final Map<Integer, String> history, final Data data) {
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
     * Return data object with value from history string.
     *
     * @param searchData the data that encapsulate all field exception value.
     * @param histString the string which all history pairs data and value .
     * @return foundData the data object with value from history string, or
     * null.
     */
    public static Data getDataFromHistory(Data searchData, String histString) {
        StringTokenizer token;
        String dataElem;
        String valElem;
        long dataId;
        int value;
        int type;
        Long val;
        Data foundData;
        token = new StringTokenizer(histString, " ");
        while (token.hasMoreElements()) {
            try {
                dataElem = (String) token.nextElement();
                valElem = (String) token.nextElement();
                if (isNegative(valElem)) {
                    valElem = valElem.replace("-", "");
                }
                dataId = Long.parseLong(dataElem);
                value = Integer.parseInt(valElem);
                type = (int) dataId;// type of value (like 4096)
                if ((type & 0xC000) != 0xC000) {
                    dataId = (type & 0xFFF); // remove type to get an index 4096&0xFFF -> 0
                    } else {
                        dataId = (type & 0xFFFF);
                    }
                if(dataId == searchData.getId()){
                    val = Long.parseLong(valElem);
                    searchData.setValue(val);
                    foundData = (Data) searchData.clone();
                    return foundData;
                }
            } catch (NoSuchElementException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * Return data object with value from history string.
     *
     * @param histString the string which all history pairs data and value .
     * @return foundData the data object with value from history string, or
     * null.
     */
    public static Long getValueByDataIdFromHistory(Long dataId, String histString) {
        Long returnVal = 0L;
        if (histString.startsWith("-1") || histString.endsWith("-1")) {
            return returnVal;
        }

        StringTokenizer token = new StringTokenizer(histString, " ");

        while (token.hasMoreElements()) {
            try {
                String dataElem = (String) token.nextElement();
                String valElem = (String) token.nextElement();
                if (isNegative(valElem)) {
                    valElem = valElem.replace("-", "");
                }
                String dataType = dataId.toString();
                if (dataElem.equals(dataType)) {
                    returnVal = Long.parseLong(valElem);
                    break;
                }
            } catch (NoSuchElementException e) {
                break;
            }
        }
        return returnVal;
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

    //*****************************************************************************************************************
    public static Map<Integer, Data> createHistoryData(final Map<Integer, List<Data>> history, final Data dataToFind) {

        Map<Integer, Data> histDataByGrowDay = new TreeMap<Integer, Data>();

        Set<Entry<Integer, List<Data>>> historyEntries = history.entrySet();

        for (Entry entry : historyEntries) {

            List<Data> histList = (List<Data>)entry.getValue();
            for(Data histData : histList){

                if(histData.getValue().equals("-1")){

                    Data data = (Data)dataToFind.clone();
                    data.setValue(0L);
                    histDataByGrowDay.put((Integer)entry.getKey(), data);

                } else {

                    if(histData.getId().equals(dataToFind.getId())) {

                        histDataByGrowDay.put((Integer) entry.getKey(), histData);

                    }
                }
            }
        }
        return histDataByGrowDay;
    }
    //*********************************************************************************************************
}