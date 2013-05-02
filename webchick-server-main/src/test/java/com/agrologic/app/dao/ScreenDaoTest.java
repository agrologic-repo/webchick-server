/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.agrologic.app.dao;

import com.agrologic.app.model.Screen;
import java.sql.SQLException;
import java.util.Collection;
import org.junit.*;

/**
 *
 * @author Administrator
 */
@Ignore
public class ScreenDaoTest {
    long programId  = 83901;
    long screenId   = 1;
    long langId     = 1;
    ScreenDao dao;

    public ScreenDaoTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
        dao = DbImplDecider.use(DaoType.MYSQL).getDao(ScreenDao.class);
    }

    @After
    public void tearDown() {

    }

    @Test
    public void testGetAllByProgram() throws SQLException {
        Collection<Screen> screens = dao.getAllProgramScreens(programId);
        screens.size();
    }
}
