/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.agrologic.app.dao.derby;

import com.agrologic.app.dao.CreatebleDao;
import com.agrologic.app.dao.DaoType;
import com.agrologic.app.dao.DbImplDecider;
import com.agrologic.app.dao.LaborDao;
import com.agrologic.app.dao.derby.impl.DerbyLaborDaoImpl;
import com.agrologic.app.model.Labor;
import java.sql.SQLException;
import java.util.List;

import org.junit.*;

import static org.junit.Assert.*;

/**
 *
 * @author Administrator
 */
@Ignore
public class DerbyLaborDaoTest {

    private Long cellinkId = Long.valueOf(34);
    private Long flockId = Long.valueOf(1);
    private Labor labor;
    private LaborDao dao;

    public DerbyLaborDaoTest() {

    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
        dao = DbImplDecider.use(DaoType.MYSQL).getDao(LaborDao.class);
    }

    @Test
    public void testCreateTable() throws SQLException {
        if (!((CreatebleDao) dao).tableExist()) {
            ((CreatebleDao) dao).createTable();
        }
        assertTrue(((CreatebleDao) dao).tableExist());
    }

//    @Test
    public void testDropTable() throws SQLException {
        ((DerbyLaborDaoImpl) dao).dropTable();
        assertFalse(((CreatebleDao) dao).tableExist());
    }

//    @Test
    public void testInsert() throws SQLException {
        if (!((CreatebleDao) dao).tableExist()) {
            ((CreatebleDao) dao).createTable();
        }

        labor = new Labor();
        labor.setFlockId(flockId);
        labor.setDate("3/03/13");
        dao.insert(labor);
    }

//    @Test
    public void testRemove() throws SQLException {
        dao.remove(labor.getId());
        labor = dao.getById(flockId);
        assertNull(labor);
    }
}
