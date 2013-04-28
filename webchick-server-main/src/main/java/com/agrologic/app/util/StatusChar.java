/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.agrologic.app.util;

/**
 * {Insert class description here}
 *
 * @version $Revision: 1.1.1.1 $
 * @since Build {insert version here} (MM YYYY)
 * @author Valery Manakhimov
 * @author $Author: nbweb $, (this version)
 */
public class StatusChar {
    static final int CHAR_NUMS = 4;
    static char chars[] = new char[] {'|','/','-','\\'};
    static int i = 0;

    public static char getChar() {
        if(i == CHAR_NUMS) {
            i = 0;
        }
        return chars[i++];
    }
}
