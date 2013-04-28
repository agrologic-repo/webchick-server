
/*
* Gprs.java
*
* Created on July 7, 2008, 9:35 AM
*
* To change this template, choose Tools | Template Manager
* and open the template in the editor.
 */
package com.agrologic.app.model;



import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

//~--- JDK imports ------------------------------------------------------------

import java.io.Serializable;

import java.util.ArrayList;
import java.util.List;

/**
 * Title:       <br>
 * Decription:
 * Copyright:   Copyright (c) 2008
 * Company:     Agro Logic
 * @author      Valery Manakhimov
 * @version     1.0
 */
public class ControllerDto implements Serializable {
    public static final int   COLUMN_NUMBERS   = 4;
    private static final long serialVersionUID = 1L;
    private boolean           active;
    private Integer           area;
    private Long              cellinkId;
    private List<FlockDto>    flocks;
    private Long              id;
    private String            name;
    private String            netName;
    private ProgramDto        program;
    private Long              programId;
    private DataDto           setClock;
    private DataDto           setDate;
    private String            title;

    public String getName() {
        return name;
    }

    public void setName(String contrName) {
        this.name = contrName;
    }

    public Long getCellinkId() {
        return cellinkId;
    }

    public void setCellinkId(Long gprsId) {
        this.cellinkId = gprsId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNetName() {
        return netName;
    }

    public void setNetName(String netName) {
        this.netName = netName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Long getProgramId() {
        return programId;
    }

    public void setProgramId(Long programId) {
        this.programId = programId;
    }

    public int getArea() {
        return (area == null)
               ? 0
               : area;
    }

    public void setArea(Integer area) {
        this.area = area;
    }

    public ProgramDto getProgram() {
        return program;
    }

    public void setProgram(ProgramDto program) {
        this.program = program;
    }

    public List<ScreenDto> getScreens() {
        return program.getScreens();
    }

    public void setScreens(List<ScreenDto> screens) {
        this.program.setScreens(screens);
    }

    public void setSetClock(DataDto setClock) {
        this.setClock = setClock;
    }

    public DataDto getSetClock() {
        return setClock;
    }

    public void setSetDate(DataDto setDate) {
        this.setDate = setDate;
    }

    public DataDto getSetDate() {
        return setDate;
    }

    public List<TableDto> getSellectedScreenTables(Long screenId) {
        for (ScreenDto s : program.getScreens()) {
            if (s.getId() == screenId) {
                return s.getTables();
            }
        }

        return new ArrayList<TableDto>();
    }

    public DataDto getInterestData(Long screenId, Long tableId, Long dataId) {
        for (ScreenDto s : program.getScreens()) {
            if (s.getId().equals(screenId)) {
                List<TableDto> tables = s.getTables();

                for (TableDto t : tables) {
                    if (t.getId().equals(tableId)) {
                        for (DataDto d : t.getDataList()) {
                            if (d.getId().equals(dataId)) {
                                return d;
                            }
                        }
                    }
                }
            }
        }

        return null;
    }

    public List<FlockDto> getFlocks() {
        return flocks;
    }

    public void setFlocks(List<FlockDto> flocks) {
        this.flocks = flocks;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isAvailable() {
        boolean available = (getSetClock().getValue() == null)
                            ? false
                            : true;

        return available;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }

        if (o == this) {
            return true;
        }

        if (!(o instanceof ControllerDto)) {
            return false;
        }

        ControllerDto contr = (ControllerDto) o;

        return new EqualsBuilder().append(this.id, contr.id).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(this.id).toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append(this.title).append(this.netName).append(this.name).toString();
    }
}



