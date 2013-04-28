
/*
* To change this template, choose Tools | Templates
* and open the template in the editor.
 */
package com.agrologic.app.excel;



import com.agrologic.app.model.DataDto;
import com.agrologic.app.model.DataFormat;
import java.util.*;

public class DataForExcelCreator {

    /**
     * Creates list with data object that holds the value from history24 string by hour.
     *
     * @param historyByGrowDayMap the history by grow day map
     * @param data the object that encapsulate data.
     * @return dataByGrowDay the data by grow day map
     */
    /**
     *
     * @param historyByHourMap
     * @param format
     * @return
     */
    public static List<String> createDataHistory24List(final Map<Integer, String> historyByHourMap, final int format) {
        List<String>       dataByHour   = new ArrayList<String>();
        Collection<String> stringValues = historyByHourMap.values();

        for (String stringValue : stringValues) {
            dataByHour.add(DataFormat.formatToStringValue(format, Long.valueOf(stringValue)));
        }

        return dataByHour;
    }

    /**
     * Creates list with data object that holds the value from history string by grow day.
     *
     * @param historyByGrowDayMap the history by grow day map
     * @param data the object that encapsulate data.
     * @return dataByGrowDay the data by grow day map
     */
    public static List<String> createDataHistoryList(final Map<Integer, String> historyByGrowDayMap,
            final DataDto data) {
        List<String>       dataByGrowDay = new ArrayList<String>();
        Collection<String> stringValues  = historyByGrowDayMap.values();

        for (String stringValue : stringValues) {
            StringTokenizer token = new StringTokenizer(stringValue, " ");

            while (token.hasMoreElements()) {
                try {
                    String dataElem = (String) token.nextElement();

                    if (dataElem.equals(data.getType().toString())) {
                        String svalue = (String) token.nextElement();

                        data.setValue(Long.parseLong(svalue));

                        String value = valueToString(data);

                        dataByGrowDay.add(value);
                    } else {
                        token.nextElement();
                    }
                } catch (NoSuchElementException e) {
                    e.printStackTrace();
                }
            }
        }

        return dataByGrowDay;
    }

    /**
     *
     * @param data
     * @return
     */
    private static String valueToString(DataDto data) {
        Long value = data.getValue();

        if (value == -1) {
            return "-1";
        }

        if (DataFormat.TIME == data.getFormat()) {
            long h = value / 100;
            long m = value % 100;
            long t = h * 60 + m;

            return String.valueOf(t);
        }

        return data.getFormatedValue();
    }

    /**
     *
     * @param growDays
     * @return
     */
    public static List<String> createDataList(final Set<Integer> growDays) {
        List<String>      dataByGrowDay = new ArrayList<String>();
        Iterator<Integer> iter          = growDays.iterator();

        while (iter.hasNext()) {
            Integer value = iter.next();

            dataByGrowDay.add(value.toString());
        }

        return dataByGrowDay;
    }
}



