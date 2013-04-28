
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.agrologic.app.messaging;

//~--- non-JDK imports --------------------------------------------------------
import com.agrologic.app.network.CommandType;
import java.util.Observable;
import java.util.PriorityQueue;
import org.apache.log4j.Logger;

/**
 * {Insert class description here}
 *
 * @version $Revision: 1.1.1.1 $
 * @since Build {insert version here} (MM YYYY)
 * @author Valery Manakhimov
 * @author $Author: nbweb $, (this version)
 */
public class RequestMessageQueue extends Observable {

    /**
     * Queue with requests
     */
    private PriorityQueue<RequestMessage> queue;
    /**
     * true if request must send again
     */
    private boolean replyRequest;
    /**
     * Last sent request
     */
    private RequestMessage requestToSend;

    public RequestMessageQueue() {
        this.replyRequest = false;
        this.queue = new PriorityQueue<RequestMessage>();
    }

    public void addRequest(RequestMessage sendMessage) {
        queue.add(sendMessage);
    }

    public void removeRequest(RequestMessage sendMessage) {
        queue.remove(sendMessage);
    }

    public void prepareRequests() {
        setChanged();
        notifyObservers(CommandType.CREATE_REQUEST);
    }

    public void setReplyRequest(boolean reply) {
        this.replyRequest = reply;
    }

    /**
     * Return next request controller from request queue.
     *
     * @return requestToSend the request from controller
     */
    public Message getRequest() {
        Logger log = Logger.getLogger(RequestMessageQueue.class);
        // reply same request if needed
        if (replyRequest() == false) {
            // if there is a request to write
            // send it first
            if (queue.isEmpty()) {
                setChanged();
                notifyObservers(CommandType.CREATE_REQUEST);
            }
            try {
                Thread.sleep(500);
            } catch (InterruptedException ex) {
            }
            requestToSend = queue.poll();

            if (requestToSend == null) {
                log.debug("Notify request to write creation");
                setChanged();
                notifyObservers(CommandType.CREATE_REQUEST_TO_WRITE);
            } else {
                return requestToSend;
            }
            try {
                Thread.sleep(500);
            } catch (InterruptedException ex) {
            }
            requestToSend = queue.poll();
            if (requestToSend == null) {
                log.debug("Notify request creation");
                setChanged();
                notifyObservers(CommandType.CREATE_REQUEST);
            } else {
                return requestToSend;
            }
            try {
                Thread.sleep(500);
            } catch (InterruptedException ex) {
            }
            requestToSend = queue.poll();
            log.debug("Request to send : " + requestToSend);
        } else {
            setReplyRequest(false);
        }
        return requestToSend;
    }

    public void getRequestToChange() {
        setChanged();
        notifyObservers(CommandType.CREATE_REQUEST_TO_WRITE);
    }

    public boolean replyRequest() {
        return replyRequest;
    }
}

