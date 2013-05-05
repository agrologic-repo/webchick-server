package com.agrologic.app.model;

public class Worker {
    private Long   cellinkId;
    private String define;
    private Float  hourCost;
    private Long   id;
    private String name;
    private String phone;

    public Worker() {}

    public String getDefine() {
        return define;
    }

    public void setDefine(String define) {
        this.define = define;
    }

    public Float getHourCost() {
        return hourCost;
    }

    public void setHourCost(Float hourCost) {
        this.hourCost = hourCost;
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

    public Long getCellinkId() {
        return cellinkId;
    }

    public void setCellinkId(Long cllinkId) {
        this.cellinkId = cllinkId;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }

        if (getClass() != obj.getClass()) {
            return false;
        }

        final Worker other = (Worker) obj;

        if ((this.id != other.id) && ((this.id == null) ||!this.id.equals(other.id))) {
            return false;
        }

        if ((this.name == null)
            ? (other.name != null)
            : !this.name.equals(other.name)) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;

        hash = 11 * hash + ((this.id != null)
                            ? this.id.hashCode()
                            : 0);
        hash = 11 * hash + ((this.name != null)
                            ? this.name.hashCode()
                            : 0);

        return hash;
    }

    @Override
    public String toString() {
        return "WorkerDto{" + "name=" + name + "define=" + define + "hourCost=" + hourCost + '}';
    }
}


