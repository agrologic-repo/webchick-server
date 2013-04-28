/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.agrologic.app.dao.derby;

import com.agrologic.app.dao.CreatebleDao;
import com.agrologic.app.dao.DaoType;
import com.agrologic.app.dao.DbImplDecider;
import com.agrologic.app.dao.WorkerDao;
import com.agrologic.app.dao.derby.impl.DerbyWorkerDaoImpl;
import com.agrologic.app.model.Worker;
import java.sql.SQLException;
import java.util.List;

import org.junit.*;

import static org.junit.Assert.*;

/**
 *
 * @author Administrator
 */
@Ignore
public class DerbyWorkerDaoTest {

    private Long cellinkId = Long.valueOf(34);
    private Long flockId = Long.valueOf(1);
    private Worker worker;
    private WorkerDao dao;

    public DerbyWorkerDaoTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
        dao = DbImplDecider.getDaoFactory(DaoType.DERBY).getWorkerDao();
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
        ((DerbyWorkerDaoImpl) dao).dropTable();
        assertFalse(((CreatebleDao) dao).tableExist());
    }

    @Test
    public void testInsert() throws SQLException {
        if (!((CreatebleDao) dao).tableExist()) {
            ((CreatebleDao) dao).createTable();
        }

        worker = new Worker();
        worker.setCellinkId(cellinkId);
        worker.setDefine("Developer");
        worker.setHourCost(Float.valueOf("40.0"));
        worker.setId(Long.valueOf("1"));
        worker.setName("Tset Test");
        worker.setPhone("0000000000");
        dao.insert(worker);
    }

    @Test
    public void testRemove() throws SQLException {
        dao.remove(Long.valueOf(3));
        worker = dao.getById(flockId);
        assertNull(worker);
    }
}
