
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.agrologic.app.table;


import com.agrologic.app.model.DataDto;

//~--- JDK imports ------------------------------------------------------------

import java.util.*;

public class TableOfHistoryCreator {

    /**
     * Create history data by grow day.
     *
     * @param history the history map per grow day.
     * @param data the searching data .
     * @return histDataByGrowDay the map with searched history data per grow day
     */
    public static Map<Integer, DataDto> createHistDataByGrowDay(final Map<Integer, String> history,
            final DataDto data) {
        Map<Integer, DataDto> histDataByGrowDay = new TreeMap<Integer, DataDto>();
        Iterator<Integer> iter = history.keySet().iterator();

        while (iter.hasNext()) {
            Integer key = iter.next();
            DataDto dataFromHist = getDataFromHist(data, history.get(key));

            if (dataFromHist != null) {
                histDataByGrowDay.put(key, dataFromHist);
            } else {
                histDataByGrowDay.put(key, null);
            }
        }

        return histDataByGrowDay;
    }

    /**
     * Create history data by grow day.
     *
     * @param history the history map per grow day.
     * @param searchData the searching data .
     * @return histDataByGrowDay the map with searched history data per grow day
     */
    public static Map<Integer, DataDto> createEggCountHistDataByGrowDay(final Map<Integer, String> history,
            final DataDto searchData) {
        int SHIFT_16_BIT        = 16;
        int HIGH_16BIT_ON_MASK  = 0x8000;
        int HIGH_32BIT_OFF_MASK = 0x0000FFFF;

        Map<Integer, DataDto> histDataByGrowDay = new TreeMap<Integer, DataDto>();
        Iterator<Integer> iter = history.keySet().iterator();
        while (iter.hasNext()) {
            Integer key = iter.next();
            DataDto dataFromHist = null;
            StringTokenizer token = new StringTokenizer(history.get(key), " ");

            while (token.hasMoreElements() && token.countTokens() >= 4) {
                try {
                    String dataIdString = (String) token.nextElement();
                    String valueString = (String) token.nextElement();
                    String dataType = searchData.getId().toString();

                    long dataId = Long.parseLong(dataIdString);
                    int value = Integer.parseInt(valueString);
                    int type = (int) dataId;// type of value (like 4096)
                    if ((type & 0xC000) != 0xC000) {
                        dataId = (type & 0xFFF); // remove type to get an index 4096&0xFFF -> 0
                    } else {
                        dataId = (type & 0xFFFF);
                    }
                    dataIdString = String.valueOf(dataId);

                    if (dataIdString.equals(dataType)) {
                        if (searchData.isLong()) {
                            token.nextToken();// skip this key
                            int highValue = value;
                            boolean negative = ((highValue & HIGH_16BIT_ON_MASK) == 0) ? false : true;
                            if (negative) {
                                // two's compliment action
                                highValue = twosCompliment(highValue);
                            }
                            highValue <<= SHIFT_16_BIT;
                            valueString = token.nextToken();// get low value
                            value = Integer.parseInt(valueString);

                            int lowValue = value;
                            negative = ((lowValue & HIGH_16BIT_ON_MASK) == 0) ? false : true;
                            if (negative) {
                                // two's compliment action
                                lowValue = twosCompliment(lowValue);
                            }
                            value = highValue + lowValue;
                        }
                        valueString = String.valueOf(value);
                        searchData.setValue(Long.valueOf(valueString));
                        dataFromHist = (DataDto) searchData.clone();

                        break;
                    }
                } catch (NoSuchElementException e) {
                    e.printStackTrace();
                }
            }
            if (dataFromHist != null) {
                histDataByGrowDay.put(key, dataFromHist);
            }
        }

        return histDataByGrowDay;
    }

    /**
     * Return number after two's compliment for integer type.
     *
     * @param val the number
     * @return number the number after two's compliment
     */
    private static int twosCompliment(int val) {
        final int HIGH_32BIT_OFF_MASK = 0x0000FFFF;
        int tVal = val;
        if (tVal != -1) {
            //two's compliment action
            tVal = Math.abs(tVal);
            tVal = ~tVal;
            tVal &= HIGH_32BIT_OFF_MASK;
            tVal += 1;
        }
        return tVal;
    }

    /**
     *
     * @param growDays
     * @return
     */
    public static Map<Integer, DataDto> createGrowDayList(final Map<Integer, String> history, DataDto data) {
        Map<Integer, DataDto> histDataByGrowDay = new TreeMap<Integer, DataDto>();
        Iterator<Integer> iter = history.keySet().iterator();

        while (iter.hasNext()) {
            Integer key = iter.next();

            data.setValue((long) key);
            histDataByGrowDay.put(key, data);
        }

        return histDataByGrowDay;
    }

    /**
     * Return data object with value from history string.
     *
     * @param searchData the data which encapsulate all field except value.
     * @param histString the string with all history pairs data and value .
     * @return foundData the data object with value from history string, or null.
     */
    private static DataDto getDataFromHist(DataDto searchData, String histString) {
        StringTokenizer token = new StringTokenizer(histString, " ");
        DataDto foundData = null;

        while (token.hasMoreElements()) {
            try {
                if (token.countTokens() < 2) {
                    return null;
                }

                String dataElem = (String) token.nextElement();
                String valElem = (String) token.nextElement();
                String dataType = searchData.getType().toString();

                if (dataElem.equals(dataType)) {
                    searchData.setValue(Long.valueOf(valElem));
                    foundData = (DataDto) searchData.clone();

                    break;
                }
            } catch (NoSuchElementException e) {
                e.printStackTrace();
            }
        }

        return foundData;
    }
}



