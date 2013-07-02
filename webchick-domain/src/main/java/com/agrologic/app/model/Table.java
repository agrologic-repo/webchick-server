package com.agrologic.app.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

public class Table implements Serializable, Comparable<Table> {
    private static final long serialVersionUID = 3L;
    private Collection<Data> dataList;
    private String display;
    private Long id;
    private Long langId;
    private Integer position;
    private Long programId;
    private Long screenId;
    private String title;
    private String unicodeTitle;

    public String getDisplay() {
        return display;
    }

    public void setDisplay(String display) {
        this.display = display;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getLangId() {
        return langId;
    }

    public void setLangId(Long langId) {
        this.langId = langId;
    }

    public Long getScreenId() {
        return screenId;
    }

    public void setScreenId(Long screenId) {
        this.screenId = screenId;
    }

    public Long getProgramId() {
        return programId;
    }

    public void setProgramId(Long programId) {
        this.programId = programId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    public Collection<Data> getDataList() {
        if (dataList == null) {
            return new ArrayList<Data>();
        }
        // Collections.sort(dataList);
        return dataList;
    }

    public void setDataList(Collection<Data> dataList) {
        this.dataList = dataList;
    }

//    public void replaceWithSpecialData(List<Data> specialDataList) {
//        for(Data d:specialDataList) {
//            if(dataList.indexOf(d) != -1) {
//                int index = dataList.indexOf(d);
//                dataList.get(index).setUnicodeLabel(d.getUnicodeLabel());
//            }
//        }
//    }
//
//    public void updateDataList(List<Data> updatedDataList) {
//        for (Data d : updatedDataList) {
//            int i = dataList.indexOf(d);
//
//            if (i != -1) {
//                dataList.set(i, d);
//            }
//        }
//
////      this.dataList = dataList;
//    }

    public String isChecked() {
        return ("yes".equals(display))
                ? "checked"
                : "unchecked";
    }

    public String getUnicodeTitle() {
        return (unicodeTitle == null)
                ? ""
                : unicodeTitle;
    }

    public void setUnicodeTitle(String unicodeTitle) {
        this.unicodeTitle = unicodeTitle;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }

        if (getClass() != obj.getClass()) {
            return false;
        }

        final Table other = (Table) obj;

        if ((this.id != other.id) && ((this.id == null) || !this.id.equals(other.id))) {
            return false;
        }

        if ((this.screenId != other.screenId) && ((this.screenId == null) || !this.screenId.equals(other.screenId))) {
            return false;
        }

        if ((this.programId != other.programId)
                && ((this.programId == null) || !this.programId.equals(other.programId))) {
            return false;
        }

        if ((this.position != other.position) && ((this.position == null) || !this.position.equals(other.position))) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int hash = 3;

        hash = 67 * hash + ((this.id != null)
                ? this.id.hashCode()
                : 0);
        hash = 67 * hash + ((this.screenId != null)
                ? this.screenId.hashCode()
                : 0);
        hash = 67 * hash + ((this.programId != null)
                ? this.programId.hashCode()
                : 0);
        hash = 67 * hash + ((this.position != null)
                ? this.position.hashCode()
                : 0);

        return hash;
    }

    @Override
    public String toString() {
        return new StringBuilder().append(" ID : ").append(id).append(" TITLE :").append(title).append(
                " POSITION : ").append(position).append(" SCREENID : ").append(screenId).append(" PROGRAMID : ").append(
                programId).toString();
    }

    @Override
    public int compareTo(Table table) {
        return position.compareTo(table.position);
    }
}


