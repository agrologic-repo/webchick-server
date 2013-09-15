package com.agrologic.app.model;

public class FeedType {
    private Long cellinkId;
    private String feedType;
    private Long id;
    private Float price;

    public FeedType() {
    }

    public Long getCellinkId() {
        return cellinkId;
    }

    public void setCellinkId(Long cellinkId) {
        this.cellinkId = cellinkId;
    }

    public String getFeedType() {
        return feedType;
    }

    public void setFeedType(String feedType) {
        this.feedType = feedType;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }

        if (getClass() != obj.getClass()) {
            return false;
        }

        final FeedType other = (FeedType) obj;

        if ((this.id != other.id) && ((this.id == null) || !this.id.equals(other.id))) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;

        hash = 17 * hash + ((this.id != null)
                ? this.id.hashCode()
                : 0);

        return hash;
    }

    @Override
    public String toString() {
        return "FeedType{" + "id=" + id + "cellinkId=" + cellinkId + "feedType=" + feedType + "price=" + price + '}';
    }
}


