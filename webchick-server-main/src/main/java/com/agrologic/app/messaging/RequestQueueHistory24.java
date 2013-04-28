
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.agrologic.app.messaging;


import com.agrologic.app.model.Data;

import java.util.*;

/**
 * Title: RequestQueueHistory24.java <br> Description: <br> Copyright: Copyright © 2010 <br> Company: AgroLogic Ltd.
 * ®<br>
 *
 * @author Valery Manakhimov <br>
 * @version 0.1.1 <br>
 */
public class RequestQueueHistory24 {

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
    private Map<RequestMessage, Boolean> defaultList = new HashMap<RequestMessage, Boolean>();

    /**
     * Constructor with one parameter , takes default parameter DEFAULT_GROWDAY.
     *
     * @param netname the net name
     */
    public RequestQueueHistory24(final String netname) {
        this(netname, DEFAULT_GROWDAY);
    }

    /**
     * Constructor with net name and grow day.
     *
     * @param netname the net name
     * @param growDay the grow day
     */
    public RequestQueueHistory24(final String netname, final Integer growDay) {
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
            defaultList.put(request, true);
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
//        for (RequestMessage rm : requestList) {
            requests.add(rm);
        }
    }

    private void setActiveRequests(List<Data> dataList) {
        if (dataList == null || dataList.isEmpty()) {
            Iterator iter = defaultList.keySet().iterator();
            while (iter.hasNext()) {
                RequestMessage rm = (RequestMessage) iter.next();
                activeRequests.add(rm);
            }
        } else {
            Iterator iter = defaultList.keySet().iterator();
            while (iter.hasNext()) {
                RequestMessage rm = (RequestMessage) iter.next();
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

    public void printAll() {
        for (Object o : activeRequests) {
            System.out.println(o);
        }
    }

    /**
     * Creating history requests with new grow day parameter.
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
     * Removes the first occurrence of the specified element from this list, if it is present. If this list does not
     * contain or the element <tt>isUnusedType</tt> returns false , it is unchanged.
     *
     * @param msg element to be removed from this list, if unused.
     * @return <tt>true</tt> if the list contained the specified element and element <tt>isUnusedType</tt> returns true.
     */
    public void removedUnused(final RequestMessage msg) {
//        requestList.remove(msg);
        activeRequests.remove(msg);
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

    public static void main(String args[]) {
        RequestQueueHistory24 rqh24 = new RequestQueueHistory24("T901");
        rqh24.initDnMap();
        List<Data> dl = new ArrayList<Data>();
        Data d0 = new Data();
        d0.setId(Long.valueOf(3122));
        Data d1 = new Data();
        d1.setId(Long.valueOf(3107));
        Data d2 = new Data();
        d2.setId(Long.valueOf(3142));
        Data d3 = new Data();
        d3.setId(Long.valueOf(1302));
        Data d4 = new Data();
        d4.setId(Long.valueOf(1301));
        dl.add(d0);
        dl.add(d1);
        dl.add(d2);
        dl.add(d3);
        dl.add(d4);
//        dnMap.put("D18", Long.valueOf(3122));
//        dnMap.put("D19", Long.valueOf(3107));
//        dnMap.put("D2", Long.valueOf(1301));
//        dnMap.put("D20", Long.valueOf(3142));
//        dnMap.put("D21", Long.valueOf(1302));
//        rqh24.setActiveRequests(dl);
        rqh24.printAll();
    }
}

