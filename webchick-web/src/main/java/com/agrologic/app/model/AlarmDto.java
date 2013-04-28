
/*
* To change this template, choose Tools | Templates
* and open the template in the editor.
 */
package com.agrologic.app.model;



import java.io.Serializable;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * Title: AlarmDto <br>
 * Description: <br>
 * Copyright:   Copyright (c) 2009 <br>
 * Company:     AgroLogic LTD. <br>
 * @author      Valery Manakhimov <br>
 * @version     1.1 <br>
 */
public class AlarmDto implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long              id;
    private String            text;
    private String            unicodeText;

    public AlarmDto() {}

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
    public boolean equals(final Object obj) {
        if (obj == null) {
            return false;
        }

        if (!(obj instanceof AlarmDto)) {
            return false;
        }

        final AlarmDto other = (AlarmDto) obj;

        return new EqualsBuilder().append(this.text, other.text).isEquals();
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



