/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.agrologic.app.dao.service;

import com.agrologic.app.dao.DaoType;
import com.agrologic.app.dao.DbImplDecider;
import com.agrologic.app.dao.UserDao;
import org.junit.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * @author Administrator
 */
@Ignore
public class DatabaseGeneralServiceTest {

    UserDao userDao;

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
        userDao = DbImplDecider.use(DaoType.MYSQL).getDao(UserDao.class);
    }

    @After
    public void tearDown() {
    }

    @Test
    public void initDaoByTypeTest() {
        assertNotNull(userDao);
        assertNotNull("getUserDao() should return not null", userDao);
        assertEquals("userDao.getClass() should be the same", userDao.getClass(),
                DbImplDecider.use(DaoType.MYSQL).getDao(UserDao.class).getClass());
    }
}
