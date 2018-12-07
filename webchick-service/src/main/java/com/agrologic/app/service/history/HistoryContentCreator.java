package com.agrologic.app.service.history;

import com.agrologic.app.dao.DaoType;
import com.agrologic.app.dao.DataDao;
import com.agrologic.app.dao.DbImplDecider;
import com.agrologic.app.model.Data;
import com.agrologic.app.model.DataFormat;
import org.springframework.dao.EmptyResultDataAccessException;

import java.sql.SQLException;
import java.util.*;

/**
 * Created by Valery on 1/7/14.
 */
public class HistoryContentCreator {
//    private static final int SHIFT_16_BIT = 16;
//    private static final int HIGH_16BIT_ON_MASK = 0x8000;

    /**
     * Creates list with data object that holds the value from management string by grow day .
     *
     * @param historyDataByGrowDayMap the management by grow day map
     * @return HistoryContent the history data content
     */
    public static HistoryContent createPerDayHistoryContent(final Map<Integer, String> historyDataByGrowDayMap) throws SQLException {

        DataDao dataDao = DbImplDecider.use(DaoType.MYSQL).getDao(DataDao.class);
        HistoryContent historyContent = new HistoryContent();

        for (Map.Entry<Integer, String> entry : historyDataByGrowDayMap.entrySet()) {

            List<Data> dayHistLst = new ArrayList<Data>();

            Integer grDay = entry.getKey();
            String histStr = entry.getValue();

            if (!(histStr.equals("-1"))){

                String[] tokens = histStr.split(" ");

                int i = 0;

                while (!(i == tokens.length)) {

                    Long dataId = Long.parseLong(tokens[i]);

                    Long type = dataId;

                    if ((type & 0xC000) != 0xC000) {
                        dataId = (type & 0xFFF);
                    } else {
                        dataId = (type & 0xFFFF);
                    }

                    Data data = null;
                    Long valueH = null;
                    Long valueL = null;
                    Long value = null;

                    try {

                        data = dataDao.getById(dataId);/// empty rezult

                        switch (data.getFormat()) {
                            case DataFormat.DEC_0:

                                value = Long.parseLong(tokens[i + 1]);
                                data.setValue(value);
                                dayHistLst.add(data);
                                i = i + 2;

                                break;

                            case DataFormat.DEC_1: // format 11

                                value = Long.parseLong(tokens[i + 1]);
                                data.setValue(value);
                                dayHistLst.add(data);
                                i = i + 2;

                                break;

                            case DataFormat.DEC_2: // format 2

                                value = Long.parseLong(tokens[i + 1]);
                                data.setValue(value);
                                dayHistLst.add(data);
                                i = i + 2;

                                break;

                            case DataFormat.DEC_3:// format 3

                                value = Long.parseLong(tokens[i + 1]);
                                data.setValue(value);
                                dayHistLst.add(data);
                                i = i + 2;

                                break;

                            case DataFormat.HUMIDITY:

                                value = Long.parseLong(tokens[i + 1]);
                                data.setValue(value);
                                dayHistLst.add(data);
                                i = i + 2;

                                break;

                            case DataFormat.TIME:

                                value = Long.parseLong(tokens[i + 1]);
                                value = DataFormat.convertToTimeFormat(value);
                                data.setValue(value);
                                dayHistLst.add(data);
                                i = i + 2;

                                break;

                            case DataFormat.TIME_SEC:

                                break;

                            case DataFormat.DATE:

                                break;

                            case DataFormat.DEC_4://format 8

                                valueH = Long.parseLong(tokens[i + 1]);
                                valueL = Long.parseLong(tokens[i + 3]);
                                value = ((valueH << 16) & 0xFFFF0000) | (valueL & 0x0000FFFF);
                                data.setValue(value);
                                dayHistLst.add(data);
                                i = i + 4;

                                break;

                            case DataFormat.DEC_5://format 10

                                value = Long.parseLong(tokens[i + 1]);

                                if (value < 0 && value != -1) {
                                    value = Math.abs(value);
                                    value = 65536 - value;
                                }

                                data.setValue(value);
                                dayHistLst.add(data);
                                i = i + 2;

                                break;

                            case DataFormat.DEC_11:

                                value = Long.parseLong(tokens[i + 1]);
                                data.setValue(value);
                                dayHistLst.add(data);
                                i = i + 2;

                                break;

                            case DataFormat.PRICE:

                                break;
                        }
                    } catch (EmptyResultDataAccessException e) {

                    }
                }
            }
            historyContent.addHistoryContentPerDay(grDay, dayHistLst);
        }
        return historyContent;
    }

