/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.agrologic.app.messaging;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author Administrator
 */
public class ResponseMessageTest {
    private static final String SINGLE_DATA_RESPONSE = "\u00181\u00164096 257 177\u0004";
    private static final String GRAPH_DATA_RESPONSE = "\u00182\u0016229 253 256 257 245 255 258 250 261 267 266 266 267 263 261 261 260 256 253 250 " +
            "247 246 253 256 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 " +
            "0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 " +
            "0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 " +
            "0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 " +
            "0 0 0 0 0 0 0 0 0 0 0 0 21\u0004";
    private static final String MULTI_DATA_RESPONSE = "\u00183\u001621789 5666 21810 5666 1360 -16418 15233 415 7197 261 7218 261 7268 261 " +
            "1014 0 1015 0 1016 0 1017 0 1018 70 1019 1 1022 1 1069 15 5166 0 1131 0 1132 0 21618 0 1140 0 1141 0 21622" +
            " 0 21623 0 1147 0 5294 30840 1200 1 1301 0 1302 0 21783 5656 21789 5666 29982 -1 1311 -1 1312 7889 -31450 " +
            "5 -31449 9176 -31448 2 -31447 -28180 1328 0 21810 5666 -31435 34 -31434 29699 -31433 0 -31432 0 -31431 0 -" +
            "31430 0 -31429 10 -31428 2560 -31427 0 -31426 0 -31425 0 -31424 0 -31423 0 -31422 0 -31421 0 -31420 0 -314" +
            "19 0 -31418 2560 -31417 0 -31416 1 -31412 2 -31411 -28180 1358 0 1360 -16418 1361 0 1362 0 1363 0 1364 0 1" +
            "365 0 1366 0 21847 5440 21848 5632 21849 5664 21850 5696 1379 28 1399 0 1533 512 1534 0 1539 0 -23027 3 15" +
            "56 10 1557 0 1600 -1 1601 -1 1602 -1 1603 -1 1604 -1 1605 -1 1606 -1 1607 -1 1608 -1 22089 -1 22090 -1 220" +
            "91 -1 22092 -1 22093 -1 22094 -1 22095 -1 22096 -1 22097 -1 22098 -1 100 7 -1 0 186\u0004";

    private ResponseMessage responseMessage;

    @Before
    public void setUP() {
    }

    @Test
    public void testConstructorWithSingleDataResponse() {
        responseMessage = new ResponseMessage(SINGLE_DATA_RESPONSE.getBytes());
        String expectedString = "4096 257 ";
        assertEquals(expectedString, responseMessage.toString());
    }

    @Test
    public void testConstructorWithGraphDataResponse() {
        responseMessage = new ResponseMessage(GRAPH_DATA_RESPONSE.getBytes());
        String expectedString = GRAPH_DATA_RESPONSE.substring(3, GRAPH_DATA_RESPONSE.length() - 3);
        assertEquals(expectedString, responseMessage.toString());
    }

    @Test
    public void testConstructorWithMultiDataResponse() {
        responseMessage = new ResponseMessage(MULTI_DATA_RESPONSE.getBytes());
        String expectedString = MULTI_DATA_RESPONSE.substring(3, MULTI_DATA_RESPONSE.length() - 4);
        assertEquals(expectedString, responseMessage.toString());
    }

    @Test
    public void calcCheckSumShortString() {
        int checksum = ResponseMessage.calcChecksum(SINGLE_DATA_RESPONSE.getBytes(), 3, SINGLE_DATA_RESPONSE.length() - 5);
        assertEquals(177, checksum);
    }

    @Test
    public void calcCheckSumGraphString() {
        int checksum = ResponseMessage.calcChecksum(GRAPH_DATA_RESPONSE.getBytes(), 3, GRAPH_DATA_RESPONSE.length() - 4);
        assertEquals(21, checksum);
    }

    @Test
    public void calcCheckSumLongString() {
        int calcCheckSum = ResponseMessage.calcChecksum(MULTI_DATA_RESPONSE.getBytes(), 3,
                MULTI_DATA_RESPONSE.length() - 5);
        assertEquals(186, calcCheckSum);
    }

    public void calcCheckSumLongStringError() {
        int calcCheckSum = ResponseMessage.calcChecksum(MULTI_DATA_RESPONSE.getBytes(), 3,
                MULTI_DATA_RESPONSE.length() - 5);
        int calcCheckSum2 = ResponseMessage.overFlowErrorChecksum(MULTI_DATA_RESPONSE.getBytes(), 3,
                MULTI_DATA_RESPONSE.length() - 5);
        assertEquals(calcCheckSum, calcCheckSum2);
    }

}
