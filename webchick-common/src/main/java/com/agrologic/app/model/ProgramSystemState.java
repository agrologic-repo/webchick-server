package com.agrologic.app.model;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import java.io.Serializable;

public class ProgramSystemState implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long dataId;
    private Integer number;
    private Long programId;
    private Integer systemStateNumber;
    private Long systemStateTextId;
    private String text;

    public ProgramSystemState() {
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

        if (!(o instanceof ProgramSystemState)) {
            return false;
        }

        ProgramSystemState programSystemState = (ProgramSystemState) o;

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


