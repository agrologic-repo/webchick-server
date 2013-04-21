/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.agrologic.app.dao.derby;

import com.agrologic.app.dao.CreatebleDao;
import com.agrologic.app.dao.DaoFactory;
import com.agrologic.app.dao.DaoType;
import com.agrologic.app.dao.ProgramDao;
import com.agrologic.app.dao.derby.impl.DerbyProgramDaoImpl;
import org.junit.*;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 *
 * @author Administrator
 */
@Ignore
public class DerbyProgramDaoTest {
    private ProgramDao dao;

    public DerbyProgramDaoTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
        dao = DaoFactory.getDaoFactory(DaoType.DERBY).getProgramDao();
    }

    @Test
    public void testDropTable() throws Exception {
        assertTrue(((CreatebleDao) dao).tableExist());
        ((DerbyProgramDaoImpl) dao).dropTable();
        assertFalse(((CreatebleDao) dao).tableExist());
    }
}
