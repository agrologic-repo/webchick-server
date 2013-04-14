
/*
* To change this template, choose Tools | Templates
* and open the template in the editor.
 */
package com.agrologic.app.network;

/**
 * Title: ServerNetState <br>
 * Description: <br>
 * Copyright:   Copyright (c) 2009 <br>
 * Company:     AgroLogic LTD. <br>
 * @author      Valery Manakhimov <br>
 * @version     1.1 <br>
 */
public enum NetworkState {

    /** waiting keep alive */
    WAIT_KEEP_ALIVE, STATE_ACCEPT_KEEP_ALIVE, STATE_KEEP_ALIVE_TIMEOUT, STATE_STARTING, STATE_SEND, STATE_BUSY,
                     STATE_TIMEOUT, STATE_DELAY, STATE_STOP, STATE_RESTART, STATE_ABORT, STATE_IDLE, STATE_READ,
                     STATE_ERROR
}


//~ Formatted by Jindent --- http://www.jindent.com
