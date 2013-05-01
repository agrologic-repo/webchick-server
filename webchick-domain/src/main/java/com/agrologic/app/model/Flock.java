package com.agrologic.app.model;

import com.agrologic.app.util.MathUtil;

import java.io.Serializable;
import java.util.Map;
import java.util.TreeMap;

public class Flock implements Serializable {

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
    private String controllerName;
    private Map<Long, HistoryDataSetting> historySetting;

    public Flock() {
        historySetting = new TreeMap();
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

    public void setGasBegin(int beginGas) {
        this.beginGas = beginGas;
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
        return MathUtil.setTwoDecimalPoint(quantMale * costChickMale + quantFemale * costChickFemale);
    }

    public float calcTotalWaterCost() {
        float totalWaterCost = 0;
        if (quantWater != 0 && costWater != 0) {
            totalWaterCost = quantWater * costWater;
        }
        return MathUtil.setTwoDecimalPoint(totalWaterCost);
    }

    public float calcTotalElectCost() {
        float totalEctCost = 0;
        if (quantElect != 0 && costElect != 0) {
            totalEctCost = quantElect * costElect;
        }
        return MathUtil.setTwoDecimalPoint(totalEctCost);
    }

    public float calcTotalFuelCost() {
        float value = (beginFuel * costFuel - endFuel * costFuelEnd) + getTotalFuel();
        return MathUtil.setTwoDecimalPoint(value);
    }

    public float calcTotalGasCost() {
        return MathUtil.setTwoDecimalPoint((beginGas * costGas - endGas * costGasEnd) + getTotalGas());
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
        costPerKg = calcTotalExpenses() / getTotalChicks();
        return Math.round(costPerKg);
    }
    // TODO : delete if code is unused
//    public void setHistoryData(List<Data> dataList) {
//        int length = dataList.size();
//        for (int i = 0; i < length; i++) {
//            Data d = dataList.get(i);
//            if (historySetting.containsKey(d.getId()) == false) {
//                historySetting.put(d.getId(), new HistoryDataSetting(d.getId(), "unchecked"));
//            }
//        }
//    }
//
//    public String getHistoryDataSetting(Long id) {
//        return (String) ((HistoryDataSetting) historySetting.get(id)).isChecked();
//    }
//
//    public void setHistoryDataSetting(Data data, String checked) {
//        if (historySetting.get(data.getId()) == null) {
//            historySetting.put(data.getId(), new HistoryDataSetting(data.getId(), checked));
//        } else {
//            historySetting.get(data.getId()).setChecked(checked);
//        }
//    }

    public void setControllerName(String controllerName) {
        this.controllerName = controllerName;
    }

    public String getControllerName() {
        return controllerName;
    }

    /**
     * Inner class for using to incapsulate data id and state checked/unchecked
     *
     * @param <Data>
     * @param <String>
     */
    static class HistoryDataSetting<Data, String> {

        Data data;
        String checked;

        HistoryDataSetting(Data data, String checked) {
            this.data = data;
            this.checked = checked;
        }
        // TODO : delete if code is unused
//        public Data getData() {
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
    }
}
