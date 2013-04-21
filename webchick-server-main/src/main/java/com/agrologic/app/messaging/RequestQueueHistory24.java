
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.agrologic.app.messaging;

//~--- JDK imports ------------------------------------------------------------
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
    private static final String D1 = "D1";
    private static final String D2 = "D2";
    private static final String D18 = "D18";
    private static final String D19 = "D19";
    private static final String D20 = "D20";
    private static final String D21 = "D21";
    private static final String D42 = "D42";
    private static final String D43 = "D43";
    private static final String D70 = "D70";
    private static final String D71 = "D71";
    private static final String D72 = "D72";
    private static final String D73 = "D73";
    private Integer growDay;
    private final String netname;
    private List<RequestMessage> requestList;
    private final CyclicQueue<RequestMessage> requests;
    private List<RequestMessage> activeRequests = new ArrayList<RequestMessage>();
    private Map<String, Long> dnMap = new HashMap<String, Long>();
    private Map<RequestMessage, Boolean> defaultList = new HashMap<RequestMessage, Boolean>();

    /**
     *
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
        this.requests = new CyclicQueue<RequestMessage>();
        this.requestList = new ArrayList<RequestMessage>();
        initRequestList();
        initDefaultList();
        initDnMap();
        setActiveRequests(null);
        initHistory24Request();
    }

    private void initRequestList() {
        requestList.add(new RequestMessage(MessageType.REQUEST_HISTORY_24_HOUR, netname, growDay, D18));
        requestList.add(new RequestMessage(MessageType.REQUEST_HISTORY_24_HOUR, netname, growDay, D19));
        requestList.add(new RequestMessage(MessageType.REQUEST_HISTORY_24_HOUR, netname, growDay, D20));
        requestList.add(new RequestMessage(MessageType.REQUEST_HISTORY_24_HOUR, netname, growDay, D21));
        requestList.add(new RequestMessage(MessageType.REQUEST_HISTORY_24_HOUR, netname, growDay, D70));
        requestList.add(new RequestMessage(MessageType.REQUEST_HISTORY_24_HOUR, netname, growDay, D71));
        requestList.add(new RequestMessage(MessageType.REQUEST_HISTORY_24_HOUR, netname, growDay, D72));
        requestList.add(new RequestMessage(MessageType.REQUEST_HISTORY_24_HOUR, netname, growDay, D73));
//      requestList.add(new RequestMessage(MessageType.REQUEST_HISTORY_HISTOGRAM, netname, growDay, "A"));
//      requestList.add(new RequestMessage(MessageType.REQUEST_HISTORY_HISTOGRAM, netname, growDay, "B"));
//      requestList.add(new RequestMessage(MessageType.REQUEST_HISTORY_HISTOGRAM, netname, growDay, "C"));
//      requestList.add(new RequestMessage(MessageType.REQUEST_HISTORY_HISTOGRAM, netname, growDay, "D"));
    }

    private void initDefaultList() {
        defaultList.put(new RequestMessage(MessageType.REQUEST_HISTORY_24_HOUR, netname, growDay, D1), true);
        defaultList.put(new RequestMessage(MessageType.REQUEST_HISTORY_24_HOUR, netname, growDay, D2), true);
        defaultList.put(new RequestMessage(MessageType.REQUEST_HISTORY_24_HOUR, netname, growDay, D18), true);
        defaultList.put(new RequestMessage(MessageType.REQUEST_HISTORY_24_HOUR, netname, growDay, D19), true);
        defaultList.put(new RequestMessage(MessageType.REQUEST_HISTORY_24_HOUR, netname, growDay, D20), true);
        defaultList.put(new RequestMessage(MessageType.REQUEST_HISTORY_24_HOUR, netname, growDay, D21), true);
        defaultList.put(new RequestMessage(MessageType.REQUEST_HISTORY_24_HOUR, netname, growDay, D70), true);
        defaultList.put(new RequestMessage(MessageType.REQUEST_HISTORY_24_HOUR, netname, growDay, D71), true);
        defaultList.put(new RequestMessage(MessageType.REQUEST_HISTORY_24_HOUR, netname, growDay, D72), true);
        defaultList.put(new RequestMessage(MessageType.REQUEST_HISTORY_24_HOUR, netname, growDay, D73), true);
//        defaultList.put(new RequestMessage(MessageType.REQUEST_HISTORY_24_HOUR, netname, growDay, "D165"), true);
//        defaultList.put(new RequestMessage(MessageType.REQUEST_HISTORY_24_HOUR, netname, growDay, "D166"), true);
//        defaultList.put(new RequestMessage(MessageType.REQUEST_HISTORY_24_HOUR, netname, growDay, "D169"), true);
//        defaultList.put(new RequestMessage(MessageType.REQUEST_HISTORY_24_HOUR, netname, growDay, "D170"), true);
    }

    private void initDnMap() {
        dnMap.put(D1, Long.valueOf(1302));
        dnMap.put(D2, Long.valueOf(1301));
        dnMap.put(D18, Long.valueOf(3122));
        dnMap.put(D19, Long.valueOf(3107));
        dnMap.put(D20, Long.valueOf(3142));
        dnMap.put(D21, Long.valueOf(1302));
        dnMap.put(D42, Long.valueOf(1329));
        dnMap.put(D43, Long.valueOf(1328));
        dnMap.put(D70, Long.valueOf(3009));
        dnMap.put(D71, Long.valueOf(1329));
        dnMap.put(D72, Long.valueOf(1301));
        dnMap.put(D73, Long.valueOf(1328));
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
//        for (RequestMessage rm : requestList) {
            RequestMessage newrm = new RequestMessage(MessageType.REQUEST_HISTORY_24_HOUR,
                    netname, growDay, rm.getDnum());
            requests.add(newrm);
        }
    }

    /**
     * Returns the next element in the list.
     *
     * @return the nextElem element in the list.
     * @exception NoSuchElementException if the iteration has no nextElem element.
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
//~ Formatted by Jindent --- http://www.jindent.com
