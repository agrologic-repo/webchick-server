
/*
* To change this template, choose Tools | Templates
* and open the template in the editor.
 */
package com.agrologic.app.messaging;

/**
 *
 * @author JanL
 */
public enum MessagePriority {

    /** highest priority {changing data on controllers} */
    URGENT,

    /** high priority {almost for all request} */
    HIGH,

    /** medium priority {not in use} */
    MEDIUM,

    /** low priority {request changed data} */
    LOW
}



