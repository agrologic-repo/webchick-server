package com.agrologic.app.dao.mysql.impl;

import com.agrologic.app.dao.DataDao;
import com.agrologic.app.model.Data;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

import static org.unitils.reflectionassert.ReflectionAssert.assertReflectionEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"/common-dao-context.xml", "/mysql-dao-context.xml"})
@TransactionConfiguration
@Transactional

public class DataDaoImplTest {

    @Autowired
    private DataDao dataDao;

    @Test
    @Ignore
    public void getOnlineTableDataList() throws Exception {
        Long controllerId = 57L;
        Long programId = 96701L;
        Long screenId = 1L;
        Long tableId = 1L;
        Long langId = 1L;
        Collection<Data> dataList = dataDao.getOnlineTableDataList(controllerId, programId, screenId, tableId, langId);
        assertReflectionEquals(17, dataList.size());
    }


}

