package com.agrologic.app.messaging;

import com.agrologic.app.network.CommandType;

import java.util.HashMap;
import java.util.Map;
import java.util.Observable;
import java.util.Set;

public class ResponseMessageMap extends Observable {
    /**
     * The map of request response
     */
    private Map<RequestMessage, ResponseMessage> responseMap;

    public ResponseMessageMap() {
        responseMap = new HashMap<RequestMessage, ResponseMessage>();
    }

    public synchronized void put(RequestMessage sendMessage, ResponseMessage receiveMessage) {
        synchronized (responseMap) {
            if (responseMap == null) {
                responseMap = new HashMap<RequestMessage, ResponseMessage>();
            }

            MessageType type = receiveMessage.getMessageType();
            responseMap.put(sendMessage, receiveMessage);

            switch (type) {
                case RESPONSE_DATA:
                    setChanged();
                    notifyObservers(CommandType.UPDATE);

                    break;

                case SKIP_UNUSED_RESPONSE:
                    setChanged();
                    notifyObservers(CommandType.SKIP_UNUSED);

                    break;

                case SKIP_RESPONSE_TO_WRITE:
                    setChanged();
                    notifyObservers(CommandType.SKIP_TO_WRITE);

                    break;

                case ERROR:
                    setChanged();
                    notifyObservers(CommandType.ERROR);

                    break;

                default:
                    break;
            }
        }
    }

    public synchronized Boolean isContainRequest(RequestMessage requestMessage) {
        boolean exist = false;
        Set<Map.Entry<RequestMessage, ResponseMessage>> entrySet = responseMap.entrySet();
        for (Map.Entry<RequestMessage, ResponseMessage> entry : entrySet) {
            RequestMessage rm = entry.getKey();
            if (rm.equals(requestMessage)) {
                exist = true;
                break;
            }
        }
        return exist;
    }

    public synchronized ResponseMessage removeResponse(Message request) {
        return responseMap.remove(request);
    }

    public synchronized Map<RequestMessage, ResponseMessage> getResponseMap() {
        return responseMap;
    }
}



