
/*
 * SerialPortControl.java
 *
 * Created on July 29, 2008, 2:50 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package com.agrologic.app.network.rxtx;

//~--- non-JDK imports --------------------------------------------------------

import com.agrologic.app.config.Configuration;
import com.agrologic.app.config.Protocol;
import com.agrologic.app.exception.SerialPortControlFailure;
import com.agrologic.app.messaging.Message;
import com.agrologic.app.messaging.ResponseMessage;
import com.agrologic.app.network.CommControl;
import com.agrologic.app.util.ApplicationUtil;
import gnu.io.*;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Timestamp;
import java.util.Timer;
import java.util.TimerTask;
import java.util.TooManyListenersException;

/**
 * Title: SerialPortControl <br> Description: <br> Copyright: Copyright (c) 2008 <br> Company: Agro Logic LTD. <br>
 *
 * @author Valery Manakhimov <br>
 * @version 1.0 <br>
 */
public class SerialPortControl implements SerialPortEventListener {

    private static final int BUFFER_SIZE = 32000;
    private static final int TIME_OUT = 1000;
    private static final int PORT = 2000;
    private boolean sotrecieved = false;
    private final Network network;
    private volatile long sendTime;
    private volatile long sotDelay;
    private volatile long eotDelay;
    private int count;
    private byte[] buffer;
    private Protocol protocolType;
    private InputStream in;
    private OutputStream out;
    private SerialPort serialPort;
    private Timer timer;
    private boolean DEBUG = false;
    private SlipProtocol.SlipStates state;
    private Logger logger = Logger.getLogger(SerialPortControl.class);

    /**
     * Creates a new instance of SerialPortControl .
     *
     * @param com     the com name
     * @param network the network thread.
     * @throws SerialPortControlFailure if failed initialization .
     */
    public SerialPortControl(final String com, final Network network) throws SerialPortControlFailure {
        Configuration configuration = new Configuration();
        Integer protocol = Integer.parseInt(configuration.getProtocol());
        protocolType = Protocol.get(protocol);
        init(com);
        this.sendTime = System.currentTimeMillis();
        this.sotrecieved = false;
        this.sotDelay = 10000;
        this.eotDelay = 10000;
        this.network = network;
        this.timer = new Timer();
        this.buffer = new byte[BUFFER_SIZE];
        this.timer.schedule(new TimerTick(), 5 * TIME_OUT, TIME_OUT);
        resetCount();

    }

    private void clearAvailableData() throws IOException {
        while (availableData() > 0) {
            clearInputStream();
            ApplicationUtil.sleep(10);
        }
    }

