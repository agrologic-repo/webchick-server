package com.agrologic.app.messaging;

import org.apache.commons.lang.Validate;

import java.util.Arrays;

/**
 * RequestMessage using to create request message for controllers
 *
 * @author Valery Manakhimov
 * @version 1.0 <br>
 */
public class RequestMessage implements Message, Comparable<RequestMessage> {

    private String reqIndex;
    /**
     * message body
     */
    private byte[] buffer;
    /**
     * message type
     */
    private MessageType messageType;
    /**
     * priority message
     */
    private Priority priority;
    /**
     * net name
     */
    private String netName;
    /**
     * dataType using in change value message
     */
    private Long dataType;
    /**
     * value using in change value message
     */
    private Long value;
    /**
     * value using in management request
     */
    private Integer growDay;
    /**
     * histogram plate
     */
    private String plate;
    /**
     * using management
     */
    private String dnum;
    /**
     * keep alive string
     */
    private String keepAlive;

    public RequestMessage(final MessageType type) {
        this.messageType = type;
        initialize();
    }

    public RequestMessage(final MessageType type, final int keepAlive) {
        this.messageType = type;
        this.keepAlive = Integer.toString(keepAlive);
        initialize();
    }

    public RequestMessage(final MessageType type, final String netName) {
        this.netName = netName;
        this.messageType = type;
        initialize();
    }

    public RequestMessage(final MessageType type, final String netName, final Integer growDay) {
        this.netName = netName;
        this.messageType = type;
        this.growDay = growDay;
        initialize();
    }

    public RequestMessage(final MessageType type, final String netName, Integer growDay, String dnum) {
        this.netName = netName;
        this.messageType = type;
        this.growDay = growDay;
        this.dnum = dnum;
        initialize();
    }

    public RequestMessage(final MessageType type, final String netName, Long dataType, Long value) {
        this.netName = netName;
        this.dataType = dataType;
        this.value = value;
        this.messageType = type;
        initialize();
    }

