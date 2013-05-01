
package com.agrologic.app.model;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import java.io.Serializable;
import java.net.Socket;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class Cellink implements Comparable<Cellink>, Serializable {
    public static final long ONE_HOUR = 1000 * 60 * 60;
    public static final long ONLINE_TIMEOUT = ONE_HOUR * 8;
    private static final long serialVersionUID = 1L;

    /**
     * network traffic should\shouldn't displayed in log flag.
     */
    private Boolean withLogging = false;
    private List<CellinkListener> cellinkStateListeners = new LinkedList<CellinkListener>();
    private Boolean actual;
    private List<Controller> controllers;
    private Long id;
    private String ip;
    private String name;
    private String password;
    private Integer port;
    private Long screenId;
    private String simNumber;
    private int state;
    private Timestamp time;
    private String type;
    private Long userId;
    private Boolean validate;
    private String version;

    public Cellink() {
        this.controllers = new ArrayList<Controller>();
        this.validate = false;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String farmName) {
        this.name = farmName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void setTime(Timestamp time) {
        this.time = time;
    }

    public Timestamp getTime() {
        return time;
    }

    public void setPort(int port) {
        this.setPort((Integer) port);
    }

    public int getPort() {
        return port;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getIp() {
        return ip;
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

    public void setState(Integer state) {
        this.state = state;
        fireCellinkStateChanged(new CellinkEvent(id, new CellinkState(state)));
    }

    public Boolean isActual() {
        return actual;
    }

    public void setActual(Boolean actual) {
        this.actual = actual;
    }

    public String getVersion() {
        return (version == null)
                ? "N/A"
                : version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public boolean getValidate() {
        return isValidate();
    }

    public void setValidate(boolean validate) {
        this.validate = validate;
    }

    public String getSimNumber() {
        return simNumber;
    }

    public void setSimNumber(String simNumber) {
        this.simNumber = simNumber;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void registrate(Socket socket) {
        setIp(socket.getInetAddress().getHostAddress());
        setPort(socket.getPort());
        setTime(new Timestamp(System.currentTimeMillis()));
    }

    public CellinkState getCellinkState() {
        return CellinkState.intToState(getState());
    }

    /**
     * @param port the port to set
     */
    public void setPort(Integer port) {
        this.port = port;
    }

    /**
     * @return the validate
     */
    public boolean isValidate() {
        return validate;
    }

    /**
     * Returns whether network traffic should  displayed in  log .
     *
     * @return the true if it is; false otherwise.
     */
    public Boolean isWithLogging() {
        return withLogging;
    }

    /**
     * Set with logging flag  .
     *
     * @param withLogging the log flag
     */
    public void setWithLogging(Boolean withLogging) {
        if (withLogging == true) {
            System.out.println();
        }

        this.withLogging = withLogging;
    }

    /**
     * Returns an unmodifiable list of controllers.
     *
     * @return an unmodifiable view of the list of controllers.
     */
    public List<Controller> getControllers() {
        if (controllers == null) {
            controllers = new ArrayList<Controller>();
        }

        return Collections.unmodifiableList(controllers);
    }

    /**
     * Set list of controllers to cellink object
     *
     * @param list the list of controllers
     */
    public void setControllers(List<Controller> list) {
        if (controllers == null) {
            controllers = new ArrayList<Controller>();
        }

        for (Controller controller : list) {
            this.controllers.add(controller);
        }
    }

    /**
     * Add controller object to list of controllers
     *
     * @param controller the controller
     */
    public void addController(Controller controller) {
        this.controllers.add(controller);
    }

    /**
     * Returns the controller at the specified position in this list of controllers.
     *
     * @param index - index of the controller object to return
     * @return the controller at the specified position in this list of controllers
     */
    public Controller getController(int index) {
        return controllers.get(index);
    }

    /**
     * Replaces the controller at the specified position in this list of controllers with the specified element.
     *
     * @param index      - index of the controller to replace
     * @param controller - controller to be stored at the specified position
     */
    public void setController(int index, Controller controller) {
        controllers.set(index, controller);
    }

    /**
     * Removes the specified listener from the list of observers.
     *
     * @param listener the listener to remove from list of observers
     * @return <code>true</code> if the specified listener was removed from the list,
     *         <code>false</code> otherwise
     */
    public boolean removeCellinkStateListener(CellinkListener listener) {
        return cellinkStateListeners.remove(listener);
    }

    /**
     * Adds specified listener to the list of observers.
     *
     * @param listener the listener to be added to the list of observers of model changes
     */
    public void addCellinkStateListener(CellinkListener listener) {
        cellinkStateListeners.add(listener);
    }

    /**
     * This method is fired with cellink state were changed.
     *
     * @param event the object that carries the information about event
     */
    public void fireSettingsChanged(CellinkEvent event) {
        for (CellinkListener listener : cellinkStateListeners) {
            listener.cellinkChanged(event);
        }
    }

    /**
     * This method is fired with cellink state were changed.
     *
     * @param event the object that carries the information about event
     */
    public void fireCellinkStateChanged(CellinkEvent event) {
        for (CellinkListener listener : cellinkStateListeners) {
            listener.cellinkChanged(event);
        }
    }

    @Override
    public int compareTo(Cellink o) {
        return this.getId().compareTo(o.getId());
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Cellink)) {
            return false;
        }

        Cellink other = (Cellink) obj;

        return new EqualsBuilder().append(this.id, other.id).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(7, 31).append(this.getId()).toHashCode();
    }

    @Override
    public String toString() {
        if (validate == true) {
            return new StringBuilder().append(" ID: ").append(this.getId()).append("; NAME: ").append(
                    this.getName()).append("; PASSWORD: ").append(this.getPassword()).append("; IP: ").append(
                    this.getIp()).append("; PORT: ").append(this.getPort()).append("; STATE: ").append(
                    this.getState()).append("; VERSION: ").append(this.getVersion()).toString();
        } else {
            return new StringBuilder().append(" NAME : ").append(name).append("; PASSWORD : ").append(
                    password).toString();
        }
    }
}


