package com.agrologic.app.dao.derby.impl;

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
@ContextConfiguration({"/common-dao-context.xml", "/derby-dao-context.xml"})
@TransactionConfiguration
@Transactional
public class DerbyProgramAlarmDaoImplTest {
    @Autowired
    private ProgramAlarmDao programAlarmDao;

    @Test
    @Ignore
    public void getAllReturnAll() throws Exception {
        Collection<ProgramAlarm> programAlarms = programAlarmDao.getAllProgramAlarms(83103L);
        assertEquals(16, programAlarms.size());
    }

    @Test
    @Ignore
    public void getAllUsingLangugeReturnAll() throws Exception {
        Collection<ProgramAlarm> programAlarms = programAlarmDao.getAllProgramAlarms(83103L, 2L);
        for (ProgramAlarm p : programAlarms) {
            System.out.println(String.format("Alarm name %s , Program id %d ", p.getText(), p.getProgramId()));
        }
        assertEquals(16, programAlarms.size());
    }
}

