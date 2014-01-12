package com.agrologic.app.management;

import com.agrologic.app.model.Data;

import java.util.*;

public class TableContentCreator {

    /**
     * Create management data by grow day.
     *
     * @param history    the management map per grow day.
     * @param searchData the searching data .
     * @return histDataByGrowDay the map with searched management data per grow day
     */
    public static Map<Integer, Data> createEggCountHistDataByGrowDay(final Map<Integer, String> history,
                                                                     final Data searchData) {
        int SHIFT_16_BIT = 16;
        int HIGH_16BIT_ON_MASK = 0x8000;

        Map<Integer, Data> histDataByGrowDay = new TreeMap<Integer, Data>();
        Iterator<Integer> iter = history.keySet().iterator();
        while (iter.hasNext()) {
            Integer key = iter.next();
            Data dataFromHist = null;
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
                        if (searchData.isLongType()) {
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
                        dataFromHist = (Data) searchData.clone();

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

    public static Map<Integer, Data> createGrowDayList(final Map<Integer, String> history, Data data) {
        Map<Integer, Data> histDataByGrowDay = new TreeMap<Integer, Data>();
        Iterator<Integer> iter = history.keySet().iterator();

        while (iter.hasNext()) {
            Integer key = iter.next();

            data.setValue((long) key);
            histDataByGrowDay.put(key, data);
        }

        return histDataByGrowDay;

    }
}



