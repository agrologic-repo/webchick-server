
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
import com.agrologic.app.except.SerialPortControlFailure;
import com.agrologic.app.messaging.Message;
import com.agrologic.app.messaging.ResponseMessage;
import gnu.io.*;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Timer;
import java.util.TimerTask;
import java.util.TooManyListenersException;
import org.apache.log4j.Logger;

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
    private Logger logger = Logger.getLogger(SerialPortControl.class);

    /**
     * Creates a new instance of SerialPortControl .
     *
     * @param com the com name
     * @param network the network thread.
     * @throws SerialPortControlException if failed initialization .
     */
    public SerialPortControl(final String com, final Network network) throws SerialPortControlFailure {
        Configuration c = new Configuration();
        Integer protocol = Integer.parseInt(c.getProtocol());
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
            try {
                Thread.sleep(10);
            } catch (InterruptedException ex) {
                // ignore
            }
        }
    }

    /**
     * Initialization serial port control fields.
     *
     * @param comport com name
     * @throws SerialPortControlException if failed initialization .
     */
    private void init(final String comport) throws SerialPortControlFailure {
        try {
            Configuration config = new Configuration();
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
            if(protocolType == Protocol.HIGH_ASCII
                    || protocolType == Protocol.HIGH_BINARY) {
                boud = 9600;
            }
            serialPort.setSerialPortParams(boud,
                    SerialPort.DATABITS_8,
                    SerialPort.STOPBITS_1,
                    SerialPort.PARITY_NONE);
            serialPort.setFlowControlMode(SerialPort.FLOWCONTROL_NONE);
        } catch (UnsupportedCommOperationException ex) {
            logger.debug(ex);
            throw new SerialPortControlFailure("COM port does not support operation you choosed .", ex);
        } catch (TooManyListenersException ex) {
            logger.debug(ex);
            throw new SerialPortControlFailure("Serial port does not failure.", ex);
        } catch (IOException ex) {
            logger.debug(ex);
            throw new SerialPortControlFailure("Can not create in\\out streams for serial port.", ex);
        } catch (PortInUseException ex) {
            logger.debug(ex);
            throw new SerialPortControlFailure("Selected port in use . Try select another port.", ex);
        } catch (NoSuchPortException ex) {
            logger.debug(ex);
            throw new SerialPortControlFailure("No such port .Try select another port.", ex);
        }
    }

    /**
     * Write a buffer array over a serial port.
     *
     * @param message the message object
     * @param sot the start of transmission index.
     * @param eot the end of transmission index.
     * @throws IOException if I/O exception occurs.
     */
    public synchronized void write(final Message message, final int sot, final int eot) throws IOException {

        // reset counter
        resetCount();
        clearBuffer();
        sendTime = System.currentTimeMillis();
        sotrecieved = false;
        sotDelay = sot;
        eotDelay = eot;
        out.write(message.getBuffer());
        if (DEBUG) {
            logger.info("Sent :  " + message);
        }

    }

    /**
     * Write a buffer array over a serial port.
     *
     * @param message the message object
     * @param sot the start of transmission index.
     * @param eot the end of transmission index.
     * @throws IOException if I/O exception occurs.
     */
    public synchronized void write(String vindex, final Message message, final int sot, final int eot) throws IOException {

        // reset counter
        resetCount();
        clearBuffer();
        clearAvailableData();

        sotrecieved = false;
        sotDelay = sot;
        eotDelay = eot;

        if ((vindex != null) && (vindex.length() > 0)) {

            // out.write(index.getBytes());
            out.write(mergeIndexAndBuffer(vindex.getBytes(), message.getBuffer()));
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
     *
     * @param index
     * @param buffer
     * @return
     */
    private byte[] mergeIndexAndBuffer(byte[] index, byte[] buffer) {
        byte[] sendBuffer = new byte[index.length + buffer.length];
        int i = 0;

        for (byte b : index) {
            sendBuffer[i++] = b;
        }

        for (byte b : buffer) {
            sendBuffer[i++] = b;
        }

        System.out.println(new String(sendBuffer, 0, sendBuffer.length));

        return sendBuffer;
    }

    /**
     * Return the response.
     *
     * @return response the response.
     */
    public synchronized Message read() {
        final ResponseMessage response = new ResponseMessage(buffer);

        if (DEBUG) {
            logger.info("Received :  " + response);
        }

        return response;
    }

    /**
     *
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
     * @param event A serial port event. See the JavaDoc for the Java CommAPI for details.
     */
    @Override
    public void serialEvent(SerialPortEvent serialPortEvent) {
        switch (serialPortEvent.getEventType()) {
            case SerialPortEvent.OUTPUT_BUFFER_EMPTY:
                clearBuffer();
                break;

            case SerialPortEvent.DATA_AVAILABLE:
                sotrecieved = true;
                if(protocolType == Protocol.LOW_ASCII ||
                        protocolType == Protocol.HIGH_ASCII) {
                    readASCIIData();
                }

                if(protocolType == Protocol.LOW_BINARY ||
                        protocolType == Protocol.HIGH_BINARY) {
                    readBinaryData();
                }
                break;
        }
    }

    private void readASCIIData() {
        try {
            int data;
            // read until detect EOT byte
            while (in.available() > 0) {
                data = in.read() & 0x7F;
                if (((byte) data) == Message.EOT) {
                    buffer[ count++ ] = (byte) data;
                    network.setThreadState(com.agrologic.app.network.rxtx.NetworkState.STATE_READ);
                    break;
                } else {
                    buffer[ count++ ] = (byte) data;
                }
                sendTime = System.currentTimeMillis();
                Thread.sleep(1);
            }
        } catch (IOException e) {
            e.printStackTrace();
            network.setThreadState(com.agrologic.app.network.rxtx.NetworkState.STATE_ABORT);
        } catch (InterruptedException e) {
            // ignore
            e.printStackTrace();
        }
    }

    private void readBinaryData() {
        try {
            int data;
            // read until detect EOT byte
            while (in.available() > 0) {
                data = in.read() & 0x7F;
                if (((byte) data) == Message.EOT) {
                    buffer[count++] = (byte) data;
                    network.setThreadState(com.agrologic.app.network.rxtx.NetworkState.STATE_READ);
                    break;
                } else {
                    buffer[count++] = (byte) data;
                }
                sendTime = System.currentTimeMillis();
                Thread.sleep(10);
            }
        } catch (IOException e) {
            e.printStackTrace();
            network.setThreadState(com.agrologic.app.network.rxtx.NetworkState.STATE_ABORT);
        } catch (InterruptedException e) {
            // ignore
            e.printStackTrace();
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
            final com.agrologic.app.network.rxtx.NetworkState state = network.getNetworkState();
            if (state == com.agrologic.app.network.rxtx.NetworkState.STATE_BUSY) {
                final long waitingForDataTime = sendTime + sotDelay;
                if ((waitingForDataTime < System.currentTimeMillis()) && !sotrecieved) {
                    logger.debug("Read from stream Timeout. " + waitingForDataTime);
                    network.setThreadState(com.agrologic.app.network.rxtx.NetworkState.STATE_TIMEOUT);
                }
                if ((waitingForDataTime + eotDelay) < System.currentTimeMillis() && sotrecieved) {
                    clearInputStream();
                    logger.debug("End Of Transmission Error." + waitingForDataTime);
                    network.setThreadState(com.agrologic.app.network.rxtx.NetworkState.STATE_ERROR);
                }
            } else {
                try {
                    if (in.available() > 0) {
                        // skip
                        logger.debug("timertick execute");
                    }
                } catch (IOException ex) {
                    network.setThreadState(com.agrologic.app.network.rxtx.NetworkState.STATE_ABORT);
                }
            }
        }
    }
}
//~ Formatted by Jindent --- http://www.jindent.com
