package com.agrologic.app.service.history;

import com.agrologic.app.dao.DaoType;
import com.agrologic.app.dao.DataDao;
import com.agrologic.app.dao.DbImplDecider;
import com.agrologic.app.model.Data;
import com.agrologic.app.model.DataFormat;
import org.springframework.dao.EmptyResultDataAccessException;

import java.sql.SQLException;
import java.util.*;


public class HistoryContentCreator {

    public static HistoryContent createPerDayHistoryContent(final Map<Integer, String> historyDataByGrowDayMap) throws SQLException {

        DataDao dataDao = DbImplDecider.use(DaoType.MYSQL).getDao(DataDao.class);
        HistoryContent historyContent = new HistoryContent();

        for (Map.Entry<Integer, String> entry : historyDataByGrowDayMap.entrySet()) {

            List<Data> dayHistLst = new ArrayList<Data>();

            Integer grDay = entry.getKey();
            String histStr = entry.getValue();

//            if (!(histStr.equals("-1")) && !(histStr.equals("SOT Error")) && !(histStr.equals("Timeout Error")) && !(histStr.equals("Request Error"))){
            if (!(histStr.equals("-1"))){

                String[] tokens = histStr.split(" ");

                int i = 0;

                while (!(i == tokens.length)) {

                    Long dataId = Long.parseLong(tokens[i]);

//                    if (dataId < 0){
//                        dataId = dataId + 65536;
//                    }
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

                        data = dataDao.getById(dataId);

                        switch (data.getFormat()) {
                            case DataFormat.DEC_0:

//                                value = Long.parseLong(tokens[i + 1]);
                                data.setValue(Long.parseLong(tokens[i + 1]));
                                dayHistLst.add(data);
                                i = i + 2;

                                break;

                            case DataFormat.DEC_1:

//                                value = Long.parseLong(tokens[i + 1]);
                                data.setValue(Long.parseLong(tokens[i + 1]));
                                dayHistLst.add(data);
                                i = i + 2;

                                break;

                            case DataFormat.DEC_2:

//                                value = Long.parseLong(tokens[i + 1]);
                                data.setValue(Long.parseLong(tokens[i + 1]));
                                dayHistLst.add(data);
                                i = i + 2;

                                break;

                            case DataFormat.DEC_3:

//                                value = Long.parseLong(tokens[i + 1]);
                                data.setValue(Long.parseLong(tokens[i + 1]));
                                dayHistLst.add(data);
                                i = i + 2;

                                break;

                            case DataFormat.HUMIDITY:

//                                value = Long.parseLong(tokens[i + 1]);
                                data.setValue(Long.parseLong(tokens[i + 1]));
                                dayHistLst.add(data);
                                i = i + 2;

                                break;

                            case DataFormat.TIME:

//                                value = Long.parseLong(tokens[i + 1]);
                                value = DataFormat.convertToTimeFormat(Long.parseLong(tokens[i + 1]));
                                data.setValue(value);
                                dayHistLst.add(data);
                                i = i + 2;

                                break;

                            case DataFormat.TIME_SEC:

                                break;

                            case DataFormat.DATE:

                                break;

                            case DataFormat.DEC_4:

//                                valueH = Long.parseLong(tokens[i + 1]);
//                                valueL = Long.parseLong(tokens[i + 3]);
//                                value = ((valueH << 16) & 0xFFFF0000) | (valueL & 0x0000FFFF);
                                value = DataFormat.convertHLValue(Long.parseLong(tokens[i + 1]), Long.parseLong(tokens[i + 3]));

                                data.setValue(value);
                                dayHistLst.add(data);
                                i = i + 4;

                                break;

                            case DataFormat.DEC_5:

//                                value = Long.parseLong(tokens[i + 1]);

//                                if (value < 0 && value != -1) {
//                                    value = Math.abs(value);
//                                    value = 65536 - value;
//                                }
                                value = DataFormat.convertToPositiveValue(Long.parseLong(tokens[i + 1]));

                                data.setValue(value);
                                dayHistLst.add(data);
                                i = i + 2;

                                break;

                            case DataFormat.DEC_11:

//                                value = Long.parseLong(tokens[i + 1]);
                                data.setValue(Long.parseLong(tokens[i + 1]));
                                dayHistLst.add(data);
                                i = i + 2;

                                break;

                            case DataFormat.PRICE:

                                break;
                        }
                    } catch (EmptyResultDataAccessException e) {
                        try {
                            data = dataDao.getById(dataId);
                        } catch (EmptyResultDataAccessException ex){
                            i = i + 2;
                        }
                        if (data != null) {
                            data.setValue(Long.parseLong(tokens[i + 1]));
                            dayHistLst.add(data);
                            i = i + 2;
                        }
                    }
                }
                historyContent.addHistoryContentPerDay(grDay, dayHistLst);
            } else {
                historyContent.addHistoryContentPerDay(grDay, dayHistLst);
            }
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


    public static List<Data> history_str_to_list_without_value(String str, Long lang_id) throws SQLException {

        DataDao dataDao = DbImplDecider.use(DaoType.MYSQL).getDao(DataDao.class);
        List<Data> list = new ArrayList<Data>();

//        if (!(str.equals("-1")) && !(str.equals("SOT Error")) && !(str.equals("Timeout Error")) && !(str.equals("Request Error"))){
        if (!(str.equals("-1"))){

            String[] tokens = str.split(" ");

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

                try {

                    data = dataDao.getById(dataId, lang_id);

                    switch (data.getFormat()) {

                        case DataFormat.DEC_0:
                            list.add(data);
                            i = i + 2;
                            break;

                        case DataFormat.DEC_1:
                            list.add(data);
                            i = i + 2;
                            break;

                        case DataFormat.DEC_2:
                            list.add(data);
                            i = i + 2;
                            break;

                        case DataFormat.DEC_3:
                            list.add(data);
                            i = i + 2;
                            break;

                        case DataFormat.HUMIDITY:
                            list.add(data);
                            i = i + 2;
                            break;

                        case DataFormat.TIME:
                            list.add(data);
                            i = i + 2;
                            break;

                        case DataFormat.TIME_SEC:
                            break;

                        case DataFormat.DATE:
                            break;

                        case DataFormat.DEC_4:
                            list.add(data);
                            i = i + 4;
                            break;

                        case DataFormat.DEC_5:
                            list.add(data);
                            i = i + 2;
                            break;

                        case DataFormat.DEC_11:
                            list.add(data);
                            i = i + 2;
                            break;

                        case DataFormat.PRICE:
                            break;
                        }

                } catch (EmptyResultDataAccessException e) {

                    try {
                        data = dataDao.getById(dataId);
                    } catch (EmptyResultDataAccessException ex){
                        i = i + 2;
                    }
                    if (data != null) {
                        data.setUnicodeLabel(data.getLabel());
                        list.add(data);
                        i = i + 2;
                    }
                }
            }
        } else {
            return null;
        }
        return list;
    }
}