package com.agrologic.app.messaging;


import com.agrologic.app.model.Controller;

import java.util.List;
import java.util.PriorityQueue;

public class RequestPriorityQueue extends PriorityQueue<RequestMessage> {
    private final String netname;
    private List<RequestMessage> requestList;

    /**
     * Construct a RequestPriorityQueue with default list of requests.
     *
     * @param controller the controller for creating request.
     */
    public RequestPriorityQueue(final Controller controller) {
        this.netname = controller.getNetName();
        onCreateQueue(controller);
    }

    /**
     * Create real time data default request list
     */
    public final void onCreateQueue(final Controller controller) {
        requestList = MessageFactory.createRealTimeRequests(netname);
        if (controller.getName().equals("T911") || controller.getName().equals("607") || controller.getName().equals("616") || controller.getName().equals("610")) {
            requestList.remove(4);// remove request changed 'f'
        }
        initReaTimeRequest();
    }

    /**
     * Create and add to queue general request .
     */
    public final void initReaTimeRequest() {
        for (RequestMessage rm : requestList) {
            add(rm);
        }
    }

    /**
     * Removes unused request from request list .If controller does not response for some rezone there is requests that
     * do not have been requested .
     *
     * @param msg the request
     * @return true if request removed otherwise false .
     */
    public boolean removedUnusedFromQueue(final RequestMessage msg) {
        if (msg == null) {
            return false;
        } else if (msg.isUnusedType()) {
            requestList.remove(msg);
            return true;
        } else {
            return false;
        }
    }

    /**
     * Returns the nextElem element in the list.
     *
     * @return the nextElem element in the list.
     * @throws IllegalAccessException if the iteration has no nextElem element.
     */
    public final RequestMessage next() throws IllegalAccessException {
        if (isEmpty()) {
            initReaTimeRequest();
        }

        if (peek().getMessageType() == MessageType.REQUEST_CHANGED) {
            return peek();
        } else {
            return poll();
        }
    }
}



