/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.agrologic.app.dao.derby;

import com.agrologic.app.dao.*;
import com.agrologic.app.dao.derby.impl.DerbyFlockDaoImpl;
import com.agrologic.app.model.Flock;
import java.sql.SQLException;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import static org.junit.Assert.*;
import org.junit.*;

/**
 *
 * @author Administrator
 */
public class DerbyFlockDaoTest extends BaseDaoTestCase {

    private long flockId = 1;
    private Flock flock;
    private FlockDao dao;

    public DerbyFlockDaoTest() {
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
        dao = DaoFactory.getDaoFactory(DaoType.DERBY).getFlockDao();
    }

    @After
    public void tearDown() {
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
        ((DerbyFlockDaoImpl) dao).dropTable();
        assertFalse(((CreatebleDao) dao).tableExist());
    }

//    @Test
    public void testInsert() throws SQLException {
        if (!((CreatebleDao) dao).tableExist()) {
            ((CreatebleDao) dao).createTable();
        }
        flock = new Flock();
        flock.setFlockId(Long.valueOf(1));
        flock.setControllerId(Long.valueOf(216));
        flock.setFlockName("Flock 1");
        flock.setStatus("Open");
        flock.setStartDate("22/8/2011");
        dao.insert(flock);
        assertEquals(flock, dao.getById(flockId));
    }

//    @Test
    public void testRemove() throws SQLException {
        dao.remove(flockId);
        flock = dao.getById(flockId);
        assertNull(flock);
    }

    @Test
    public void testRemoveFlockHistory() throws SQLException {
        Integer growDay = dao.getUpdatedGrowDayHistory(flockId);
        for (int i = growDay; i > 0; i--) {
            dao.removeHistoryByGrowDay(flockId, i);
        }
        Integer updatedGrowDay = dao.getUpdatedGrowDayHistory(flockId);
        assertNotSame(growDay, updatedGrowDay);
    }

//    @Test
    public void testGetFlockByController() throws SQLException {
        long cid = 57;
        Flock flock = dao.getOpenFlockByController(cid);
        assertNotNull(flock);
    }

    @Test
    public void testGetUpdatedGrowDayHistory() throws SQLException {
        Integer growDay = dao.getUpdatedGrowDayHistory(flockId);
        System.out.println(growDay);
    }

//    @Test
    public void testGetUpdatedGrowDayHistory24() throws SQLException {
        Integer growDay = dao.getUpdatedGrowDayHistory24(flockId);
        System.out.println(growDay);
    }

//    @Test
    public void testUpdateHistoryByGrowDay() throws SQLException {
        long id = 1;
        int growday = 1;
        String values = "1301 239 1302 0 1329 10 1400 0 1401 0 1402 0 1403 0 1404 0 1405 0 1406 0 1407 0 1413 0 1414 0 "
                + "1415 0 1416 0 1417 0 1418 0 1419 0 1420 0 1426 0 1427 0 1428 0 1429 0 1430 0 1431 0 1432 0 1433 0 "
                + "1439 0 1440 0 1441 0 1442 0 1443 0 1444 0 1445 0 1446 0 1452 0 1453 0 1454 0 1455 0 1456 0 1457 0 "
                + "1458 0 1459 0 1477 0 1478 0 1479 0 1480 0 1481 0 1482 0 1483 0 1484 0 1825 2 1826 36 14162 0 14163 0 "
                + "-22413 0 -22386 10 2602 0 2603 0 2604 0 2605 0 2606 0 2607 0 2608 0 2609 0 2617 0 2618 0 2619 0 "
                + "2620 0 2621 0 2622 0 2623 0 2624 0 2632 0 2633 0 2634 0 2635 0 2636 0 2637 0 2638 0 2639 0 2647 0 "
                + "2648 0 2649 0 2650 0 2651 0 2652 0 2653 0 2654 0 15189 -1 15190 -1 2929 -1 2930 -1 15221 -1 15222 -1 "
                + "2937 -1 2938 -1 15250 -1 15251 -1 2966 -1 2967 -1 7098 202 7099 -15616 7100 195 7101 164 3006 74 "
                + "3007 68 23489 512 3017 0 3033 0 3038 0 3041 -1 3042 -1 3566 5 3567 0 7715 0 3660 3411 3661 2921 ";
        dao.updateHistoryByGrowDay(id, growday, values);
    }

//    @Test
    public void testGetAllHistoryByFlock() throws SQLException {

        Map<Integer, String> map = dao.getAllHistoryByFlock(flockId);
        System.out.println(map);

//        Set<Entry<Integer, String>> entries = map.entrySet();
//        for (Entry entry : entries) {
//            if (!entry.getValue().equals("-1")) {
//                System.out.println("Grow day : " + entry.getKey() + " Vaues : " + entry.getValue());
//            }
//        }
    }

//    @Test
    public void testCopyFlockHistory() throws SQLException {
        long flockIdMySql = 137;
        long flockIdDerby = 1;
        dao = DaoFactory.getDaoFactory(DaoType.MYSQL).getFlockDao();
        Map<Integer, String> historyByFlockMySQL = dao.getAllHistoryByFlock(flockIdMySql);

        dao = DaoFactory.getDaoFactory(DaoType.DERBY).getFlockDao();
        Map<Integer, String> historyByFlockDerby = dao.getAllHistoryByFlock(flockIdDerby);

        Set<Entry<Integer, String>> entries = historyByFlockDerby.entrySet();
        for (Entry<Integer, String> entry : entries) {
            dao.removeHistoryByGrowDay(flockIdDerby, entry.getKey());
        }

        entries = historyByFlockMySQL.entrySet();
        for (Entry<Integer, String> entry : entries) {
            dao.updateHistoryByGrowDay(flockIdDerby, entry.getKey(), entry.getValue());
        }
    }
}
