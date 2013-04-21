
/*
* To change this template, choose Tools | Templates
* and open the template in the editor.
 */
package com.agrologic.app.dao;

//~--- non-JDK imports --------------------------------------------------------

import com.agrologic.app.model.Language;

//~--- JDK imports ------------------------------------------------------------

import java.sql.SQLException;

import java.util.Collection;

/**
 * Title: ILanguageDao <br> Description: <br> Copyright: Copyright (c) 2009 <br> Company: Agro Logic LTD. <br>
 *
 * @author Valery Manakhimov <br>
 * @version 1.1 <br>
 */
public interface LanguageDao {

    /**
     *
     * @param language
     * @throws SQLException
     */
    void insert(Language language) throws SQLException;

    /**
     * Insert language names
     *
     * @param languageList the language list
     * @throws SQLException if failed to insert to the language table
     */
    void insert(Collection<Language> languageList) throws SQLException;

    /**
     *
     * @param language
     * @throws SQLException
     */
    void update(Language language) throws SQLException;

    /**
     *
     * @param langId
     * @throws SQLException
     */
    void remove(Long langId) throws SQLException;

    /**
     *
     * @param l
     * @return
     * @throws SQLException
     */
    Long getLanguageId(String l) throws SQLException;

    /**
     *
     * @param langId
     * @return
     * @throws SQLException
     */
    Language getById(Long langId) throws SQLException;

    /**
     *
     * @return @throws SQLException
     */
    Collection<Language> geAll() throws SQLException;
}


//~ Formatted by Jindent --- http://www.jindent.com
