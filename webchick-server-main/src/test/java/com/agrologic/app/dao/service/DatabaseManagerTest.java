/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.agrologic.app.dao.service;


import com.agrologic.app.config.Configuration;
import com.agrologic.app.dao.*;
import com.agrologic.app.dao.service.impl.DatabaseManager;
import com.agrologic.app.model.Cellink;
import com.agrologic.app.model.Controller;
import com.agrologic.app.model.Data;
import com.agrologic.app.model.User;
import com.agrologic.app.util.PropertyFileUtil;
import org.junit.*;

import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertEquals;

@Ignore
public class DatabaseManagerTest {

    long userId = 119;
    long cellinkId = 127;
    long pid = 36;
    long sid = 6;
    long tid = 26;
    long lid = 1;
    DatabaseManager dbManager;
    Configuration config;

    public DatabaseManagerTest() {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
        config = new Configuration();
        dbManager = new DatabaseManager(DaoType.DERBY);
        dbManager.getDatabaseGeneralService().setDatabaseDir(PropertyFileUtil.getProgramPath());
    }

    @After
    public void tearDown() {
    }

    @Test
    @Ignore
    public void doLoadTableDataTest() throws Exception {

        dbManager.doLoadTableData();

        User user = DbImplDecider.use(DaoType.MYSQL).getDao(UserDao.class).getById(userId);
        Cellink cellink = DbImplDecider.use(DaoType.MYSQL).getDao(CellinkDao.class).getById(cellinkId);
        user.addCellink(cellink);

        Collection<Controller> controllers = DbImplDecider.use(DaoType.MYSQL).getDao(ControllerDao.class).getActiveCellinkControllers(cellinkId);
        cellink.setControllers(controllers);

        DataDao ddi = dbManager.getDatabaseGeneralService().getDataDao();
        long pid = 33501;
        long sid = 6;
        long tid = 26;
        long lid = 1;
        List<Data> dl = (List<Data>) ddi.getOnScreenDataList(pid, sid, tid, lid);
        assertEquals(3, dl.size());
        lid = 2;
        dl = (List<Data>) ddi.getOnScreenDataList(pid, sid, tid, lid);
        assertEquals(3, dl.size());
    }

    @Test
    public void testGetSpecial() throws SQLException {
        DataDao dao = dbManager.getDatabaseGeneralService().getDataDao();
        List<Data> dataList = (List<Data>) dao.getSpecialData(pid, lid);
        assertEquals(42, dataList.size());
    }
}
