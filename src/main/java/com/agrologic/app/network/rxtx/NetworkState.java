
/*
* To change this template, choose Tools | Templates
* and open the template in the editor.
 */
package com.agrologic.app.network.rxtx;

/**
 * Title: NetworkState <br> Decription: <br> Copyright: Copyright (c) 2009 <br>
 * Company: Agro Logic LTD. <br>
 *
 * @author Valery Manakhimov <br>
 * @version 1.1 <br>
 */
public enum NetworkState {
    STATE_IDLE, STATE_WAIT_KEEP_ALIVE, STATE_STARTING, STATE_SEND, STATE_READ, STATE_BUSY, STATE_TIMEOUT, STATE_ERROR,
    STATE_DELAY, STATE_STOP, STATE_ABORT,
}


//~ Formatted by Jindent --- http://www.jindent.com
