/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.agrologic.app.model;

import java.io.Serializable;

/**
 * Title: FlockDto.java <br>
 * Description: <br>
 * Copyright:   Copyright © 2010 <br>
 * Company:     Agro Logic Ltd. ®<br>
 * @author      Valery Manakhimov <br>
 * @version     0.1.1.1 <br>
 */
public class FlockDto implements Serializable {

    private static final long serialVersionUID = 2L;
    private Long flockId;
    private Long controllerId;
    private String flockName;
    private String status;      // open/close
    private String startTime;
    private String endTime;
    private Long programId;
    private int quantMale;
    private int quantFemale;
    private int quantElect;
    private int quantSpread;
    private int quantWater;
    private int beginElect;
    private int endElect;
    private int beginFuel;
    private int endFuel;
    private int beginGas;
    private int endGas;
    private int beginWater;
    private int endWater;
    private float costChickMale;
    private float costChickFemale;
    private float costElect;
    private float costFuel;
    private float costFuelEnd;
    private float costGas;
    private float costGasEnd;
    private float costWater;
    private float costSpread;
    private float costMaleKg;
    private int amountFuel;
    private int amountGas;
    private int amountFeed;
    private int amountSpread;
    private float expenses;
    private float revenues;
    private float costPerKg;
    private float totalCostElect;
    private float totalCostFuel;
    private float totalCostGas;
    private float totalCostWater;
    private float totalCostFeed;
    private float totalCostSpread;
    private float totalCostMedic;
    private float totalCostChicks;
    private float totalCostLabor;
    private String currency;

    public FlockDto() {
    }

    public long getProgramId() {
        return programId;
    }

    public void setProgramId(Long programId) {
        this.programId = programId;
    }

    public float getCostPerKg() {
        return costPerKg;
    }

    public void setCostPerKg(float costPerKg) {
        this.costPerKg = costPerKg;
    }

    public float getExpenses() {
        return expenses;
    }

    public void setExpenses(float expenses) {
        this.expenses = expenses;
    }

    public int getFeedAdd() {
        return amountFeed;
    }

    public void setFeedAdd(int feedAdd) {
        this.amountFeed = feedAdd;
    }

    public int getFuelAdd() {
        return amountFuel;
    }

    public void setFuelAdd(int fuelAdd) {
        this.amountFuel = fuelAdd;
    }

    public int getGasAdd() {
        return amountGas;
    }

    public void setGasAdd(int gazAdd) {
        this.amountGas = gazAdd;
    }

    public float getCostFuel() {
        return costFuel;
    }

    public void setCostFuel(float costFuel) {
        this.costFuel = costFuel;
    }

    public float getCostFuelEnd() {
        return costFuelEnd;
    }

    public void setCostFuelEnd(float costFuelEnd) {
        this.costFuelEnd = costFuelEnd;
    }

    public float getCostGas() {
        return costGas;
    }

    public void setCostGas(float costGas) {
        this.costGas = costGas;
    }

    public float getCostGasEnd() {
        return costGasEnd;
    }

    public void setCostGasEnd(float costGasEnd) {
        this.costGasEnd = costGasEnd;
    }

    public float getCostMaleKg() {
        return costMaleKg;
    }

    public void setCostMaleKg(float costMaleKg) {
        this.costMaleKg = costMaleKg;
    }

    public float getCostSpread() {
        return costSpread;
    }

    public void setCostSpread(float costSpread) {
        this.costSpread = costSpread;
    }

    public float getCostWater() {
        return costWater;
    }

    public void setCostWater(float costWater) {
        this.costWater = costWater;
    }

    public float getRevenues() {
        return revenues;
    }

    public void setRevenues(float revenues) {
        this.revenues = revenues;
    }

    public int getSpreadAdd() {
        return amountSpread;
    }

    public void setSpreadAdd(int spreadAdd) {
        this.amountSpread = spreadAdd;
    }

    public Long getControllerId() {
        return controllerId;
    }

    public void setControllerId(Long controllerId) {
        this.controllerId = controllerId;
    }

    public int getElectBegin() {
        return beginElect;
    }

    public void setElectBegin(int electBegin) {
        this.beginElect = electBegin;
    }

    public int getElectEnd() {
        return endElect;
    }

