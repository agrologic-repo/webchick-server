package com.agrologic.app.model.history;

public class FromDayToDayParam {
    public static final String FROM = " FROM ";
    public static final String TO = " TO ";
    public static final String GROW_DAY = " GROW DAY ";
    private int growDay;
    private int fromDay;
    private int toDay;
    private boolean useRange = false;

    public FromDayToDayParam() {
        this.growDay = 1;
        this.useRange = false;
    }

    public FromDayToDayParam(String growDay) {
        try {
            this.growDay = Integer.parseInt(growDay);
            this.useRange = false;
        } catch (Exception e) {
            this.growDay = -1;
        }
    }

    public FromDayToDayParam(String fromDay, String toDay) {
        try {
            this.fromDay = Integer.parseInt(fromDay);
            this.toDay = Integer.parseInt(toDay);
            if (this.fromDay != -1 && this.toDay != -1) {
                setUseRange(true);
            }
        } catch (Exception e) {
            this.fromDay = -1;
            this.toDay = -1;
        }
    }

    public int getGrowDay() {
        return growDay;
    }

    public void setGrowDay(int growDay) {
        this.growDay = growDay;
    }

    public int getFromDay() {
        return fromDay;
    }

    public void setFromDay(int fromDay) {
        this.fromDay = fromDay;
    }

    public int getToDay() {
        return toDay;
    }

    public void setToDay(int toDay) {
        this.toDay = toDay;
    }

    public boolean useRange() {

        return useRange;
    }

    public void setUseRange(boolean useRange) {
        this.useRange = useRange;
    }

    public String getDayRangeParamString() {
        StringBuilder range = new StringBuilder();
        try {
            if ((fromDay != -1) && (toDay != -1)) {
                range.append(FROM).append(fromDay).append(TO).append(toDay).append(GROW_DAY);
            }
        } catch (Exception ex) {
            fromDay = -1;
            toDay = -1;
        }
        return range.toString();
    }

    @Override
    public String toString() {
        return getDayRangeParamString();
    }
}
