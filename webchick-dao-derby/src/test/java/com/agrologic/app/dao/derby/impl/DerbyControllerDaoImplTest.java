package com.agrologic.app.dao.derby.impl;

import com.agrologic.app.model.Cellink;
import com.agrologic.app.model.Controller;
import com.agrologic.app.model.User;
import org.junit.*;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.sql.Timestamp;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static org.unitils.reflectionassert.ReflectionAssert.assertReflectionEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"/common-dao-context.xml", "/derby-dao-context.xml"})
@TransactionConfiguration
@Transactional
public class DerbyControllerDaoImplTest extends AbstractDaoTest {

    @Before
    public void setUp() throws SQLException {
        User user = user();
        userDao.insert(user);

        Cellink cellink = cellink();
        cellinkDao.insert(cellink);
    }

    @After
    public void tearDown() throws SQLException {
        controllerDao.remove(1L);
        cellinkDao.remove(1L);
        userDao.remove(1L);
    }

    @Test
    public void getCanFindAfterInsert() throws SQLException {
        Controller expected = controller();
        controllerDao.insert(expected);
        Controller actual = controllerDao.getById(expected.getId());
        assertReflectionEquals(expected, actual);
    }

    @Test
    public void getUpdatedGraphTimeNotNull() throws Exception {
        Timestamp expected = new Timestamp(System.currentTimeMillis());
        String values = "0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 ";
        controllerDao.insert(controller());
        controllerDao.updateControllerGraph(1L, values, expected);
        String graphs = controllerDao.getControllerGraph(1L);
        assertNotNull(graphs);
        Timestamp actual = controllerDao.getUpdatedGraphTime(1L);
        assertEquals(expected, actual);
    }

    @Test
    @Ignore
    public void getUpdatedGraphTimeShouldHaveNull() throws Exception {
        Timestamp time = controllerDao.getUpdatedGraphTime(1L);
        Assert.assertNull(time);
    }
}
