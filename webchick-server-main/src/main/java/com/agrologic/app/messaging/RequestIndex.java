
/*
* To change this template, choose Tools | Templates
* and open the template in the editor.
 */
package com.agrologic.app.messaging;

import com.agrologic.app.util.StringUtil;

public class RequestIndex {

    /**
     * the max index of request
     */
    private static final int MAX_INDEX = 99;

    /**
     * the index
     */
    private int index;

    public RequestIndex() {
        resetIndex();
    }

    /**
     * Return current request index
     *
     * @return the index
     */
    public final String getIndex() {
        if (index < 10) {
            return "0".concat(StringUtil.intToString(index));
        } else {
            return StringUtil.intToString(index);
        }
    }

    /**
     * Increments index by 1 , if index equals MAX_INDEX call resetIndex().
     */
    public final void nextIndex() {
        if (index == MAX_INDEX) {
            resetIndex();
        } else {
            index++;
        }
    }

    /**
     * Return index 00 if error appear
     *
     * @return index 00 if error appear
     */
    public String getErrorIndex() {
        index = 0;

        return "0" + index;
    }

    /**
     * Set index value to 1
     */
    public final void resetIndex() {
        index = 1;
    }

    @Override
    public String toString() {
        return new StringBuilder().append("RequestIndex : " + index).toString();
    }
}