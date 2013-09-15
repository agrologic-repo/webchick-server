package com.agrologic.app.dao.derby.impl;

import com.agrologic.app.dao.CreatebleDao;
import com.agrologic.app.model.Alarm;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;

import static org.junit.Assert.assertEquals;
import static org.unitils.reflectionassert.ReflectionAssert.assertReflectionEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"/common-dao-context.xml", "/derby-dao-context.xml"})
@TransactionConfiguration
@Transactional
public class DerbyAlarmDaoImplTest extends AbstractDaoTest {

    @Override
    public void setUp() throws SQLException {
//        alarmDao.remove(alarm());
    }

    @Override
    public void tearDown() throws SQLException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Test
    public void getCanFindAfterInsert() throws Exception {
        Alarm expected = alarm();
        alarmDao.insert(expected);
        Alarm actual = alarmDao.getById(expected.getId());
        assertReflectionEquals(expected, actual);
    }

    @Test
    public void createTableImplementsWithAOP() throws SQLException {
        try {
            ((CreatebleDao) alarmDao).createTable();
            boolean result = ((CreatebleDao) alarmDao).tableExist();
            assertEquals(true, result);
        } catch (SQLException e) {

        }
    }
}
