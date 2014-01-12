package com.agrologic.app.model;

import java.io.Serializable;

public class HistorySetting implements Serializable {
    private static final long serialVersionUID = 1L;
    private String checked;
    private long dataId;
    private long programId;

    public HistorySetting() {
    }

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



