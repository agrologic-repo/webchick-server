package com.agrologic.app.dao;

import com.agrologic.app.model.Language;

import java.sql.SQLException;
import java.util.Collection;

public interface LanguageDao {

    /**
     * @param language
     * @throws java.sql.SQLException
     */
    void insert(Language language) throws SQLException;

    /**
     * Insert language names
     *
     * @param languages the language list
     * @throws java.sql.SQLException if failed to insert to the language table
     */
    void insert(Collection<Language> languages) throws SQLException;

    /**
     * @param language
     * @throws java.sql.SQLException
     */
    void update(Language language) throws SQLException;

    /**
     * @param langId
     * @throws java.sql.SQLException
     */
    void remove(Long langId) throws SQLException;

    /**
     * @param l
     * @return
     * @throws java.sql.SQLException
     */
    Long getLanguageId(String l) throws SQLException;

    /**
     * @param langId
     * @return
     * @throws java.sql.SQLException
     */
    Language getById(Long langId) throws SQLException;

    /**
     * @return @throws SQLException
     */
    Collection<Language> geAll() throws SQLException;
}


