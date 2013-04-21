/**
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.agrologic.app.dao.derby;

import com.agrologic.app.dao.CreatebleDao;
import com.agrologic.app.dao.DaoFactory;
import com.agrologic.app.dao.DaoType;
import com.agrologic.app.dao.DistribDao;
import com.agrologic.app.dao.derby.impl.DerbyDistribDaoImpl;
import com.agrologic.app.model.Distrib;
import java.sql.SQLException;

import org.junit.*;

import static org.junit.Assert.*;

/**
 *
 * @author Administrator
 */
@Ignore
public class DerbyDistribDaoTest {

    private Long flockId = Long.valueOf(1);
    private Distrib distrib;
    private DistribDao dao;

    public DerbyDistribDaoTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
        dao = DaoFactory.getDaoFactory(DaoType.DERBY).getDistribDao();
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
        ((DerbyDistribDaoImpl) dao).dropTable();
        assertFalse(((CreatebleDao) dao).tableExist());
    }

    @Test
    public void testInsert() throws SQLException {
        if (!((CreatebleDao) dao).tableExist()) {
            ((CreatebleDao) dao).createTable();
        }

        distrib = new Distrib();
        distrib.setFlockId(flockId);
        distrib.setAccountNumber(1);
        distrib.setSex("Male");
        distrib.setTarget("Target");
        distrib.setNumOfBirds(10);
        distrib.setQuantityA(5);
        distrib.setQuantityB(2);
        distrib.setQuantityC(3);
        distrib.setBadVeterinary(0);
        distrib.setBadAnother(0);
        distrib.setPriceA(Double.valueOf(0.1));
        distrib.setPriceB(Double.valueOf(0.1));
        distrib.setPriceC(Double.valueOf(0.1));
        distrib.setAgeDistrib(10);
        distrib.setAverageWeight(10);
        distrib.setDtA("10");
        distrib.setDtB("10");
        distrib.setDtC("10");
        distrib.setDtVeterinary("");
        distrib.setDtAnother("");
        distrib.setCalcSum(Double.valueOf(10.0));
        distrib.setHandSum(Double.valueOf(10.0));
        distrib.setTotal(Double.valueOf(10.0));
        dao.insert(distrib);

    }

//    @Test
    public void testRemove() throws SQLException {
        dao.remove(Long.valueOf(3));
        distrib = dao.getById(flockId);
        assertNull(distrib);
    }
}
