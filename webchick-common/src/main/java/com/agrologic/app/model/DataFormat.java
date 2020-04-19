package com.agrologic.app.model;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class DataFormat {

    public static final int DEC_0 = 0;
    public static final int DEC_1 = 1;
    public static final int DEC_2 = 2;
    public static final int DEC_3 = 3;
    public static final int HUMIDITY = 4;
    public static final int TIME = 5;
    public static final int TIME_SEC = 6;
    public static final int DATE = 7;
    public static final int DEC_4 = 8;
    public static final int DEC_5 = 10;
    public static final int DEC_11 = 11;
    public static final int PRICE = 20;
    public static final String DOT_DELIMITER = ".";
    public static final String EMPTY_DELIMITER = "";
    public static final String TIME_DELIMITER = ":";
    public static final String DATE_DELIMITER = "/";
    /**
     * Map of Strings to GraphType objects.
     */
    private static final Map DATA_FORMAT_MAP = new HashMap();

    // initialize a String -> CellinkState map
    static {
        DATA_FORMAT_MAP.put(DEC_0, "xxxx");
        DATA_FORMAT_MAP.put(DEC_1, "xx.x");
        DATA_FORMAT_MAP.put(DEC_2, "xx.xx");
        DATA_FORMAT_MAP.put(DEC_3, "xx.xxx");
        DATA_FORMAT_MAP.put(HUMIDITY, "xxx");
        DATA_FORMAT_MAP.put(TIME, "hh:mm");
        DATA_FORMAT_MAP.put(TIME_SEC, "hh:mm:ss");
        DATA_FORMAT_MAP.put(DATE, "dd/mm/yyyy");
        DATA_FORMAT_MAP.put(DEC_4, "xxxxxx");
        DATA_FORMAT_MAP.put(DEC_5, "xxxxx");
        DATA_FORMAT_MAP.put(DEC_11, "xxx.x");
        DATA_FORMAT_MAP.put(PRICE, "xxx.xx");
    }

    private int currentFormat;

    public DataFormat(int format) {
        this.currentFormat = format;
    }

    public int getFormat() {
        return currentFormat;
    }

    public void setFormat(int format) {
        this.currentFormat = format;
    }

    /**
     * Convert a Format to a String object.
     *
     * @param format The Format
     * @return The String or null
     */
    public String formatToPattern(int format) {
        return (String) DATA_FORMAT_MAP.get(format);
    }

    /**
     * Convert a DataFormat to a String object.
     *
     * @param format
     * @return The String or null
     */
    public static String formatToStringValue(int format, long value) {
        try {
            StringBuilder sb = new StringBuilder();
            String delim;

            sb.append(Long.toString(value));

            int position = 0;
            int len = sb.length();
            int val = (int) value;
            int before_dec, after_dec;

            switch (format) {
                case DEC_0:
                    // do nothing           //xxxx
                    break;

                case DEC_1:

                    delim = DOT_DELIMITER;     // xxx.x
                    if(value < 0){
                        if (len < DEC_1 + 2){
                            len = sb.length() - DEC_1 - 1;
                            if (len <= 0){
                                len = DEC_1 + 2 - sb.length();
                            }
                            while (len > 0){
                                sb.insert(1, "0");
                                len--;
                            }
                        }
                        position = sb.length() - DEC_1;
                        sb.insert(position, delim);
                    } else {
                        if (len < DEC_1 + 1) {
                            len = DEC_1 - len + 1;
                            while (len > 0) {
                                sb.insert(0, "0");
                                len--;
                            }
                        }
                        position = sb.length() - DEC_1;
                        sb.insert(position, delim);
                    }

                    break;

                case DEC_2:

                    delim = DOT_DELIMITER;        // xx.xx
                    if (value < 0){
                        if (len < DEC_2 + 2){
                            len = sb.length() - DEC_2 - 1;
                            if (len <= 0){
                                len = DEC_2 + 2 - sb.length();
                            }
                            while (len > 0){
                                sb.insert(1, "0");
                                len--;
                            }
                        }
                        position = sb.length() - DEC_2;
                        sb.insert(position, delim);
                    } else {
                        if (len < DEC_2 + 1) {
                            len = DEC_2 - len + 1;

                            while (len > 0) {
                                sb.insert(0, "0");
                                len--;
                            }
                        }
                        position = sb.length() - DEC_2;
                        sb.insert(position, delim);
                    }

                    break;

                case DEC_3:

                    delim = DOT_DELIMITER;        // xx.xxx
                    if (value < 0) {
                        if (len < DEC_3 + 2){
                            len = sb.length() - DEC_3 - 1;
                            if (len <= 0){
                                len = DEC_3 + 2 - sb.length();
                            }
                            while (len > 0){
                                sb.insert(1, "0");
                                len--;
                            }
                        }
                        position = sb.length() - DEC_3;
                        sb.insert(position, delim);
                    } else {
                        if (len < DEC_3 + 1) {
                            len = DEC_3 - len + 1;
                                while (len > 0) {
                                    sb.insert(0, "0");
                                    len--;
                                }
                        }
                        position = sb.length() - DEC_3;
                        sb.insert(position, delim);
                    }

                    break;
                case HUMIDITY:                   // xxx
                    break;

                case TIME:

                    if (value < 0){

                    } else {
                        delim = TIME_DELIMITER;       // hh:mm
                        while (len < 4) {
                            sb.insert(0, "0");
                            len++;
                        }
                        position = sb.length() - 2;
                        sb.insert(position, delim);
                    }

                    break;

                case TIME_SEC:

                    if (value < 0){

                    } else {
                        delim = TIME_DELIMITER;    // hh:mm:ss
                        position = sb.length() - 2;
                        sb.insert(position, delim).insert(position - 2, delim);
                    }

                    break;

                case DATE:

                    if (value < 0){

                    } else {
                        int vl = (int) value;
                        int days = ((vl >> 28) & 0xF) * 10 + ((vl >> 24) & 0xF);
                        int months = ((vl >> 20) & 0xF) * 10 + ((vl >> 16) & 0xF);
                        int years = ((vl >> 12) & 0xF) * 1000 + ((vl >> 8) & 0xF) * 100 + ((vl >> 4) & 0xF) * 10 + (vl & 0xF);
                        sb = new StringBuilder();
                        sb.append(days).append(DATE_DELIMITER).append(months).append(DATE_DELIMITER).append(years);
                    }

                    break;

                case DEC_4:
                    // XXXXXX
                    break;

                case DEC_5: // xxxxx

                    if (value < 0 && value != -1) {

                        value = Math.abs(value);
                        value = 65536 - value;

                        sb = new StringBuilder();
                        sb.append(Long.toString(value));

                    } else {

                    }

                    break;

                case DEC_11:

                    if(value < 0){

                    } else {
                        before_dec = (val / 1000) * 100;
                        before_dec = before_dec + ((val % 1000) / 100) * 10;
                        before_dec = before_dec + (((val % 1000) % 100) / 10);
                        after_dec = (((val % 1000) % 100) % 10);
                        sb = new StringBuilder();
                        sb.append(before_dec).append(DOT_DELIMITER).append(after_dec);
                    }

                    break;

                case PRICE:

                    if (value < 0) {

                    } else {
                        before_dec = (val / 1000) * 100;
                        before_dec = before_dec + ((val % 1000) / 100) * 10;
                        before_dec = before_dec + (((val % 1000) % 100) / 10);
                        after_dec = (((val % 1000) % 100) % 10);
                        sb = new StringBuilder();
                        sb.append(before_dec).append(DOT_DELIMITER).append(after_dec);
                    }


                    break;

                default:
                    break;
            }
            return String.format(Locale.ENGLISH, sb.toString());
        } catch (RuntimeException ex) {
            return "";
        }
    }

    /**
     * Returns a string representation of the current <tt>DataFormat</tt>.
     */
    public String pattern() {
        return formatToPattern(currentFormat);
    }

    /**
     * Convert value to string representation of the current <tt>DataFormat</tt>.
     *
     * @param value the value
     * @return a string representation of the current <tt>DataFormat</tt>.
     */
    public String toStringValue(long value) {
        return formatToStringValue(currentFormat, value);
    }

    @Override
    public String toString() {
        return pattern();
    }

    public static long convertToTimeFormat(long value) {
        long newValue;
        String hexString = Long.toHexString(value);
        if (!isLetter(hexString)) {
            newValue = Long.parseLong(hexString);
        } else {
            newValue = value;
        }
        return newValue;
    }

    public static boolean isLetter(String s) {
        for (Character c : s.toCharArray()) {
            if (Character.isLetter(c)) {
                return true;
            }
        }

        return false;
    }

    public static Long convertToPositiveValue(Long value){

        if (value < 0 && value != -1) {
            value = Math.abs(value);
            value = 65536 - value;
        }

        return value;
    }

    public static Long convertDataId(long receivedDataId){
        if (((int) receivedDataId & 0xC000) != 0xC000) {
            receivedDataId = ((int) receivedDataId & 0xFFF);   // remove type to get an index 4096&0xFFF -> 0
        } else {
            receivedDataId = ((int) receivedDataId & 0xFFFF);
        }
        return receivedDataId;
    }

    public static Long convertHLValue (Long valueH, Long valueL){

        return ((valueH << 16) & 0xFFFF0000) | (valueL & 0x0000FFFF);

    }
}


