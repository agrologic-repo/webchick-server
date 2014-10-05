package com.agrologic.app.web.controller;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class OverviewControllerTest {

    @Test
    public void getRecordsTo() {
        assertEquals(25, OverviewController.getRecordsTo(0, 100));
        assertEquals(50, OverviewController.getRecordsTo(25, 100));
        assertEquals(27, OverviewController.getRecordsTo(25, 27));
    }


    @Test
    public void getRecordsFrom() {
        assertEquals(1, OverviewController.getRecordsFrom(0));
        assertEquals(26, OverviewController.getRecordsFrom(25));
    }

}