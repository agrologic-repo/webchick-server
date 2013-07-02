package com.agrologic.app.model;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import java.io.Serializable;

public class ProgramRelay implements Serializable {
    private static final long serialVersionUID = 1L;
    private Integer bitNumber;
    private Long dataId;
    private Integer index;
    private String on;
    private Long programId;
    private Integer relayNumber;
    private Long relayTextId;
    private String text;
    private String unicodeText;

    public ProgramRelay() {
    }

    public Integer getBitNumber() {
        return bitNumber;
    }

    public void setBitNumber(Integer bitNumber) {
        this.bitNumber = bitNumber;
    }

    public Long getRelayTextId() {
        return relayTextId;
    }

    public void setRelayTextId(Long relayTextId) {
        this.relayTextId = relayTextId;
    }

    public Integer getRelayNumber() {
        return relayNumber;
    }

    public void setRelayNumber(Integer relayNumber) {
        this.relayNumber = relayNumber;
    }

    public Long getDataId() {
        return dataId;
    }

    public void setDataId(Long dataId) {
        this.dataId = dataId;
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

    public String getUnicodeText() {
        return unicodeText;
    }

    public void setUnicodeText(String unicodeText) {
        this.unicodeText = unicodeText;
    }

    // TODO : delete if code is unused
    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    // TODO : delete if code is unused
    public String getOn() {
        return on;
    }

    public void setOn() {
        this.on = "ON";
    }

    public void setOff() {
        this.on = "OFF";
    }

    public void init(long val) {
        if (val == -1) {
            setOff();
            return;
        }
        int res = ((int) val >> bitNumber) & 0x0001;

        if (res > 0) {
            setOn();
        } else {
            setOff();
        }
    }

    public boolean isOn() {
        return on.equals("ON")
                ? true
                : false;
    }

    public String displayTemplateValue() {
        return (bitNumber % 2 == 0)
                ? "On"
                : "Off";
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }

        if (o == this) {
            return true;
        }

        if (!(o instanceof ProgramRelay)) {
            return false;
        }

        ProgramRelay programRelay = (ProgramRelay) o;

        return new EqualsBuilder().append(this.text, programRelay.text).append(this.relayNumber,
                programRelay.relayNumber).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(this.text).append(this.relayNumber).toHashCode();
    }

    @Override
    public String toString() {
        return new StringBuilder().append(" ID : ").append(dataId).append(" TYPE : ").append(bitNumber).append(
                " LABEL :").append(text).append(" RELAY TEXT ID : ").append(relayTextId).toString();
    }
}


