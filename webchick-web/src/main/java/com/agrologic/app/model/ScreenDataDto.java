
/*
* To change this template, choose Tools | Templates
* and open the template in the editor.
 */
package com.agrologic.app.model;

import java.io.Serializable;

/**
 * @author JanL
 */
public class ScreenDataDto implements Serializable {
    private static final long serialVersionUID = 5L;
    private Long dataId;
    private Long gprsId;
    private Long screenId;

    public ScreenDataDto() {
    }

    public Long getDataId() {
        return dataId;
    }

    public void setDataId(Long dataId) {
        this.dataId = dataId;
    }

    public Long getGprsId() {
        return gprsId;
    }

    public void setGprsId(Long gprsId) {
        this.gprsId = gprsId;
    }

    public Long getScreenId() {
        return screenId;
    }

    public void setScreenId(Long screenId) {
        this.screenId = screenId;
    }
}



