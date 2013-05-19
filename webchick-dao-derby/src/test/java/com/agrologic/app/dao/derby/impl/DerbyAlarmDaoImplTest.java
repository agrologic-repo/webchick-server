package com.agrologic.app.dao.derby.impl;

import com.agrologic.app.dao.AlarmDao;
import com.agrologic.app.model.Alarm;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"/common-dao-context.xml", "/derby-dao-context.xml"})
@TransactionConfiguration
@Transactional
public class DerbyAlarmDaoImplTest {
    @Autowired
    private AlarmDao alarmDao;

    @Test
    @Ignore
    public void testName() throws Exception {
        Alarm alarm = new Alarm();
        alarm.setId(1L);
        alarm.setText("text");
        alarmDao.insert(alarm);
    }
}
