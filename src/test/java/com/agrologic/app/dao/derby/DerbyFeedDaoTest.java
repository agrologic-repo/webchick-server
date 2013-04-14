/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.agrologic.app.dao.derby;

import com.agrologic.app.dao.CreatebleDao;
import com.agrologic.app.dao.DaoFactory;
import com.agrologic.app.dao.DaoType;
import com.agrologic.app.dao.FeedDao;
import com.agrologic.app.dao.derby.impl.DerbyFeedDaoImpl;
import com.agrologic.app.model.Feed;
import java.sql.SQLException;
import java.util.List;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author Administrator
 */
public class DerbyFeedDaoTest {

    private Long cellinkId = Long.valueOf(34);
    private Long flockId = Long.valueOf(1);
    private Feed feed;
    private FeedDao dao;

    public DerbyFeedDaoTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
        dao = DaoFactory.getDaoFactory(DaoType.DERBY).getFeedDao();
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
        ((DerbyFeedDaoImpl) dao).dropTable();
        assertFalse(((CreatebleDao) dao).tableExist());
    }

    @Test
    public void testInsert() throws SQLException {
        if (!((CreatebleDao) dao).tableExist()) {
            ((CreatebleDao) dao).createTable();
        }

        feed = new Feed();
        feed.setType(Long.valueOf(14));
        feed.setFlockId(flockId);
        feed.setDate("3/03/13");
        feed.setAmount(1000);
        feed.setNumberAccount(1);
        feed.setTotal(Float.valueOf("25656.0"));
        dao.insert(feed);
    }

//    @Test
    public void testRemove() throws SQLException {
        dao.remove(feed.getId());
        feed = dao.getById(flockId);
        assertNull(feed);
    }

//    @Test
    public void testRemoveAll() throws SQLException {
//        List<Feed> feedList = dao.getAllByCellinkId(flockId);
//        for (Feed g : feedList) {
//            dao.remove(g.getId());
//        }
//        feedList = dao.getAllByCellinkId(flockId);
//        assertEquals(0, feedList.size());
    }
}
