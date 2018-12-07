package com.agrologic.app.messaging;

import com.agrologic.app.model.Data;
import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class MessageFactory {
    /**
     * Creating a set of messages to be used to get interactive controller state. These messages are used all the time
     * when cellink is RUNNING.
     *
     * @param netname the type of the controller and its network index
     * @return a set of messages to be used to get interactive controller state
     */
    public static List<RequestMessage> createRealTimeRequests(String netname) {
        return Lists.newArrayList(
                new RequestMessage(MessageType.REQUEST_PANEL, netname),
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
    public static List<RequestMessage> createPerHourDailyReportRequests(String netname, int growDay) {
        return createPerHourReportRequests(netname, growDay, RequestMessageQueueHistory24.DataType.values());
    }

    /**
     * Creating request to get the set of data that used for 24 hour graphs.
     *
     * @param netname the type of the controller and its network index
     * @return the request message
     */
    public static RequestMessage createGraphRequest(String netname) {
        return new RequestMessage(MessageType.REQUEST_GRAPHS, netname);
    }

    /**
     * Create request that used to notice client about the error.
     *
     * @return the request message to notice client about the error
     */
    public static RequestMessage createErrorMessage() {
        return new RequestMessage(MessageType.ERROR);
    }

    /**
     * Create request message to send back to cellink . This message used when cellink send keep alive and waiting
     * acknowledge with keep alive timeout in minutes.
     *
     * @param keepAliveTimeoutMinutes the timeout for next keep alive message from cellink
     * @return the request message with keep alive timeout in minutes
     */
    public static RequestMessage createKeepAlive(int keepAliveTimeoutMinutes) {
        return new RequestMessage(MessageType.KEEP_ALIVE, keepAliveTimeoutMinutes);
    }

    /**
     * Creates request message that used to change value of specified data type on controller .
     *
     * @param netname   the type of the controller and its network index
     * @param dataType  the data type that have to be changed
     * @param propValue the new value to send
     * @return the write request message that was created
     */
    public static RequestMessage createWriteRequest(String netname, Long dataType, Long propValue) {
        return new RequestMessage(MessageType.REQUEST_TO_WRITE, netname, dataType, propValue);
    }

    /**
     * Create the set of messages to get daily reports from controller. This message are used when new flock
     * opened and age of bird was increased .
     *
     * @param netname the type of the controller and its network index
     * @param growDay the age of birds we're going to get data for
     * @return request message for daily to be used to grab daily report
     */
    public static RequestMessage createDailyReportRequests(String netname, int growDay) {
        return new RequestMessage(MessageType.REQUEST_HISTORY, netname, growDay);
    }

    /**
     * Create the set of messages to get per hour reports from controller. This messages are used when new flock
     * opened and age of bird was increased .
     *
     * @param netname   the type of the controller and its network index
     * @param growDay   the age of birds we're going to get data for
     * @param dataTypes the list of data types that used for request reports
     * @return result a set of messages to be used to grab daily report
     */
    public static List<RequestMessage> createPerHourReportRequests(String netname, int growDay,
                                                                   RequestMessageQueueHistory24.DataType... dataTypes) {
        List<RequestMessage> result = new ArrayList<RequestMessage>(dataTypes.length);
        for (RequestMessageQueueHistory24.DataType dataType : dataTypes) {
            result.add(new RequestMessage(MessageType.REQUEST_PER_HOUR_REPORTS, netname, growDay, dataType.name));
        }
        return result;
    }

    /**
     * Create the set of messages to get per hour reports from controller. This messages are used when new flock
     * opened and age of bird was increased .
     *
     * @param netname        the type of the controller and its network index
     * @param growDay        the age of birds we're going to get data for
     * @param dataCollection the list of data types that used for request reports
     * @return result a set of messages to be used to grab daily report
     */
    public static List<RequestMessage> createPerHourHistoryRequests(String netname, int growDay,
                                                                    Collection<Data> dataCollection) {
        List<RequestMessage> result = new ArrayList<RequestMessage>(dataCollection.size());
        for (Data data : dataCollection) {
            result.add(new RequestMessage(MessageType.REQUEST_PER_HOUR_REPORTS, netname, growDay,
                    data.getHistoryHourDNum()));
        }
        return result;
    }
}
