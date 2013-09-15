package com.agrologic.app.dao.mysql.impl;

import com.agrologic.app.model.Cellink;
import com.agrologic.app.model.Controller;
import com.agrologic.app.model.User;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.sql.Timestamp;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"/common-dao-context.xml", "/mysql-dao-context.xml"})
@TransactionConfiguration
@Transactional
public class ControllerDaoImplTest extends AbstractDaoTest {

    @Before
    public void setUp() throws SQLException {
        User expected = user();
        userDao.insert(expected);

        Cellink cellink = cellink();
        cellinkDao.insert(cellink);

        Controller controller = controller();
        controllerDao.insert(controller);
    }

    @After
    public void tearDown() throws SQLException {
        controllerDao.remove(1L);
        cellinkDao.remove(1L);
        userDao.remove(1L);
    }

    @Test
    public void getUpdatedGraphTimeNotNullAfterInsert() throws Exception {
        controllerDao.updateControllerGraph(1L, "0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 ",
                new Timestamp(System.currentTimeMillis()));
        Timestamp time = controllerDao.getUpdatedGraphTime(1L);
        System.out.println("Time : " + time);
        Assert.assertNotNull(time);
    }
}
