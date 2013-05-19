package com.agrologic.app.dao.mysql.impl;

import com.agrologic.app.dao.AlarmDao;
import com.agrologic.app.model.Alarm;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import static org.unitils.reflectionassert.ReflectionAssert.assertReflectionEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"/common-dao-context.xml", "/mysql-dao-context.xml"})
@TransactionConfiguration
@Transactional
public class AlarmDaoImplTest {
    @Autowired
    private AlarmDao alarmDao;

    @Test
    public void getCanFindRecordsAfterInsert() throws Exception {
        Alarm expected = new Alarm();
        expected.setId(1L);
        expected.setText("text");
        alarmDao.insert(expected);
        expected.setUnicodeText("text");
        expected.setLangId(1L);

        Alarm actual = alarmDao.getById(1L);
        assertReflectionEquals(expected, actual);
    }
}
