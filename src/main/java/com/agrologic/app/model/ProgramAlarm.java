
/*
* To change this template, choose Tools | Templates
* and open the template in the editor.
 */
package com.agrologic.app.model;

//~--- non-JDK imports --------------------------------------------------------

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

//~--- JDK imports ------------------------------------------------------------

import java.io.Serializable;

/**
 * Title: ProgramAlarmDto <br> Description: <br> Copyright: Copyright (c) 2009 <br> Company: AgroLogic LTD. <br>
 *
 * @author Valery Manakhimov <br>
 * @version 1.1 <br>
 */
public class ProgramAlarm implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long              alarmTextId;
    private Long              dataId;
    private Integer           digitNumber;
    private Long              programId;
    private String            text;

    public ProgramAlarm() {}

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

        if (!(o instanceof ProgramAlarm)) {
            return false;
        }

        ProgramAlarm programAlarm = (ProgramAlarm) o;

        return new EqualsBuilder().append(this.dataId, programAlarm.dataId).append(this.programId,
                                          programAlarm.programId).append(this.digitNumber,
                                              programAlarm.digitNumber).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17,
                                   37).append(this.dataId).append(this.programId).append(this.digitNumber).toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append(this.text).toString();
    }
}


//~ Formatted by Jindent --- http://www.jindent.com
