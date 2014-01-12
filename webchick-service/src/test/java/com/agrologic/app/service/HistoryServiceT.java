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
        int expected = 567;
        int actual = historyService.getHistoryData().size();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testPerDayHistoryData() throws Exception {
        int expected = 532;
        int actual = historyService.getPerDayHistoryData(1L).size();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testPerHourHistoryData() throws Exception {
        int expected = 57;
        int actual = historyService.getPerHourHistoryData(1L).size();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testDayAndHourHistoryData() throws Exception {
        int expected = 22;
        int actual = historyService.getPerDayAndHourHistoryData(1L).size();
        Assert.assertEquals(expected, actual);
    }

}
