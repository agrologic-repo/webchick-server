
/*
* To change this template, choose Tools | Templates
* and open the template in the editor.
 */
package com.agrologic.app.messaging;

/**
 * Title: MessageType <br>
 * Description: <br>
 * Copyright:   Copyright (c) 2009 <br>
 * Company:     Agro Logic LTD. <br>
 *
 * @author Valery Manakhimov <br>
 * @version 1.0 <br>
 */
public enum MessageType {

    FAKE_MESSAGE,

    TEST_MESSAGE,

    /**
     * A keep alive message.
     */
    KEEP_ALIVE,

    /**
     * A login message.
     */
    LOGIN,

    /**
     * An error message
     */
    ERROR,

    /**
     *
     */
    TIME_OUT_ERROR,

    /**
     * An acknowledge
     */
    ACK,

    /**
     * A request the value of a panel data item.
     * The format is  "Controller Type + Net Name + a + ' ' + CheckSum"
     * For Example "T901a CHS\CR\NL".
     */
    REQUEST_PANEL,

    /**
     * A request the value of a controller data item.
     * The format is  "Controller Type + Net Name + b + ' ' + CheckSum"
     * For Example "T901b CHS\CR\NL".
     */
    REQUEST_CONTROLLER,

    /**
     * A request the value of a chick scale  data item.
     * The format is  "Controller Type + Net Name + c + ' ' + CheckSum"
     * For Example "T901c CHS\CR\NL".
     */
    REQUEST_CHICK_SCALE,

    /**
     * A request the value of a egg counter  data item.
     * The format is  "Controller Type + Net Name + d + ' ' + CheckSum"
     * For Example "T901d CHS\CR\NL".
     */
    REQUEST_EGG_COUNT,

    /**
     * A request the the value of daily graph.
     * The format is  "Controller Type + Net Name + * + ' ' + CheckSum"
     * For Example "T901* CHS\CR\NL".
     */
    REQUEST_GRAPHS,

    /**
     * A request the the value of daily histogram.
     * The format is  "Controller Type + Net Name + H + ' ' + CheckSum"
     * For Example "TA01H CHS\CR\NL". A,B,C,D
     */
    REQUEST_HISTOGRAM,

    /**
     * A request the the value of daily graph.
     * The format is  "Controller Type + Net Name + h + ' ' + 'GrawDay' + ' ' + CheckSum"
     * For Example "T901h 12 CHS\CR\NL".
     */
    REQUEST_HISTORY,

    /**
     * A request the the value of daily graph.
     * The format is  "Controller Type + Net Name + DN+ ' ' + 'GrawDay' + ' ' + CheckSum"
     * For Example "T901D1 12 CHS\CR\NL".
     */
    REQUEST_PER_HOUR_REPORTS,

    /**
     * A request the the value of daily histogram.
     * The format is  "Controller Type + Net Name + H ' ' + GrowDay + ' ' + CheckSum"
     * For Example "TA01H 1 CHS\CR\NL". A,B,C,D - plate,  1 - grow day
     */
    REQUEST_HISTORY_HISTOGRAM,

    /**
     * A request to write the value of a data item.
     * The format is  "Controller Type + Net Name + W + 'DATAID ' + ' ' + 'Value' + ' ' + CheckSum"
     * For Example "T901W4096 250 CHS\CR\NL".
            */
    REQUEST_TO_WRITE,

    /**
     * A response message.
     */
    RESPONSE_DATA,

    /**
     * A skip response message.
     */
    SKIP_UNUSED_RESPONSE,

    /**
     * Skip response to write data if error occurs
     */
    SKIP_RESPONSE_TO_WRITE,

    /**
     * A request the changed data .
     * The format is  "Controller Type + Net Name + f + ' ' + CheckSum"
     * For Example "T901f CHS\CR\NL".
     */
    REQUEST_CHANGED
}



