
/*
* To change this template, choose Tools | Templates
* and open the template in the editor.
 */
package com.agrologic.app.model;


import java.io.Serializable;

/**
 *
 * @author JanL
 */
public class Distrib implements Serializable {
    private static final long serialVersionUID = 1L;
    private int               accountNumber;
    private int               ageDistrib;
    private int               averageWeight;
    private int               badAnother;
    private int               badVeterinary;
    private double            calcSum;
    private String            date;
    private String            dtA;
    private String            dtB;
    private String            dtC;
    private String            dtAnother;
    private String            dtVeterinary;
    private double            handSum;
    private Long              id;
    private Long              flockId;
    private int               numOfBirds;
    private double            priceA;
    private double            priceB;
    private double            priceC;
    private int               quantityA;
    private int               quantityB;
    private int               quantityC;
    private String            sex;
    private String            target;
    private double            total;
    private int               weight;

    public Distrib() {}

    public Distrib(Long id) {
        this.id = id;
    }

    public Distrib(Long id, int accountNumber, String sex, String target, int numOfBirds, int weight, int quantityA,
                      int quantityB, int quantityC, int badVeterinary, int badAnother, double priceA, double priceB,
                      double priceC, int ageDistrib, int averageWeight, String dtA, String dtB, String dtC,
                      String dtVeterinary, String dtAnother, double calcSum, double handSum, double total) {
        this.id            = id;
        this.accountNumber = accountNumber;
        this.sex           = sex;
        this.target        = target;
        this.numOfBirds    = numOfBirds;
        this.weight        = weight;
        this.quantityA     = quantityA;
        this.quantityB     = quantityB;
        this.quantityC     = quantityC;
        this.badVeterinary = badVeterinary;
        this.badAnother    = badAnother;
        this.priceA        = priceA;
        this.priceB        = priceB;
        this.priceC        = priceC;
        this.ageDistrib    = ageDistrib;
        this.averageWeight = averageWeight;
        this.dtA           = dtA;
        this.dtB           = dtB;
        this.dtC           = dtC;
        this.dtVeterinary  = dtVeterinary;
        this.dtAnother     = dtAnother;
        this.calcSum       = calcSum;
        this.handSum       = handSum;
        this.total         = total;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getFlockId() {
        return flockId;
    }

    public void setFlockId(Long flockId) {
        this.flockId = flockId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(int accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public int getNumOfBirds() {
        return numOfBirds;
    }

    public void setNumOfBirds(int numOfBirds) {
        this.numOfBirds = numOfBirds;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public int getQuantityA() {
        return quantityA;
    }

    public void setQuantityA(int quantityA) {
        this.quantityA = quantityA;
    }

    public int getQuantityB() {
        return quantityB;
    }

    public void setQuantityB(int quantityB) {
        this.quantityB = quantityB;
    }

    public int getQuantityC() {
        return quantityC;
    }

    public void setQuantityC(int quantityC) {
        this.quantityC = quantityC;
    }

    public int getBadVeterinary() {
        return badVeterinary;
    }

    public void setBadVeterinary(int badVeterinary) {
        this.badVeterinary = badVeterinary;
    }

    public int getBadAnother() {
        return badAnother;
    }

    public void setBadAnother(int badAnother) {
        this.badAnother = badAnother;
    }

    public double getPriceA() {
        return priceA;
    }

    public void setPriceA(double priceA) {
        this.priceA = priceA;
    }

    public double getPriceB() {
        return priceB;
    }

    public void setPriceB(double priceB) {
        this.priceB = priceB;
    }

    public double getPriceC() {
        return priceC;
    }

    public void setPriceC(double priceC) {
        this.priceC = priceC;
    }

    public int getAgeDistrib() {
        return ageDistrib;
    }

    public void setAgeDistrib(int ageDistrib) {
        this.ageDistrib = ageDistrib;
    }

    public int getAverageWeight() {
        return averageWeight;
    }

    public void setAverageWeight(int averageWeight) {
        this.averageWeight = averageWeight;
    }

    public String getDtA() {
        return dtA;
    }

    public void setDtA(String dtA) {
        this.dtA = dtA;
    }

    public String getDtB() {
        return dtB;
    }

    public void setDtB(String dtB) {
        this.dtB = dtB;
    }

    public String getDtC() {
        return dtC;
    }

    public void setDtC(String dtC) {
        this.dtC = dtC;
    }

    public String getDtVeterinary() {
        return dtVeterinary;
    }

    public void setDtVeterinary(String dtVeterinary) {
        this.dtVeterinary = dtVeterinary;
    }

    public String getDtAnother() {
        return dtAnother;
    }

    public void setDtAnother(String dtAnother) {
        this.dtAnother = dtAnother;
    }

    public double getCalcSum() {
        return calcSum;
    }

    public void setCalcSum(double calcSum) {
        this.calcSum = calcSum;
    }

    public double getHandSum() {
        return handSum;
    }

    public void setHandSum(double handSum) {
        this.handSum = handSum;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    @Override
    public int hashCode() {
        int hash = 0;

        hash += ((id != null)
                 ? id.hashCode()
                 : 0);

        return hash;
    }

    @Override
    public boolean equals(Object object) {

        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Distrib)) {
            return false;
        }

        Distrib other = (Distrib) object;

        if (((this.id == null) && (other.id != null)) || ((this.id != null) &&!this.id.equals(other.id))) {
            return false;
        }

        return true;
    }

    @Override
    public String toString() {
        return "[id=" + id + "]";
    }
}


