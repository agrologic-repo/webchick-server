package com.agrologic.app.model;

/*
* CellinkDto.java
*
* Created on 10 August 2008 , 15:03
*
* To change this template, choose Tools | Template Manager
* and open the template in the editor.
 */



import com.agrologic.app.web.CellinkState;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

//~--- JDK imports ------------------------------------------------------------

import java.io.Serializable;

import java.sql.Timestamp;

import java.text.SimpleDateFormat;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Title:  CellinkDto<br>
 * Description:  <br>
 * Copyright:   Copyright (c) 2008 <br>
 * Company:     AgroLogic LTD. <br>
 * @author      Valery Manakhimov <br>
 * @version     1.0 <br>
 */
public class CellinkDto implements Serializable {
    private static final long serialVersionUID = 2L;

    /** List of Strings for cellink types. */
    private static final List<String> CELLINK_TYPES = new ArrayList<String>();

    // initialize a String -> CellinkState map
    static {
        CELLINK_TYPES.add("WEB");
        CELLINK_TYPES.add("PC");
        CELLINK_TYPES.add("PC&WEB");
        CELLINK_TYPES.add("MINSERVER");
    }

    private boolean             actual;
    private List<ControllerDto> controllers;
    private Long                id;
    private String              ip;
    private String              name;
    private String              password;
    private Integer             port;
    private Long                screenId;
    private String              simNumber;
    private Integer             state;
    private Timestamp           time;
    private String              type;
    private Long                userId;
    private Boolean             validate;
    private String              version;

    /**
     * Constructor without parameters
     */
    public CellinkDto() {
        controllers = new ArrayList<ControllerDto>();
        validate    = false;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public Long getScreenId() {
        return screenId;
    }

    public void setScreenId(Long screenId) {
        this.screenId = screenId;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public Timestamp getTime() {
        return time;
    }

    public String getFormatedTime() {
        Date             now         = new Date(time.getTime());
        String           DATE_FORMAT = "HH:mm:ss dd/MM/yyyy";
        SimpleDateFormat sdf         = new SimpleDateFormat(DATE_FORMAT);
        String           datetime    = sdf.format(now);

        return datetime;    // DateFormat.getInstance().format(now);

        // return DateFormat.getTimeInstance(DateFormat.LONG).format(now);
    }

    public void setTime(Timestamp time) {
        this.time = time;
    }

    public String getSimNumber() {
        return simNumber;
    }

    public void setSimNumber(String simNumber) {
        this.simNumber = simNumber;
    }

    public boolean isValidate() {
        return validate;
    }

    public void setValidate(boolean validate) {
        this.validate = validate;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getVersion() {
        if ((version == null) || version.equals("")) {
            version = "n/a";
        }

        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public boolean isActual() {
        return actual;
    }

    public void setActual(boolean actual) {
        this.actual = actual;
    }

    public List<ControllerDto> getControllers() {
        return controllers;
    }

    public void setControllers(List<ControllerDto> controllers) {
        this.controllers = controllers;
    }

    public void addController(ControllerDto controller) {
        controllers.add(controller);
    }

    public CellinkState getCellinkState() {
        return CellinkState.intToState(getState());
    }

    public boolean isOnline() {
        if ((getState() == CellinkState.STATE_OFFLINE) || (getState() == CellinkState.STATE_STOP)
                || (getState() == CellinkState.STATE_UNKNOWN)) {
            return false;
        } else {
            return true;
        }
    }

    public String getImageByState() {
        String imageName = "img/" + CellinkState.stateToString(state) + ".gif";

        return imageName;
    }

    public static List<String> getTypeList() {
        return CELLINK_TYPES;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }

        if (o == this) {
            return true;
        }

        if (!(o instanceof CellinkDto)) {
            return false;
        }

        CellinkDto farm = (CellinkDto) o;

        return new EqualsBuilder().append(this.name, farm.name).append(this.password, farm.password).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(this.name).append(this.password).toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append(this.name).append(this.password).toString();
    }
}



