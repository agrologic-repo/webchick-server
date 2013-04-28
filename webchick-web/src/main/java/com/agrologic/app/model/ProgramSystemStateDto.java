
/*
* To change this template, choose Tools | Templates
* and open the template in the editor.
 */
package com.agrologic.app.model;



import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

//~--- JDK imports ------------------------------------------------------------

import java.io.Serializable;

/**
 * Title: ProgramSystemStateDto <br>
 * Decription: <br>
 * Copyright:   Copyright (c) 2009 <br>
 * Company:     Agro Logic LTD. <br>
 * @author      Valery Manakhimov <br>
 * @version     1.1 <br>
 */
public class ProgramSystemStateDto implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long              dataId;
    private Integer           number;    // the value
    private Long              programId;
    private Integer           systemStateNumber;
    private Long              systemStateTextId;
    private String            text;      // the text by value

//  private Long alarmTextId;
    public ProgramSystemStateDto() {
        this.text = "---";
    }

    public Long getDataId() {
        return dataId;
    }

    public void setDataId(Long dataId) {
        this.dataId = dataId;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
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

    public Integer getSystemStateNumber() {
        return systemStateNumber;
    }

    public void setSystemStateNumber(Integer systemStateNumber) {
        this.systemStateNumber = systemStateNumber;
    }

    public Long getSystemStateTextId() {
        return systemStateTextId;
    }

    public void setSystemStateTextId(Long systemStateTextId) {
        this.systemStateTextId = systemStateTextId;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }

        if (o == this) {
            return true;
        }

        if (!(o instanceof ProgramSystemStateDto)) {
            return false;
        }

        ProgramSystemStateDto programSystemState = (ProgramSystemStateDto) o;

        return new EqualsBuilder().append(this.text, programSystemState.text).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(this.text).toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append(this.dataId).append(this.text).append(
            this.systemStateNumber).toString();
    }
}



