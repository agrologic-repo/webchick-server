package com.agrologic.app.management;

public enum PerHourHistoryDataType {
    INSIDE_TEMPERATURE("D18", 3122),
    OUTSIDE_TEMPERATURE("D19", 3107),
    PER_HOUR_HUMIDITY("D20", 3142),
    PER_HOUR_WATER_CONSUMPTION("D21", 1302),
    PER_HOUR_FEED_CONSUMPTION("D72", 1329);

    public final String name;
    public final int id;

    private PerHourHistoryDataType(String name, int id) {
        this.name = name;
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
