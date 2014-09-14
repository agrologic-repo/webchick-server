package com.agrologic.app.dao.derby.impl;

import com.agrologic.app.model.Data;
import com.agrologic.app.model.Language;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;

import static org.unitils.reflectionassert.ReflectionAssert.assertReflectionEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"/common-dao-context.xml", "/derby-dao-context.xml"})
@TransactionConfiguration
@Transactional
public class DerbyDataDaoImplTest extends AbstractDaoTest {

    @Override
    public void setUp() throws SQLException {
        //alarmDao.remove(alarm());
    }

    @Override
    public void tearDown() throws SQLException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Test
    @Ignore
    public void getCanFindAfterInsert() throws Exception {
        dataDao.migrate("ALTER TABLE DATATABLE ADD COLUMN HISTORYOPT VARCHAR(10) ");
        dataDao.migrate("ALTER TABLE DATATABLE ADD COLUMN HISTORYDNUM VARCHAR(12) ");
        Data expected = data();
        dataDao.insert(expected);

        Language language = language();
        language.setId(2L);
        language.setLanguage("Hebrew");
        language.setShortLang("iw");
        languageDao.insert(language);

        dataDao.insertDataTranslation(expected.getId(), 2L, expected.getUnicodeLabel());

        Data actual = dataDao.getById(expected.getId(), 2L);
        assertReflectionEquals(expected, actual);
    }
}
