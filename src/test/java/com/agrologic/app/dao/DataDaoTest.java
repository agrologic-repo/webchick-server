
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.agrologic.app.dao;

//~--- non-JDK imports --------------------------------------------------------
import com.agrologic.app.model.Data;
import java.sql.SQLException;
import java.util.Collection;
import static org.junit.Assert.*;
import org.junit.*;
/**
 *
 * @author Administrator
 */
public class DataDaoTest {

    static DataDao dao;

    public DataDaoTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {

    }

    @AfterClass
    public static void tearDownClass() throws Exception {

    }

    @Before
    public void setUp() {
        dao = DaoFactory.getDaoFactory(DaoType.DERBY).getDataDao();
    }

    @After
    public void tearDown() {
    }

    @Test
    public void getAll() throws SQLException {
        Collection<Data> list = dao.getAll();
        assertEquals(4662, list.size());
    }

    @Test
    public void getAllWithTranslation() throws SQLException {
        Collection<Data> list = dao.getAllWithTranslation();
        assertEquals(10844, list.size());
    }

    @Test
    public void getById() throws SQLException {
        Data data = dao.getById(Long.valueOf(0));
        Data other = new Data();
        other.setId(Long.valueOf(0));
        assertNotNull(data);
        assertEquals(other, data);
    }

    @Test
    public void getSetclock() throws SQLException {
        long cid = 392;
        Data setClock = dao.getSetClockByController(cid);
        System.out.println(setClock);
    }
}


//~ Formatted by Jindent --- http://www.jindent.com