    public static HistoryContent createPerHourHistoryContent(Integer growDay, Collection<Data> perHourHistoryList) {
        HistoryContent historyContent;
        String historyData;
        List<String> parsedHistoryPerHour;

        historyContent = new HistoryContent();
        historyContent.setGrowDay(growDay);
        for (Data data : perHourHistoryList) {
            historyData = data.getHistoryData();
            parsedHistoryPerHour = parseHistoryPerHour(0, historyData, data.getFormat());
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

//    public DataValueParser invoke(StringTokenizer token, int receivedValue) {
//        String valueString;
//        token.nextToken();// skip this key
//
//        int highValue = receivedValue;
////            boolean negative = ((highValue & HIGH_16BIT_ON_MASK) == 0) ? false : true;
////            if (negative) {
////                // two's compliment action
////                highValue = twosCompliment(highValue);
////            }
////            highValue <<= SHIFT_16_BIT;
//        valueString = token.nextToken();// get low value
//
//        int lowValue = Integer.parseInt(valueString);
////            negative = ((lowValue & HIGH_16BIT_ON_MASK) == 0) ? false : true;
////            if (negative) {
////                // two's compliment action
////                lowValue = twosCompliment(lowValue);
////            }
////            this.receivedValue = highValue&0xFFFF0000 + lowValue&0x0000FFFF;
//        this.receivedValue = ((highValue << 16)&0xFFFF0000) | (lowValue&0x0000FFFF);
//        return this;
//    }



//    public static HistoryContent createPerDayHistoryContent(final Map<Integer, String> historyDataByGrowDayMap, final List<Data> perDayHistoryList) {
//        DataDao DataDao = DbImplDecider.use(DaoType.MYSQL).getDao(DataDao.class);
//        HistoryContent historyContent = new HistoryContent();
//        for (Map.Entry<Integer, String> entry : historyDataByGrowDayMap.entrySet()) {
//            List<Data> actualPerDayHistoryList = new ArrayList<Data>();
//            for (Data data : perDayHistoryList) {
//                if (data.getId() == 800) {
//                    data.setValue(entry.getKey().longValue());
//                    actualPerDayHistoryList.add((Data) data.clone());
//                } else {
//                    if (!entry.getValue().equals("-1")) {
//                        StringTokenizer token = new StringTokenizer(entry.getValue(), " ");
//                        while (token.hasMoreElements() && token.countTokens() >= 4) {
//                            try {
//                                String dataIdString = (String) token.nextElement();
//                                String valueString = (String) token.nextElement();
//
////                                if(dataIdString.equals("-22413")){
////                                    int c = 3 + 3;
////                                }
//
//                                long dataId = Long.parseLong(dataIdString);
////                                int value = Integer.parseInt(valueString);
//
//                                int value = Integer.parseInt(valueString);
//
//                                int type = (int) dataId;// type of value (like 4096)
//                                if ((type & 0xC000) != 0xC000) {
//                                    dataId = (type & 0xFFF); // remove type to get an index 4096&0xFFF -> 0
//                                } else {
//                                    dataId = (type & 0xFFFF);
//                                }
//
//                                Data d = DataDao.getById(Long.valueOf(dataId));
//
////                                if (data.getId().equals(Long.valueOf(dataId))) {//////////////////////////////////
//                                if (d.getId().equals(Long.valueOf(dataId))) {
////                                    if (data.isLongType()) {////////////////////////
//                                    if (d.isLongType()) {
//                                        token.nextToken();// skip this key
//                                        int highValue = value;
//                                        valueString = token.nextToken();// get low value
//                                        value = Integer.parseInt(valueString);
//                                        int lowValue = value;
//                                        value = ((highValue << 16)&0xFFFF0000) | (lowValue&0x0000FFFF);
//                                    }
//                                    else if(d.isDEC_5() && value < 0){
//                                        value = Math.abs(value);
//                                        value = 65536 - value;
//                                    }
//                                    data.setValue(Long.valueOf(value));
//                                    // we need only data than not equals to -1
//                                    if (!data.getValue().equals(-1L)) {
//                                        actualPerDayHistoryList.add((Data) data.clone());
//                                    }
//                                }
//                            } catch (NoSuchElementException e) {
//                                // skip this exception
//                            } catch (SQLException e) {//////////////////////////////
//
//                            }/////////////////////////////////////////////////////
//                        }
//                    }
//                }
//            }
//            Collections.sort(actualPerDayHistoryList);//added 13/09/2017
//            historyContent.addHistoryContentPerDay(entry.getKey(), actualPerDayHistoryList);
//        }
//        return historyContent;
//    }