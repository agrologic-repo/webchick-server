
/*
* To change this template, choose Tools | Templates
* and open the template in the editor.
 */
package com.agrologic.app.model;



import java.io.Serializable;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

public class ProgramAlarmDto implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long              alarmTextId;
    private Long              dataId;
    private Integer           digitNumber;
    private Long              programId;
    private String            text;

    public ProgramAlarmDto() {}

    public Long getDataId() {
        return dataId;
    }

    public void setDataId(Long dataId) {
        this.dataId = dataId;
    }

    public Integer getDigitNumber() {
        return digitNumber;
    }

    public void setDigitNumber(Integer digitNumber) {
        this.digitNumber = digitNumber;
    }

    public Long getProgramId() {
        return programId;
    }

    public void setProgramId(Long programId) {
        this.programId = programId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Long getAlarmTextId() {
        return alarmTextId;
    }

    public void setAlarmTextId(Long alarmTextId) {
        this.alarmTextId = alarmTextId;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }

        if (o == this) {
            return true;
        }

        if (!(o instanceof ProgramAlarmDto)) {
            return false;
        }

        ProgramAlarmDto programAlarm = (ProgramAlarmDto) o;

        return new EqualsBuilder().append(this.text, programAlarm.text).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(this.text).toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append(this.text).toString();
    }
}



