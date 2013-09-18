package com.agrologic.app.network;

import com.agrologic.app.exception.EOTException;
import com.agrologic.app.exception.SOTException;
import com.agrologic.app.exception.TimeoutException;
import com.agrologic.app.messaging.Message;
import com.agrologic.app.messaging.ResponseMessage;
import com.agrologic.app.model.CellinkVersion;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.concurrent.TimeUnit;

public class CommControl {
    private Socket socket;
    private InputStream in;
    private OutputStream out;
    private String cellinkVersion;
    private ResponseMessage response;
    private boolean stop = false;
    private Object lock;
    public static Logger logger = Logger.getLogger(CommControl.class);

    /**
     * Constructor create communication control object
     *
     * @param socket the socket
     * @throws IOException if an I/O error occurs
     */
    public CommControl(Socket socket) throws IOException {
        this.socket = socket;
        this.socket.setTcpNoDelay(true);
        this.in = socket.getInputStream();
        this.out = socket.getOutputStream();

        lock = new Object();
    }

    /**
     * This returns the response
     *
     * @return ResponseMessage byte array is the response buffer from cellink .
     */
    public Message read() {
        return response;
    }

    /**
     * Read available data from input stream
     *
     * @param buffer
     * @return n number of available data
     * @throws IOException if an I/O error occurs
     */
    public int read(byte[] buffer) throws IOException {
        return in.read(buffer);
    }

    /**
     * Read the response from input stream .
     *
     * @param sotdelay the sot delay
     * @param eotdelay the eot delay
     * @return the state of thread
     */
    public NetworkState doread(int sotdelay, int eotdelay) {
        NetworkState networkState = NetworkState.STATE_READ;
        ReadBuffer readBuffer = new ReadBuffer(/*this*/);
        readBuffer.setDelays(sotdelay, eotdelay);
        response = new ResponseMessage();

        try {
            stop = false;
            logger.debug("++++++++++++++++++++++++++++++++++++++++++++++++++");
            logger.debug("Start read from input stream");

            synchronized (lock) {
                while (!stop) {
                    if (in.available() >= CellinkVersion.headerBytesByVersion(cellinkVersion)) {
                        readBuffer.readHead(in, cellinkVersion);
                        readBuffer.readData(in);
                    }
                    if (readBuffer.isReady()) {
                        stop = true;
                        response.parsingReceiveBuffer(readBuffer);
                    } else {
                        readBuffer.checkTimeout();
                    }
                    Thread.sleep(10);
                }
            }
            logger.debug("End read from input stream");
            logger.debug("++++++++++++++++++++++++++++++++++++++++++++++++++");
        } catch (IOException ex) {
            logException(ex);
            networkState = NetworkState.STATE_ABORT;
        } catch (SOTException ex) {
            logException(ex);
            response.setErrorCodes(Message.ErrorCodes.SOT_ERROR);
            networkState = NetworkState.STATE_TIMEOUT;
        } catch (EOTException ex) {
            logException(ex);
            response.setErrorCodes(Message.ErrorCodes.EOT_ERROR);
            networkState = NetworkState.STATE_TIMEOUT;
        } catch (TimeoutException ex) {
            logException(ex);
            response.setErrorCodes(Message.ErrorCodes.TIME_OUT_ERROR);
            networkState = NetworkState.STATE_TIMEOUT;
        } catch (InterruptedException ex) {
            logException(ex);
            response.setErrorCodes(Message.ErrorCodes.TIME_OUT_ERROR);
            networkState = NetworkState.STATE_TIMEOUT;
        }
        return networkState;
    }

    /**
     * Write message to output stream , if there is available data in input stream we need clear input stream (if
     * response time was expire)
     *
     * @param msg the message to write.
     * @throws IOException if an I/O error occurs
     */
    public void write(Message msg) throws IOException {
        out.write(msg.getBuffer());
    }

    /**
     * Write message to output stream , if there is available data in input stream we need clear input stream (if
     * response time was expire)
     *
     * @param index
     * @param msg   the message to write.
     * @throws IOException if an I/O error occurs
     */
    public void write(String index, Message msg) throws IOException {
        final int DELAY_SILENCE_TIME = (int) TimeUnit.SECONDS.toMillis(3);// wait 3 seconds
        // wait for silence
        while (availableData() > 0) {
            synchronized (this) {
                try {
                    clearInputStream();
                    wait(DELAY_SILENCE_TIME);
                } catch (InterruptedException ex) {
                }
            }
        }

        if (index != null && index.length() > 0) {
            //out.write(index.getBytes());
            byte[] sendBuffer = mergeIndexAndBuffer(index.getBytes(), msg.getBuffer());
            logger.info("Sending : " + new String(sendBuffer, 0, sendBuffer.length));
            socket.setSendBufferSize(sendBuffer.length);
            out.write(sendBuffer, 0, sendBuffer.length);
        } else {
            out.write(msg.getBuffer());
            logger.info(msg);
        }
        out.flush();
    }

    /**
     * Returns request in byte buffer with request index .
     *
     * @param index  the index
     * @param buffer the request buffer
     * @return sendBuffer the buffer with request index
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

        return sendBuffer;
    }

    /**
     * This will clear input stream
     *
     * @throws IOException if an I/O error occurs.
     */
    public void clearInputStream() {
        try {
            int len = in.available();
            in.read(new byte[len]);
        } catch (IOException ex) {
            logException(ex);
        }
    }

