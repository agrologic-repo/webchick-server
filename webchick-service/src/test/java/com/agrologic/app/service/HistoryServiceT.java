package com.agrologic.app.service;

import com.agrologic.app.service.history.HistoryService;
import com.agrologic.app.service.history.transaction.HistoryServiceImpl;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;

public class HistoryServiceT {
    HistoryService historyService;

    @Before
    public void setUp() {
        historyService = new HistoryServiceImpl();
    }

    @Test
    public void testHistoryData() throws Exception {
        int expected = 565;
        int actual = historyService.getHistoryData().size();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testPerDayHistoryData() throws Exception {
        int expected = 509;
        int actual = historyService.getPerDayHistoryData(1L).size();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testPerHourHistoryData() throws Exception {
        int expected = 68;
        int actual = historyService.getPerHourHistoryData(1L).size();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testDayAndHourHistoryData() throws Exception {
        int expected = 24;
        int actual = historyService.getPerDayAndHourHistoryData(1L).size();
        Assert.assertEquals(expected, actual);
    }

}
