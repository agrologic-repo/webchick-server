
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.agrologic.app.messaging;

import com.agrologic.app.except.ReadChecksumException;
import com.agrologic.app.util.ByteUtil;
import org.apache.log4j.Logger;

public final class ResponseMessage implements Message {
    /**
     * Message buffer
     */
    private byte[] buffer;
    /**
     * Tells which format controller sent the requested message
     */
    private byte bufferType;
    /**
     * Calculated checksum receiving message
     */
    private int calcCHS;
    /**
     * Check sum that controller sent in request message
     */
    private int recvCHS;
    /**
     * Response index
     */
    private String index;
    /**
     * The body of message without head and check sum
     */
    private String messageBody;
    /**
     * The message type
     */
    private MessageType messageType;

    private int errorCode = 0;

    private Logger logger = Logger.getLogger(ResponseMessage.class);

    /**
     * Constructor
     */
    public ResponseMessage() {
        this(null);
    }

    /**
     * Constructor
     *
     * @param buffer
     */
    public ResponseMessage(byte[] buffer) {
        if (buffer == null) {
            this.messageType = MessageType.ERROR;
            this.errorCode = SOT_ERROR;
            recvCHS = -1;
            calcCHS = -1;
        } else {
            this.messageType = MessageType.RESPONSE_DATA;
            this.errorCode = 0;
            this.buffer = buffer;
            init();
        }
    }

//    /**
//     * Constructor
//     *
//     * @param error
//     */
//    public ResponseMessage(int error) {
//        this.messageType = MessageType.ERROR;
//        errorCode = error;
//    }

    /**
     * @param buffer
     * @param bufferType
     */
    public void init(byte[] buffer, final byte bufferType) {
        this.messageType = MessageType.RESPONSE_DATA;
        this.errorCode = 0;
        this.bufferType = bufferType;
        setBuffer(buffer);
        init();
    }

    /**
     *
     */
    private void init() {
        try {
            byte[] tempBuffer = getBuffer().clone();
//            int soiIndex = ByteUtil.lastIndexOf(tempBuffer, Message.SOINDX);
//            int sotIndex = ByteUtil.lastIndexOf(tempBuffer, Message.SOT);
            int eotIndex = ByteUtil.lastIndexOf(tempBuffer, Message.EOT);

            String receivedString = new String(tempBuffer, 0, eotIndex + 1);
            logger.debug(receivedString);
            int soi = receivedString.indexOf(Message.SOINDX);     // sot of transmission index
            int sot = receivedString.indexOf(Message.SOT);        // sot of transmission index
            int eot = receivedString.lastIndexOf(Message.EOT);    // end of transmission index

            // binary message does not have checksum
            if (bufferType == Message.LAST_COMPRSSED_WITH_IND_BINARY_MESSAGE) {
                // get begin data and end data without checksum
                if ((soi == -1) && (sot == -1)) {
                    index = "100";
                    messageBody = "-1 ";
                    setMessageType(MessageType.ERROR);
                    setErrorCode(SKP_ERROR);
                } else {
                    index = receivedString.substring(soi + 1, sot);
                    messageBody = receivedString.substring(sot + 1, eot);

                    if (index.equals("0")) {
                        if (messageBody.equals("-1 ")) {
                            setMessageType(MessageType.ERROR);
                            setErrorCode(SKP_ERROR);
                            return;
                        } else if (messageBody.equals("-2 ")) {
                            setMessageType(MessageType.ERROR);
                            setErrorCode(CON_ERROR);
                            return;
                        }
                    } else {
                        if (messageBody.equals("-1 -1")) {
                            setMessageType(MessageType.SKIP_UNUSED_RESPONSE);
                            setErrorCode(REQ_ERROR);
                            return;
                        } else if (messageBody.equals("-1 -2")) {
                            setMessageType(MessageType.ERROR);
                            setErrorCode(CON_ERROR);
                            return;
                        }
                    }
                }
            } else {
                // get begin data and end data without checksum
                int space = receivedString.lastIndexOf(Message.SPACE);// space before check sum
                if ((soi < 0) && (sot < 0)) {
                    sot = 0;
                    index = "100";
                    messageBody = receivedString.substring(sot, space + 1);
                    if ((eot == -1) || messageBody.equals("-1 ")) {
                        setMessageType(MessageType.ERROR);
                        setErrorCode(SKP_ERROR);
                        return;
                    }
                } else {
                    index = receivedString.substring(soi + 1, sot);
                    messageBody = receivedString.substring(sot + 1, space + 1);
                    if (index.equals("0")) {
                        if (messageBody.equals("-1 ")) {
                            setMessageType(MessageType.SKIP_UNUSED_RESPONSE);
                            setErrorCode(REQ_ERROR);
                            return;
                        } else if (messageBody.equals("-2 ")) {
                            setMessageType(MessageType.ERROR);
                            setErrorCode(CON_ERROR);

                            return;
                        }
                    }
                }
                calcCHS = calcChecksum(buffer, sot + 1, space);
                recvCHS = readChecksum(receivedString, space + 1, eot);
                logger.debug("Calculated checksum { " + calcCHS + " } ");
                logger.debug("Received   checksum { " + recvCHS + " }");

                if (!checksumWithOverFlowErrorCorrect(buffer, calcCHS, recvCHS, sot, space)) {
                    setMessageType(MessageType.ERROR);
                    setErrorCode(CHS_ERROR);
                    logger.error("Checksum error detected. Calculated checksum: { " + calcCHS + " } ," +
                            " Received checksum { " + recvCHS + " } .");
                    return;
                }

                if (isUnusedResponse()) {
                    logger.info("This response message was defined as unused.");
                    return;
                }
            }
        } catch (Exception e) {
            logger.error("Response message initialisation error ", e);
            return;
        }
        // set parsed data
        setBuffer(messageBody.getBytes());
    }

