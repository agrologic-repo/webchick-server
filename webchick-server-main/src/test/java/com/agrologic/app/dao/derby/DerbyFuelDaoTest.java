/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.agrologic.app.dao.derby;

import com.agrologic.app.dao.CreatebleDao;
import com.agrologic.app.dao.DaoType;
import com.agrologic.app.dao.DbImplDecider;
import com.agrologic.app.dao.FuelDao;
import com.agrologic.app.dao.derby.impl.DerbyFuelDaoImpl;
import com.agrologic.app.model.Fuel;
import java.sql.SQLException;
import java.util.List;

import org.junit.*;

import static org.junit.Assert.*;

/**
 *
 * @author Administrator
 */
@Ignore
public class DerbyFuelDaoTest {

    private Long flockId = Long.valueOf(1);
    private Fuel fuel;
    private FuelDao dao;

    public DerbyFuelDaoTest() {

    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
        dao = DbImplDecider.use(DaoType.MYSQL).getDao(FuelDao.class);
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
        ((DerbyFuelDaoImpl) dao).dropTable();
        assertFalse(((CreatebleDao) dao).tableExist());
    }

//    @Test
    public void testInsert() throws SQLException {
        if (!((CreatebleDao) dao).tableExist()) {
            ((CreatebleDao) dao).createTable();
        }

        fuel = new Fuel();
        fuel.setAmount(Integer.valueOf(500));
        fuel.setDate("29/06/2013");
        fuel.setFlockId(Long.valueOf(1));
        fuel.setNumberAccount(Integer.valueOf(5));
        fuel.setPrice(Float.valueOf("12.34"));
        fuel.setTotal(Float.valueOf("10.25"));
        dao.insert(fuel);
        assertEquals(5, dao.getAllByFlockId(flockId).size());
        List<Fuel> Fuels = dao.getAllByFlockId(flockId);
        for(Fuel g:Fuels) {
            System.out.println(g);
        }
    }

//    @Test
    public void testRemove() throws SQLException {
        dao.remove(fuel.getId());
        fuel = dao.getById(flockId);
        assertNull(fuel);
    }

    @Test
    public void testRemoveAll() throws SQLException {
        List<Fuel> FuelList = dao.getAllByFlockId(flockId);
        for(Fuel g:FuelList) {
            dao.remove(g.getId());
        }
        FuelList = dao.getAllByFlockId(flockId);
        assertEquals(0,FuelList.size());
    }
}
