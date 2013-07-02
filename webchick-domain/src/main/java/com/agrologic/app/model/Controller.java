package com.agrologic.app.model;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;

public class Controller implements Serializable {

    private static final long serialVersionUID = 1L;
    public static final long OFF_STATE_DELAY = TimeUnit.MINUTES.toMillis(10);
    private boolean on = true;
    private boolean active;
    private Integer area;
    private Long cellinkId;
    private long graphUpdateTime;
    private Long id;
    private String name;
    private String netName;
    private long offStateTime;
    private HashMap<Long, Data> onlineData;
    private Program program;
    private Long programId;
    private String title;
    private int networkOkCount;

    public Controller() {
        // recieve message counter
        networkOkCount = 4;
        onlineData = new LinkedHashMap<Long, Data>();
    }

    public void decNetworkOkCount() {
        networkOkCount--;
    }

    public boolean isNetworkOkCounter() {
        return networkOkCount == 0 ? true : false;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getNetName() {
        return netName;
    }

    public void setNetName(String netName) {
        this.netName = netName;
    }

    public Long getTerminalId() {
        return cellinkId;
    }

    public Long getCellinkId() {
        return cellinkId;
    }

    public void setCellinkId(Long cellinkId) {
        this.cellinkId = cellinkId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getArea() {
        return area;
    }

    public void setArea(Integer area) {
        this.area = area;
    }

    public Long getProgramId() {
        return programId;
    }

    public void setProgramId(Long programId) {
        this.programId = programId;
    }

    public long getGraphUpdateTime() {
        return graphUpdateTime;
    }

    public void setGraphUpdateTime(long graphUpdateTime) {
        this.graphUpdateTime = graphUpdateTime;
    }

    public long timeSinceGraphUpdated() {
        return System.currentTimeMillis() - graphUpdateTime;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    /**
     * If the controller doesn't respond to the request, the controller shuts down for a period of 1 minute.
     *
     * @return true if controller is on , otherwise false
     */
    public boolean isOn() {
        return on;
    }

    /**
     * Set state on
     */
    public void switchOn() {
        on = true;
        offStateTime = 0;
    }

    /**
     * If the controller doesn't respond to the request, stop asking controller for a while(1 minute)
     */
    public void switchOff() {
        on = false;
        offStateTime = System.currentTimeMillis();
    }

    /**
     * Controller in off state timeout checking.
     *
     * @return true if timeout expired, otherwise false;
     */
    public boolean shouldExitFromOfState() {
        if (!isOn() && (offStateTime + OFF_STATE_DELAY) < System.currentTimeMillis()) {
            return true;
        }

        return false;
    }

    /**
     * Get controller communication status string .
     *
     * @return string net name : on if communication OK , otherwise Off .
     */
    public String getStatusString() {
        if (on) {
            return new StringBuilder().append(netName).append(":").append("On;").toString();
        } else {
            return new StringBuilder().append(netName).append(":").append("Off;").toString();
        }
    }

    public Program getProgram() {
        return program;
    }

    public void setProgram(Program program) {
        this.program = program;
    }

    /**
     * Initialize online data map of controller object.
     *
     * @param dataMap the map of data by data id.
     */
    public final void initOnlineData(final Map<Long, Data> dataMap) {
        Iterator<Map.Entry<Long, Data>> iter = dataMap.entrySet().iterator();
        while (iter.hasNext()) {
            Entry<Long, Data> entry = iter.next();
            onlineData.put(entry.getKey(), entry.getValue());
        }
    }

    public final Iterator<Map.Entry<Long, Data>> getDataValues() {
        return onlineData.entrySet().iterator();
    }

    public Map getOnlineData() {
        return onlineData;
    }

    @Override
    public boolean equals(Object obj) {
        if (null == obj) {
            return false;
        }

        if (obj == this) {
            return true;
        }

        if (!(obj instanceof Controller)) {
            return false;
        }

        Controller other = (Controller) obj;

        return new EqualsBuilder().append(this.getTitle(), other.getTitle()).append(this.getNetName(),
                other.getNetName()).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(13, 37).append(this.getTitle()).append(this.getNetName()).toHashCode();
    }

    @Override
    public String toString() {
        return new StringBuilder().append(" ID: ").append(this.getId()).append(" TITLE: ").append(
                this.getTitle()).append(" NET : ").append(this.getNetName()).append(" NAME : ").append(
                this.getName()).toString();
    }
}


