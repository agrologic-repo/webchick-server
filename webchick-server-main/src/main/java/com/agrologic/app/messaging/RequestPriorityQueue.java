
/*
* To change this template, choose Tools | Templates
* and open the template in the editor.
 */
package com.agrologic.app.messaging;



import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

/**
 * RequestPriorityQueue
 *
 * @author Administrator
 * @version 1.0 <br>
 */
public class RequestPriorityQueue extends PriorityQueue<RequestMessage> {
    private static final int     DEFAULT_GROWDAY = 1;
    private Integer              growDay;
    private String               netname;
    private List<RequestMessage> requestList;

    /**
     * Construct a RequestPriorityQueue with default list of requests.
     *
     * @param netname the net name for creating request.
     * @param growDay the grow day for history requests.
     */
    public RequestPriorityQueue(final String netname) {
        this(netname, DEFAULT_GROWDAY);
    }

    /**
     * Construct a RequestPriorityQueue with default list of requests.
     *
     * @param netname the net name for creating request.
     * @param growDay the grow day for history requests.
     */
    public RequestPriorityQueue(final String netname, final Integer growDay) {
        super();
        this.netname = netname;
        this.growDay = growDay;
        onCreateQueue();
    }

    /**
     * Create real time data default request list
     */
    public final void onCreateQueue() {
        requestList = new ArrayList<RequestMessage>();
        requestList.add(new RequestMessage(MessageType.REQUEST_PANEL, netname));
        requestList.add(new RequestMessage(MessageType.REQUEST_CONTROLLER, netname));
        requestList.add(new RequestMessage(MessageType.REQUEST_CHICK_SCALE, netname));
        requestList.add(new RequestMessage(MessageType.REQUEST_EGG_COUNT, netname));
        requestList.add(new RequestMessage(MessageType.REQUEST_CHANGED, netname));
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
     * Creates request for changed data on controller and added to queue of requests.
     */
    public final void createChangeRequests() {
        add(new RequestMessage(MessageType.REQUEST_CHANGED, netname));
    }

    /**
     * Remove request for changed data on controller
     *
     * @param request the request to remove
     */
    public final void removeChangeRequest(RequestMessage request) {
        remove(request);
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
     * @exception NoSuchElementException if the iteration has no nextElem element.
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

    /**
     * Returns grow day of controller .
     *
     * @return the growDay
     */
    public Integer getGrowDay() {
        return growDay;
    }

    /**
     * Sets controller grow day .
     *
     * @param growDay the growDay to set
     */
    public void setGrowDay(Integer growDay) {
        this.growDay = growDay;
    }
}



