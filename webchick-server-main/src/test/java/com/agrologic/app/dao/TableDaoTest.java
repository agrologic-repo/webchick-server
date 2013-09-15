/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.agrologic.app.dao;

import com.agrologic.app.model.Table;
import org.junit.*;

import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * @author Administrator
 */
@Ignore
public class TableDaoTest {

    long programId = 83901;
    long screenId = 1;
    long langId = 1;
    TableDao dao;

    public TableDaoTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
        dao = DbImplDecider.use(DaoType.MYSQL).getDao(TableDao.class);
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testGetById() throws SQLException {

        long tableid = 1;
        Table table = dao.getById(programId, screenId, tableid, langId);
        assertNotNull(table);

    }

    @Test
    public void testGetAllScreenTables() throws SQLException {

        Collection<Table> tableList = dao.getScreenTables(programId, screenId, langId, true);
        assertEquals(1, tableList.size());

        List<Table> tables = (List<Table>) tableList;
        assertNotNull(tables.get(0).getUnicodeTitle());
    }

    @Test
    public void testGetAllScreenTablesWithTranslation() throws SQLException {

        Collection<Table> tableList = dao.getAllWithTranslation();
        assertEquals(146, tableList.size());

        List<Table> tables = (List<Table>) tableList;
        assertNotNull(tables.get(0).getUnicodeTitle());

        int len = tables.get(0).getUnicodeTitle().length();
        assertEquals(32, len);
    }
}
