package com.agrologic.app.network;

import com.agrologic.app.exception.EOTException;
import com.agrologic.app.exception.SOTException;
import com.agrologic.app.exception.TimeoutException;
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
    private byte bufferFormat;
    private long eotDelay;
    private long sotDelay;
    private long startTime;
    private ByteBuffer inputBuffer;
    private boolean ready;
    private CommControl.ReadState readState;

    public ReadBuffer() {
        this.inputBuffer = ByteBuffer.allocate(BUFFER_SIZE);
        this.startTime = System.currentTimeMillis();
        this.ready = false;
        this.readState = CommControl.ReadState.WAIT_SOT;
    }

    /**
     * Read first n bytes . The n bytes depending on version
     *
     * @param in      the InputStream
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
    public synchronized void readData(InputStream in) throws IOException, SOTException, EOTException, TimeoutException {
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

                    CommControl.logger.debug("Buffer overflow exception . Received {} bytes ", inputBuffer.position());
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
    }

    /**
     * Return buffer type (asci or binary) .
     *
     * @return bufferFormat;
     */
    public synchronized byte getBufferFormat() {
        return bufferFormat;
    }

    public synchronized void setBufferFormat(byte bufferFormat) {
        this.bufferFormat = bufferFormat;
    }

    /**
     * Return true if timed out.
     *
     * @return true if time out occurred .
     */
    public synchronized void checkTimeout() throws SOTException, EOTException, TimeoutException {
        long currTime = System.currentTimeMillis();
        Timestamp currT = new Timestamp(currTime);
        Timestamp sotT = new Timestamp(startTime);

        switch (readState) {
            case WAIT_SOT:
                if ((startTime + sotDelay) < currTime) {
                    throw new SOTException();
                }

                break;

            case WAIT_EOT:
                Timestamp delta = new Timestamp(startTime + sotDelay);
                if ((startTime + sotDelay + eotDelay) < currTime) {
                    throw new EOTException("Current time : " + currT.toString() + " Start time : " + sotT.toString()
                            + " Delay time : " + delta.toString());
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
     * Return true if bufferIsLast equal to ERROR or LAST_COMPRESSED.
     *
     * @return true if last buffer received ,false - otherwise.
     */
    public synchronized boolean isReady() {
        if ((bufferIsLast == Message.LastIndicators.COMPRSSED_TEXT.getValue())
                || (bufferIsLast == Message.LastIndicators.COMPRSSED_TEXT_WITH_INDEX.getValue())
                || (bufferIsLast == Message.LastIndicators.COMPRSSED_BINARY_WITH_INDEX.getValue())) {
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
            setBufferFormat((byte) bufferIsLast /* default */);
            if (bufferIsLast == Message.LastIndicators.COMPRSSED_TEXT.getValue()) {
                buffer = CommControl.Decompressor.decompress(buffer, false);
            }

            if (bufferIsLast == Message.LastIndicators.COMPRSSED_TEXT_WITH_INDEX.getValue()) {
                buffer = CommControl.Decompressor.decompress(buffer, true);
            }

            if (bufferIsLast == Message.LastIndicators.COMPRSSED_BINARY_WITH_INDEX.getValue()) {
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



