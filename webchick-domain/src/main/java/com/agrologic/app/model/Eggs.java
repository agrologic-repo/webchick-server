package com.agrologic.app.model;

public class Eggs {
    Long flockId;
    Integer day;
    Integer numOfBirds;
    Integer eggQuantity;
    Integer softShelled;
    Integer cracked;

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
}
