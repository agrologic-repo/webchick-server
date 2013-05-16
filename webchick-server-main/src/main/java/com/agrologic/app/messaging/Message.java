package com.agrologic.app.messaging;

public interface Message {

    /**
     * Acknowledge - 6
     */
    static final byte ACK = 6;

    /**
     * check sum error
     */
    static final int CHS_ERROR = 30;

    /**
     * skip request error
     */
    static final int CON_ERROR = 40;

    /**
     * End of transmission - 4
     */
    static final byte EOT = 4;

    /**
     * end of transmission error message
     */
    static final int EOT_ERROR = 20;

    /**
     * Error flag - 25
     */
    static final byte ERROR = 25;

    /**
     * End of text - 3
     */
    static final byte ETX = 3;

    /**
     * Indicate last received buffer - 2
     */
    static final byte LAST_COMPRSSED_TEXT_MESSAGE = 2;

    /**
     * Indicate last received binary buffer with index in response - 4
     */
    static final byte LAST_COMPRSSED_WITH_IND_BINARY_MESSAGE = 4;

    /**
     * Indicate last received buffer with index in response - 3
     */
    static final byte LAST_COMPRSS_WITH_IND_TEXT_MESSAGE = 3;

    /**
     * Indicate last received buffer - 1
     */
    static final byte LAST_TEXT_MESSAGE = 1;

    /**
     * Negative acknowledge - 21
     */
    static final byte NACK = 21;

    /**
     * Indicate first or middle received buffer - 0
     */
    static final int  OK               = 0;

    /**
     * skip request error
     */
    static final int REQ_ERROR = 50;

    /**
     * Record separator - 30
     */
    static final byte RS = 30;

    /**
     * skip request error
     */
    static final int SKP_ERROR = 60;

    /**
     * Start of index - 24
     */
    static final byte SOINDX = 24;

    /**
     * Start of transmission - 22
     */
    static final byte SOT = 22;

    /**
     * start of transmission error
     */
    static final int SOT_ERROR = 10;

    /**
     * White space -32
     */
    static final byte SPACE = 32;

    /**
     * Start of text - 2
     */
    static final byte STX = 2;

    /**
     * timeout error
     */
    static final int TMO_ERROR = 80;

    /**
     * unknown error
     */
    static final int UNW_ERROR = 70;

    /**
     * Return message buffer.
     *
     * @return the message buffer
     */
    byte[] getBuffer();

    /**
     * Set message buffer
     *
     * @param buffer the message buffer
     */
    void setBuffer(byte[] buffer);

    /**
     * Return message type
     *
     * @return the message type
     */
    MessageType getMessageType();

    /**
     * Set message type
     *
     * @param type the message type
     */
    void setMessageType(MessageType type);

    /**
     * Return message index
     *
     * @return the message index
     */
    String getIndex();

    /**
     * Set message index
     *
     * @param index the message index
     */
    void setIndex(String index);

    @Override
    String toString();
}



