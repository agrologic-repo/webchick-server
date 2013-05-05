package com.agrologic.app.model;

import java.io.Serializable;

public class SystemState implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long              id;
    private Long              langId;
    private String            text;
    private String            unicodeText;

    public SystemState() {}

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
        return unicodeText;
    }

    public void setUnicodeText(String unicodeText) {
        this.unicodeText = unicodeText;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }

        if (getClass() != obj.getClass()) {
            return false;
        }

        final SystemState other = (SystemState) obj;

        if ((this.id != other.id) && ((this.id == null) ||!this.id.equals(other.id))) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int hash = 3;

        hash = 43 * hash + ((this.id != null)
                            ? this.id.hashCode()
                            : 0);

        return hash;
    }

    @Override
    public String toString() {
        return "SystemState{" + "id=" + id + ", text=" + text + '}';
    }
}


