
package com.agrologic.app.messaging;

import com.agrologic.app.except.ReadChecksumException;
import com.agrologic.app.network.ReadBuffer;
import com.agrologic.app.util.ByteUtil;
import org.apache.log4j.Logger;

public final class ResponseMessage implements Message {
    /**
     * Message buffer
     */
    private byte[] buffer;
    /**
     * Tells which format controller sent the requested message binary or text (ascii)
     */
    private Format format;
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

//    private int errorCode = 0;

    private ErrorCodes errorCodes = ErrorCodes.OK;

    private Logger logger = Logger.getLogger(ResponseMessage.class);

    public ResponseMessage() {
        this(null);
    }

    public ResponseMessage(byte[] buffer) {
        this(buffer, Format.TEXT);
    }

    public ResponseMessage(byte[] buffer, Format format) {
        if (buffer == null) {
            this.messageType = MessageType.ERROR;
            this.format = Format.TEXT;
            recvCHS = -1;
            calcCHS = -1;
        } else {
            this.messageType = MessageType.RESPONSE_DATA;
            this.buffer = buffer;
            this.format = format;
            parsingReceiveBuffer();
        }
    }

    public void parsingReceiveBuffer(ReadBuffer readBuffer) {
        this.messageType = MessageType.RESPONSE_DATA;
        this.setBuffer(readBuffer.toArray());
        this.setFormat(readBuffer.getBufferFormat());
        this.parsingReceiveBuffer();
    }

    private void parsingReceiveBuffer() {
        try {
            byte[] tempBuffer = getBuffer().clone();
            int eotIndex = ByteUtil.lastIndexOf(tempBuffer, ProtocolBytes.EOT.getValue());

            String receivedString = new String(tempBuffer, 0, eotIndex + 1);
            logger.debug(receivedString);
            int soi = receivedString.indexOf(ProtocolBytes.SOINDX.getValue());     // soi - start of index index
            int sot = receivedString.indexOf(ProtocolBytes.SOT.getValue());        // sot - start of transmission index
            int eot = receivedString.lastIndexOf(ProtocolBytes.EOT.getValue());    // end - end of transmission index

            // binary message does not have checksum
            if (getFormat() == Format.BINARY) {
                // get begin data and end data without checksum
                // controller responded data without request index
                if ((soi == -1) && (sot == -1)) {
                    index = "100";
                    messageBody = receivedString;
                    setMessageType(MessageType.ERROR);
                    setErrorCodes(ErrorCodes.SKP_ERROR);
                } else {
                    index = receivedString.substring(soi + 1, sot);
                    messageBody = receivedString.substring(sot + 1, eot);
                    // if controller does not responded for some reson
                    // 4 0 0 8 (soi)24 0 (sot) 22 -1 0 7f 4
                    if (index.equals("0")) {
                        if (messageBody.equals("-1 ")) {
                            setMessageType(MessageType.ERROR);
                            setErrorCodes(ErrorCodes.SKP_ERROR);
                            return;
                        } else if (messageBody.equals("-2 ")) {
                            setMessageType(MessageType.ERROR);
                            setErrorCodes(ErrorCodes.CON_ERROR);
                            return;
                        }
                    } else {
                        if (messageBody.equals("-1 -1")) {
                            setMessageType(MessageType.SKIP_UNUSED_RESPONSE);
                            setErrorCodes(ErrorCodes.REQ_ERROR);
                            return;
                        } else if (messageBody.equals("-1 -2")) {
                            setMessageType(MessageType.ERROR);
                            setErrorCodes(ErrorCodes.CON_ERROR);
                            return;
                        }
                    }
                }
            } else {
                // get begin data and end data without checksum
                int space = receivedString.lastIndexOf(ProtocolBytes.SPACE.getValue());// space before check sum
                // controller responded data without request index
                if ((soi == -1) && (sot == -1)) {
                    sot = 0;
                    index = "100";
                    messageBody = receivedString.substring(sot, space + 1);
                    if ((eot == -1) || messageBody.equals("-1 ")) {
                        setMessageType(MessageType.ERROR);
                        setErrorCodes(ErrorCodes.SKP_ERROR);
                        return;
                    }
                } else {
                    index = receivedString.substring(soi + 1, sot);
                    messageBody = receivedString.substring(sot + 1, space + 1);
                    // if controller does not responded for some reason
                    // 3 0 0 8 (soi)24 0 (sot) 22 -1 0 7f 4
                    if (index.equals("0")) {
                        if (messageBody.equals("-1 ")) {
                            setMessageType(MessageType.SKIP_UNUSED_RESPONSE);
                            setErrorCodes(ErrorCodes.REQ_ERROR);
                            return;
                        } else if (messageBody.equals("-2 ")) {
                            setMessageType(MessageType.ERROR);
                            setErrorCodes(ErrorCodes.CON_ERROR);
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
                    setErrorCodes(ErrorCodes.CHS_ERROR);
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

        Integer.parseInt("1");
        // set parsed data
        setBuffer(messageBody.getBytes());
    }

    private void setFormat(byte bufferFormat) {
        format = LastIndicators.getIndicator(bufferFormat);
    }

    private Format getFormat() {
        return format;
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
            setErrorCodes(ErrorCodes.REQ_ERROR);
            return true;
        }

        return false;
    }

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

    public ErrorCodes getErrorCodes() {
        return errorCodes;
    }

    public void setErrorCodes(ErrorCodes errorCodes) {
        this.errorCodes = errorCodes;
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
        if (errorCodes.toString().equals("")) {
            return messageBody;
        } else {
            return errorCodes.toString();
        }
    }
}



