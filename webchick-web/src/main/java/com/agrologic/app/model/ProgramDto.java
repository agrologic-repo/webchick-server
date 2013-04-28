
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

import java.util.ArrayList;
import java.util.List;

/**
 * Title: ProgramDto <br>
 * Description: <br>
 * Copyright:   Copyright (c) 2009 <br>
 * Company:     AgroLogic LTD. <br>
 * @author      Valery Manakhimov <br>
 * @version     1.1 <br>
 */
public class ProgramDto implements Serializable {
    private static final long           serialVersionUID = 2L;
    private String                      createdDate;
    private Long                        id;
    private String                      modifiedDate;
    private String                      name;
    private List<ProgramAlarmDto>       programAlarms;
    private List<ProgramRelayDto>       programRelays;
    private List<ProgramSystemStateDto> programSystemStates;
    private List<ScreenDto>             screens;

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

    public List<ProgramAlarmDto> getProgramAlarms() {
        return programAlarms;
    }

    public void setProgramAlarms(List<ProgramAlarmDto> programAlarms) {
        this.programAlarms = programAlarms;
    }

    public List<ProgramRelayDto> getProgramRelays() {
        return programRelays;
    }

    public void setProgramRelays(List<ProgramRelayDto> programRelays) {
        this.programRelays = programRelays;
    }

    public List<ProgramSystemStateDto> getProgramSystemStates() {
        return programSystemStates;
    }

    public void setProgramSystemStates(List<ProgramSystemStateDto> programSystemStates) {
        this.programSystemStates = programSystemStates;
    }

    public ProgramSystemStateDto getSystemStateByNumber(long number) {
        int systemStateNumber = (int) number;

        for (ProgramSystemStateDto ps : programSystemStates) {
            if (ps.getSystemStateNumber().equals(systemStateNumber)) {
                return ps;
            } else {
                continue;
            }
        }

        return new ProgramSystemStateDto();
    }

    public List<ScreenDto> getScreens() {
        if (screens == null) {
            screens = new ArrayList<ScreenDto>();
        }

        return screens;
    }

    public void setScreens(final List<ScreenDto> screens) {
        this.screens = screens;
    }

    public void addScreen(final ScreenDto screen) {
        getScreens().add(screen);
    }

    public ScreenDto getScreenById(final Long screenId) {
        for (ScreenDto s : getScreens()) {
            if (s.getId().equals(screenId)) {
                return s;
            }
        }

        return null;
    }

    public List<ProgramRelayDto> getProgramRelayByData(long dataId) {
        List<ProgramRelayDto> relayByData = new ArrayList<ProgramRelayDto>();

        for (ProgramRelayDto e : programRelays) {
            if (e.getDataId() == dataId) {
                relayByData.add(e);
            }
        }

        return relayByData;
    }

    public List<ProgramAlarmDto> getProgramAlarmsByData(long dataId) {
        List<ProgramAlarmDto> alarmsByData = new ArrayList<ProgramAlarmDto>();

        for (ProgramAlarmDto e : programAlarms) {
            if (e.getDataId() == dataId) {
                alarmsByData.add(e);
            }
        }

        return alarmsByData;
    }

    public List<ProgramSystemStateDto> getProgramSystemStateByData(long dataId) {
        List<ProgramSystemStateDto> systemStateByData = new ArrayList<ProgramSystemStateDto>();

        for (ProgramSystemStateDto e : programSystemStates) {
            if (e.getDataId() == dataId) {
                systemStateByData.add(e);
            }
        }

        return systemStateByData;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }

        if (o == this) {
            return true;
        }

        if (!(o instanceof ProgramDto)) {
            return false;
        }

        ProgramDto program = (ProgramDto) o;

        return new EqualsBuilder().append(this.name, program.name).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(this.name).toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append(this.name).toString();
    }
}



