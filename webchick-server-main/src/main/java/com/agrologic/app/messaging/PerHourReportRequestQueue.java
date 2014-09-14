package com.agrologic.app.messaging;

import com.agrologic.app.model.Data;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class PerHourReportRequestQueue {
    private static final int DEFAULT_GROW_DAY = 1;
    private int size;
    private Integer growDay;
    private final String netname;
    private final CyclicQueue<RequestMessage> requests;
    private final Collection<Data> perHourReportDataList;
    private List<RequestMessage> activeRequests = new ArrayList<RequestMessage>();

    /**
     * Constructor with net name and grow day.
     *
     * @param netname the net name
     */
    public PerHourReportRequestQueue(final String netname, Collection<Data> perHourReportDataList) {
        this.netname = netname;
        this.growDay = DEFAULT_GROW_DAY;
        this.requests = new CyclicQueue<RequestMessage>();
        this.perHourReportDataList = perHourReportDataList;
        this.size = perHourReportDataList.size();
        createRequests();
    }


    private void createRequests() {
        activeRequests = MessageFactory.createPerHourHistoryRequests(netname, growDay, perHourReportDataList);
        requests.clear();
        for (RequestMessage rm : activeRequests) {
            requests.add(new RequestMessage(MessageType.REQUEST_PER_HOUR_REPORTS, netname, growDay, rm.getDnum()));
        }
    }

    /**
     * Creating management requests with new grow day parameter .
     *
     * @param newGrowDay the grow day .
     */
    public void recreateRequests(final Integer newGrowDay) {
        growDay = newGrowDay;
        requests.clear();
        for (RequestMessage rm : activeRequests) {
            requests.add(new RequestMessage(MessageType.REQUEST_PER_HOUR_REPORTS, netname, growDay, rm.getDnum()));
        }
    }

    /**
     * Returns the next element in the list .
     *
     * @return the nextElem element in the list .
     * @throws java.util.NoSuchElementException if the iteration has no nextElem element.
     */
    public final RequestMessage next() throws IllegalAccessException {
        return requests.nextElem();
    }

    public int getSize() {
        return size;
    }

    /**
     * Return true if head equal tail
     *
     * @return true if head equal tail.
     */
    public boolean isCycleComplete() {
        return requests.isCycleCompleted();
    }

    /**
     * Reset flag that indicate that head equals tail.
     */
    public void resetCycleFlag() {
        requests.resetCycleFlag();
    }

    public Integer getGrowDay() {
        return growDay;
    }
}