    /**
     * This method call abstract method create().Method create() implements in child class .
     */
    private void initialize() {
        switch (this.messageType) {
            case REQUEST_PANEL:
            case REQUEST_CONTROLLER:
            case REQUEST_EGG_COUNT:
            case REQUEST_CHICK_SCALE:
            case REQUEST_HISTOGRAM:
                priority = Priority.HIGH;
                break;
            case REQUEST_TO_WRITE:
                priority = Priority.URGENT;
                break;
            case REQUEST_CHANGED:
                priority = Priority.LOW;
                break;
            default:
                priority = Priority.MEDIUM;
                break;
        }

        StringBuilder messageString = new StringBuilder();
        switch (getMessageType()) {
            case ACK:
                setBuffer(new byte[]{ProtocolBytes.SOT.getValue(), ProtocolBytes.ACK.getValue(), '\r'});
                break;
            case ERROR:
                setBuffer(new byte[]{ProtocolBytes.SOT.getValue(), ProtocolBytes.NACK.getValue(), '\r'});
                break;
            case KEEP_ALIVE:
                byte[] ka = getKeepAlive().getBytes();
                setBuffer(new byte[]{ProtocolBytes.SOT.getValue(), ka[0], '\r'});
                break;

            case TEST_MESSAGE:
                messageString.append("%").append(getNetName()).append("R4096 100").append(" ").append(calcCheckSumToSend(messageString.toString().getBytes())).append("\r");
                setBuffer(messageString.toString().getBytes());
                break;

            case REQUEST_PANEL:
                messageString.append("%").append(getNetName()).append("a ").append(calcCheckSumToSend(messageString.toString().getBytes())).append("\r");
                setBuffer(messageString.toString().getBytes());
                break;

            case REQUEST_CONTROLLER:
                messageString.append("%").append(getNetName()).append("b ").append(calcCheckSumToSend(messageString.toString().getBytes())).append("\r");
                setBuffer(messageString.toString().getBytes());
                break;

            case REQUEST_CHICK_SCALE:
                messageString.append("%").append(getNetName()).append("c ").append(calcCheckSumToSend(messageString.toString().getBytes())).append("\r");
                setBuffer(messageString.toString().getBytes());
                break;

            case REQUEST_EGG_COUNT:
                messageString.append("%").append(getNetName()).append("d ").append(calcCheckSumToSend(messageString.toString().getBytes())).append("\r");
                setBuffer(messageString.toString().getBytes());
                break;
            case REQUEST_CHANGED:
                messageString.append("%").append(getNetName()).append("f ").append(calcCheckSumToSend(messageString.toString().getBytes())).append("\r");
                setBuffer(messageString.toString().getBytes());
                break;
            case REQUEST_GRAPHS:
                messageString.append("%").append(getNetName()).append("* ").append(calcCheckSumToSend(messageString.toString().getBytes())).append("\r");
                setBuffer(messageString.toString().getBytes());
                break;
            case REQUEST_TO_WRITE:
                messageString.append("%").append(getNetName()).append("W").append(getDataIdWithFormat()).append(" ").append(getValue()).append(" ").append(calcCheckSumToSend(messageString.toString().getBytes())).append("\r");
                setBuffer(messageString.toString().getBytes());
                break;
            case REQUEST_HISTORY:
                messageString.append("%").append(getNetName()).append("h").append(getGrowDay()).append(" ").append(calcCheckSumToSend(messageString.toString().getBytes())).append("\r");
                setBuffer(messageString.toString().getBytes());
                break;
            case REQUEST_HISTORY_24_HOUR:
                messageString.append("%").append(getNetName()).append(getDnum()).append(" ").append(getGrowDay()).append(" ").append(calcCheckSumToSend(messageString.toString().getBytes())).append("\r");
                setBuffer(messageString.toString().getBytes());
                break;
            case REQUEST_HISTOGRAM:
                messageString.append("%").append(getNetName().replace("9", plate)).append("H").append(" ").append(calcCheckSumToSend(messageString.toString().getBytes())).append("\r");
                setBuffer(messageString.toString().getBytes());
                break;
            case REQUEST_HISTORY_HISTOGRAM:
                messageString.append("%").append(getNetName().replace("9", plate)).append("H").append(getGrowDay()).append(" ").append(calcCheckSumToSend(messageString.toString().getBytes())).append("\r");
                setBuffer(messageString.toString().getBytes());
                break;
            default:
                setBuffer(new byte[]{ProtocolBytes.SOT.getValue(), ProtocolBytes.NACK.getValue(), '\r'});
                break;
        }
    }

    /**
     * Return calculated checksum for buffer to send
     *
     * @param buffer buffer to send
     * @return checksum the check sum
     */
    public static int calcCheckSumToSend(final byte[] buffer) throws NullPointerException {
        Validate.notNull(buffer, "buffer can not be null");

        int checksum = 0;
        for (int i = 0; i < buffer.length; i++) {
            checksum += buffer[i];
        }
        checksum -= '%';
        while ((checksum - 256) >= 0) {
            checksum -= 256;
        }
        return checksum;
    }

    /**
     * Return net name of message.
     *
     * @return netName the net name of message
     */
    public String getNetName() {
        if (netName == null || netName.equals("")) {
            return "T901";
        } else {
            return netName;
        }
    }

    /**
     * The grow day for management request
     *
     * @return growDay the grow day
     */
    public Integer getGrowDay() {
        return growDay;
    }

    /**
     * Return request message index .
     *
     * @return reqIndex the request message index .
     */
    public String getIndex() {
        return reqIndex;
    }

    /**
     * Set request message index
     *
     * @param reqIndex the request index
     */
    public void setIndex(String reqIndex) {
        this.reqIndex = reqIndex;
    }

    /**
     * Return data id with data format . If data id is 0 then data id with type 4096
     *
     * @return datatype the data id with format
     */
    public Long getDataIdWithFormat() {
        return dataType;
    }

    /**
     * Return data id .
     *
     * @return data id
     */
    public Long getDataId() {
        if ((dataType & 0xC000) != 0xC000) {
            return (dataType & 0xFFF); // remove type to get an index 4096&0xFFF -> 0
        } else {
            return (dataType & 0xFFFF);// remove type to get an index -16490&0xFFFF -> 49046
        }
    }

