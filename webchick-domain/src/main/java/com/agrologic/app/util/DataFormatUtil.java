
/*
* To change this template, choose Tools | Templates
* and open the template in the editor.
 */
package com.agrologic.app.util;

/**
 * {Insert class description here}
 *
 * @author Valery Manakhimov
 * @author $Author: nbweb $, (this version)
 * @version $Revision: 1.1.1.1 $
 * @since Build {insert version here} (MM YYYY)
 */
public class DataFormatUtil {
    public static final int DATE = 7;
    public static final String DATE_DELIM = "/";
    public static final int DEC_0 = 0;
    public static final int DEC_1 = 1;
    public static final int DEC_2 = 2;
    public static final int DEC_3 = 3;
    public static final int DEC_4 = 8;
    public static final int DEC_5 = 10;
    public static final int DEC__ = 9;
    public static final String DOT_DELIM = ".";
    public static final String EMPTY_DELIM = "";
    public static final int HUMIDITY = 4;
    public static final int TIME = 5;
    public static final String TIME_DELIM = ":";
    public static final int TIME_SEC = 6;

    public static boolean isDelimtChar(char c) {
        if ((c == '.') || (c == ':') || (c == '/')) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isAnyDelimtExist(String s) {
        if ((s.indexOf(DOT_DELIM) > -1) || (s.indexOf(DATE_DELIM) > -1) || (s.indexOf(TIME_DELIM) > -1)) {
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

        string = clearDelimt(string);

        double number = Double.parseDouble(string);

        switch (format) {
            case DEC_0:
                string = clearZero(string);

                if (string.length() > 4) {
                    string = string.substring(1, string.length());
                    string = clearZero(string);
                }

                break;

            case DEC_1:
                number /= 10;
                string = String.format("%.1f", number);

                if (string.length() > 4) {
                    string = string.substring(1, string.length());
                }

                break;

            case DEC_2:
                number /= 100;
                string = String.format("%.2f", number);

                if (string.length() > 5) {
                    string = string.substring(1, string.length());
                }

                break;

            case DEC_3:
                number /= 1000;
                string = String.format("%.3f", number);

                if (string.length() > 5) {
                    string = string.substring(1, string.length());
                }

                break;

            case DEC_4:
                string = clearZero(string);

                if (string.length() > 6) {
                    string = string.substring(1, string.length());
                    string = clearZero(string);
                }

                break;

            case DEC_5:
                string = clearZero(string);

                if (string.length() > 5) {
                    string = string.substring(1, string.length());
                    string = clearZero(string);
                }

                break;

            case HUMIDITY:
                string = clearZero(string);

                if (string.length() > 3) {
                    string = string.substring(1, string.length());
                    string = clearZero(string);
                }

                break;

            case TIME:
                number /= 100;
                string = String.format("%.2f", number);

                if (string.length() > 5) {
                    string = string.substring(1, string.length());
                }

                string = string.replace(".", ":");

                break;

            case DATE:
                number /= 100;
                string = Double.toString(number);
                string = String.format("%.2f", number);

                if (string.length() > 5) {
                    string = string.substring(1, string.length());
                }

                string = string.replace(".", "/");

                break;
        }

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

    public static String clearDelimt(String s) {
        String string = s;

        if (string.indexOf(".") > -1) {
            string = string.replace(".", "");
        }

        if (string.indexOf(":") > -1) {
            string = string.replace(":", "");
        }

        if (string.indexOf("/") > -1) {
            string = string.replace("/", "");
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


