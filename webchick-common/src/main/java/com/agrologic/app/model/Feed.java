package com.agrologic.app.model;

public class Feed {
    private Integer amount;
    private String date;
    private Long type;
    private Long flockId;
    private Long id;
    private Integer numberAccount;
    private Float total;

    public Feed() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getType() {
        return type;
    }

    public void setType(Long type) {
        this.type = type;
    }

    public Integer getNumberAccount() {
        return numberAccount;
    }

    public void setNumberAccount(Integer accountNumber) {
        this.numberAccount = accountNumber;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Long getFlockId() {
        return flockId;
    }

    public void setFlockId(Long flockId) {
        this.flockId = flockId;
    }

    public Float getTotal() {
        return total;
    }

    public void setTotal(Float total) {
        this.total = total;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }

        if (getClass() != obj.getClass()) {
            return false;
        }

        final Feed other = (Feed) obj;

        if ((this.id != other.id) && ((this.id == null) || !this.id.equals(other.id))) {
            return false;
        }

        if ((this.flockId != other.flockId) && ((this.flockId == null) || !this.flockId.equals(other.flockId))) {
            return false;
        }

        if ((this.type != other.type)
                && ((this.type == null) || !this.type.equals(other.type))) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;

        return hash;
    }

    @Override
    public String toString() {
        return "Feed{" + "id=" + id + "flockId=" + flockId + "feedTypeId=" + type + "amount=" + amount
                + "date=" + date + "numberAccount=" + numberAccount + "total=" + total + '}';
    }
}


