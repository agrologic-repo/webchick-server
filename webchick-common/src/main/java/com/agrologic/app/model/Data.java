package com.agrologic.app.model;

import java.io.Serializable;

public class Data implements Serializable, Comparable<Data>, Cloneable {
    public static final int ALARM = 2;
    public static final int DATA = 4;
    public static final int HISTORY = 5;
    public static final int RELAY = 1;
    public static final int STATUS = 0;
    public static final int SYSTEM_STATE = 3;
    private static final long serialVersionUID = 2L;
    private boolean updated = false;
    private String display;
    private Integer format;
    private Long id;
    private Boolean isRelay;
    private String label;
    private Long langId;
    private Integer position;
    private Boolean readonly;
    private Integer relayStatus;
    private Integer special;
    private Boolean status;
    private String title;
    private Long type;
    private String unicodeLabel;
    private Long value;
    private String historyOpt;
    private String historyDNum;
    private String historyData;

    public Data() {
    }

    /**
     * Copy constructor
     */
    public Data(Data copy) {
        this.id = copy.id;
        this.type = copy.type;
        this.status = copy.status;
        this.readonly = copy.readonly;
        this.title = copy.title;
        this.format = copy.format;
        this.label = copy.label;
        this.unicodeLabel = copy.unicodeLabel;
        this.relayStatus = copy.relayStatus;
        this.isRelay = copy.isRelay;
        this.special = copy.special;
        this.value = copy.value;
        this.langId = copy.langId;
        this.updated = false;
        this.position = copy.position;
        this.display = copy.display;
        this.historyOpt = copy.historyOpt;
        this.historyDNum = copy.historyDNum;
    }

    public boolean isUpdated() {
        return updated;
    }

    public void setUpdated(boolean updated) {
        this.updated = updated;
    }

    public String getDisplay() {
        return display;
    }

    public void setDisplay(String display) {
        this.display = display;
    }

    public String isChecked() {
        return ("yes".equals(display))
                ? "checked"
                : "unchecked";
    }