    public void setElectEnd(int electEnd) {
        this.endElect = electEnd;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndDate(String endTime) {
        this.endTime = endTime;
    }

    public Long getFlockId() {
        return flockId;
    }

    public void setFlockId(Long flockId) {
        this.flockId = flockId;
    }

    public String getFlockName() {
        return flockName;
    }

    public void setFlockName(String flockName) {
        this.flockName = flockName;
    }

    public int getFuelBegin() {
        return beginFuel;
    }

    public void setFuelBegin(int fuelBegin) {
        this.beginFuel = fuelBegin;
    }

    public int getFuelEnd() {
        return endFuel;
    }

    public void setFuelEnd(int fuelEnd) {
        this.endFuel = fuelEnd;
    }

    public int getGasBegin() {
        return beginGas;
    }

    public void setGasBegin(int gazBegin) {
        this.beginGas = gazBegin;
    }

    public int getGasEnd() {
        return endGas;
    }

    public void setGasEnd(int gazEnd) {
        this.endGas = gazEnd;
    }

    public float getCostChickFemale() {
        return costChickFemale;
    }

    public void setCostChickFemale(float costChickFemale) {
        this.costChickFemale = costChickFemale;
    }

    public float getCostChickMale() {
        return costChickMale;
    }

    public void setCostChickMale(float costChickMale) {
        this.costChickMale = costChickMale;
    }

    public float getCostElect() {
        return costElect;
    }

    public void setCostElect(float costElect) {
        this.costElect = costElect;
    }

    public int getQuantityElect() {
        return quantElect;
    }

    public void setQuantityElect(int quantityElect) {
        this.quantElect = quantityElect;
    }

    public int getQuantityFemale() {
        return quantFemale;
    }

    public void setQuantityFemale(int quantityFemale) {
        this.quantFemale = quantityFemale;
    }

    public int getQuantityMale() {
        return quantMale;
    }

    public void setQuantityMale(int quantityMale) {
        this.quantMale = quantityMale;
    }

    public int getQuantityChicks() {
        return quantMale + quantFemale;
    }

    public int getQuantitySpread() {
        return quantSpread;
    }

    public void setQuantitySpread(int quantitySpread) {
        this.quantSpread = quantitySpread;
    }

    public int getQuantityWater() {
        return quantWater;
    }

    public void setQuantityWater(int quantityWater) {
        this.quantWater = quantityWater;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartDate(String startTime) {

        this.startTime = startTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getWaterBegin() {
        return beginWater;
    }

    public void setWaterBegin(int waterBegin) {
        this.beginWater = waterBegin;
    }

    public int getWaterEnd() {
        return endWater;
    }

    public void setWaterEnd(int waterEnd) {
        this.endWater = waterEnd;
    }

    public float calcTotalChicksCost() {
        return quantMale * costChickMale + quantFemale * costChickFemale;
    }

    public float calcTotalWaterCost() {
        float totalWaterCost = 0;
        if (quantWater != 0 && costWater != 0) {
            totalWaterCost = quantWater * costWater;
        }
        return totalWaterCost;
    }

    public float calcTotalelectCost() {
        float totalEctCost = 0;
        if (quantElect != 0 && costElect != 0) {
            totalEctCost = quantElect * costElect;
        }
        return totalEctCost;
    }

    public float calcTotalFuelCost() {
        return (beginFuel * costFuel - endFuel * costFuelEnd) + getTotalFuel();
    }

    public float calcTotalGasCost() {
        return (beginGas * costGas - endGas * costGasEnd) + getTotalGas();
    }

    public int calcTotalQuantityGas() {
        return beginGas - endGas + getGasAdd();
    }

    public int calcTotalQuantityFuel() {
        return beginFuel - endFuel + getFuelAdd();
    }

    public float getTotalChicks() {
        return totalCostChicks;
    }

    public void setTotalChicks(float totalChicks) {
        this.totalCostChicks = totalChicks;
    }

    public float getTotalElect() {
        return totalCostElect;
    }

    public void setTotalElect(float totalElect) {
        this.totalCostElect = totalElect;
    }

    public float getTotalFuel() {

        return totalCostFuel;
    }

    public void setTotalFuel(float totalFuel) {
        this.totalCostFuel = totalFuel;
    }

    public float getTotalGas() {
        return totalCostGas;
    }

    public void setTotalGas(float totalGas) {
        this.totalCostGas = totalGas;
    }

    public float getTotalLabor() {
        return totalCostLabor;
    }

    public void setTotalLabor(float totalLabor) {
        this.totalCostLabor = totalLabor;
    }

    public float getTotalMedic() {
        return totalCostMedic;
    }

    public void setTotalMedic(float totalMedic) {
        this.totalCostMedic = totalMedic;
    }

    public float getTotalSpread() {
        return totalCostSpread;
    }

    public void setTotalSpread(float totalSpread) {
        this.totalCostSpread = totalSpread;
    }

    public float getTotalWater() {
        return totalCostWater;
    }

    public void setTotalWater(float totalWater) {
        this.totalCostWater = totalWater;
    }

    public float getTotalFeed() {
        return totalCostFeed;
    }

    public void setTotalFeed(float totalFeed) {
        this.totalCostFeed = totalFeed;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public float calcTotalRevenues() {
        float totRevenues = 0;
        totRevenues = getRevenues();
        return totRevenues;
    }

    public float calcTotalExpenses() {
        float totExpenses = 0;
        totExpenses = getTotalChicks() + getTotalLabor() + getTotalElect() + getTotalFeed() + getTotalGas()
                + getTotalFuel() + getTotalMedic() + getTotalSpread() + getTotalWater() + getRevenues();
        return totExpenses;
    }

    public float calcTotCostPerKGBirds() {
        costPerKg = calcTotalExpenses()/getTotalChicks();
        return Math.round(costPerKg);
    }

//    private static final long serialVersionUID = 2L;
////    private static final SimpleDateFormat monthDayYearformatter = new SimpleDateFormat("dd/MM/yyyy");
//
//    private Long flockId;
//    private Long controllerId;
//    private String flockName;
//    private String status;      // open/close
//    private String startTime;
//    private String endTime;
//    private Long programId;
//    private int quantityMale;
//    private int quantityFemale;
//    private int quantityElect;
//    private int quantitySpread;
//    private int quantityWater;
//    private int electBegin;
//    private int electEnd;
//    private int fuelBegin;
//    private int gazBegin;
//    private int gazEnd;
//    private int waterBegin;
//    private int waterEnd;
//
//    private float priceChickMale;
//    private float priceChickFemale;
//    private float priceElect;
//    private float priceFuel;
//    private float priceFuelEnd;
//    private float priceGaz;
//    private float priceGazEnd;
//    private float priceWater;
//    private float priceSpread;
//    private float priceMaleKg;
//
//    private int fuelAdd;
//    private int gazAdd;
//    private int feedAdd;
//    private int spreadAdd;
//    private int expensive;
//    private int revenues;
//    private int costPerKg;
//
//    private int totalElect;
//    private int totalFuel;
//    private int totalGas;
//    private int totalWater;
//    private int totalSpread;
//    private int totalMedic;
//    private int totalChicks;
//    private int totalLabor;
//
//    private Map<Long,HistoryDataSetting> historySetting;
////    private Map<Integer,String> historyByGrowDay;
//
//    public FlockDto() {
//        historySetting = new TreeMap();
//    }
//
//    public long getProgramId() {
//        return programId;
//    }
//
//    public void setProgramId(Long programId) {
//        this.programId = programId;
//    }
//
//    public int getCostPerKg() {
//        return costPerKg;
//    }
//
//    public void setCostPerKg(int costPerKg) {
//        this.costPerKg = costPerKg;
//    }
//
//    public int getExpensive() {
//        return expensive;
//    }
//
//    public void setExpensive(int expensive) {
//        this.expensive = expensive;
//    }
//
//    public int getFeedAdd() {
//        return feedAdd;
//    }
//
//    public void setFeedAdd(int feedAdd) {
//        this.feedAdd = feedAdd;
//    }
//
//    public int getFuelAdd() {
//        return fuelAdd;
//    }
//
//    public void setFuelAdd(int fuelAdd) {
//        this.fuelAdd = fuelAdd;
//    }
//
//    public int getGazAdd() {
//        return gazAdd;
//    }
//
//    public void setGazAdd(int gazAdd) {
//        this.gazAdd = gazAdd;
//    }
//
//    public float getPriceFuel() {
//        return priceFuel;
//    }
//
//    public void setPriceFuel(float priceFuel) {
//        this.priceFuel = priceFuel;
//    }
//
//    public float getPriceFuelEnd() {
//        return priceFuelEnd;
//    }
//
//    public void setPriceFuelEnd(float priceFuelEnd) {
//        this.priceFuelEnd = priceFuelEnd;
//    }
//
//    public float getPriceGaz() {
//        return priceGaz;
//    }
//
//    public void setPriceGaz(float priceGaz) {
//        this.priceGaz = priceGaz;
//    }
//
//    public float getPriceGazEnd() {
//        return priceGazEnd;
//    }
//
//    public void setPriceGazEnd(float priceGazEnd) {
//        this.priceGazEnd = priceGazEnd;
//    }
//
//    public float getPriceMaleKg() {
//        return priceMaleKg;
//    }
//
//    public void setPriceMaleKg(float priceMaleKg) {
//        this.priceMaleKg = priceMaleKg;
//    }
//
//    public float getPriceSpread() {
//        return priceSpread;
//    }
//
//    public void setPriceSpread(float priceSpread) {
//        this.priceSpread = priceSpread;
//    }
//
//    public float getPriceWater() {
//        return priceWater;
//    }
//
//    public void setPriceWater(float priceWater) {
//        this.priceWater = priceWater;
//    }
//
//    public int getRevenues() {
//        return revenues;
//    }
//
//    public void setRevenues(int revenues) {
//        this.revenues = revenues;
//    }
//
//    public int getSpreadAdd() {
//        return spreadAdd;
//    }
//
//    public void setSpreadAdd(int spreadAdd) {
//        this.spreadAdd = spreadAdd;
//    }
//
//    public int getTotalChicks() {
//        return totalChicks;
//    }
//
//    public void setTotalChicks(int totalChicks) {
//        this.totalChicks = totalChicks;
//    }
//
//    public int getTotalElect() {
//        return totalElect;
//    }
//
//    public void setTotalElect(int totalElect) {
//        this.totalElect = totalElect;
//    }
//
//    public int getTotalFuel() {
//        return totalFuel;
//    }
//
//    public void setTotalFuel(int totalFuel) {
//        this.totalFuel = totalFuel;
//    }
//
//    public int getTotalGas() {
//        return totalGas;
//    }
//
//    public void setTotalGas(int totalGas) {
//        this.totalGas = totalGas;
//    }
//
//    public int getTotalLabor() {
//        return totalLabor;
//    }
//
//    public void setTotalLabor(int totalLabor) {
//        this.totalLabor = totalLabor;
//    }
//
//    public int getTotalMedic() {
//        return totalMedic;
//    }
//
//    public void setTotalMedic(int totalMedic) {
//        this.totalMedic = totalMedic;
//    }
//
//    public int getTotalSpread() {
//        return totalSpread;
//    }
//
//    public void setTotalSpread(int totalSpread) {
//        this.totalSpread = totalSpread;
//    }
//
//    public int getTotalWater() {
//        return totalWater;
//    }
//
//    public void setTotalWater(int totalWater) {
//        this.totalWater = totalWater;
//    }
//
//    public Long getControllerId() {
//        return controllerId;
//    }
//
//    public void setControllerId(Long controllerId) {
//        this.controllerId = controllerId;
//    }
//
//    public int getElectBegin() {
//        return electBegin;
//    }
//
//    public void setElectBegin(int electBegin) {
//        this.electBegin = electBegin;
//    }
//
//    public int getElectEnd() {
//        return electEnd;
//    }
//
//    public void setElectEnd(int electEnd) {
//        this.electEnd = electEnd;
//    }
//
//    public String getEndTime() {
//        return endTime;
//    }
//
//    public void setEndDate(String endTime) {
//        this.endTime = endTime;
//    }
//
//    public Long getFlockId() {
//        return flockId;
//    }
//
//    public void setFlockId(Long flockId) {
//        this.flockId = flockId;
//    }
//
//    public String getFlockName() {
//        return flockName;
//    }
//
//    public void setFlockName(String flockName) {
//        this.flockName = flockName;
//    }
//
//    public int getFuelBegin() {
//        return fuelBegin;
//    }
//
//    public void setFuelBegin(int fuelBegin) {
//        this.fuelBegin = fuelBegin;
//    }
//
//    public int getGazBegin() {
//        return gazBegin;
//    }
//
//    public void setGazBegin(int gazBegin) {
//        this.gazBegin = gazBegin;
//    }
//
//    public int getGazEnd() {
//        return gazEnd;
//    }
//
//    public void setGazEnd(int gazEnd) {
//        this.gazEnd = gazEnd;
//    }
//
//    public float getPriceChickFemale() {
//        return priceChickFemale;
//    }
//
//    public void setPriceChickFemale(float priceChickFemale) {
//        this.priceChickFemale = priceChickFemale;
//    }
//
//    public float getPriceChickMale() {
//        return priceChickMale;
//    }
//
//    public void setPriceChickMale(float priceChickMale) {
//        this.priceChickMale = priceChickMale;
//    }
//
//    public float getPriceElect() {
//        return priceElect;
//    }
//
//    public void setPriceElect(float priceElect) {
//        this.priceElect = priceElect;
//    }
//
//    public int getQuantityElect() {
//        return quantityElect;
//    }
//
//    public void setQuantityElect(int quantityElect) {
//        this.quantityElect = quantityElect;
//    }
//
//    public int getQuantityFemale() {
//        return quantityFemale;
//    }
//
//    public void setQuantityFemale(int quantityFemale) {
//        this.quantityFemale = quantityFemale;
//    }
//
//    public int getQuantityMale() {
//        return quantityMale;
//    }
//
//    public void setQuantityMale(int quantityMale) {
//        this.quantityMale = quantityMale;
//    }
//
//    public int getQuantitySpread() {
//        return quantitySpread;
//    }
//
//    public void setQuantitySpread(int quantitySpread) {
//        this.quantitySpread = quantitySpread;
//    }
//
//    public int getQuantityWater() {
//        return quantityWater;
//    }
//
//    public void setQuantityWater(int quantityWater) {
//        this.quantityWater = quantityWater;
//    }
//
//    public String getStartTime() {
//        return startTime;
//    }
//
//    public void setStartDate(String startTime) {
//
//        this.startTime = startTime;
//    }
//
//    public String getStatus() {
//        return status;
//    }
//
//    public void setStatus(String status) {
//        this.status = status;
//    }
//
//    public int getWaterBegin() {
//        return waterBegin;
//    }
//
//    public void setWaterBegin(int waterBegin) {
//        this.waterBegin = waterBegin;
//    }
//
//    public int getWaterEnd() {
//        return waterEnd;
//    }
//
//    public void setWaterEnd(int waterEnd) {
//        this.waterEnd = waterEnd;
//    }
//
//    public float calcTotalChicksPrice() {
//        float totalChicksPrice = 0;
//        if(quantityMale != 0 && quantityFemale != 0 && priceChickFemale!=0 && priceChickMale != 0) {
//            totalChicksPrice = quantityMale*priceChickMale + quantityFemale*priceChickFemale;
//        }
//        return totalChicksPrice;
//    }
//
//    public float calcTotalWaterPrice() {
//        float totalWaterPrice = 0;
//        if(quantityWater != 0 && priceWater != 0) {
//            totalWaterPrice = quantityWater*priceWater;
//        }
//        return totalWaterPrice;
//    }
//
//    public float calcTotalelectPrice() {
//        float totalEctPrice = 0;
//        if(quantityElect != 0 && priceElect != 0) {
//            totalEctPrice = quantityElect*priceElect;
//        }
//        return totalEctPrice;
//    }
//
//    public void setHistoryData(List<DataDto> dataList) {
//        int length = dataList.size();
//        for(int i = 0; i < length; i++ ) {
//            DataDto d = dataList.get(i);
//            if(historySetting.containsKey(d.getId()) ==  false) {
//                historySetting.put(d.getId(), new HistoryDataSetting(d.getId(), "unchecked"));
//            }
//        }
//    }
//
//    public String getHistoryDataSetting(Long id) {
//        return (String)((HistoryDataSetting)historySetting.get(id)).isChecked();
//    }
//
//    public void setHistoryDataSetting(DataDto data, String checked) {
//        if(historySetting.get(data.getId()) == null) {
//            historySetting.put(data.getId(), new HistoryDataSetting(data.getId(), checked));
//        } else {
//            historySetting.get(data.getId()).setChecked(checked);
//        }
//    }
//
//    /**
//     * Inner class for using to incapsulate data id and state
//     * checked/unchecked
//     *
//     * @param <Long>
//     * @param <String>
//     */
//    static class HistoryDataSetting<DataDto,String> {
//        DataDto data;
//        String checked;
//
//        HistoryDataSetting(DataDto data,String checked) {
//            this.data = data;
//            this.checked = checked;
//        }
//
//        public DataDto getData() {
//            return data;
//        }
//
//        public String isChecked() {
//            return checked;
//        }
//
//        public void setChecked(String checked) {
//            this.checked = checked;
//        }
//    }

}
