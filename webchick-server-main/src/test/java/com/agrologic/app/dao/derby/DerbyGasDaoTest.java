/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.agrologic.app.dao.derby;

import com.agrologic.app.dao.CreatebleDao;
import com.agrologic.app.dao.DaoType;
import com.agrologic.app.dao.DbImplDecider;
import com.agrologic.app.dao.GasDao;
import com.agrologic.app.dao.derby.impl.DerbyGasDaoImpl;
import com.agrologic.app.model.Gas;
import java.sql.SQLException;
import java.util.List;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import static org.junit.Assert.*;
import org.junit.*;

/**
 *
 * @author Administrator
 */
@Ignore
public class DerbyGasDaoTest {

    private Long flockId = Long.valueOf(1);
    private Gas gas;
    private GasDao dao;

    public DerbyGasDaoTest() {

    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
        dao = DbImplDecider.use(DaoType.MYSQL).getDao(GasDao.class);
    }

//    @Test
    public void testCreateTable() throws SQLException {
        if (!((CreatebleDao) dao).tableExist()) {
            ((CreatebleDao) dao).createTable();
        }
        assertTrue(((CreatebleDao) dao).tableExist());
    }

//    @Test
    public void testDropTable() throws SQLException {
        ((DerbyGasDaoImpl) dao).dropTable();
        assertFalse(((CreatebleDao) dao).tableExist());
    }

//    @Test
    public void testInsert() throws SQLException {
        if (!((CreatebleDao) dao).tableExist()) {
            ((CreatebleDao) dao).createTable();
        }

        gas = new Gas();
        gas.setAmount(Integer.valueOf(500));
        gas.setDate("29/06/2013");
        gas.setFlockId(Long.valueOf(1));
        gas.setNumberAccount(Integer.valueOf(5));
        gas.setPrice(Float.valueOf("12.34"));
        gas.setTotal(Float.valueOf("10.25"));
        dao.insert(gas);
        assertEquals(5, dao.getAllByFlockId(flockId).size());
        List<Gas> gass = dao.getAllByFlockId(flockId);
        for(Gas g:gass) {
            System.out.println(g);
        }
    }

//    @Test
    public void testRemove() throws SQLException {
        dao.remove(gas.getId());
        gas = dao.getById(flockId);
        assertNull(gas);
    }

    @Test
    public void testRemoveAll() throws SQLException {
        List<Gas> gasList = dao.getAllByFlockId(flockId);
        for(Gas g:gasList) {
            dao.remove(g.getId());
        }
        gasList = dao.getAllByFlockId(flockId);
        assertEquals(0,gasList.size());
    }
}
