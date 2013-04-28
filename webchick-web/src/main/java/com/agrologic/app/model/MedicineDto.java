
/*
* To change this template, choose Tools | Templates
* and open the template in the editor.
 */
package com.agrologic.app.model;

/**
 *
 * @author JanL
 */
public class MedicineDto {
    private Integer amount;
    private Long    flockId;
    private Long    id;
    private String  name;
    private Float   price;
    private Float   total;

    public MedicineDto() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getFlockId() {
        return flockId;
    }

    public void setFlockId(Long flockId) {
        this.flockId = flockId;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
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

        final MedicineDto other = (MedicineDto) obj;

        if ((this.id != other.id) && ((this.id == null) ||!this.id.equals(other.id))) {
            return false;
        }

        if ((this.flockId != other.flockId) && ((this.flockId == null) ||!this.flockId.equals(other.flockId))) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;

        hash = 47 * hash + ((this.id != null)
                            ? this.id.hashCode()
                            : 0);
        hash = 47 * hash + ((this.flockId != null)
                            ? this.flockId.hashCode()
                            : 0);

        return hash;
    }

    @Override
    public String toString() {
        return "MedicineDto{" + "id=" + id + "amount=" + amount + "name=" + name + "price=" + price + "total=" + total
               + '}';
    }
}



