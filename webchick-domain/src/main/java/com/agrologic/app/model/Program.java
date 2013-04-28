
/*
* To change this template, choose Tools | Templates
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

/**
 * Title: Program <br> Description: <br> Copyright: Copyright (c) 2009 <br> Company: AgroLogic LTD. <br>
 *
 * @author Valery Manakhimov <br>
 * @version 1.1 <br>
 */
public class Program implements Serializable, Comparable<Program> {
    private static final long        serialVersionUID = 2L;
    private String                   createdDate;
    private Long                     id;
    private String                   modifiedDate;
    private String                   name;
    private List<ProgramAlarm>       programAlarms;
    private List<ProgramRelay>       programRelays;
    private List<ProgramSystemState> programSystemStates;
    private List<Screen>             screens;
    private List<Data>               specialList;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(String modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public List<ProgramAlarm> getProgramAlarms() {
        return programAlarms;
    }

    public void setProgramAlarms(List<ProgramAlarm> programAlarms) {
        this.programAlarms = programAlarms;
    }

    public List<ProgramRelay> getProgramRelays() {
        return programRelays;
    }

    public void setProgramRelays(List<ProgramRelay> programRelays) {
        this.programRelays = programRelays;
    }

    public List<ProgramSystemState> getProgramSystemStates() {
        return programSystemStates;
    }

    public void setProgramSystemStates(List<ProgramSystemState> programSystemStates) {
        this.programSystemStates = programSystemStates;
    }

    public ProgramSystemState getSystemStateByNumber(long number) {
        int systemStateNumber = (int) number;

        for (ProgramSystemState ps : programSystemStates) {
            if (ps.getSystemStateNumber().equals(systemStateNumber)) {
                return ps;
            } else {
                continue;
            }
        }

        return new ProgramSystemState();
    }

    public List<Screen> getScreens() {
        if (screens == null) {
            screens = new ArrayList<Screen>();
        }

        return screens;
    }

    public void setScreens(final List<Screen> screens) {
        this.screens = screens;
    }

    public void addScreen(final Screen screen) {
        getScreens().add(screen);
    }

    public Screen getScreenById(final Long screenId) {
        for (Screen s : getScreens()) {
            if (s.getId().equals(screenId)) {
                return s;
            }
        }

        return null;
    }

    public List<ProgramRelay> getProgramRelayByData(long dataId) {
        List<ProgramRelay> relayByData = new ArrayList<ProgramRelay>();

        for (ProgramRelay e : programRelays) {
            if (e.getDataId() == dataId) {
                relayByData.add(e);
            }
        }

        return relayByData;
    }

    public List<ProgramAlarm> getProgramAlarmsByData(long dataId) {
        List<ProgramAlarm> alarmsByData = new ArrayList<ProgramAlarm>();

        for (ProgramAlarm e : programAlarms) {
            if (e.getDataId() == dataId) {
                alarmsByData.add(e);
            }
        }

        return alarmsByData;
    }

    public List<ProgramSystemState> getProgramSystemStateByData(long dataId) {
        List<ProgramSystemState> systemStateByData = new ArrayList<ProgramSystemState>();

        for (ProgramSystemState e : programSystemStates) {
            if (e.getDataId() == dataId) {
                systemStateByData.add(e);
            }
        }

        return systemStateByData;
    }

    @Override
    public int compareTo(Program o) {
        return this.id.compareTo(o.id);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }

        if (o == this) {
            return true;
        }

        if (!(o instanceof Program)) {
            return false;
        }

        Program program = (Program) o;

        return new EqualsBuilder().append(this.id, program.id).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(this.id).toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append(this.name).toString();
    }

    public List<Data> getSpecialList() {
        return specialList;
    }

    public void setSpecialList(List<Data> sl) {
        this.specialList = sl;
    }
}


