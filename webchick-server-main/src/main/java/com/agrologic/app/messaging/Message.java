package com.agrologic.app.messaging;

public interface Message {

    public enum ProtocolBytes {

        ACK((byte) 6),
        NACK((byte) 21),
        SOT((byte) 22),
        EOT((byte) 4),
        STX((byte) 2),
        ETX((byte) 3),
        ERROR((byte) 25),
        SOINDX((byte) 24),
        SPACE((byte) 32),
        RS((byte) 30);


        ProtocolBytes(byte value) {
            this.value = value;
        }

        public byte getValue() {
            return value;
        }

        private final byte value;
    }

    public enum LastIndicators {

        TEXT((byte) 1),
        COMPRSSED_TEXT((byte) 2),
        COMPRSSED_TEXT_WITH_INDEX((byte) 3),
        COMPRSSED_BINARY_WITH_INDEX((byte) 4);

        LastIndicators(byte value) {
            this.value = value;
        }

        public byte getValue() {
            return value;
        }

        public static Format getIndicator(byte val) {
            switch (val) {
                default:
                case 1:
                case 2:
                case 3:
                    return Format.TEXT;
                case 4:
                    return Format.BINARY;
            }
        }

        private final byte value;
    }

    public enum ErrorCodes {

        /**
         * start of transmission error
         */
        SOT_ERROR((byte) 10),
        /**
         * end of transmission error message
         */
        EOT_ERROR((byte) 20),
        /**
         * check sum error
         */
        CHS_ERROR((byte) 30),
        /**
         * skip request error
         */
        CON_ERROR((byte) 40),
        /**
         * Indicate first or middle received buffer - 0
         */
        OK((byte) 0),

        /**
         * skip request error
         */
        REQ_ERROR((byte) 50),

        /**
         * skip request error
         */
        SKP_ERROR((byte) 60),

        /**
         * timeout error
         */
        TIME_OUT_ERROR((byte) 80),

        /**
         * unknown error
         */
        UNW_ERROR((byte) 70);

        ErrorCodes(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }


        public String toString() {
            switch (this) {
                case SOT_ERROR:
                    return "SOT Error";

                case EOT_ERROR:
                    return "EOT Error";

                case CHS_ERROR:
                    return "Checksum Error";

                case SKP_ERROR:
                    return "Skip Response Error";

                case UNW_ERROR:
                    return "Unknown Error";

                case TIME_OUT_ERROR:
                    return "Timeout Error";

                case CON_ERROR:
                    return "Connection to Controller Error";

                case REQ_ERROR:
                    return "Request Error";

                default:
                    return "";
            }
        }

        private int value;
    }

    public enum Format {
        BINARY, TEXT
    }
//    /**
//     * Acknowledge - 6
//     */
//    static final byte ACK = 6;
//
//    /**
//     * End of transmission - 4
//     */
//    static final byte EOT = 4;
//
//    /**
//     * Error flag - 25
//     */
//    static final byte ERROR = 25;
//
//    /**
//     * End of text - 3
//     */
//    static final byte ETX = 3;
//
//    /**
//     * Indicate last received buffer - 2
//     */
//    static final byte LAST_COMPRSSED_TEXT_MESSAGE = 2;
//
//    /**
//     * Indicate last received binary buffer with index in response - 4
//     */
//    static final byte LAST_COMPRSSED_WITH_IND_BINARY_MESSAGE = 4;
//
//    /**
//     * Indicate last received buffer with index in response - 3
//     */
//    static final byte LAST_COMPRSS_WITH_IND_TEXT_MESSAGE = 3;
//
//    /**
//     * Indicate last received buffer - 1
//     */
//    static final byte LAST_TEXT_MESSAGE = 1;
//
//    /**
//     * Negative acknowledge - 21
//     */
//    static final byte NACK = 21;
//
//    /**
//     * Record separator - 30
//     */
//    static final byte RS = 30;
//
//    /**
//     * Start of index - 24
//     */
//    static final byte SOINDX = 24;
//
//    /**
//     * Start of transmission - 22
//     */
//    static final byte SOT = 22;
//
//    /**
//     * White space -32
//     */
//    static final byte SPACE = 32;
//
//    /**
//     * Start of text - 2
//     */
//    static final byte STX = 2;
//
//    /**
//     * start of transmission error
//     */
//    static final int SOT_ERROR = 10;
//
//    /**
//     * check sum error
//     */
//    static final int CHS_ERROR = 30;
//
//    /**
//     * skip request error
//     */
//    static final int CON_ERROR = 40;
//
//    /**
//     * end of transmission error message
//     */
//    static final int EOT_ERROR = 20;
//
//    /**
//     * Indicate first or middle received buffer - 0
//     */
//    static final int OK       = 0;
//
//    /**
//     * skip request error
//     */
//    static final int REQ_ERROR = 50;
//
//    /**
//     * skip request error
//     */
//    static final int SKP_ERROR = 60;
//
//    /**
//     * timeout error
//     */
//    static final int TIME_OUT_ERROR = 80;
//
//    /**
//     * unknown error
//     */
//    static final int UNW_ERROR = 70;

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



