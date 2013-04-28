
/*
* To change this template, choose Tools | Templates
* and open the template in the editor.
 */
package com.agrologic.app.model;

//~--- JDK imports ------------------------------------------------------------

import java.io.Serializable;

public class HistorySettingDto implements Serializable {
    private static final long serialVersionUID = 1L;
    private String            checked;
    private long              dataId;
    private long              programId;

    public HistorySettingDto() {}

    public String getChecked() {
        return checked;
    }

    public void setChecked(String checked) {
        this.checked = checked;
    }

    public long getDataId() {
        return dataId;
    }

    public void setDataId(long dataId) {
        this.dataId = dataId;
    }

    public long getProgramId() {
        return programId;
    }

    public void setProgramId(long programId) {
        this.programId = programId;
    }
}



