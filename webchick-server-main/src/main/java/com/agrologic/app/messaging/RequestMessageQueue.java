package com.agrologic.app.messaging;

import com.agrologic.app.network.CommandType;

import java.util.Observable;
import java.util.PriorityQueue;

public class RequestMessageQueue extends Observable {
    /**
     * true if request must send again
     */
    private boolean replyForPreviousRequestPending;

    /**
     * last sent request
     */
    private RequestMessage requestToSend;

    /**
     * Queue with requests
     */
    private final PriorityQueue<RequestMessage> queue = new PriorityQueue<RequestMessage>();

    /**
     * Add request to queue . The request message that controller prepared .
     *
     * @param requestMessage next request message
     */
    public void addRequest(RequestMessage requestMessage) {
        queue.add(requestMessage);
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
        if (replyForPreviousRequestPending) {  //true if request must send again
            setReplyForPreviousRequestPending(false);
        } else {
            if (queue.isEmpty()) {// Queue with requests
                setChanged();
                notifyObservers(CommandType.CREATE_REQUEST);
//                queue.add(new RequestMessage(REQUEST_PANEL))
            }
            requestToSend = queue.poll();
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

