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

import java.util.ArrayList;
import java.util.Collection;

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
        expected.setId(100L);
        expected.setText("text");
        expected.setUnicodeText("text");
        expected.setLangId(1L);
        alarmDao.insert(expected);

        Alarm actual = alarmDao.getById(100L);
        assertReflectionEquals(expected, actual);
    }

    @Test
    public void getCanFindRecordsAfterInsertCollection() throws Exception {
        Collection<Alarm> expectedAlarms = new ArrayList<Alarm>();
        Alarm alarm = new Alarm();
        alarm.setId(100L);
        alarm.setText("text1");
        alarm.setUnicodeText("text1");
        alarm.setLangId(1L);
        expectedAlarms.add(alarm);
        alarmDao.insert(expectedAlarms);

        Collection<Alarm> actualAlarms = alarmDao.getAll();
        assertReflectionEquals(expectedAlarms, actualAlarms);
    }
}
