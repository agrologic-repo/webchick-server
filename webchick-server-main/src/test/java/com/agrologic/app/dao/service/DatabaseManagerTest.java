/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.agrologic.app.dao.service;

import com.agrologic.app.config.Configuration;
import com.agrologic.app.dao.DaoFactory;
import com.agrologic.app.dao.DaoType;
import com.agrologic.app.dao.DataDao;

import com.agrologic.app.dao.DbImplDecider;
import com.agrologic.app.dao.service.impl.DatabaseManager;
import com.agrologic.app.except.ObjectDoesNotExist;
import com.agrologic.app.model.Cellink;
//import com.agrologic.app.model.Configuration;
import com.agrologic.app.model.Controller;
import com.agrologic.app.model.Data;
import com.agrologic.app.model.User;
import com.agrologic.app.util.PropertyFileUtil;
import java.sql.SQLException;
import java.util.List;
import static org.junit.Assert.assertEquals;
import org.junit.*;

/**
 *
 * @author Administrator
 */
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

    @BeforeClass
    public static void setUpClass() throws Exception {
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

    //@Test
    public void doLoadTableDataTest() throws SQLException, ObjectDoesNotExist {
        dbManager.doLoadTableData(config.getUserId(), config.getCellinkId());

        User user = DbImplDecider.getDaoFactory(DaoType.DERBY).getUserDao().getById(userId);
        Cellink cellink = DbImplDecider.getDaoFactory(DaoType.DERBY).getCellinkDao().getById(cellinkId);
        user.addCellink(cellink);

        List<Controller> controllers = (List<Controller>) DbImplDecider.getDaoFactory(DaoType.DERBY).getControllerDao().getActiveCellinkControllers(cellinkId);
        cellink.setControllers(controllers);

        assertEquals("getUser()", user, dbManager.getDatabaseLoader().getUser());
        assertEquals("getCellink()", cellink, dbManager.getDatabaseLoader().getUser().getCellinkById(cellinkId));

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
