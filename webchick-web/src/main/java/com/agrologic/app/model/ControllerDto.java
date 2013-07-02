
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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ControllerDto implements Serializable {
    public static final int COLUMN_NUMBERS = 4;
    private static final long serialVersionUID = 1L;
    private boolean active;
    private Integer area;
    private Long cellinkId;
    private List<FlockDto> flocks;
    private Long id;
    private String name;
    private String netName;
    private Program program;
    private Long programId;
    private Data setClock;
    private Data setDate;
    private String title;

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

    public Program getProgram() {
        return program;
    }

    public void setProgram(Program program) {
        this.program = program;
    }

    public List<Screen> getScreens() {
        return program.getScreens();
    }

    public void setScreens(List<Screen> screens) {
        this.program.setScreens(screens);
    }

    public void setSetClock(Data setClock) {
        this.setClock = setClock;
    }

    public Data getSetClock() {
        return setClock;
    }

    public void setSetDate(Data setDate) {
        this.setDate = setDate;
    }

    public Data getSetDate() {
        return setDate;
    }

    public Collection<Table> getSellectedScreenTables(Long screenId) {
        for (Screen s : program.getScreens()) {
            if (s.getId() == screenId) {
                return s.getTables();
            }
        }

        return new ArrayList<Table>();
    }

    public Data getInterestData(Long screenId, Long tableId, Long dataId) {
        for (Screen s : program.getScreens()) {
            if (s.getId().equals(screenId)) {
                Collection<Table> tables = s.getTables();

                for (Table t : tables) {
                    if (t.getId().equals(tableId)) {
                        for (Data d : t.getDataList()) {
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



