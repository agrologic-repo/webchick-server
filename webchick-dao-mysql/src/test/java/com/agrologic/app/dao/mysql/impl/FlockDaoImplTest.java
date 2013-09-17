package com.agrologic.app.dao.mysql.impl;


import com.agrologic.app.model.Cellink;
import com.agrologic.app.model.Controller;
import com.agrologic.app.model.Flock;
import com.agrologic.app.model.User;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.SQLException;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

public class FlockDaoImplTest extends AbstractDaoTest {

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
    public void getCanFindRecordsAfterInsert() throws SQLException {
        Flock flock = flock();
        flockDao.insert(flock);

        Flock actual = flockDao.getById(1L);
        assertNotNull(actual);
    }

    @Test
    public void getUpdatedHistoryNotEmptyAfterUpdating() throws SQLException {
        String values = "1301 0 1302 0 21783 0 21784 0 21785 -1 21786 -1 21787 -1 21788 -1 " +
                "1328 -1 1329 -1 1358 -1 -31382 -1 -31381 -1 1388 -1 1389 -1 1390 -1 1391 -1 " +
                "1392 -1 1393 -1 1394 -1 1395 -1 1396 -1 1397 -1 1398 -1 2000 -1 2001 -1 " +
                "2002 -1 2003 -1 2004 -1 2005 -1 2006 -1 2007 -1 2008 -1 2009 -1 2010 -1 " +
                "2011 -1 2012 -1 2013 -1 2014 -1 2015 -1 2016 -1 2017 -1 2018 -1 2019 -1 " +
                "2020 -1 2021 -1 2022 -1 2023 -1 2024 -1 2025 -1 2026 -1 2027 -1 2028 -1 " +
                "2029 -1 2030 -1 2031 -1 2144 -1 2145 -1 -22413 -1 -22412 -1 2171 -1 2172 -1 " +
                "2173 -1 2174 -1 2175 -1 -22386 -1 2191 -1 22674 -1 22675 -1 22676 -1 22677 -1 " +
                "22678 -1 22679 -1 2929 0 2930 0 2931 0 2932 0 15221 0 15222 0 15223 0 15224 0 " +
                "2937 0 2938 0 2939 0 2940 0 15250 0 15251 0 15252 0 15253 0 2966 0 2967 0 " +
                "2968 0 2969 0 7098 100 7099 400 7100 100 7101 400 3006 0 3007 0 3008 0 " +
                "23489 0 3015 0 3017 0 3029 0 3030 0 3031 0 3032 0 3033 0 3034 0 3035 0 " +
                "3036 0 3037 0 3038 0 3041 0 3042 0 3043 0 3044 0 -21013 -1 3566 0 3567 0 " +
                "7715 -1 7716 -1 ";
        flockDao.updateHistoryByGrowDay(1L, 1, values);
        int growDay = flockDao.getUpdatedGrowDayHistory(1L);
        assertEquals(1, growDay);
    }

    @Test
    public void getUpdatedHistory24NotEmptyAfterUpdating() throws SQLException {
        String values = "0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 ";
        flockDao.updateHistory24ByGrowDay(1L, 1, "D21", values);
        int growDay = flockDao.getUpdatedGrowDayHistory24(1L);
        assertEquals(1, growDay);
    }
}
