
/*
* To change this template, choose Tools | Templates
* and open the template in the editor.
 */
package com.agrologic.app.network;

//~--- non-JDK imports --------------------------------------------------------

import com.agrologic.app.messaging.Message;

/**
 * A MessageListener object is used to receive asynchronously delivered messages.
 * Each session must insure that it passes messages serially to the listener.
 * This means that a listener assigned to one or more consumers of the same
 * session can assume that the onMessage method is not called with the next
 * message until the session has completed the last call.
 *
 * @author Valery Manakhimov
 */
public interface MessageListener {

    /**
     * Passes a message to the listener
     *
     * @param message - the message passed to the listener
     */
    void onMessage(Message message);
}


//~ Formatted by Jindent --- http://www.jindent.com