    /**
     * Initialization serial port control fields.
     *
     * @param comport com name
     * @throws SerialPortControlFailure if failed initialization .
     */
    private void init(final String comport) throws SerialPortControlFailure {
        try {
            CommPortIdentifier portId;
            portId = CommPortIdentifier.getPortIdentifier(comport);
            serialPort = (SerialPort) portId.open("Terminal", PORT);
            in = serialPort.getInputStream();
            out = serialPort.getOutputStream();
            serialPort.addEventListener(this);
            serialPort.notifyOnDataAvailable(true);
            serialPort.notifyOnOutputEmpty(true);
            serialPort.setDTR(true);
            serialPort.setRTS(true);
            int boud = 2400;
            if (protocolType == Protocol.HIGH_ASCII || protocolType == Protocol.HIGH_BINARY) {
                boud = 9600;
            }
            serialPort.setSerialPortParams(boud, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
            serialPort.setFlowControlMode(SerialPort.FLOWCONTROL_NONE);
        } catch (UnsupportedCommOperationException ex) {
            logger.debug(ex);
            throw new SerialPortControlFailure("COM port does not support operation you choose ", ex);
        } catch (TooManyListenersException ex) {
            logger.debug(ex);
            throw new SerialPortControlFailure("Serial port does not failure.", ex);
        } catch (IOException ex) {
            logger.debug(ex);
            throw new SerialPortControlFailure("Can not create in\\out streams for serial port", ex);
        } catch (PortInUseException ex) {
            logger.debug(ex);
            throw new SerialPortControlFailure("Selected port in use . Select another port ", ex);
        } catch (NoSuchPortException ex) {
            logger.debug(ex);
            throw new SerialPortControlFailure("No such port .Try select another port ", ex);
        }
    }

//    /**
//     * Write a buffer array over a serial port.
//     *
//     * @param message the message object
//     * @param sot     the start of transmission index.
//     * @param eot     the end of transmission index.
//     * @throws IOException if I/O exception occurs.
//     */
//    public synchronized void write(final Message message, final int sot, final int eot) throws IOException {
//        state = SlipProtocol.SlipStates.WAIT;
//        // reset counter
//        resetCount();
//        clearBuffer();
//        sendTime = System.currentTimeMillis();
//        sotrecieved = false;
//        sotDelay = sot;
//        eotDelay = eot;
//        out.write(message.getBuffer());
//        if (DEBUG) {
//            logger.info("Sent :  " + message);
//        }
//
//    }

    /**
     * Write a buffer array over a serial port.
     *
     * @param message the message object
     * @param sot     the start of transmission index.
     * @param eot     the end of transmission index.
     * @throws IOException if I/O exception occurs.
     */
    public synchronized void write(String vindex, final Message message, final int sot, final int eot) //VreqIndex, sendMessage, sotDelay, eotDelay
            throws IOException {
        state = SlipProtocol.SlipStates.WAIT;
        // reset counter
        resetCount();
        clearBuffer();
        clearAvailableData();

        sotrecieved = false;
        sotDelay = sot;
        eotDelay = eot;

        if ((vindex != null) && (vindex.length() > 0)) {
            out.write(combineIndexAndBuffer(vindex.getBytes(), message.getBuffer()));
        } else {
            out.write(message.getBuffer());
        }

        if (DEBUG) {
            logger.info("Sent :  " + message);
        }
        out.flush();
        sendTime = System.currentTimeMillis();
    }

    /**
     * Prepare request to send . Combine index and buffer into one buffer.
     *
     * @param index  the index of buffer
     * @param buffer the buffer with data
     * @return buffer contain index and data to send
     */
    private byte[] combineIndexAndBuffer(byte[] index, byte[] buffer) {
        byte[] sendBuffer = new byte[index.length + buffer.length];
        int i = 0;

        for (byte b : index) {
            sendBuffer[i++] = b;
        }

        for (byte b : buffer) {
            sendBuffer[i++] = b;
        }
        return sendBuffer;
    }

    /**
     * Return the response.
     *
     * @return response the response.
     */
    public synchronized Message read() {
        final ResponseMessage response;
        if (protocolType == Protocol.LOW_BINARY || protocolType == Protocol.HIGH_BINARY) {
            response = new ResponseMessage(buffer, ResponseMessage.Format.BINARY);
        } else {
            response = new ResponseMessage(buffer, ResponseMessage.Format.TEXT);
        }

        if (DEBUG) {
            logger.info("Received :  " + response);
        }
        return response;
    }

    /**
     * Skips over and discards <code>in#available()</code> bytes of data from this input stream.
     *
     * @see java.io.InputStream#skip(long)
     */
    public void clearInputStream() {
        try {
            in.skip(in.available());
        } catch (IOException ex) {
            logger.error(ex);
        }
    }

    /**
     * Method declaration
     *
     * @param serialPortEvent A serial port event. See the JavaDoc for the Java CommAPI for details.
     */
    @Override
    public void serialEvent(SerialPortEvent serialPortEvent) {
        switch (serialPortEvent.getEventType()) {
            case SerialPortEvent.OUTPUT_BUFFER_EMPTY:
                clearBuffer();
                break;

            case SerialPortEvent.DATA_AVAILABLE:
                synchronized (serialPortEvent) {
                    if (sotrecieved == false) {
                        sotrecieved = true;
                    }
                }
                if (protocolType == Protocol.LOW_ASCII || protocolType == Protocol.HIGH_ASCII) {
                    readASCIIData();
                }

                if (protocolType == Protocol.LOW_BINARY || protocolType == Protocol.HIGH_BINARY) {
                    readBinaryData();
                }
                break;
        }
    }

    /**
     * Read from serial port method, ascii protocol
     */
    private void readASCIIData() {
        try {
            int data;
            // read until detect EOT byte
            while (in.available() > 0) {
                data = in.read() & 0x7F;
                if (((byte) data) == Message.ProtocolBytes.EOT.getValue()) {
                    buffer[count++] = (byte) data;
                    network.setThreadState(NetworkState.STATE_READ);
                    break;
                } else {
                    buffer[count++] = (byte) data;
                }
                sendTime = System.currentTimeMillis();
                ApplicationUtil.sleep(1);
            }
        } catch (IOException e) {
            e.printStackTrace();
            network.setThreadState(NetworkState.STATE_ABORT);
        }
    }

    /**
     * Read from serial port, binary protocol
     */
    private void readBinaryData() {
        try {
            int data;
            // read until detect EOT byte
            while (in.available() > 0) {
                data = in.read() & 0x00FF;
                switch (state) {
                    case WAIT:
                        if (data == 0xC0) {
                            buffer[count++] = (byte) data;
                            state = SlipProtocol.SlipStates.DATA;
                        }
                        break;
                    case DATA:
                        if (data == 0xC0) {
                            buffer[count++] = (byte) data;
                            state = SlipProtocol.SlipStates.READY;
                        } else {
                            buffer[count++] = (byte) data;
                        }
                        break;
                    case READY:

                        break;
                }
                sendTime = System.currentTimeMillis();
                ApplicationUtil.sleep(5);
            }
            if (state == SlipProtocol.SlipStates.READY) {
                buffer = clearGarbage(buffer, count);
                buffer = SlipProtocol.parseSlipBuffer(buffer);
                buffer = CommControl.Decompressor.decompressBinary(buffer, true);
                network.setThreadState(NetworkState.STATE_READ);
            }
        } catch (IOException e) {
            e.printStackTrace();
            network.setThreadState(NetworkState.STATE_ABORT);
        }
    }

    /**
     * Reset byte counter
     */
    private synchronized void resetCount() {
        count = 0;
    }

    /**
     * Clear buffer
     */
    private synchronized void clearBuffer() {
        buffer = new byte[BUFFER_SIZE];
    }

    /**
     * Return estimate of the number of bytes from input stream.
     *
     * @return an estimate of the number of bytes.
     * @throws IOException if an I/O error occurs.
     */
    public int availableData() throws IOException {
        return in.available();
    }

    /**
     * Close serialPort connection
     */
    public void close() {
        if (DEBUG) {
            logger.debug("Closing serial port...");
        }

        serialPort.close();
    }

    /**
     * TimerTick task runs every 100 ms and checks if timeout or error occurs during waiting for data from communication
     * port
     */
    class TimerTick extends TimerTask {    // runs every 100 ms

        @Override
        public void run() {
            final NetworkState state = network.getNetworkState();
            if (state == NetworkState.STATE_BUSY) {
                final long waitingForDataTime = sendTime + sotDelay;
                if ((waitingForDataTime < System.currentTimeMillis()) && !sotrecieved) {
                    logger.debug("Read from stream timeout " + waitingForDataTime);
                    network.setThreadState(NetworkState.STATE_TIMEOUT);
                }
                if ((waitingForDataTime + eotDelay) < System.currentTimeMillis() && sotrecieved) {
                    clearInputStream();
                    logger.debug("End of transmission error \n"
                            + "   Waiting for data time " + new Timestamp(waitingForDataTime)
                            + "\n Waiting for data time and eot " + new Timestamp(waitingForDataTime + eotDelay));
                    network.setThreadState(NetworkState.STATE_ERROR);
                }
            } else {
                try {
                    if (in.available() > 0) {
                        // skip
                        logger.debug("timertick execute");
                    }
                } catch (IOException ex) {
                    network.setThreadState(NetworkState.STATE_ABORT);
                }
            }
        }
    }

    static class SlipProtocol {

        /**
         * parsing slip protocol buffer states
         */
        enum SlipStates {

            WAIT, DATA, READY
        }

        /**
         * Parsing the buffer that converted to slip protocol .
         *
         * @param buffer the buffer
         * @return rxBuffer the after parsing
         */
        public static byte[] parseSlipBuffer(byte[] buffer) {
            int rxPtr = 0;
            byte[] rxBuffer = new byte[buffer.length];
            boolean escFound = false;

            SlipProtocol.SlipStates slipstate = SlipProtocol.SlipStates.WAIT;
            LOOP:
            for (byte c : buffer) {
                int intc = (int) c & 0x00FF;
                switch (slipstate) {
                    case WAIT:
                        if (intc == 0xC0) {
                            rxPtr = 0;
                            CRC.initCrc();
                            slipstate = SlipProtocol.SlipStates.DATA;
                            escFound = false;
                        }
                        break;
                    case DATA:
                        if (intc == 0xC0) {
                            slipstate = SlipProtocol.SlipStates.READY;
                            break;
                        }

                        if (escFound) {
                            if (intc == 0xDC) {
                                intc = (byte) 0xC0;
                            }
                            if (intc == 0xDD) {
                                intc = (byte) 0xDB;
                            }
                            escFound = false;
                        } else {
                            if (intc == 0xDB) {
                                escFound = true;
                                break;
                            }
                        }

                        CRC.doCrc((byte) intc);
                        if (rxPtr < rxBuffer.length) {
                            rxBuffer[rxPtr++] = (byte) intc;
                        }

                        break;
                    case READY: // just wait for proccecing
                        break LOOP;
                }
            }

            try {
                rxBuffer = clearGarbage(rxBuffer, rxPtr - 2);
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            }
            return rxBuffer;
        }
    }

    static class CRC {

        /**
         * crc value
         */
        public static int crcValue = 0;

        public static void initCrc() {
            crcValue = 0;
        }

        public static int doCrc(byte c) {
            // Update the CRC for transmitted and received data using
            // the CCITT 16bit algorithm (X^16 + X^12 + X^5 + 1).
            crcValue = ((((crcValue >> 8) & 0xFF) | (crcValue << 8)) & 0xFFFF);
            crcValue ^= (c & 0xff);
            crcValue ^= (crcValue & 0xff) >> 4;
            crcValue ^= ((crcValue & 0xff) << 12) & 0xFFFF;
            crcValue ^= (crcValue & 0xff) << 5;
            return (crcValue & 0xFFFF);
        }
    }

    /**
     * Return buffer without unused bytes in tail of array of bytes.
     *
     * @param buffer the buffer.
     * @param length the length of buffer that we need .
     * @return cleanBuffer the clear buffer.
     */
    public static byte[] clearGarbage(final byte[] buffer, final int length) {
        if (length < 0) {
            throw new IllegalArgumentException("Length should be 0 or positive value !");
        }
        byte[] cleanBuffer = new byte[length];
        System.arraycopy(buffer, 0, cleanBuffer, 0, cleanBuffer.length);
        return cleanBuffer;
    }
}