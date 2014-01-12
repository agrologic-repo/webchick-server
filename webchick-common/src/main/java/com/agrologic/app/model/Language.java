package com.agrologic.app.model;

import java.io.Serializable;

public class Language implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long id;
    private String language;
    private String shortLang;

    public Long getId() {
        return id;
    }

    public void setId(Long langId) {
        this.id = langId;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getShortLang() {
        return shortLang;
    }

    public void setShortLang(String shortLang) {
        this.shortLang = shortLang;
    }
}


