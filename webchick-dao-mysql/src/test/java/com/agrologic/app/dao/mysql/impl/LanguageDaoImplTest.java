package com.agrologic.app.dao.mysql.impl;

import com.agrologic.app.model.Language;
import org.junit.Test;

import java.sql.SQLException;

import static org.unitils.reflectionassert.ReflectionAssert.assertReflectionEquals;

public class LanguageDaoImplTest extends AbstractDaoTest {


    @Test
    public void getCanFindAfterInsert() throws Exception {
        Language expected = createLanguage();
        languageDao.insert(expected);
        Language actual = languageDao.getById(1L);
        assertReflectionEquals(expected, actual);
    }

    public Language createLanguage() {
        Language language = new Language();
        language.setId(1L);
        language.setLanguage("Eglish");
        language.setShortLang("en");
        return language;
    }

    @Override
    public void setUp() throws SQLException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void tearDown() throws SQLException {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
