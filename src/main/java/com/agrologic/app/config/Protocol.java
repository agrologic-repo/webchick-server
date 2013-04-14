/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.agrologic.app.config;

/**
 *
 * @author Administrator
 */
public enum Protocol {

    LOW_ASCII(0, "2400", "Text 2400 bps"),
    HIGH_ASCII(1, "9600", "Text 9600 bps"),
    LOW_BINARY(2, "2400", "Binary 2400 bps"),
    HIGH_BINARY(3, "9600", "Binary 9600 bps");
    private Integer num;
    private String baud;
    private String text;

    Protocol(Integer n, String b, String t) {
        num = n;
        baud = b;
        text = t;
    }

    public String getBaud() {
        return baud;
    }

    public Integer getNum() {
        return num;
    }

    public String getText() {
        return text;
    }

    public static Protocol get(Integer n) {
        switch (n) {
            case 0:
                return Protocol.LOW_ASCII;
            case 1:
                return Protocol.HIGH_ASCII;
            case 2:
                return Protocol.LOW_BINARY;
            case 3:
                return Protocol.HIGH_BINARY;
            default:
                return Protocol.LOW_ASCII;
        }
    }
}
