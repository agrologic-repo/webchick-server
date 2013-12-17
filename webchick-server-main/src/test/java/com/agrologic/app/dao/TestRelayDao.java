/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.agrologic.app.dao;

import com.agrologic.app.model.Relay;
import org.junit.*;

import java.sql.SQLException;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * @author Administrator
 */
@Ignore
public class TestRelayDao {

    RelayDao dao;

    public TestRelayDao() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
        dao = DbImplDecider.use(DaoType.MYSQL).getDao(RelayDao.class);
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testGetAll() throws SQLException {
        List<Relay> relays = (List<Relay>) dao.getAll();
        assertEquals(191, relays.size());

        relays = (List<Relay>) dao.getAllWithTranslation();
        assertEquals(597, relays);
    }
}
