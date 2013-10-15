package com.agrologic.app.management;


public enum PerGrowDayHistoryDataType {
    GROW_DAY("D18", 800L),
    HEATER_1_TIME_ON("D18", 1303L), HEATER_2_TIME_ON("D18", 1304L),
    HEATER_3_TIME_ON("D18", 1305L), HEATER_4_TIME_ON("D18", 1306L),
    HEATER_5_TIME_ON("D18", 1307L), HEATER_6_TIME_ON("D18", 1308L),
    FEED_CONSUMPTION_ID("D18", 1301L), WATER_CONSUMPTION_ID("D18", 1302L),
    AVERAGE_WEIGHT_1_ID("D18", 2933L), AVERAGE_WEIGHT_2_ID("D19", 2934L),
    AVERAGE_WEIGHT_3_ID("D20", 2935L), AVERAGE_WEIGHT_4_ID("D21", 2936L),
    MAX_TEMP_IN("D18", 3002L), MIN_TEMP_IN("D18", 3003L),
    MAX_TEMP_OUT("D18", 3004L), MIN_TEMP_OUT("D18", 3005L),
    MAX_HUMIDITY("D18", 3006L), MIN_HUMIDITY("D18", 3007L),
    DAY_MORTALITY("D18", 3017L), DAY_MORTALITY_MALE("D18", 3033L),
    DAY_MORTALITY_FEMALE("D18", 3038L);

    public final String name;
    public final long id;

    private PerGrowDayHistoryDataType(String name, long id) {
        this.name = name;
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
