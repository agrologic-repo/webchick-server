/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.agrologic.app.dao.derby;

import com.agrologic.app.dao.CreatebleDao;
import com.agrologic.app.dao.DaoFactory;
import com.agrologic.app.dao.DaoType;
import com.agrologic.app.dao.FeedTypeDao;
import com.agrologic.app.dao.derby.impl.DerbyFeedTypeDaoImpl;
import com.agrologic.app.model.FeedType;
import java.sql.SQLException;
import java.util.List;

import org.junit.*;

import static org.junit.Assert.*;

/**
 *
 * @author Administrator
 */
@Ignore
public class DerbyFeedTypeDaoTest {
    private Long cellinkId  = Long.valueOf(34);
    private Long flockId = Long.valueOf(1);
    private FeedType feedType;
    private FeedTypeDao dao;

    public DerbyFeedTypeDaoTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
        dao = DaoFactory.getDaoFactory(DaoType.DERBY).getFeedTypeDao();
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
        ((DerbyFeedTypeDaoImpl) dao).dropTable();
        assertFalse(((CreatebleDao) dao).tableExist());
    }

//    @Test
    public void testInsert() throws SQLException {
        if (!((CreatebleDao) dao).tableExist()) {
            ((CreatebleDao) dao).createTable();
        }

        feedType = new FeedType();
        feedType.setCellinkId(cellinkId);
        feedType.setFeedType("A");
        feedType.setPrice(Float.parseFloat("1.55"));
        dao.insert(feedType);
    }

//    @Test
    public void testRemove() throws SQLException {
        dao.remove(feedType.getId());
        feedType = dao.getById(flockId);
        assertNull(feedType);
    }

//    @Test
    public void testRemoveAll() throws SQLException {
        List<FeedType> feedTypeList = dao.getAllByCellinkId(flockId);
        for(FeedType g:feedTypeList) {
            dao.remove(g.getId());
        }
        feedTypeList = dao.getAllByCellinkId(flockId);
        assertEquals(0,feedTypeList.size());
    }
}
