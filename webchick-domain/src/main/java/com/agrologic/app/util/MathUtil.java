/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.agrologic.app.util;

import java.text.DecimalFormat;

/**
 * {Insert class description here}
 *
 * @version $Revision: 1.1.1.1 $
 * @since Build {insert version here} (MM YYYY)
 * @author Valery Manakhimov
 * @author $Author: nbweb $, (this version)
 */
public class MathUtil {
    public static float setTwoDecimalPoint(float value) {
        DecimalFormat newFormat = new DecimalFormat("#.##");
        return Float.valueOf(newFormat.format(value));
    }
}