    public void setFormat(Integer format) {
        this.format = format;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getIsRelay() {
        return isRelay;
    }

    public void setIsRelay(Boolean isRelay) {
        this.isRelay = isRelay;
    }

    public String getLabel() {
        return (label == null)
                ? ""
                : label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Long getLangId() {
        return langId;
    }

    public void setLangId(Long langId) {
        this.langId = langId;
    }

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    public Boolean getReadonly() {
        return readonly;
    }

    public Boolean isReadonly() {
        return readonly;
    }

    public boolean isPassword() {

        if (id.equals(3028L)) {
            return true;
        } else {
            return false;
        }
    }


    public void setReadonly(Boolean readonly) {
        this.readonly = readonly;
    }

    public Integer getSpecial() {
        return special;
    }

    public void setSpecial(Integer special) {
        this.special = special;
    }

    public Boolean isStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Long getType() {
        return type;
    }

    public void setType(Long type) {
        this.type = type;
    }

    public String getUnicodeLabel() {
        return (unicodeLabel == null) ? "" : unicodeLabel;
    }

    public void setUnicodeLabel(String unicodeLabel) {
        this.unicodeLabel = unicodeLabel;
    }

    public String getHistoryOpt() {
        return historyOpt;
    }

    public void setHistoryOpt(String historyOpt) {
        this.historyOpt = historyOpt;
    }

    public String getHistoryDNum() {
        return historyDNum;
    }

    public String getHistoryDayDNum() {
        String[] dnums = historyDNum.split(";");
        if (dnums.length > 1) {
            return dnums[0];
        } else {
            return dnums[0];
        }
    }

    public String getHistoryHourDNum() {
        String[] dnums = historyDNum.split(";");
        if (dnums.length > 1) {
            return dnums[1];
        } else {
            return dnums[0];
        }
    }

    public void setHistoryDNum(String historyDNum) {
        this.historyDNum = historyDNum;
    }

    /* ************************************************************************************** */
    /* ************************* this methods useful in webchick-web ************************ */
    /* ************************************************************************************** */
    public Long getValue() {
        return value;
    }

    public void setValue(Long value) {
        this.value = value;
        updated = true;
    }

    public String getHistoryData() {
        return historyData;
    }

    public void setHistoryData(String historyData) {
        this.historyData = historyData;
    }


    public Long getValueToUI() {
        Long valueView = value;
        if (format == null) {
            valueView = value;
        } else if ((format == DataFormat.TIME) || (format == DataFormat.TIME_SEC) || (format == DataFormat.DATE)) {
            valueView = DataFormat.convertToTimeFormat(valueView);
        } else {
            valueView = value;
        }
        this.updated = true;
        return valueView;
    }

    /**
     * Change newValue according to format.
     *
     * @param value the new newValue .
     * @see com.agrologic.app.model.Data#setValueFromUI(Long)
     */
    public void setValueFromUI(String value) {
        if (value != null && !value.equals("")) {
            Long parsed = Long.parseLong(value);
            setValueFromUI(parsed);
        }
    }

    /**
     * @param newValue
     */
    public void setValueFromUI(Long newValue) {
        if (format == DataFormat.TIME || format == DataFormat.TIME_SEC || format == DataFormat.DATE) {
            this.value = Long.valueOf(newValue.toString(), 16);
        } else {
            this.value = newValue;
        }
    }

    /**
     * Return true if there is a letter in given string, false otherwise.
     * <p/>
     * we need to check if a letter appear because of parsing hex value.
     *
     * @param s the given string to check
     * @return true if there is a letter in string , otherwise false
     */
    private boolean isLetter(String s) {
        for (Character c : s.toCharArray()) {
            if (Character.isLetter(c)) {
                return true;
            }
        }
        return false;
    }

    public String getFormattedValue() {
        if (value != null) {
            Long valueToView = getValueToUI();
            return new DataFormat(format).toStringValue(valueToView);
        } else {
            return "";
        }
    }

    public String displayTemplateValue() {
        return new DataFormat(format).toString();
    }

    public int getFormat() {
        if (format == DataFormat.DEC_0) {
            if ((id == 1302) || (id == 1329)
                    || (id == 3059) || (id == 3083)
                    || (id == 1500) || (id == 1501)
                    || (id >= 1388 && id <= 1399)
                    || (id >= 2171 && id <= 2175)) {
                return DataFormat.DEC_5;
            } else {
                return format;
            }
        } else {
            return format;
        }
    }

    public int getFormatForLocalGraphs() {
        if (id == 1302) {
            return 0;
        }
//        if (id == 2163){
//            return 0;
//        }
        else {
            return format;
        }
    }
    /*******************************************************************************************/
    /**
     * ************************ this methods useful in webchick-server************************
     */
    public boolean isAlarm() {
        return special == 2;
    }

    public boolean isSystemState() {
        return special == 3;
    }

    public boolean isAlarmOn() {
        boolean on = false;
        if(isAlarm()) {
           long val = getValue();
            if(val > 0) {
                on = true;
            }
        }
        return on;
    }

    /**
     * Return true if newValue of this data are 32 bit.
     * <p/>
     * These data values ​​are sent in parts. The first pairs sends data id and the upper 16 bits.The second pairs
     * data id+1 and lower 16 bits .
     *
     * @return true if data is long type , otherwise false
     */
    public boolean isLongType() {
        return (format == DataFormat.DEC_4)
                ? true
                : false;
    }

    public boolean isDEC_5() {//
        return (format == DataFormat.DEC_5)//
                ? true//
                : false;//
    }//

    @Override
    public Object clone() {
        try {
            Data cloned = (Data) super.clone();
            return cloned;
        } catch (CloneNotSupportedException ex) {
            return null;
        }
    }

    @Override
    public int compareTo(Data data) {
        return id.compareTo(data.id);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }

        if (getClass() != obj.getClass()) {
            return false;
        }

        final Data other = (Data) obj;

        if ((this.id != other.id) && ((this.id == null) || !this.id.equals(other.id))) {
            return false;
        }

        if ((this.value != other.value) && ((this.value == null) || !this.value.equals(other.value))) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;

        hash = 29 * hash + ((this.id != null)
                ? this.id.hashCode()
                : 0);
        hash = 29 * hash + ((this.value != null)
                ? this.value.hashCode()
                : 0);
        return hash;
    }

    @Override
    public String toString() {
        return new StringBuilder().append(" ID : ").append(id).append(" TYPE : ").append(type).append(
                " LABEL :").append(label).append(" VALUE : ").append((value == null)
                ? ""
                : value).toString();
    }
}

