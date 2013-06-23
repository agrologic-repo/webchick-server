package com.agrologic.app.messaging;

import com.agrologic.app.network.CommandType;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Observable;
import java.util.Observer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;

public class RequestMessageQueueTest implements Observer {
    private RequestMessageQueue sut; //system under test
    private MessageFactory messageFactory;

    @Before
    public void setUp() throws Exception {
        messageFactory = new MessageFactory();
        sut = new RequestMessageQueue();
        sut.addObserver(this);
        sut.notifyToPrepareRequests();
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
    public void requestDoesNotExitsInRequestQueueAfterGettingRequest() throws Exception {
        sut.getRequest(); // skip one request
        RequestMessage actual = (RequestMessage) sut.getRequest();
        RequestMessage expected = messageFactory.createRealTimeRequests("T901").get(0);
        assertNotSame(expected, actual);
    }

    @Test
    public void requestToChangeAddedToQueueAndReturnByItPriority() {
        sut.notifyToCreateRequestToChange();
        RequestMessage actual = (RequestMessage) sut.getRequest();
        RequestMessage expected = messageFactory.createWriteRequest("T901", 4096L, 255L);
        assertEquals(actual, expected);
    }

    @Test
    public void previousRequestReturnIfNeedToo() {
        sut.getRequest();// skip request and call it again
        sut.setReplyForPreviousRequestPending(true);
        RequestMessage actual = (RequestMessage) sut.getRequest();
        RequestMessage expected = messageFactory.createRealTimeRequests("T901").get(0);
        assertEquals(expected, actual);
    }

    @Override
    public void update(Observable o, Object arg) {
        CommandType actualCommand = (CommandType) arg;
        switch (actualCommand) {
            case CREATE_REQUEST:
                sut.addRequest(messageFactory.createRealTimeRequests("T901").get(0));
                sut.addRequest(messageFactory.createRealTimeRequests("T901").get(1));
                break;
            case CREATE_REQUEST_TO_WRITE:
                sut.addRequest(messageFactory.createWriteRequest("T901", 4096L, 255L));
                break;
        }
    }
}
