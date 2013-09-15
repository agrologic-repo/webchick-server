package com.agrologic.app.dao.mysql.impl;

import com.agrologic.app.model.Alarm;
import com.agrologic.app.model.Language;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

import static org.unitils.reflectionassert.ReflectionAssert.assertReflectionEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"/common-dao-context.xml", "/mysql-dao-context.xml"})
@TransactionConfiguration
@Transactional
public class AlarmDaoImplTest extends AbstractDaoTest {

    @Override
    public void setUp() throws SQLException {

    }

    @Override
    public void tearDown() throws SQLException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Test
    public void getCanFindRecordsAfterInsert() throws Exception {
        Alarm expected = alarm();
        alarmDao.insert(expected);
        Alarm actual = alarmDao.getById(expected.getId());
        assertReflectionEquals(expected, actual);
    }

    @Test
    public void getCanFindRecordsAfterInsertCollection() throws Exception {
        Collection<Alarm> expectedAlarms = createAlarmCollection();
        alarmDao.insert(expectedAlarms);
        Collection<Alarm> actualAlarms = alarmDao.getAll();
        //assertReflectionEquals(expectedAlarms, actualAlarms);
    }

    @Test
    public void getAlarmWithTranslationAfterInsert() throws SQLException {
        Language lang = language();
        languageDao.insert(lang);

        Alarm alarm = alarm();
        alarmDao.insert(alarm());
        alarmDao.insertTranslation(alarm.getId(), alarm.getLangId(), alarm.getText());
        Collection<Alarm> alarms = alarmDao.getAllWithTranslation();
        Assert.notEmpty(alarms);
    }


    public Collection<Alarm> createAlarmCollection() {
        Collection<Alarm> alarms = new ArrayList<Alarm>();
        Alarm alarm = alarm();
        alarms.add(alarm);
        return alarms;
    }
}
