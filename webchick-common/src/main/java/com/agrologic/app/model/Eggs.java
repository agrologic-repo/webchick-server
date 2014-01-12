package com.agrologic.app.model;

public class Eggs {
    private Long flockId;
    private Integer day;
    private Integer numOfBirds;
    private Integer eggQuantity;
    private Integer softShelled;
    private Integer cracked;
    private Long feedConsump;
    private Long waterConsump;
    private Long dailyMortal;

    public Eggs() {
    }

    public Long getFlockId() {
        return flockId;
    }

    public void setFlockId(Long flockId) {
        this.flockId = flockId;
    }

    public Integer getDay() {
        return day;
    }

    public void setDay(Integer day) {
        this.day = day;
    }

    public Integer getNumOfBirds() {
        return numOfBirds;
    }

    public void setNumOfBirds(Integer numOfBirds) {
        this.numOfBirds = numOfBirds;
    }

    public Integer getEggQuantity() {
        return eggQuantity;
    }

    public void setEggQuantity(Integer eggQuantity) {
        this.eggQuantity = eggQuantity;
    }

    public Integer getSoftShelled() {
        return softShelled;
    }

    public void setSoftShelled(Integer softShelled) {
        this.softShelled = softShelled;
    }

    public Integer getCracked() {
        return cracked;
    }

    public void setCracked(Integer cracked) {
        this.cracked = cracked;
    }

    public Long getFeedConsump() {
        return feedConsump;
    }

    public void setFeedConsump(Long feedConsump) {
        this.feedConsump = feedConsump;
    }

    public Long getWaterConsump() {
        return waterConsump;
    }

    public void setWaterConsump(Long waterConsump) {
        this.waterConsump = waterConsump;
    }

    public Long getDailyMortal() {
        return dailyMortal;
    }

    public void setDailyMortal(Long dailyMortal) {
        this.dailyMortal = dailyMortal;
    }
}
