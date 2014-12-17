package com.agrologic.app.management;


public enum PerGrowDayHistoryDataType {
    GROW_DAY("D18", 800L),
    HEATER_1_TIME_ON("D2", 1303L), HEATER_2_TIME_ON("D4", 1304L),
    HEATER_3_TIME_ON("D22", 1305L), HEATER_4_TIME_ON("D23", 1306L),
    HEATER_5_TIME_ON("D24", 1307L), HEATER_6_TIME_ON("D25", 1308L),
    FEED_CONSUMPTION_ID("D2", 1301L), WATER_CONSUMPTION_ID("D1", 1302L),
    AVERAGE_WEIGHT_1_ID("D26", 2933L), AVERAGE_WEIGHT_2_ID("D26", 2934L),
    AVERAGE_WEIGHT_3_ID("D26", 2935L), AVERAGE_WEIGHT_4_ID("D26", 2936L),
    MAX_TEMP_IN("D6", 3002L), MIN_TEMP_IN("5", 3003L),
    MAX_TEMP_OUT("D10", 3004L), MIN_TEMP_OUT("D9", 3005L),
    MAX_HUMIDITY("D8", 3006L), MIN_HUMIDITY("D7", 3007L),
    DAY_MORTALITY("D36", 3017L), DAY_MORTALITY_MALE("D37", 3033L),
    DAY_MORTALITY_FEMALE("D44", 3038L),
    DAY_CO2_MAX("D177", 3660L),DAY_CO2_MIN("D178", 3661L);

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
