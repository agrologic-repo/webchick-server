
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

/**
 * Title: Status <br>
 * Decription: <br>
 * Copyright:   Copyright (c) 2009 <br>
 * Company:     Agro Logic LTD. <br>
 * @author      Valery Manakhimov <br>
 * @version     1.0 <br>
 */
public class RelayDto implements Serializable {
    private Long   id;
    private String text;
    private String unicodeText;

    public RelayDto() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getUnicodeText() {
        return (unicodeText == null)
               ? ""
               : unicodeText;
    }

    public void setUnicodeText(String unicodeText) {
        this.unicodeText = unicodeText;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }

        if (o == this) {
            return true;
        }

        if (!(o instanceof RelayDto)) {
            return false;
        }

        RelayDto relay = (RelayDto) o;

        return new EqualsBuilder().append(this.text, relay.text).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(this.text).toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append(this.text).toString();
    }
}



