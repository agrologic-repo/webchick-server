
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.agrologic.app.network;

import com.agrologic.app.except.EOTException;
import com.agrologic.app.except.SOTException;
import com.agrologic.app.except.TimeoutException;
import com.agrologic.app.messaging.Message;
import com.agrologic.app.model.CellinkVersion;
import java.io.IOException;
import java.io.InputStream;
import java.nio.BufferOverflowException;
import java.nio.ByteBuffer;
import java.sql.Timestamp;

public class ReadBuffer {

    public static final int BUFFER_SIZE = 8000;
    private int byteCounter = 0;
    private int bufferIndex;
    private int bufferIsLast;
    private int bufferSize;
    private byte bufferType;
    private long eotDelay;
    private long sotDelay;
    private long startTime;
    private ByteBuffer inputBuffer;
    private boolean ready;
//    private final CommControl readBuffer;
    private CommControl.ReadState readState;
//    private BufferType bufferType;
//
//    public enum BufferType {
//        TEXT("TEXT", (byte)2), BINARY("BINARY", (byte)1301);
//
//        private BufferType(String name, byte id) {
//            this.name = name;
//            this.id = id;
//        }
//
//        public final String name;
//        public final byte id;
//    }

    public ReadBuffer() {
        this.inputBuffer = ByteBuffer.allocate(BUFFER_SIZE);
        this.startTime = System.currentTimeMillis();
        this.ready = false;
        this.readState = CommControl.ReadState.WAIT_SOT;
    }

    /**
     * Read first n bytes . The n bytes depending on version
     *
     * @param in the InputStream
     * @param version the Version
     * @throws IOException
     */
    public synchronized void readHead(InputStream in, String version) throws IOException {
        startTime = System.currentTimeMillis();
        bufferIsLast = in.read();
        bufferIndex = in.read();
        bufferSize = in.read();
        if (CellinkVersion.dataBytesByVersion(version) > 1) {
            bufferSize <<= 8;
            bufferSize += in.read();
        }
    }

    /**
     * Read available data and save into ByteBuffer ordering by index and amount of data .
     *
     * @param in the InputStream
     * @throws IOException
     * @throws TimeoutException
     */
    public synchronized void readData(InputStream in) throws IOException, SOTException, EOTException {
//        try {
            startTime = System.currentTimeMillis();
            byteCounter = 0;
            CommControl.logger.debug("+++++++++++++++++++++++++++++++++++++");
            CommControl.logger.debug("Start read buffer " + bufferIndex);

            while (byteCounter < bufferSize) {
                if (in.available() > 0) {
                    byte data = (byte) in.read();

                    try {
                        inputBuffer.put(data);
                    } catch (BufferOverflowException e) {
                        System.out.println(inputBuffer.position());
                    }
                    startTime = System.currentTimeMillis();
                    readState = CommControl.ReadState.WAIT_EOT;
                    byteCounter++;
                } else {
                    try {
                        wait(1);
                    } catch (InterruptedException ex) {

                    }
                }

                checkTimeout();
            }

            CommControl.logger.debug("End read buffer " + bufferIndex);
            CommControl.logger.debug("+++++++++++++++++++++++++++++++++++++");
//        } catch (IOException e) {
//            CommControl.logger.debug(e);
//            throw new IOException();
//        } catch (SOTException e) {
//            CommControl.logger.debug(e);
//            throw new SOTException("");
//        } catch (EOTException e) {
//            throw new EOTException("");
//        }
    }

    /**
     * Return buffer type (asci or binary) .
     *
     * @return bufferType;
     */
    public synchronized byte getBufferType() {
        return bufferType;
    }

    public synchronized void setBufferType(byte bufferType) {
        this.bufferType = bufferType;
    }

    /**
     * Return true if timed out.
     *
     * @return true if time out occurred .
     */
    public synchronized boolean timedOut() {
        long currTime = System.currentTimeMillis();
        if ((startTime + sotDelay + eotDelay) < currTime) {
            return true;
        }
        return false;
    }

    /**
     * Return true if timed out.
     *
     * @return true if time out occurred .
     */
    public synchronized void checkTimeout() throws SOTException, EOTException {
        long currTime = System.currentTimeMillis();
        Timestamp currT = new Timestamp(currTime);
        Timestamp sotT = new Timestamp(startTime);

        switch (readState) {
            case WAIT_SOT:
                if ((startTime + sotDelay) < currTime) {
                    throw new SOTException("Current time : " + currT.toString() + " Start time : " + sotT.toString());
                }

                break;

            case WAIT_EOT:
                Timestamp delT = new Timestamp(startTime + sotDelay);
                if ((startTime + sotDelay + eotDelay) < currTime) {
                    throw new EOTException("Current time : " + currT.toString() + " Start time : " + sotT.toString()
                            + " Delay time : " + delT.toString());
                }

                break;

            default:
                break;
        }
    }

    /**
     * Set delays .
     *
     * @param sotDelay the start of transmission delay.
     * @param eotDelay the end of transmission delay.
     */
    public synchronized void setDelays(long sotDelay, long eotDelay) {
        this.sotDelay = sotDelay;
        this.eotDelay = eotDelay;
    }

    /**
     * Return true if bufferIsLast equal to ERROR or LAST_COPRESSED.
     *
     * @return true if last buffer received ,false - otherwise.
     */
    public synchronized boolean isReady() {
        if ((bufferIsLast == Message.LAST_COMPRSSED_TEXT_MESSAGE)
                || (bufferIsLast == Message.LAST_COMPRSS_WITH_IND_TEXT_MESSAGE)
                || (bufferIsLast == Message.LAST_COMPRSSED_WITH_IND_BINARY_MESSAGE)) {
            if (byteCounter >= bufferSize) {
                ready = true;
            }
        }
        return ready;
    }

    /**
     * Convert inputBuffer to array of bytes and than return. If buffer was not compressed. If buffer was compressed
     * decompress and than return buffer.
     *
     * @return buffer the array of received bytes.
     */
    public byte[] toArray() {
        byte[] buffer = new byte[inputBuffer.position()];

        inputBuffer.flip();
        inputBuffer.get(buffer, 0, buffer.length);

        synchronized (this) {
            setBufferType(Message.LAST_TEXT_MESSAGE/* default */);
            if (bufferIsLast == Message.LAST_COMPRSSED_TEXT_MESSAGE) {
                setBufferType(Message.LAST_COMPRSSED_TEXT_MESSAGE);
                buffer = CommControl.Decompressor.decompress(buffer, false);
            }

            if (bufferIsLast == Message.LAST_COMPRSS_WITH_IND_TEXT_MESSAGE) {
                setBufferType(Message.LAST_COMPRSS_WITH_IND_TEXT_MESSAGE);
                buffer = CommControl.Decompressor.decompress(buffer, true);
            }

            if (bufferIsLast == Message.LAST_COMPRSSED_WITH_IND_BINARY_MESSAGE) {
                setBufferType(Message.LAST_COMPRSSED_WITH_IND_BINARY_MESSAGE);
                buffer = CommControl.SlipProtocol.parseSlipBuffer(buffer);
                if (CommControl.CRC.crcValue != 0) {
                    return null;
                }
                buffer = CommControl.Decompressor.decompressBinary(buffer, true);
            }
        }

        return buffer;
    }
}



