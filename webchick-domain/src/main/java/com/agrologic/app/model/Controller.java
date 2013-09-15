package com.agrologic.app.model;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import java.io.Serializable;
import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;

public class Controller implements Serializable {
    private static final long serialVersionUID = 166073822830898227L;
    private static final long OFF_STATE_DELAY = TimeUnit.MINUTES.toMillis(10);
    private Long id;
    private Long cellinkId;
    private Long programId;
    private Long offStateTime;
    private Long graphUpdateTime;
    private String title;
    private String name;
    private String netName;
    private Integer area;
    private Data setClock;
    private boolean on = true;
    private boolean active;
    private Program program;
    private Collection<Flock> flocks;
    private HashMap<Long, Data> onlineData;
    public static final int COLUMN_NUMBERS = 4;

    public Controller() {
        onlineData = new LinkedHashMap<Long, Data>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getCellinkId() {
        return cellinkId;
    }

    public void setCellinkId(Long cellinkId) {
        this.cellinkId = cellinkId;
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

    public Integer getArea() {
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

    public Long getGraphUpdateTime() {
        return graphUpdateTime;
    }

    public void setGraphUpdateTime(long graphUpdateTime) {
        this.graphUpdateTime = graphUpdateTime;
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
        offStateTime = 0L;
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


    public Collection<Flock> getFlocks() {
        return flocks;
    }

    public void setFlocks(Collection<Flock> flocks) {
        this.flocks = flocks;
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


