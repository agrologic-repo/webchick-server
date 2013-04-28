
/*
* To change this template, choose Tools | Templates
* and open the template in the editor.
 */
package com.agrologic.app.messaging;

//~--- non-JDK imports --------------------------------------------------------

import com.agrologic.app.network.CommandType;



import java.util.HashMap;
import java.util.Map;
import java.util.Observable;
import java.util.Set;

/**
 * {Insert class description here}
 *
 * @version $Revision: 1.1.1.1 $
 * @since Build {insert version here} (MM YYYY)
 * @author Valery Manakhimov
 * @author $Author: nbweb $, (this version)
 */
public class ResponseMessageMap extends Observable {
    private static final long serialVersionUID = 1L;

    /**
     * The map of request response
     */
    private Map<RequestMessage, ResponseMessage> responseMap;
    private RequestMessage                       sendMessage;

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
            case RESPONSE_DATA :
                setChanged();
                notifyObservers(CommandType.UPDATE);

                break;

            case SKIP_UNUSED_RESPONSE :
                setChanged();
                notifyObservers(CommandType.SKIP_UNUSED);

                break;

            case SKIP_RESPONSE_TO_WRITE :
                setChanged();
                notifyObservers(CommandType.SKIP_TO_WRITE);

                break;

            case ERROR :
                setChanged();
                notifyObservers(CommandType.ERROR);

                break;

            default :

                // unkown error
                break;
            }
        }
    }

    public synchronized Map<RequestMessage, ResponseMessage> getResponseMap() {
        return responseMap;
    }

    public synchronized Boolean isMapCountainsRequest(RequestMessage requestMessage) {
        boolean                                         exist    = false;
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
}



