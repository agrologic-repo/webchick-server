package com.agrologic.app.dao.derby.impl;

import com.agrologic.app.dao.CreatebleDao;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;

import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"/common-dao-context.xml", "/derby-dao-context.xml"})
@TransactionConfiguration
@Transactional
@Ignore
public class DerbyTransactionDaoImplTest extends AbstractDaoTest {

    @Override
    public void setUp() throws SQLException {
        //alarmDao.remove(alarm());
    }

    @Override
    public void tearDown() throws SQLException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

//    @Test
//    public void getCanFindAfterInsert() throws Exception {
//        Transaction expected = transaction();
//        transactionDao.insert(expected);
//        Alarm actual = alarmDao.getById(expected.getId());
//        assertReflectionEquals(expected, actual);
//    }

    @Test
    public void createTableImplementsWithAOP() throws SQLException {
        try {
            System.out.println("start creating table");

            if (!((CreatebleDao) transactionDao).tableExist()) {
                ((CreatebleDao) transactionDao).createTable();
                System.out.println("table created");
            }

            boolean result = ((CreatebleDao) transactionDao).tableExist();
            assertEquals(true, result);
        } catch (SQLException e) {

        }
    }
}
