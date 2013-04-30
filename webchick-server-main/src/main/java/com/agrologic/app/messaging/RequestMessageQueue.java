
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.agrologic.app.messaging;

import com.agrologic.app.network.CommandType;
import org.apache.log4j.Logger;

import java.util.Observable;
import java.util.PriorityQueue;

public class RequestMessageQueue extends Observable {

    /**
     * Queue with requests
     */
    private final PriorityQueue<RequestMessage> queue = new PriorityQueue<RequestMessage>();

    /**
     * true if request must send again
     */
    private boolean replyForPreviousRequestPending;

    /**
     * last sent request
     */
    private RequestMessage requestToSend;

    /**
     * @param sendMessage
     */
    public void addRequest(RequestMessage sendMessage) {
        queue.add(sendMessage);
    }

    /**
     * Notify observers to create request real time request.
     */
    public void notifyToPrepareRequests() {
        setChanged();
        notifyObservers(CommandType.CREATE_REQUEST);
    }

    /**
     * Set reply flag if no response was received for previous request.
     *
     * @param reply the reply flag
     */
    public void setReplyForPreviousRequestPending(boolean reply) {
        this.replyForPreviousRequestPending = reply;
    }

    /**
     * Poll next request message from queue .If queue is empty notify observers to create real time request.
     * If no response was received , method returns request that was sent  before.
     *
     * @return requestToSend the request message to controller .
     */
    public Message getRequest() {
        Logger log = Logger.getLogger(RequestMessageQueue.class);
        if (replyForPreviousRequestPending) {
            setReplyForPreviousRequestPending(false);
        } else {
            if (queue.isEmpty()) {
                setChanged();
                notifyObservers(CommandType.CREATE_REQUEST);
            }
            requestToSend = queue.poll();
            log.debug("Request to send : " + requestToSend);
        }
        return requestToSend;
    }

    /**
     * Notify observers to create request to change data . Priority of this request message is higher than any other
     * requests , therefore request to write will send before other requests .
     */
    public void notifyToCreateRequestToChange() {
        setChanged();
        notifyObservers(CommandType.CREATE_REQUEST_TO_WRITE);
    }
}