    public void clearInputStreamWithDelayForSilence() {
        final int DELAY_SILENCE_TIME = (int) TimeUnit.SECONDS.toMillis(5);// wait 5 second
        // wait for silence
        try {
            try {
                clearInputStream();
                Thread.sleep(DELAY_SILENCE_TIME);
            } catch (InterruptedException ex) {

            }
            while (availableData() > 0) {
                synchronized (this) {
                    try {
                        clearInputStream();
                        Thread.sleep(DELAY_SILENCE_TIME);
                    } catch (InterruptedException ex) {
                    }
                }
            }
        } catch (IOException ex) {
            logException(ex);
        }
    }

    /**
     * Close streams and socket
     */
    public final void close() {
        if (socket != null && socket.isConnected()) {
            try {
                socket.close();
                in.close();
                out.close();
            } catch (IOException e) {
                logException(e);
            }
        }
    }

    /**
     * Send to logger caused exception object .
     *
     * @param ex the exception
     */
    public void logException(final Exception ex) {
        Throwable t = ex.getCause();
        while (t != null) {
            logger.error("Cause: " + t);
            t = t.getCause();
        }
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
     * Return socket.
     *
     * @return the socket
     */
    public Socket getSocket() {
        return socket;
    }

    public synchronized void setCellinkVersion(String cellinkVersion) {
        this.cellinkVersion = cellinkVersion;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final CommControl other = (CommControl) obj;
        if (this.socket != other.socket && (this.socket == null
                || !this.socket.equals(other.socket))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 19 * hash + (this.socket != null ? this.socket.hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        return new StringBuilder().append("IP=").
                append(this.socket.getInetAddress()).
                append(" Port=").
                append(this.socket.getPort()).toString();
    }

    enum ReadState {

        WAIT_SOT, WAIT_EOT
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

    public static class Decompressor {

        /**
         * Static method that takes compressed buffer and return decompressed buffer.
         *
         * @param buffer     the compressed buffer.
         * @param indexExist response with comes with index of request.
         * @return newBuffer the decompressed buffer.
         */
        public static byte[] decompress(byte[] buffer, boolean indexExist) {
            byte[] newBuffer = new byte[(buffer.length) * 4];// ? danger multiply
            int high, low, count = 0;

            for (int i = 0; i < buffer.length; ) {

                if (indexExist) {

                    if (i == 0) {
                        newBuffer[count++] = buffer[i++];
                        int val = (int) buffer[i++];
                        byte[] ba = String.valueOf(val).getBytes();
                        for (byte b : ba) {
                            newBuffer[count++] = b;
                        }
                        newBuffer[count++] = buffer[i++];
                    }
                }

                high = (int) buffer[i++];

                // last byte found
                if (i == buffer.length && count > 0) {
                    newBuffer[count - 1] = buffer[i - 1];   // clear last space before EOT
                    break;
                }
                // here we recognize if there was
                // negative couple of bytes
                low = (int) buffer[i++] & 0x00FF;           // positive
                int val = (high * 256) + low;

                byte[] ba = String.valueOf(val).getBytes();
                for (byte b : ba) {
                    newBuffer[count++] = b;
                }
                newBuffer[count++] = ' ';
            }
            newBuffer = clearGarbage(newBuffer, count);
            return newBuffer;
        }

        /**
         * parse states
         */
        enum ParseState {

            START, DATA, READY
        }

        /**
         * Static method that takes compressed buffer and return decompressed buffer. [binary to asci ]
         *
         * @param buffer     the compressed buffer.
         * @param indexExist response with comes with index of request.
         * @return newBuffer the decompressed buffer.
         */
        public static byte[] decompressBinary(byte[] buffer, boolean indexExist) {
            byte[] newBuffer = new byte[(buffer.length) * 4];// ? danger multiply
            int newBufCnt = 0;

            ParseState parseState = ParseState.START;

            for (int i = 0; i < buffer.length; ) {
                switch (parseState) {
                    case START:
                        newBuffer[newBufCnt++] = Message.ProtocolBytes.SOINDX.getValue();
                        int val = (int) buffer[i++];
                        val <<= 8;
                        val += (int) buffer[i++];
                        byte[] ba = String.valueOf(val).getBytes();
                        for (byte b : ba) {
                            newBuffer[newBufCnt++] = b;
                        }
                        newBuffer[newBufCnt++] = Message.ProtocolBytes.SOT.getValue();
                        parseState = ParseState.DATA;
                        break;
                    case DATA:
                        val = (int) buffer[i++];
                        val <<= 8;
                        val += (int) buffer[i++] & 0x00FF;// positive
                        ba = String.valueOf(val).getBytes();
                        for (byte b : ba) {
                            newBuffer[newBufCnt++] = b;
                        }
                        newBuffer[newBufCnt++] = ' ';
                        if (i == buffer.length) {
                            newBuffer[newBufCnt - 1] = Message.ProtocolBytes.EOT.getValue();
                        }
                        break;
                    case READY:
                        break;
                }
            }
            try {
                newBuffer = clearGarbage(newBuffer, newBufCnt);
            } catch (IllegalArgumentException e) {
                logger.error(e);
            }

            return newBuffer;
        }
    }

    /**
     *
     */
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

            SlipStates slipstate = SlipStates.WAIT;
            for (byte c : buffer) {
                int intc = (int) c & 0x00FF;
                switch (slipstate) {
                    case WAIT:
                        if (intc == 0xC0) {
                            rxPtr = 0;
                            CRC.initCrc();
                            slipstate = SlipStates.DATA;
                            escFound = false;
                        }
                        break;
                    case DATA:
                        if (intc == 0xC0) {
                            slipstate = SlipStates.READY;
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
                        break;
                }

            }
            try {
                rxBuffer = clearGarbage(rxBuffer, rxPtr - 2);
            } catch (IllegalArgumentException e) {
                logger.error(e);
            }

            return rxBuffer;
        }
    }

    /**
     *
     */
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
}
