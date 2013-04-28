/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.agrologic.app.dao.derby;

import com.agrologic.app.dao.CreatebleDao;
import com.agrologic.app.dao.DaoType;
import com.agrologic.app.dao.DbImplDecider;
import com.agrologic.app.dao.TransactionDao;
import com.agrologic.app.dao.derby.impl.DerbyTransactionDaoImpl;
import com.agrologic.app.model.Transaction;
import java.sql.SQLException;

import org.junit.*;

import static org.junit.Assert.*;

/**
 *
 * @author Administrator
 */
@Ignore
public class DerbyTransactionDaoTest {

    private Long flockId = Long.valueOf(1);
    private Transaction transaction;
    private TransactionDao dao;

    public DerbyTransactionDaoTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
        dao = DbImplDecider.getDaoFactory(DaoType.DERBY).getTransactionDao();
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
        ((DerbyTransactionDaoImpl) dao).dropTable();
        assertFalse(((CreatebleDao) dao).tableExist());
    }

//    @Test
    public void testInsert() throws SQLException {
        if (!((CreatebleDao) dao).tableExist()) {
            ((CreatebleDao) dao).createTable();
        }

        transaction = new Transaction();
        transaction.setFlockId(flockId);
        transaction.setName("test");
        transaction.setExpenses(Float.valueOf("1000.0"));
        transaction.setRevenues(Float.valueOf("555.56"));
        dao.insert(transaction);
    }

//    @Test
    public void testRemove() throws SQLException {
        dao.remove(Long.valueOf(3));
        transaction = dao.getById(flockId);
        assertNull(transaction);
    }
}
