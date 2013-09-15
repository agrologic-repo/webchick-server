
/*
* To change this template, choose Tools | Templates
* and open the template in the editor.
 */
package com.agrologic.app.model;

import java.io.Serializable;

public class ActionSet implements Serializable {
    private static final long serialVersionUID = 2L;
    private Long dataId;
    private String displayOnPage;
    private String label;
    private Integer position;
    private Long programId;
    private Long screenId;
    private String unicodeLabel;
    private Long valueId;

    public ActionSet() {
        position = 0;
    }

    public Long getProgramId() {
        return programId;
    }

    public void setProgramId(Long programId) {
        this.programId = programId;
    }

    public Long getScreenId() {
        return screenId;
    }

    public void setScreenId(Long screenId) {
        this.screenId = screenId;
    }

    public Long getDataId() {
        return dataId;
    }

    public void setDataId(Long dataId) {
        this.dataId = dataId;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Long getValueId() {
        return valueId;
    }

    public void setValueId(Long valueId) {
        this.valueId = valueId;
    }

    /**
     * Get the value of unicodeLabel
     *
     * @return the value of unicodeLabel
     */
    public String getUnicodeLabel() {
        return (unicodeLabel != null)
                ? unicodeLabel
                : label;
    }

    /**
     * Set the value of unicodeLabel
     *
     * @param unicodeLabel new value of unicodeLabel
     */
    public void setUnicodeText(String unicodeLabel) {
        this.unicodeLabel = unicodeLabel;
    }

    public String isDisplayOnPage() {
        return displayOnPage;
    }

    public void setDisplayOnPage(String displayOnPage) {
        this.displayOnPage = displayOnPage;
    }

    public String isChecked() {
        if (displayOnPage == null) {
            return "";
        }

        return (displayOnPage.equals("yes") == true)
                ? "checked"
                : "";
    }

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }

        if (getClass() != obj.getClass()) {
            return false;
        }

        final ActionSet other = (ActionSet) obj;

        if ((this.valueId != other.valueId) && ((this.valueId == null) || !this.valueId.equals(other.valueId))) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;

        hash = 83 * hash + ((this.valueId != null)
                ? this.valueId.hashCode()
                : 0);

        return hash;
    }
}



