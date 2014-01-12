package com.agrologic.app.model;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import java.io.Serializable;

public class Relay implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long id;
    private Long langId;
    private String text;
    private String unicodeText;

    public Relay() {
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

        if (!(o instanceof Relay)) {
            return false;
        }

        Relay relay = (Relay) o;

        return new EqualsBuilder().append(this.id, relay.id).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(this.id).toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append(this.text).toString();
    }
}


