package com.agrologic.app.dao.derby.impl;

import com.agrologic.app.model.Cellink;
import com.agrologic.app.model.Controller;
import com.agrologic.app.model.Flock;
import com.agrologic.app.model.User;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;

import static junit.framework.Assert.assertNotNull;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"/common-dao-context.xml", "/derby-dao-context.xml"})
@TransactionConfiguration
@Transactional
public class DerbyFlockDaoImplTest extends AbstractDaoTest {

    @Before
    public void setUp() throws SQLException {
        User user = user();
        userDao.insert(user);

        Cellink cellink = cellink();
        cellinkDao.insert(cellink);

        Controller controller = controller();
        controllerDao.insert(controller);
    }

    @After
    public void tearDown() throws SQLException {
        flockDao.remove(1L);
        controllerDao.remove(1L);
        cellinkDao.remove(1L);
        userDao.remove(1L);
    }

    @Test
    public void getCanFindAfterInsert() throws Exception {
        Flock flock = flock();
        flockDao.insert(flock);

        Flock actual = flockDao.getById(1L);
        assertNotNull(actual);
    }
}
