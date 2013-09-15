/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.agrologic.app.dao.derby;

import com.agrologic.app.dao.*;
import com.agrologic.app.model.Language;
import org.junit.*;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

import static org.junit.Assert.*;

/**
 * @author Administrator
 */
@Ignore
public class DerbyLanguageDaoTest extends BaseDaoTestCase {

    Language language;
    LanguageDao dao;

    public DerbyLanguageDaoTest() {
        super();
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
        dao = DbImplDecider.use(DaoType.MYSQL).getDao(LanguageDao.class);
    }

    @After
    public void tearDown() {
    }

    @Test
    public void createTable() throws Exception {
        if (!((CreatebleDao) dao).tableExist()) {
            ((CreatebleDao) dao).createTable();
        }
        assertTrue(((CreatebleDao) dao).tableExist());
    }

    @Test
    public void testAddAndRemoveLanguage() throws Exception {
        language = new Language();
        language.setId(Long.valueOf(1));
        language.setLanguage("English");
        language.setShortLang("en");

        if (dao.getById(language.getId()).getId() == null) {
            dao.insert(language);
        }

        assertEquals(language.getLanguage(), "English");
        assertNotNull(language.getId());

        if (log.isDebugEnabled()) {
            log.debug("removing language...");
        }
        dao.remove(language.getId());

        try {
            language = dao.getById(language.getId());
            if (language.getId() != null) {
                fail("language found in database");
            }
        } catch (SQLException dae) {
            log.debug("Expected exception: " + dae.getMessage());
            assertNotNull(dae);
        }
    }

    @Test
    public void testAddManyLanguages() throws SQLException {

        // if database not empty remove all
        Collection<Language> list = dao.geAll();
        if (list.size() > 0) {
            for (Language l : list) {
                dao.remove(l.getId());
            }
        }

        Collection<Language> languages = new ArrayList<Language>();
        Language english = new Language();
        english.setId(Long.valueOf(1));
        english.setLanguage("English");
        english.setShortLang("en");
        languages.add(english);
        Language hebrew = new Language();
        hebrew.setId(Long.valueOf(2));
        hebrew.setLanguage("Hebrew");
        hebrew.setShortLang("iw");
        languages.add(hebrew);
        Language russian = new Language();
        russian.setId(Long.valueOf(3));
        russian.setLanguage("Russian");
        russian.setShortLang("ru");
        languages.add(russian);

        for (Language l : languages) {
            dao.insert(l);
        }

        list = dao.geAll();
        assertEquals(languages.size(), list.size());
    }
}
