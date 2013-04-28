
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
 *
 * @author JanL
 */
public class ScreenDto implements Serializable {
    public static final int   COLUMN_NUMBERS   = 4;
    private static final long serialVersionUID = 3L;
    private String            descript;
    private String            display;
    private Long              id;
    private Integer           position;
    private Long              programId;
    private List<TableDto>    tables;
    private String            title;
    private String            unicodeTitle;

    public ScreenDto() {}

    public ScreenDto(Long id, String title) {
        this.id    = id;
        this.title = title;
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

    public String getDisplay() {
        return display;
    }

    public void setDisplay(String dipslay) {
        this.display = dipslay;
    }

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    public String getUnicodeTitle() {
        return (unicodeTitle == null)
               ? ""
               : unicodeTitle;
    }

    public void setUnicodeTitle(String unicodeTitle) {
        this.unicodeTitle = unicodeTitle;
    }

    public String getDescript() {
        return descript;
    }

    public void setDescript(String descript) {
        this.descript = descript;
    }

    public Long getProgramId() {
        return programId;
    }

    public void setProgramId(Long programId) {
        this.programId = programId;
    }

    public List<TableDto> getTables() {
        return tables;
    }

    public void setTables(List<TableDto> tables) {
        this.tables = tables;
    }

    public String isChecked() {
        return ("yes".equals(display))
               ? "checked"
               : "unchecked";
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }

        if (o == this) {
            return true;
        }

        if (!(o instanceof ScreenDto)) {
            return false;
        }

        ScreenDto screen = (ScreenDto) o;

        return new EqualsBuilder().append(this.title, screen.title).isEquals();
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