    /**
     * Return the value of request message if request was to change data value.
     *
     * @return value the value
     */
    public Long getValue() {
        return value;
    }

    /**
     * Set the request value
     *
     * @param value the value
     */
    public final void setValue(Long value) {
        this.value = value;
    }

    /**
     * Return keep alive value
     *
     * @return keepAlive the keepAlive
     */
    public String getKeepAlive() {
        return keepAlive;
    }

    /**
     * Set keepAlive value
     *
     * @param keepAlive the keepAlive
     */
    public void setKeepAlive(String keepAlive) {
        this.keepAlive = keepAlive;
    }

    /**
     * Return the number for request management 24 hour data
     *
     * @return dnum the number of management data
     */
    public String getDnum() {
        return dnum;
    }

    /**
     * Return plate name of chick scale
     *
     * @return plate name
     */
    public String getPlate() {
        return plate;
    }

    /**
     * Override method return message buffer
     *
     * @return buffer the message buffer
     */
    public synchronized byte[] getBuffer() {
        return buffer;
    }

    /**
     * Override method set message buffer
     *
     * @param buffer the buffer to set message buffer.
     */
    @Override
    public synchronized void setBuffer(final byte[] buffer) {
        this.buffer = buffer;
    }

    /**
     * Return the message type.
     *
     * @return type the message type.
     */
    @Override
    public synchronized MessageType getMessageType() {
        return messageType;
    }

    @Override
    public void setMessageType(MessageType type) {
        this.messageType = type;
    }

    public boolean isUnusedType() {
        if (getMessageType() == MessageType.REQUEST_EGG_COUNT
                || getMessageType() == MessageType.REQUEST_CHICK_SCALE
                || getMessageType() == MessageType.REQUEST_CHANGED) {
            return true;
        }
        return false;
    }

    public Priority getPriority() {
        return priority;
    }

    public int compareTo(RequestMessage obj) {
        if (this.getPriority().compareTo(obj.getPriority()) == 0) {
            if (this.getNetName().compareTo(obj.getNetName()) == 0) {
                return this.getMessageType().compareTo(obj.getMessageType());

            } else {
                return this.getNetName().compareTo(obj.getNetName());
            }
        } else {
            return this.getPriority().compareTo(obj.getPriority());
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final RequestMessage other = (RequestMessage) obj;
        if (this.messageType != other.messageType) {
            return false;
        }
        if ((this.netName == null) ? (other.netName != null) : !this.netName.equals(other.netName)) {
            return false;
        }

        if (!Arrays.equals(buffer, other.buffer)) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 59 * hash + (this.messageType != null ? this.messageType.hashCode() : 0);
        hash = 59 * hash + (this.buffer != null ? Arrays.hashCode(this.buffer) : 0);
        return hash;
    }

    @Override
    public String toString() {
        String str = "";
        switch (this.messageType) {
            case REQUEST_PANEL:
            case REQUEST_CONTROLLER:
            case REQUEST_EGG_COUNT:
            case REQUEST_CHICK_SCALE:
            case REQUEST_TO_WRITE:
            case REQUEST_CHANGED:
            case REQUEST_GRAPHS:
            case REQUEST_HISTORY:
            case REQUEST_HISTOGRAM:
            case REQUEST_HISTORY_24_HOUR:
                str = new String(buffer, 0, buffer.length);
                break;
            case ACK:
                str = "acknowledge";
                break;
            case ERROR:
                str = "error";
                break;
            case KEEP_ALIVE:
                str = "keep alive : " + keepAlive;
                break;
            default:
                priority = Priority.LOW;
                str = "To String not Implemented for this type of RequestMessage";
                break;
        }
        return str;
    }

    public static enum Priority {

        /**
         * highest priority {changing data on controllers}
         */
        URGENT,

        /**
         * high priority {almost for all request}
         */
        HIGH,

        /**
         * medium priority {not in use}
         */
        MEDIUM,

        /**
         * low priority {request changed data}
         */
        LOW
    }
}
