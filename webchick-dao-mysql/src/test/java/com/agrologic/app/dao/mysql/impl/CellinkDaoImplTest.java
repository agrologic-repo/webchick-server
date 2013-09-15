package com.agrologic.app.dao.mysql.impl;

import com.agrologic.app.model.Cellink;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.sql.SQLException;
import java.util.Collection;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"/common-dao-context.xml", "/mysql-dao-context.xml"})
@TransactionConfiguration
@Transactional
public class CellinkDaoImplTest extends AbstractDaoTest {

    @Test
    public void getByIdReturnObject() throws SQLException {
        Collection<Cellink> cellinks = cellinkDao.getAll();
        System.out.println(" Cellinks : " + cellinks.size());
    }

    @Test
    @Ignore
    public void getCellinkWithCriteria() throws Exception {
        Collection<Cellink> cellinks = cellinkDao.getAllUserCellinks(1L);
        Assert.notEmpty(cellinks);
    }

    @Override
    public void setUp() throws SQLException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void tearDown() throws SQLException {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
