
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.agrologic.app.messaging;

//~--- non-JDK imports --------------------------------------------------------
import com.agrologic.app.except.ReadChecksumException;
import com.agrologic.app.network.MessageListener;
import java.util.List;
import org.apache.log4j.Logger;

/**
 * Title: ResponseMessage <br> Description: <br> Copyright: Copyright (c) 2009 <br> Company: AgroLogic LTD. <br>
 *
 * @author Valery Manakhimov <br>
 * @version 1.1 <br>
 */
public final class ResponseMessage implements Message {

    /**
     * Constant for a message type for binary messages (value = "binary").
     */
    public static final String BINARY_MESSAGE = "binary";
    /**
     * Constant for a message type for text messages (value = "text").
     */
    public static final String TEXT_MESSAGE = "text";
    private int errorCode = 0;
    private Logger logger = Logger.getLogger(ResponseMessage.class);
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
     * Response index
     */
    private String index;
    /**
     * The body of message without head and check sum
     */
    private String messageBody;

    private List<MessageListener> messageListeners;
    /**
     * The message type
     */
    private MessageType messageType;
    /**
     * Check sum that controller sent in request message
     */
    private int recvCHS;

    public ResponseMessage() {
    }

    public ResponseMessage(byte[] buffer) {
        if (buffer == null) {
            this.messageType = MessageType.ERROR;
            this.errorCode = SOT_ERROR;
            recvCHS = -1;
            calcCHS = -1;
        } else {
            this.messageType = MessageType.RESPONSE_DATA;
            this.buffer = buffer;
            init();
        }
    }

    public ResponseMessage(int error) {
        this.messageType = MessageType.ERROR;
        errorCode = error;
    }

    public ResponseMessage(byte[] buf, byte buftype) {
        if (buf == null) {
            this.messageType = MessageType.ERROR;
            this.errorCode = SOT_ERROR;
            recvCHS = -1;
            calcCHS = -1;
        } else {
            this.messageType = MessageType.RESPONSE_DATA;
            this.bufferType = buftype;
            setBuffer(buf);
            init();
        }
    }

    private void init() {
        try {
            if (bufferType == Message.LAST_COMPRSSED_WITH_IND_BINARY_MESSAGE) {
                byte[] tempBuffer = getBuffer();

                // get begin data and end data without checksum
                String recievedString = new String(tempBuffer, 0, tempBuffer.length);
                int soidx = recievedString.indexOf(Message.SOINDX);    // sot of transmision index
                int sot = recievedString.indexOf(Message.SOT);
                int eot = recievedString.lastIndexOf(Message.EOT);

                if ((soidx == -1) && (sot == -1)) {
                    sot = 0;
                    index = "100";
                    messageBody = "-1 ";
                    setMessageType(MessageType.ERROR);
                    setErrorCode(SKP_ERROR);
                } else {
                    index = recievedString.substring(soidx + 1, sot);
                    messageBody = recievedString.substring(sot + 1, eot);

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
                byte[] tempBuffer = getBuffer();

                // get begin data and end data without checksum
                String recievedString = new String(tempBuffer, 0, tempBuffer.length);
                int soidx = recievedString.indexOf(Message.SOINDX);       // sot of transmision index
                int sot = recievedString.indexOf(Message.SOT);          // sot of transmision index
                int eot = recievedString.lastIndexOf(Message.EOT);      // end of transmision index
                int space = recievedString.lastIndexOf(Message.SPACE);    // space before check sum

                if ((soidx < 0) && (sot < 0)) {
                    sot = 0;
                    index = "100";
                    messageBody = recievedString.substring(sot, space + 1);

                    if ((eot == -1) || messageBody.equals("-1 ")) {
                        setMessageType(MessageType.ERROR);
                        setErrorCode(SKP_ERROR);
                        return;
                    }

                    calcCHS = calcChecksum(buffer, sot + 1, space);
                    recvCHS = readChecksum(recievedString, space + 1, eot);
                } else {
                    index = recievedString.substring(soidx + 1, sot);
                    messageBody = recievedString.substring(sot + 1, space + 1);
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
                    calcCHS = calcChecksum(buffer, sot + 1, space);
                    recvCHS = readChecksum(recievedString, space + 1, eot);
                }
                if (checksumCorrect(sot, space)) {
                    return;
                }
                if (isUnusedResponse()) {
                    return;
                }
            }
        } catch (Exception e) {
            return;
        }

        setBuffer(messageBody.getBytes());
    }

    public void init(byte[] buf, final byte buftype) {
        if (buf == null) {
            this.messageType = MessageType.ERROR;
            this.errorCode = SOT_ERROR;
            recvCHS = -1;
            calcCHS = -1;
        } else {
            this.messageType = MessageType.RESPONSE_DATA;
            this.bufferType = buftype;
            setBuffer(buf);
            init();
        }
    }

    private boolean checksumCorrect(int sot, int space) {

        System.out.println(new String(buffer, 0, buffer.length));


        if (calcCHS != recvCHS) {
            calcCHS = overFlowErrorChecksum(buffer, sot + 1, space);
            if (calcCHS != recvCHS) {
                logger.info("calculated check sum = " + calcCHS + ", read check sum = " + recvCHS);
                setMessageType(MessageType.ERROR);
                setErrorCode(CHS_ERROR);
                return true;
            }
        }
        return false;
    }

    private boolean isUnusedResponse() {
        if (messageBody.startsWith("-1 ")) {
            setMessageType(MessageType.SKIP_UNUSED_RESPONSE);
            setErrorCode(REQ_ERROR);
            return true;
        }

        return false;
    }

    private int readChecksum(final String recievedString, final int start, final int end) {
        if (start < 0) {
            throw new IllegalArgumentException("Negative SOT index");
        }

        if (end > recievedString.length()) {
            throw new IllegalArgumentException("EOT index bugger than size of buffer ");
        }

        if (start > end) {
            throw new IllegalArgumentException("SOT index " + start + " bigger than EOT index " + end);
        }

        try {
            return Integer.parseInt(recievedString.substring(start, end));
        } catch (ReadChecksumException ex) {
            throw new ReadChecksumException(recievedString.substring(start, end));
        }
    }

    /**
     * Return calculated checksum for received buffer
     *
     * @param buffer received buffer
     * @return checksum the check sum
     */
    protected int calcCheckSum(final byte[] buffer) {
        return calcChecksum(buffer, 0, buffer.length);
    }

    private int calcChecksum(final byte[] buffer, int sot, int space) {
        int checksum = 0;

        for (int i = sot; i < space + 1; i++) {
            checksum += buffer[i];
        }

        while ((checksum - 256) >= 0) {
            checksum -= 256;
        }

        return checksum;
    }

    protected static int overFlowErrorChecksum(final byte[] buffer, int sot, int space) {
        int checksum = 0;

        for (int i = sot; i <= space; i++) {
            if (((i + 1) <= buffer.length) && (buffer[i] == '-') && (buffer[i + 1] == '1')) {
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

    public String getBody() {
        return messageBody;
    }

    public void setBody(String body) {
        this.messageBody = body;
    }

    @Override
    public String getIndex() {
        if(index == null) {
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

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public int getErrorCode() {
        return errorCode;
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



