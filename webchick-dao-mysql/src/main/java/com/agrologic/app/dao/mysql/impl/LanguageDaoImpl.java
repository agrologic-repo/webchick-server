package com.agrologic.app.dao.mysql.impl;

import com.agrologic.app.dao.LanguageDao;
import com.agrologic.app.dao.mappers.RowMappers;
import com.agrologic.app.model.Language;
import org.apache.commons.lang.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LanguageDaoImpl implements LanguageDao {

    protected final Logger logger = LoggerFactory.getLogger(LanguageDaoImpl.class);
    protected final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;

    public LanguageDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.jdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
        this.jdbcInsert.setTableName("languages");
    }

    public void insert(Language language) throws SQLException {
        logger.debug("Inserting language with id [{}]", language.getId());
        Map<String, Object> valuesToInsert = new HashMap<String, Object>();
        valuesToInsert.put("id", language.getId());
        valuesToInsert.put("lang", language.getLanguage());
        valuesToInsert.put("short", language.getShortLang());
        jdbcInsert.execute(valuesToInsert);
    }

    public void insert(Collection<Language> languages) throws SQLException {
        for (Language language : languages) {
            insert(language);
        }
    }

    public void update(Language language) throws SQLException {
        logger.debug("Update language with id [{}]", language.getId());
        jdbcTemplate.update("update languages set Name=? where ID=?", new Object[]{language.getLanguage(),
                language.getShortLang(), language.getId()});
    }

    public void remove(Long langId) throws SQLException {
        Validate.notNull(langId, "Language id can not be null");
        logger.debug("Delete alarm with id [{}]", langId);
        jdbcTemplate.update("delete from languages where ID=?", new Object[]{langId});
    }

    @Override
    public Long getLanguageId(String l) throws SQLException {
        String sqlQuery = "select id from languages where short like ?";
        return jdbcTemplate.queryForLong(sqlQuery, new Object[]{"%" + l + "%"});
    }

    @Override
    public Language getById(Long langId) throws SQLException {
        logger.debug("Get language with id [{}]", langId);
        String sqlQuery = "select * from languages where ID=?";
        List<Language> languages = jdbcTemplate.query(sqlQuery, new Object[]{langId}, RowMappers.language());
        if (languages.isEmpty()) {
            return null;
        }
        return languages.get(0);
    }

    @Override
    public Collection<Language> geAll() throws SQLException {
        logger.debug("Get all languages");
        String sqlQuery = "select * from languages";
        return jdbcTemplate.query(sqlQuery, RowMappers.language());
    }
}
