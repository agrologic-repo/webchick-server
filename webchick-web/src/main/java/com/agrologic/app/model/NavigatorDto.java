
/*
* To change this template, choose Tools | Templates
* and open the template in the editor.
 */
package com.agrologic.app.model;

//~--- JDK imports ------------------------------------------------------------

import java.io.Serializable;

/**
 * Title: NavigatorDto <br>
 * Description: <br>
 * Copyright:   Copyright (c) 2009 <br>
 * Company:     AgroLogic LTD. <br>
 * @author      Valery Manakhimov <br>
 * @version     1.1 <br>
 */
public class NavigatorDto implements Serializable {
    private static final long serialVersionUID = 3L;
    private Long              navId;
    private String            title;

    public NavigatorDto() {}

    public NavigatorDto(Long screenId, String title) {
        this.navId = screenId;
        this.title = title;
    }

    public Long getNavId() {
        return navId;
    }

    public void setNavId(Long navId) {
        this.navId = navId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }

        if (getClass() != obj.getClass()) {
            return false;
        }

        final NavigatorDto other = (NavigatorDto) obj;

        if ((this.navId != other.navId) && ((this.navId == null) ||!this.navId.equals(other.navId))) {
            return false;
        }

        if ((this.title == null)
            ? (other.title != null)
            : !this.title.equals(other.title)) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;

        hash = 59 * hash + ((this.navId != null)
                            ? this.navId.hashCode()
                            : 0);
        hash = 59 * hash + ((this.title != null)
                            ? this.title.hashCode()
                            : 0);

        return hash;
    }
}



