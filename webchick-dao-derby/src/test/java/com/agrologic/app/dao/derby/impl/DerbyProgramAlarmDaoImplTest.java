package com.agrologic.app.dao.derby.impl;

import com.agrologic.app.model.ProgramAlarm;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.util.Collection;

import static junit.framework.Assert.assertEquals;
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"/common-dao-context.xml", "/derby-dao-context.xml"})
@TransactionConfiguration
@Transactional
public class DerbyProgramAlarmDaoImplTest extends AbstractDaoTest {

    @Override
    public void setUp() throws SQLException {
        //programAlarmDao.removeAllProgramAlarms(1L);
    }

    @Override
    public void tearDown() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Test
    public void getAllReturnAll() throws Exception {
        Collection<ProgramAlarm> programAlarms = programAlarmDao.getAllProgramAlarms(1L);
        assertEquals(0, programAlarms.size());
    }

    @Test
    public void getAllUsingLangugeReturnAll() throws Exception {
        Collection<ProgramAlarm> programAlarms = programAlarmDao.getAllProgramAlarms(1L, 1L);
        for (ProgramAlarm p : programAlarms) {
            System.out.println(String.format("Alarm name %s , Program id %d ", p.getText(), p.getProgramId()));
        }
        assertEquals(0, programAlarms.size());
    }
}

