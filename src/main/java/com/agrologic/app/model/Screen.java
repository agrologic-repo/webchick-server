
/*
* To change this template, choose Tools | Templates
* and open the template in the editor.
 */
package com.agrologic.app.model;

//~--- JDK imports ------------------------------------------------------------

import java.io.Serializable;

import java.util.List;

/**
 *
 * @author JanL
 */
public class Screen implements Serializable, Comparable<Screen> {
    public static final int   COLUMN_NUMBERS   = 4;
    private static final long serialVersionUID = 3L;
    private String            descript;
    private String            display;
    private Long              id;
    private Long              langId;
    private Integer           position;
    private Long              programId;
    private List<Table>       tables;
    private String            title;
    private String            unicodeTitle;

    public Screen() {}

    public Screen(Long id, String title) {
        this.id    = id;
        this.title = title;
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

    public Long getProgramId() {
        return programId;
    }

    public void setProgramId(Long programId) {
        this.programId = programId;
    }

    public String getUnicodeTitle() {
        return unicodeTitle;
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

    public List<Table> getTables() {
        return tables;
    }

    public void setTables(List<Table> tables) {
        this.tables = tables;
    }

    public Table getTableById(final Long tableId) {
        for (Table t : getTables()) {
            if (t.getId().equals(tableId)) {
                return t;
            }
        }

        return null;
    }

    public String isChecked() {
        return ("yes".equals(display))
               ? "checked"
               : "unchecked";
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }

        if (getClass() != obj.getClass()) {
            return false;
        }

        final Screen other = (Screen) obj;

        if ((this.id != other.id) && ((this.id == null) ||!this.id.equals(other.id))) {
            return false;
        }

        if ((this.programId != other.programId)
                && ((this.programId == null) ||!this.programId.equals(other.programId))) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;

        hash = 61 * hash + ((this.id != null)
                            ? this.id.hashCode()
                            : 0);
        hash = 61 * hash + ((this.programId != null)
                            ? this.programId.hashCode()
                            : 0);

        return hash;
    }

    @Override
    public String toString() {
        return new StringBuilder().append(" ID : ").append(id).append(" TITLE :").append(title).append(
            " PROGRAMID : ").append(programId).toString();
    }

    @Override
    public int compareTo(Screen o) {
        return position.compareTo(o.position);
    }
}


//~ Formatted by Jindent --- http://www.jindent.com
