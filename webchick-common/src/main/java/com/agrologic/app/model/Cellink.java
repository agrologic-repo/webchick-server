package com.agrologic.app.model;

import com.google.common.collect.Lists;

import java.io.Serializable;
import java.net.Socket;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;

public class Cellink implements Comparable<Cellink>, Serializable {
    private static final long serialVersionUID = 1234567891234567L;
    private Long id;
    private Long userId;
    private Long screenId;
    private String ip;
    private String name;
    private String password;
    private String version;
    private String type;
    private String simNumber;
    private Integer port;
    private Integer state;
    private Timestamp time;
    private Boolean validate;
    private Boolean actual;
    private Boolean withLogging = false;
    private Collection<Controller> controllers;
    private Collection<CellinkListener> cellinkStateListeners = new LinkedList<CellinkListener>();

    /**
     * List of Strings for cellink types.
     */
    private static final List<String> CELLINK_TYPES = new ArrayList<String>();

    static {
        CELLINK_TYPES.add("WEB");
        CELLINK_TYPES.add("PC");
        CELLINK_TYPES.add("PC&WEB");
        CELLINK_TYPES.add("MRP");// changed 28/05/2017
    }

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

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
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
        try {
            fireCellinkStateChanged(new CellinkEvent(id, new CellinkState(state)));
        } catch (NullPointerException e) {
            // this method works for local version
        }
    }

    public CellinkState getCellinkState() {
        return CellinkState.intToState(state);
    }

    public Boolean isActual() {
        return actual;
    }

    public void setActual(Boolean actual) {
        this.actual = actual;
    }

    public String getVersion() {
        return ((version == null) || version.equals(""))
                ? "N/A"
                : version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public boolean getValidate() {
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

    public Timestamp getTime() {
        return time;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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
        this.withLogging = withLogging;
    }

    /**
     * Returns an unmodifiable list of controllers.
     *
     * @return an unmodifiable view of the list of controllers.
     */
    public Collection<Controller> getControllers() {
        if (controllers == null) {
            controllers = new ArrayList<Controller>();
        }
        return Collections.unmodifiableList((List<? extends Controller>) controllers);
    }

    /**
     * Set collection of controllers to cellink object
     *
     * @param controllers the controllers of controllers
     */
    public void setControllers(Collection<Controller> controllers) {
        for (Controller controller : controllers) {
            addController(controller);
        }
    }

    /**
     * Add controller object to collection of controllers
     *
     * @param controller the controller
     */
    public void addController(Controller controller) {
        if (controllers == null) {
            controllers = new ArrayList<Controller>();
        }
        controllers.add(controller);
    }

    /**
     * Returns the controller at the specified position in this list of controllers.
     *
     * @param index - index of the controller object to return
     * @return the controller at the specified position in this list of controllers
     */
    public Controller getController(int index) {
        return Lists.newArrayList(controllers).get(index);
    }

    /**
     * Replaces the controller at the specified position in this list of controllers with the specified element.
     *
     * @param index      - index of the controller to replace
     * @param controller - controller to be stored at the specified position
     */
    public void setController(int index, Controller controller) {
        //controllers.set(index, controller);
        Lists.newArrayList(controllers).set(index, controller);
    }

    /**
     * Removes the specified listener from the list of observers.
     *
     * @param listener the listener to remove from list of observers
     * @return <code>true</code> if the specified listener was removed from the list,
     * <code>false</code> otherwise
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

    public boolean isOnline() {
        if ((getState() == CellinkState.STATE_OFFLINE) || (getState() == CellinkState.STATE_STOP)
                || (getState() == CellinkState.STATE_UNKNOWN)) {
            return false;
        } else {
            return true;
        }
    }

    public String getImageByState() {
        String imageName = "resources/images/" + CellinkState.stateToString(state) + ".gif";

        return imageName;
    }

    public static List<String> getTypeList() {
        return CELLINK_TYPES;
    }

    public String getFormatedTime() {
        Date now = new Date(time.getTime());
        String DATE_FORMAT = "HH:mm:ss dd/MM/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
        String datetime = sdf.format(now);

        return datetime;
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

    public void registrate(Socket socket) {
        setIp(socket.getInetAddress().getHostAddress());
        setPort(socket.getPort());
        setTime(new Timestamp(System.currentTimeMillis()));
    }

    @Override
    public int compareTo(Cellink o) {
        return this.getId().compareTo(o.getId());
    }

//    @Override
//    public boolean equals(Object obj) {
//        if (!(obj instanceof Cellink)) {
//            return false;
//        }
//
//        Cellink other = (Cellink) obj;
//
//        return new EqualsBuilder().append(this.id, other.id).isEquals();
//    }
//
//    @Override
//    public int hashCode() {
//        return new HashCodeBuilder(7, 31).append(this.getId()).toHashCode();
//    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Cellink)) return false;

        Cellink cellink = (Cellink) o;

        if (!id.equals(cellink.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return id.hashCode();
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


