
/*
* To change this template, choose Tools | Templates
* and open the template in the editor.
 */
package com.agrologic.app.model;

//~--- JDK imports ------------------------------------------------------------

import java.io.Serializable;

/**
 * Title: Language.java <br>
 * Description: <br>
 * Copyright:   Copyright � 2010 <br>
 * Company:     Agro Logic Ltd. �<br>
 * @author      Valery Manakhimov <br>
 * @version     0.1.1.1 <br>
 */
public class Language implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long              id;
    private String            language;
    private String            shortLang;

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


//~ Formatted by Jindent --- http://www.jindent.com
