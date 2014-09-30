package com.agrologic.app.service.history;

import com.agrologic.app.model.Data;
import com.agrologic.app.model.DataFormat;

import java.util.*;

/**
 * Created by Valery on 1/7/14.
 */
public class HistoryContentCreator {
    private static final int SHIFT_16_BIT = 16;
    private static final int HIGH_16BIT_ON_MASK = 0x8000;

    /**
     * Creates list with data object that holds the value from management string by grow day .
     *
     * @param historyDataByGrowDayMap the management by grow day map
     * @return HistoryContent the history data content
     */
    public static HistoryContent createPerDayHistoryContent(final Map<Integer, String> historyDataByGrowDayMap,
                                                            final List<Data> perDayHistoryList) {
        HistoryContent historyContent = new HistoryContent();
        for (Map.Entry<Integer, String> entry : historyDataByGrowDayMap.entrySet()) {
            List<Data> actualPerDayHistoryList = new ArrayList<Data>();
            for (Data data : perDayHistoryList) {
                if (data.getId() == 800) {
                    data.setValue(entry.getKey().longValue());
                    actualPerDayHistoryList.add((Data) data.clone());
                } else {
                    if (!entry.getValue().equals("-1")) {
                        StringTokenizer token = new StringTokenizer(entry.getValue(), " ");
                        while (token.hasMoreElements() && token.countTokens() >= 4) {
                            try {
                                String dataIdString = (String) token.nextElement();
                                String valueString = (String) token.nextElement();

                                long dataId = Long.parseLong(dataIdString);
                                int value = Integer.parseInt(valueString);

                                int type = (int) dataId;// type of value (like 4096)
                                if ((type & 0xC000) != 0xC000) {
                                    dataId = (type & 0xFFF); // remove type to get an index 4096&0xFFF -> 0
                                } else {
                                    dataId = (type & 0xFFFF);
                                }

                                if (data.getId().equals(Long.valueOf(dataId))) {
                                    if (data.isLongType()) {
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
                                    data.setValue(Long.valueOf(value));
                                    // we need only data than not equals to -1
                                    if (!data.getValue().equals(-1L)) {
                                        actualPerDayHistoryList.add((Data) data.clone());
                                    }
                                }
                            } catch (NoSuchElementException e) {
                                // skip this exception
                            }
                        }
                    }
                }
            }
            historyContent.addHistoryContentPerDay(entry.getKey(), actualPerDayHistoryList);
        }
        return historyContent;
    }

    public static HistoryContent createPerHourHistoryContent(Integer growDay, Collection<Data> perHourHistoryList) {
        HistoryContent historyContent = new HistoryContent();
        historyContent.setGrowDay(growDay);
        for (Data data : perHourHistoryList) {
            String historyData = data.getHistoryData();
            List<String> parsedHistoryPerHour = parseHistoryPerHour(0, historyData, data.getFormat());
            historyContent.addHistoryContentPerHour(data, parsedHistoryPerHour);
        }
        return historyContent;
    }

    private static List<String> parseHistoryPerHour(long resetTime, String values, Integer format) {
        String[] valueList = values.split(" ");
        List<String> valuePerHour = new ArrayList<String>();
        int j = (int) resetTime / 100;
        for (int i = 0; i < valueList.length; i++) {
            if (j == 24) {
                j = 0;
            }
            valuePerHour.add(DataFormat.formatToStringValue(format, Long.valueOf(valueList[i])));
        }
        return valuePerHour;
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
}

