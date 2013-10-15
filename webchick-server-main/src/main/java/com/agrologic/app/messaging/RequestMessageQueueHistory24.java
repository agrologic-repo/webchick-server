
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.agrologic.app.messaging;


import com.agrologic.app.model.Data;

import java.util.*;

public class RequestMessageQueueHistory24 {
    private static final int DEFAULT_GROWDAY = 1;
    public enum DataType {
        OVERALL_WATER_CONSUMPTION("D1", 1302), FEED_CONSUMPTION("D2", 1301),
        INSIDE_TEMPERATURE("D18", 3122), OUTSIDE_TEMPERATURE("D19", 3107), PER_HOUR_HUMIDITY("D20", 3142),
        PER_HOUR_WATER_CONSUMPTION("D21", 1302), WATER_2_CONSUMPTION("D42", 1329), FEED_2_CONSUMPTION("D43", 1328),
        RESET_TIME("D70", 3009), PER_HOUR_WATER_2_CONSUMPTION("D71", 1329), PER_HOUR_FEED_CONSUMPTION("D72", 1301),
        PER_HOUR_FEED_2_CONSUMPTION("D73", 1328);

        private DataType(String name, int id) {
            this.name = name;
            this.id = id;
        }

        public final String name;
        public final int id;
    }
    private Integer growDay;
    private final String netname;
    private final CyclicQueue<RequestMessage> requests;
    private List<RequestMessage> activeRequests = new ArrayList<RequestMessage>();
    private Map<String, Long> dnMap = new HashMap<String, Long>();
    private Map<RequestMessage, Boolean> requestMessageBooleanHashMap = new HashMap<RequestMessage, Boolean>();

    /**
     * Constructor with one parameter , takes default parameter DEFAULT_GROWDAY.
     *
     * @param netname the net name
     */
    public RequestMessageQueueHistory24(final String netname) {
        this(netname, DEFAULT_GROWDAY);
    }

    /**
     * Constructor with net name and grow day.
     *
     * @param netname the net name
     * @param growDay the grow day
     */
    public RequestMessageQueueHistory24(final String netname, final Integer growDay) {
        this.netname = netname;
        this.growDay = growDay;
        initDefaultList();
        initDnMap();
        this.requests = new CyclicQueue<RequestMessage>();
        setActiveRequests(null);
        initHistory24Request();
    }

    private void initDefaultList() {
        for (RequestMessage request : new MessageFactory().createPerHourDailyReportRequests(netname, growDay)) {
            requestMessageBooleanHashMap.put(request, true);
        }
    }

    private void initDnMap() {
        for (DataType type : DataType.values()) {
            dnMap.put(type.name, (long) type.id);
        }
    }

    /**
     * Initialization request queue by adding to source queue requests from prepared request list queue.
     */
    private void initHistory24Request() {
        for (RequestMessage rm : activeRequests) {
            requests.add(rm);
        }
    }

    private void setActiveRequests(List<Data> dataList) {
        if (dataList == null || dataList.isEmpty()) {
            Iterator iterator = requestMessageBooleanHashMap.keySet().iterator();
            while (iterator.hasNext()) {
                RequestMessage rm = (RequestMessage) iterator.next();
                activeRequests.add(rm);
            }
        } else {
            Iterator iterator = requestMessageBooleanHashMap.keySet().iterator();
            while (iterator.hasNext()) {
                RequestMessage rm = (RequestMessage) iterator.next();
                String dn = rm.getDnum();
                Long did = dnMap.get(dn);
                for (Data d : dataList) {
                    if (d.getId().equals(did)) {
                        activeRequests.add(rm);
                    }
                }
            }
        }
    }

    /**
     * Creating management requests with new grow day parameter.
     *
     * @param newGrowDay the grow day.
     */
    public void recreateHistory24Requests(final Integer newGrowDay) {
        growDay = newGrowDay;
        requests.clear();

        for (RequestMessage rm : activeRequests) {
            RequestMessage newrm = new RequestMessage(MessageType.REQUEST_HISTORY_24_HOUR, netname, growDay, rm.getDnum());
            requests.add(newrm);
        }
    }

    /**
     * Returns the next element in the list.
     *
     * @return the nextElem element in the list.
     * @throws NoSuchElementException if the iteration has no nextElem element.
     */
    public final RequestMessage next() throws IllegalAccessException {
        return requests.nextElem();
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
}

