package com.agrologic.app.messaging;

import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.List;

public class MessageFactory {
    /**
     * Creating a set of messages to be used to get interactive controller state. These messages are used all the time
     * when cellink is RUNNING.
     *
     * @param netname the type of the controller and its network index
     * @return a set of messages to be used to get interactive controller state
     */
    public List<RequestMessage> createRealTimeRequests(String netname) {
        return Lists.newArrayList(new RequestMessage(MessageType.REQUEST_PANEL, netname),
                new RequestMessage(MessageType.REQUEST_CONTROLLER, netname),
                new RequestMessage(MessageType.REQUEST_CHICK_SCALE, netname),
                new RequestMessage(MessageType.REQUEST_EGG_COUNT, netname),
                new RequestMessage(MessageType.REQUEST_CHANGED, netname)
        );
    }

    /**
     * Creating requests to get daily report for each our of resource consumption and environment measurement like
     * humidity.
     *
     * @param netname the type of the controller and its network index
     * @param growDay the age of birds we're going to get data for
     * @return a set of messages to be used to grab daily report
     */
    public List<RequestMessage> createPerHourDailyReportRequests(String netname, int growDay) {
        return createPerHourReportRequests(netname, growDay, RequestQueueHistory24.DataType.values());
    }

    private List<RequestMessage> createPerHourReportRequests(String netname, int growDay, RequestQueueHistory24.DataType... dataTypes) {
        List<RequestMessage> result = new ArrayList<RequestMessage>(dataTypes.length);
        for (RequestQueueHistory24.DataType dataType : dataTypes) {
            result.add(new RequestMessage(MessageType.REQUEST_HISTORY_24_HOUR, netname, growDay, dataType.name));
        }
        return result;
    }
}
