/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.agrologic.app.dao.service;

import com.agrologic.app.dao.DaoFactory;
import com.agrologic.app.dao.DaoType;
import com.agrologic.app.dao.UserDao;
import com.agrologic.app.dao.service.impl.DatabaseGeneralService;
import static org.junit.Assert.*;
import org.junit.*;

/**
 *
 * @author Administrator
 */
public class DatabaseGeneralServiceTest {

    DatabaseGeneralService dgs;

    public DatabaseGeneralServiceTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
        dgs = new DatabaseGeneralService(DaoType.MYSQL);
    }

    @After
    public void tearDown() {
    }

    @Test
    public void initDaoByTypeTest() {
        UserDao userDao = DaoFactory.getDaoFactory(DaoType.MYSQL).getUserDao();
        assertNotNull(userDao);
        assertNotNull("getUserDao() should return not null", dgs.getUserDao());
        assertEquals("userDao.getClass() should be the same", userDao.getClass(), dgs.getUserDao().getClass());
    }
}
