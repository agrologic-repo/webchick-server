package com.agrologic.app.model.history;

public class DayParam {
    private int growDay;

    public DayParam(String growDay) {
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
