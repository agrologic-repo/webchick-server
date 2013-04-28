
/*
* To change this template, choose Tools | Templates
* and open the template in the editor.
 */
package com.agrologic.app.model;

//~--- JDK imports ------------------------------------------------------------

import java.io.Serializable;

public class LanguageDto implements Serializable {
    private Long   id;
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



