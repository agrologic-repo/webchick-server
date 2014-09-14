package com.agrologic.app.service;

import com.agrologic.app.dao.DaoType;
import com.agrologic.app.dao.DbImplDecider;
import com.agrologic.app.dao.LanguageDao;

import java.sql.SQLException;

/**
 * Created by Valery on 1/14/14.
 */
public class LocaleService {
    private LanguageDao languageDao;

    public LocaleService() {
        languageDao = DbImplDecider.use(DaoType.MYSQL).getDao(LanguageDao.class);
    }

    public Long getLanguageId(String lang) throws SQLException {
        return languageDao.getLanguageId(lang);
    }
}
