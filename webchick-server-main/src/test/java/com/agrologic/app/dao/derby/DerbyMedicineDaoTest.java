/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.agrologic.app.dao.derby;

import com.agrologic.app.dao.CreatebleDao;
import com.agrologic.app.dao.DaoType;
import com.agrologic.app.dao.DbImplDecider;
import com.agrologic.app.dao.MedicineDao;
import com.agrologic.app.dao.derby.impl.DerbyMedicineDaoImpl;
import com.agrologic.app.model.Medicine;
import java.sql.SQLException;

import org.junit.*;

import static org.junit.Assert.*;

/**
 *
 * @author Administrator
 */
@Ignore
public class DerbyMedicineDaoTest {

    private Long cellinkId = Long.valueOf(34);
    private Long flockId = Long.valueOf(1);
    private Medicine medicine;
    private MedicineDao dao;

    public DerbyMedicineDaoTest() {

    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
        dao = DbImplDecider.use(DaoType.MYSQL).getDao(MedicineDao.class);
    }

    @Test
    public void testCreateTable() throws SQLException {
        if (!((CreatebleDao) dao).tableExist()) {
            ((CreatebleDao) dao).createTable();
        }
        assertTrue(((CreatebleDao) dao).tableExist());
    }

//    @Test
    public void testDropTable() throws SQLException {
        ((DerbyMedicineDaoImpl) dao).dropTable();
        assertFalse(((CreatebleDao) dao).tableExist());
    }

//    @Test
    public void testInsert() throws SQLException {
        if (!((CreatebleDao) dao).tableExist()) {
            ((CreatebleDao) dao).createTable();
        }

        medicine = new Medicine();
        medicine.setFlockId(flockId);
//        medicine.setDate("3/03/13");
        dao.insert(medicine);
    }

//    @Test
    public void testRemove() throws SQLException {
        dao.remove(medicine.getId());
        medicine = dao.getById(flockId);
        assertNull(medicine);
    }
}
