
/*
* To change this template, choose Tools | Templates
* and open the template in the editor.
 */
package com.agrologic.app.model;

//~--- JDK imports ------------------------------------------------------------

import java.io.Serializable;

/**
 *
 * @author JanL
 */
public class DataDto implements Serializable, Comparable<DataDto>, Cloneable {
    public static final int   ALARM            = 2;
    public static final int   DATA             = 4;
    public static final int   HISTORY          = 5;
    public static final int   RELAY            = 1;
    public static final int   STATUS           = 0;
    public static final int   SYSTEM_STATE     = 3;
    private static final long serialVersionUID = 2L;
    private String            display;
    private Integer           format;
    private Long              id;
    private Boolean           isRelay;
    private String            label;
    private Integer           position;
    private Boolean           readonly;
    private Integer           relayStatus;
    private Integer           special;
    private Boolean           status;
    private String            title;
    private Long              type;
    private String            unicodeLabel;
    private Long              value;

    public DataDto() {}

    /**
     * Copy constructor
     */
    public DataDto(DataDto copy) {
        this.id          = copy.id;
        this.type        = copy.type;
        this.status      = copy.status;
        this.readonly    = copy.readonly;
        this.title       = copy.title;
        this.format      = copy.format;
        this.label       = copy.label;
        this.relayStatus = copy.relayStatus;
        this.isRelay     = copy.isRelay;
        this.special     = copy.special;
        this.value       = copy.value;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getFormat() {
        if (format == DataFormat.DEC_0) {
            if ((id == 1302) || (id == 1329) || (id == 3059) || (id == 3083) || (id == 1500) || (id == 1501)
                    || ((id >= 1388) && (id <= 1399)) || ((id >= 2171) && (id <= 2175))) {
                return DataFormat.DEC_5;
            } else {
                return format;
            }
        } else {
            return format;
        }
    }

    public void setFormat(int format) {
        this.format = format;
    }

    public Boolean isReadonly() {
        return readonly;
    }

    public void setReadonly(Boolean readonly) {
        this.readonly = readonly;
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

    public Long getValue() {
        if (value == null) {
            return Long.valueOf(-1);
        }

        return value;
    }

    public void setValue(Long value) {
        if ((format == DataFormat.TIME) || (format == DataFormat.TIME_SEC) || (format == DataFormat.DATE)) {
            this.value = DataFormat.convertToTimeFormat(value);
        } else {
            this.value = value;
        }
    }

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
            this.value = Long.valueOf(value.toString(), 16);
        } else {
            this.value = Long.valueOf(value);
        }
    }

    public String getLabel() {
        return (label == null)
               ? ""
               : label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getUnicodeLabel() {
        return (unicodeLabel == null)
               ? ""
               : unicodeLabel;
    }

    public void setUnicodeLabel(String unicodeLabel) {
        this.unicodeLabel = unicodeLabel;
    }

    public Integer getRelayStatus() {
        return relayStatus;
    }

    public void setRelayStatus(Integer relayStatus) {
        this.relayStatus = relayStatus;
    }

    public Boolean getIsRelay() {
        return isRelay;
    }

    public void setIsRelay(Boolean isRelay) {
        this.isRelay = isRelay;
    }

    public Integer getSpecial() {
        return special;
    }

    public void setSpecial(Integer special) {
        this.special = special;
    }

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
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

    public boolean isLong() {
        if (format == DataFormat.DEC_4) {
            return true;
        }

        return false;
    }

    @Override
    public Object clone() {
        try {
            DataDto cloned = (DataDto) super.clone();
            return cloned;
        } catch (CloneNotSupportedException ex) {
            return null;
        }
    }

    public int compareTo(DataDto o) {
        return id.compareTo(o.id);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }

        if (getClass() != obj.getClass()) {
            return false;
        }

        final DataDto other = (DataDto) obj;

        if ((this.id != other.id) && ((this.id == null) ||!this.id.equals(other.id))) {
            return false;
        }

        if ((this.value != other.value) && ((this.value == null) ||!this.value.equals(other.value))) {
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
        return new StringBuilder().append(" ID : ").append(id).append(" LABEL :").append(label).toString();
    }
}