    /**
     * Response that indicate that the requested data does not exist . When response message defined as unused , this
     * means that it no longer will be sent this request to end point.
     *
     * @return true if response should be ignored , otherwise return false
     */
    private boolean isUnusedResponse() {
        if (messageBody.startsWith("-1 ")) {
            setMessageType(MessageType.SKIP_UNUSED_RESPONSE);
            setErrorCode(REQ_ERROR);
            return true;
        }

        return false;
    }

    /**
     * Constructor
     *
     * @param receivedString
     * @param start
     * @param end
     * @return
     */
    private int readChecksum(final String receivedString, final int start, final int end) {
        if (start < 0) {
            throw new IllegalArgumentException("Negative SOT index");
        }

        if (end > receivedString.length()) {
            throw new IllegalArgumentException("EOT index size  error : " + end);
        }

        if (start > end) {
            throw new IllegalArgumentException("SOT index " + start + " bigger than EOT index " + end);
        }

        try {
            return Integer.parseInt(receivedString.substring(start, end));
        } catch (ReadChecksumException ex) {
            throw new ReadChecksumException(receivedString.substring(start, end));
        }
    }

    /**
     * @param buffer
     * @param sot
     * @param len
     * @return
     */
    public static int calcChecksum(final byte[] buffer, int sot, int len) {
        int checksum = 0;

        for (int i = sot; i < len + 1; i++) {
            checksum += buffer[i];
        }

        while ((checksum - 256) >= 0) {
            checksum -= 256;
        }

        return checksum;
    }

    /**
     * Constructor
     *
     * @param buffer
     * @param calcCHS
     * @param recvCHS
     * @param sot
     * @return
     */
    public static boolean checksumWithOverFlowErrorCorrect(byte[] buffer, int calcCHS, int recvCHS, int sot, int length) {
        if (calcCHS != recvCHS) {
            calcCHS = overFlowErrorChecksum(buffer, sot + 1, length);
            if (calcCHS != recvCHS) {
                return false;
            }
        }
        return true;
    }

    public static int overFlowErrorChecksum(final byte[] buffer, int sot, int len) {
        int checksum = 0;

        for (int i = sot; i <= len; i++) {
            if (((i + 3) <= buffer.length) && (buffer[i] == '-') && (buffer[i + 1] == '1' && (buffer[i + 2] == ' '))) {
                checksum += '6';
                checksum += '5';
                checksum += '5';
                checksum += '3';
                checksum += '5';
                i++;
            } else {
                checksum += buffer[i];
            }
        }

        while ((checksum - 256) >= 0) {
            checksum -= 256;
        }

        return checksum;
    }

    @Override
    public String getIndex() {
        if (index == null) {
            return "00";
        }
        index = index.trim();

        if ((index.length() < 2) && (index.length() > 0)) {
            return "0" + index;
        }

        return index;
    }

    @Override
    public void setIndex(String index) {
        this.index = index;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public byte[] getBuffer() {
        return buffer;
    }

    public void setBuffer(byte[] buffer) {
        this.buffer = buffer;
    }

    public MessageType getMessageType() {
        return messageType;
    }

    public void setMessageType(MessageType type) {
        this.messageType = type;
    }

    @Override
    public String toString() {
        switch (errorCode) {
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

            case TMO_ERROR:
                return "Timeout Error";

            case CON_ERROR:
                return "Connection to Controller Error";

            case REQ_ERROR:
                return "Request Error";

            default:
                return messageBody;
        }
    }
}



