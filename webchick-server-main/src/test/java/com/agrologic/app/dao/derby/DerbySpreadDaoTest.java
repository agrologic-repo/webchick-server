/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.agrologic.app.dao.derby;

import com.agrologic.app.dao.CreatebleDao;
import com.agrologic.app.dao.DaoType;
import com.agrologic.app.dao.DbImplDecider;
import com.agrologic.app.dao.SpreadDao;
import com.agrologic.app.dao.derby.impl.DerbySpreadDaoImpl;
import com.agrologic.app.model.Spread;
import org.junit.*;

import java.sql.SQLException;
import java.util.List;

import static org.junit.Assert.*;

/**
 * @author Administrator
 */
@Ignore
public class DerbySpreadDaoTest {

    private Long flockId = Long.valueOf(1);
    private Spread spread;
    private SpreadDao dao;

    public DerbySpreadDaoTest() {

    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
        dao = DbImplDecider.use(DaoType.MYSQL).getDao(SpreadDao.class);
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
        ((DerbySpreadDaoImpl) dao).dropTable();
        assertFalse(((CreatebleDao) dao).tableExist());
    }

    //    @Test
    public void testInsert() throws SQLException {
        if (!((CreatebleDao) dao).tableExist()) {
            ((CreatebleDao) dao).createTable();
        }

        spread = new Spread();
        spread.setAmount(Integer.valueOf(500));
        spread.setDate("29/06/2013");
        spread.setFlockId(Long.valueOf(1));
        spread.setNumberAccount(Integer.valueOf(5));
        spread.setPrice(Float.valueOf("12.34"));
        spread.setTotal(Float.valueOf("10.25"));
        dao.insert(spread);
        assertEquals(5, dao.getAllByFlockId(flockId).size());
        List<Spread> spreads = dao.getAllByFlockId(flockId);
        for (Spread g : spreads) {
            System.out.println(g);
        }
    }

    //    @Test
    public void testRemove() throws SQLException {
        dao.remove(spread.getId());
        spread = dao.getById(flockId);
        assertNull(spread);
    }

    //    @Test
    public void testRemoveAll() throws SQLException {
        List<Spread> spreadList = dao.getAllByFlockId(flockId);
        for (Spread g : spreadList) {
            dao.remove(g.getId());
        }
        spreadList = dao.getAllByFlockId(flockId);
        assertEquals(0, spreadList.size());
    }
}
