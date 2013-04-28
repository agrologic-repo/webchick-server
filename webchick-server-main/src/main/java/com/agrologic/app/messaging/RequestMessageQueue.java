
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.agrologic.app.messaging;

//~--- non-JDK imports --------------------------------------------------------

import com.agrologic.app.network.CommandType;
import org.apache.log4j.Logger;

import java.util.Observable;
import java.util.PriorityQueue;

/**
 * {Insert class description here}
 *
 * @author Valery Manakhimov
 * @author $Author: nbweb $, (this version)
 * @version $Revision: 1.1.1.1 $
 * @since Build {insert version here} (MM YYYY)
 */
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
     * Last sent request
     */
    private RequestMessage requestToSend;

    public void addRequest(RequestMessage sendMessage) {
        queue.add(sendMessage);
    }

    public void prepareRequests() {
        setChanged();
        notifyObservers(CommandType.CREATE_REQUEST);
    }

    public void setReplyForPreviousRequestPending(boolean reply) {
        this.replyForPreviousRequestPending = reply;
    }

    /**
     * Return next request controller from request queue.
     *
     * @return requestToSend the request from controller
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

    public void getRequestToChange() {
        setChanged();
        notifyObservers(CommandType.CREATE_REQUEST_TO_WRITE);
    }
}

