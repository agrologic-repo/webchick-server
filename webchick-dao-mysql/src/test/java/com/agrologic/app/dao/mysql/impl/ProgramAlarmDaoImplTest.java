package com.agrologic.app.dao.mysql.impl;

import com.agrologic.app.dao.ProgramAlarmDao;
import com.agrologic.app.model.ProgramAlarm;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"/common-dao-context.xml", "/mysql-dao-context.xml"})
@TransactionConfiguration
@Transactional
public class ProgramAlarmDaoImplTest {
    @Autowired
    private ProgramAlarmDao programAlarmDao;

    @Test
    @Ignore
    public void getAllUsingLangugeReturnAll() throws Exception {
        Collection<ProgramAlarm> programAlarms = programAlarmDao.getAllProgramAlarms(93801L, 1L);
        assertEquals(0, programAlarms.size());
    }
}
