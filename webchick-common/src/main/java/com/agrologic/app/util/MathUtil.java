package com.agrologic.app.util;

import java.text.DecimalFormat;

public class MathUtil {
    public static float setTwoDecimalPoint(float value) {
        DecimalFormat newFormat = new DecimalFormat("#.##");
        return Float.valueOf(newFormat.format(value));
    }
}
