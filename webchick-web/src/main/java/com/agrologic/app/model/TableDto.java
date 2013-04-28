
/*
* To change this template, choose Tools | Templates
* and open the template in the editor.
 */
package com.agrologic.app.model;



import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

//~--- JDK imports ------------------------------------------------------------

import java.io.Serializable;

import java.util.List;

/**
 * Title: TableDto <br> Decription: <br> Copyright: Copyright (c) 2009 <br> Company: Agro Logic LTD. <br>
 *
 * @author Valery Manakhimov <br>
 * @version 1.1 <br>
 */
public class TableDto implements Serializable, Comparable<TableDto> {
    private static final long serialVersionUID = 3L;
    private List<DataDto>     dataList;
    private String            display;
    private Long              id;
    private Long              langId;
    private Integer           position;
    private Long              programId;
    private Long              screenId;
    private String            title;
    private String            unicodeTitle;

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

    public String getUnicodeTitle() {
        return (unicodeTitle == null)
               ? ""
               : unicodeTitle;
    }

    public void setUnicodeTitle(String unicodeTitle) {
        this.unicodeTitle = unicodeTitle;
    }

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    public List<DataDto> getDataList() {

        // Collections.sort(dataList);
        return dataList;
    }

    public void setDataList(List<DataDto> dataList) {
        this.dataList = dataList;
    }

    public String isChecked() {
        return ("yes".equals(display))
               ? "checked"
               : "unchecked";
    }

    @Override
    public int compareTo(TableDto table) {
        return position.compareTo(table.position);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }

        if (o == this) {
            return true;
        }

        if (!(o instanceof TableDto)) {
            return false;
        }

        TableDto table = (TableDto) o;

        return new EqualsBuilder().append(this.title, table.title).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(this.title).toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append(this.title).toString();
    }
}



