package com.agrologic.app.dao.mappers;

import com.agrologic.app.model.DataFormat;

import java.util.Locale;

public class DataFormatUtil {
    public static final Locale DEFAULT_LOCALE = Locale.ENGLISH;

    public static boolean isDelimiterChar(char c) {
        if ((String.valueOf(c).equals(DataFormat.DOT_DELIMITER)) ||
                (String.valueOf(c).equals(DataFormat.TIME_DELIMITER) ||
                        (String.valueOf(c).equals(DataFormat.DATE_DELIMITER)))) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isDelimiterExist(String s) {
        if ((s.indexOf(DataFormat.DOT_DELIMITER) > -1) ||
                (s.indexOf(DataFormat.DATE_DELIMITER) > -1) ||
                (s.indexOf(DataFormat.TIME_DELIMITER) > -1)) {
            return true;
        } else {
            return false;
        }
    }

    public static String fixDecPoint(String s, int format) {
        if (s.equals("")) {
            return "";
        }

        String string = s;

        string = clearDelimiter(string);

        double number = Double.parseDouble(string);

        switch (format) {
            case DataFormat.DEC_0:
                string = clearZero(string);

                if (string.length() > 4) {
                    string = string.substring(1, string.length());
                    string = clearZero(string);
                }

                break;

            case DataFormat.DEC_1:
                number /= 10;
                string = String.format(DEFAULT_LOCALE, "%.1f", number);

                if (string.length() > 4) {
                    string = string.substring(1, string.length());
                }

                break;

            case DataFormat.DEC_2:
                number /= 100;
                string = String.format(DEFAULT_LOCALE, "%.2f", number);

                if (string.length() > 5) {
                    string = string.substring(1, string.length());
                }

                break;

            case DataFormat.DEC_3:
                number /= 1000;
                string = String.format(DEFAULT_LOCALE, "%.3f", number);

                if (string.length() > 5) {
                    string = string.substring(1, string.length());
                }

                break;

            case DataFormat.DEC_4:
                string = clearZero(string);

                if (string.length() > 6) {
                    string = string.substring(1, string.length());
                    string = clearZero(string);
                }

                break;

            case DataFormat.DEC_5:
                string = clearZero(string);

                if (string.length() > 5) {
                    string = string.substring(1, string.length());
                    string = clearZero(string);
                }

                break;

            case DataFormat.HUMIDITY:
                string = clearZero(string);

                if (string.length() > 3) {
                    string = string.substring(1, string.length());
                    string = clearZero(string);
                }

                break;

            case DataFormat.TIME:
                number /= 100;
                string = String.format(DEFAULT_LOCALE, "%.2f", number);

                if (string.length() > 5) {
                    string = string.substring(1, string.length());
                }
                string = string.replace(DataFormat.DOT_DELIMITER, DataFormat.TIME_DELIMITER);

                break;

            case DataFormat.DATE:
                number /= 100;
                string = String.format(DEFAULT_LOCALE, "%.2f", number);

                if (string.length() > 5) {
                    string = string.substring(1, string.length());
                }
                string = string.replace(DataFormat.DOT_DELIMITER, DataFormat.DATE_DELIMITER);

                break;
        }
//        return String.format(Locale.GERMAN, string);
        return string;
    }

    private static String clearZero(String s) {
        String string = s;
        boolean allZero = true;

        for (char c : string.toCharArray()) {
            if (c != '0') {
                allZero = false;
            }
        }

        if (allZero) {
            string = "0";
        } else {
            int zerocount = countFirstZero(string);

            string = string.substring(zerocount);
        }

        return string;
    }

    public static String clearDelimiter(String s) {
        String string = s;
        if (string.indexOf(DataFormat.DOT_DELIMITER) > -1) {
            string = string.replace(DataFormat.DOT_DELIMITER, DataFormat.EMPTY_DELIMITER);
        }

        if (string.indexOf(DataFormat.TIME_DELIMITER) > -1) {
            string = string.replace(DataFormat.TIME_DELIMITER, DataFormat.EMPTY_DELIMITER);
        }

        if (string.indexOf(DataFormat.DATE_DELIMITER) > -1) {
            string = string.replace(DataFormat.DATE_DELIMITER, DataFormat.EMPTY_DELIMITER);
        }
        return string;
    }

    private static int countFirstZero(String s) {
        if (s.length() == 0) {
            return 0;
        } else {
            if (s.charAt(0) == '0') {
                return 1 + countFirstZero(s.substring(1));
            } else {
                return 0;
            }
        }
    }
}


