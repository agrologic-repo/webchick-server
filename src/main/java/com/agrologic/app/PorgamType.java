/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.agrologic.app;

/**
 *
 * @author Administrator
 */
public enum PorgamType {

    APPSERVER(1),
    DERBYWIZARD(2);
    private int type;

    private PorgamType(int t) {
        this.type = t;
    }

    public static PorgamType get(int type) {
        switch (type) {
            case 1:
                return PorgamType.APPSERVER;

            case 2:
                return PorgamType.DERBYWIZARD;

            default:
                return PorgamType.APPSERVER;
        }
    }
}
