/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.agrologic.app.dao;

import com.agrologic.app.model.Alarm;
import com.agrologic.app.model.ProgramAlarm;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import org.junit.*;
import static org.junit.Assert.assertEquals;

/**
 *
 * @author Administrator
 */
@Ignore
public class TestAlarmDao {
    long programId = 83901;
    long langId = 2;
    AlarmDao dao;

    public TestAlarmDao() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
        dao = DbImplDecider.use(DaoType.MYSQL).getDao(AlarmDao.class);
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testGetAll() throws SQLException {

        Collection<Alarm> translatedAlarms = dao.getAllWithTranslation();
        assertEquals(209,translatedAlarms.size());

        List<ProgramAlarm> alarms = (List<ProgramAlarm>) dao.getSelectedProgramAlarms(programId, langId);
        assertEquals(14, alarms.size());
    }
}
