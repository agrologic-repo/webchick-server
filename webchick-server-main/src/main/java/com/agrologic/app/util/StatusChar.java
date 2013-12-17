package com.agrologic.app.util;

public class StatusChar {
    static final int CHAR_NUMS = 4;
    static char chars[] = new char[]{'|', '/', '-', '\\'};
    static int i = 0;

    public static char getChar() {
        if (i == CHAR_NUMS) {
            i = 0;
        }
        return chars[i++];
    }
}
