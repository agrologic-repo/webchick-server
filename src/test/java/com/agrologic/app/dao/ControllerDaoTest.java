/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.agrologic.app.dao;

import java.sql.SQLException;
import org.junit.*;
import static org.junit.Assert.*;

/**
 *
 * @author Administrator
 */
public class ControllerDaoTest {

    ControllerDao dao;

    public ControllerDaoTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
        dao = DaoFactory.getDaoFactory(DaoType.DERBY).getControllerDao();
    }

    @After
    public void tearDown() {
    }
    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //

    @Test
    public void getControllerDao() throws SQLException {
        long cid = 393;
        String values = dao.getControllerGraph(cid);
        System.out.println(values);
    }
}
