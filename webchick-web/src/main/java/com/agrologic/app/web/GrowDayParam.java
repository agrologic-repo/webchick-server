package com.agrologic.app.web;

public class GrowDayParam {
    public static final String GROW_DAY = " GROW DAY ";
    private int growDay;

    public GrowDayParam(String growDay) {
        try {
            this.growDay = Integer.parseInt(growDay);
        } catch (Exception e) {
            this.growDay = 1;
        }
    }

    public int getGrowDay() {
        return growDay;
    }

    public void setGrowDay(int growDay) {
        this.growDay = growDay;
    }
}
