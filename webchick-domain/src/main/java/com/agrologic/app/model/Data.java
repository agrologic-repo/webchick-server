
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
public class Data implements Serializable, Comparable<Data>, Cloneable {
    public static final int   ALARM            = 2;
    public static final int   DATA             = 4;
    public static final int   HISTORY          = 5;
    public static final int   RELAY            = 1;
    public static final int   STATUS           = 0;
    public static final int   SYSTEM_STATE     = 3;
    private static final long serialVersionUID = 2L;
    private boolean           updated          = false;
    private String            display;
    private Integer           format;
    private Long              id;
    private Boolean           isRelay;
    private String            label;
    private Long              langId;
    private Integer           position;
    private Boolean           readonly;
    private Integer           relayStatus;
    private Integer           special;
    private Boolean           status;
    private String            title;
    private Long              type;
    private String            unicodeLabel;
    private Long              value;

    public Data() {}

    /**
     * Copy constructor
     */
    public Data(Data copy) {
        this.id           = copy.id;
        this.type         = copy.type;
        this.status       = copy.status;
        this.readonly     = copy.readonly;
        this.title        = copy.title;
        this.format       = copy.format;
        this.label        = copy.label;
        this.unicodeLabel = copy.unicodeLabel;
        this.relayStatus  = copy.relayStatus;
        this.isRelay      = copy.isRelay;
        this.special      = copy.special;
        this.value        = copy.value;
        this.langId       = copy.langId;
        this.updated      = false;
        this.position     = copy.position;
        this.display      = copy.display;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDisplay() {
        return display;
    }

    public void setDisplay(String display) {
        this.display = display;
    }

    public Integer getFormat() {
        return format;
    }

    public void setFormat(Integer format) {
        this.format = format;
    }

    public Boolean getIsRelay() {
        return isRelay;
    }

    public void setIsRelay(Boolean isRelay) {
        this.isRelay = isRelay;
    }

    public boolean isAlarm() {
        return special == 2;
    }

    public boolean isSystemState() {
        return special == 3;
    }

    public String getLabel() {
        return (label == null)
               ? ""
               : label;
    }

    public void setLabel(String label) {
        this.label = label;
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

    public void setReadonly(Boolean readonly) {
        this.readonly = readonly;
    }

    public Integer getRelayStatus() {
        return relayStatus;
    }

    public void setRelayStatus(Integer relayStatus) {
        this.relayStatus = relayStatus;
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
        return (unicodeLabel == null)
               ? ""
               : unicodeLabel;
    }

    public void setUnicodeLabel(String unicodeLabel) {
        this.unicodeLabel = unicodeLabel;
    }

    public boolean isUpdated() {
        return updated;
    }

    public void setUpdated(boolean updated) {
        this.updated = updated;
    }

    public Long getValue() {
        updated = false;
        return value;
    }

    public Long getValueToView() {
        Long valueView = value;
        if (format == null) {
            valueView = value;
        } else if ((format == DataFormat.TIME) || (format == DataFormat.TIME_SEC) || (format == DataFormat.DATE)) {
            valueView = DataFormat.convertToTimeFormat(valueView);
        }
        this.updated = true;
        return valueView;
    }

    public void setValue(Long value) {

//      if (format == null) {
//          this.value = value;
//      } else if (format == DataFormat.TIME
//              || format == DataFormat.TIME_SEC
//              || format == DataFormat.DATE) {
//          this.value = DataFormat.convertToTimeFormat(value);
//      } else {
//          this.value = value;
//      }
//      this.updated = true;
        this.value = value;
        updated    = true;
    }

    public Long getLangId() {
        return langId;
    }

    public void setLangId(Long langId) {
        this.langId = langId;
    }

    public boolean isDoubleBuffer() {
        return (format == DataFormat.DEC_4)
               ? true
               : false;
    }

    public boolean isLong() {
        return (format == DataFormat.DEC_4)
               ? true
               : false;
    }

//  public void setValueToChange(Long value) {
//      if (format == DataFormat.TIME || format == DataFormat.DATE) {
////          String stringValue = Long.toHexString(value);
////          Long stringValue2 = Long.valueOf( value.toString(), 16);
//          Long.parseLong(Long.toHexString(value));
//          this.value = Long.parseLong(Long.toHexString(value));
//      } else {
//          this.value = value;
//      }
//  }
    public void setValueToChange(Long value) {
        if ((format == DataFormat.TIME) || (format == DataFormat.DATE)) {
            this.value = Long.valueOf(value.toString(), 16);
        } else {
            this.value = value;
        }
    }

    /**
     * Change value according to format.
     *
     * @param value the new value .
     */
    public void setValueToChange(String value) {
        if ((format == DataFormat.TIME) || (format == DataFormat.DATE)) {
            this.value = Long.parseLong(value);
        } else {
            this.value = Long.valueOf(value);
        }
    }

    public String isChecked() {
        return ("yes".equals(display))
               ? "checked"
               : "unchecked";
    }

    public String getFormatedValue() {
        if (value != null) {
            return new DataFormat(format).toStringValue(value);
        } else {
            return "";
        }
    }

    public String printDataValue() {
        return new DataFormat(format).toStringValue(value);
    }

    public String displayTemplateValue() {
        return new DataFormat(format).toString();
    }

    public boolean isDataReady() {
        return (value == null)
               ? false
               : true;
    }

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

        if ((this.id != other.id) && ((this.id == null) ||!this.id.equals(other.id))) {
            return false;
        }
//
//        if ((this.value != other.value) && ((this.value == null) ||!this.value.equals(other.value))) {
//            return false;
//        }

        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;

        hash = 29 * hash + ((this.id != null)
                            ? this.id.hashCode()
                            : 0);
//        hash = 29 * hash + ((this.value != null)
//                            ? this.value.hashCode()
//                            : 0);

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

