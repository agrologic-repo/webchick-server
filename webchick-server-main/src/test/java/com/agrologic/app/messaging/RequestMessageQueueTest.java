package com.agrologic.app.messaging;

import com.agrologic.app.network.CommandType;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Observable;
import java.util.Observer;

import static org.junit.Assert.assertEquals;

public class RequestMessageQueueTest implements Observer {
    RequestMessageQueue sut; //system under test
    MessageFactory messageFactory;

    @Before
    public void setUp() throws Exception {
        sut = new RequestMessageQueue();
        sut.addObserver(this);
        messageFactory = new MessageFactory();
    }

    @After
    public void tearDown() throws Exception {
        sut.deleteObserver(this);
    }

    @Test
    public void addRequest() {
        RequestMessage expected = messageFactory.createRealTimeRequests("T901").get(0);
        sut.addRequest(expected);
        RequestMessage actual = (RequestMessage) sut.getRequest();
        assertEquals(expected, actual);
    }

    @Test
    public void notifyToPrepareRequests() {
        sut.notifyToPrepareRequests();
    }

    @Test
    public void getRequestEqualsToCreatedRequest() throws Exception {
        RequestMessage actual = (RequestMessage) sut.getRequest();
        RequestMessage expected = messageFactory.createRealTimeRequests("T901").get(0);
        assertEquals(expected, actual);
    }

    @Test
    public void notifyToCreateRequestToChange() {
        sut.notifyToCreateRequestToChange();
    }

    @Override
    public void update(Observable o, Object arg) {
        CommandType actualCommand = (CommandType) arg;
        CommandType expectedCommand = sut.getCommandType();
        sut.addRequest(messageFactory.createRealTimeRequests("T901").get(0));
        assertEquals(expectedCommand, actualCommand);
    }
}
