
/*
* To change this template, choose Tools | Templates
* and open the template in the editor.
 */
package com.agrologic.app.network.rxtx;

//~--- non-JDK imports --------------------------------------------------------

import com.agrologic.app.except.TimeoutException;

//~--- JDK imports ------------------------------------------------------------

import java.io.IOException;
import java.io.InputStream;

import java.nio.ByteBuffer;

/**
 * Title: ReadBuffer.java <br>
 * Description: <br>
 * Copyright:   Copyright © 2010 <br>
 * Company:     AgroLogic Ltd. ®<br>
 * @author      Valery Manakhimov <br>
 * @version     0.1.1 <br>
 */
public class ReadBuffer {
    public static final int BUFFER_SIZE = 4096;
    public static final int HEAD_BYTES  = 3;
    private int             bufferIndex;
    private int             bufferIsLast;
    private int             bufferSize;
    private long            eotDelay;
    private ByteBuffer      inputBuffer;
    private boolean         ready;
    private long            sotDelay;
    private long            startTime;

    public ReadBuffer() {
        inputBuffer = ByteBuffer.allocate(BUFFER_SIZE);
        startTime   = System.currentTimeMillis();
        ready       = false;
    }

    /**
     * Read first 3 bytes .
     * @param in the InputStream
     * @throws IOException
     */
    public void readHead(InputStream in) throws IOException {
        bufferIsLast = in.read();
        System.out.println(bufferIsLast);
        bufferIndex  = in.read();    // unused
        System.out.println(bufferIndex);
        bufferSize   = in.read();
        System.out.println(bufferSize);

    }

    /**
     * Read available data and save into ByteBuffer
     * ordering by index and amount of data .
     * @param in the InputStream
     * @throws IOException
     * @throws TimeoutException
     */
    public void readData(final InputStream in) throws IOException, TimeoutException {
        try {
            for (int i = 0; i < bufferSize; i++) {
                if (in.available() > 0) {
                    byte data = (byte) in.read();
                    inputBuffer.put(data);
                    System.out.println(data);
                    startTime = System.currentTimeMillis();
                }

                if (timedOut()) {
                    throw new TimeoutException("Time out");
                }
            }
        } catch (IllegalArgumentException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Return true if timed out.
     * @return true if time out occurred .
     */
    public boolean timedOut() {
        long currTime = System.currentTimeMillis();

        if ((startTime + sotDelay + eotDelay) < currTime) {
            return true;
        }

        return false;
    }

    /**
     * Set delays .
     * @param sotDelay the start of transmission delay.
     * @param eotDelay the end of transmission delay.
     */
    public void setDelays(long sotDelay, long eotDelay) {
        this.sotDelay = sotDelay;
        this.eotDelay = eotDelay;
    }

    /**
     * Return true if bufferIsLast equal to ERROR or LAST_COPRESSED.
     * @return true if last buffer received ,false - otherwise.
     */
    public boolean isReady() {
        if (bufferIsLast == Network.ERROR) {
            ready = true;
        } else if (bufferIsLast == Network.LAST_COMPRESSED) {
            ready = true;
        }

        return ready;
    }

    /**
     * Convert inputBuffer to array of bytes and than
     * return. if buffer was not compressed ,
     *         if buffer was compressed decompress
     *         and than return buffer.
     * @return buffer the array of received bytes.
     */
    public byte[] toArray() {
        byte[] buffer = new byte[inputBuffer.position()];

        inputBuffer.flip();
        inputBuffer.get(buffer, 0, buffer.length);

        if (bufferIsLast == Network.LAST_COMPRESSED) {
            buffer = decompress(buffer);
        }

        return buffer;
    }

    /**
     * Static method that takes compressed buffer and return decompressed buffer.
     * @param buffer the compressed buffer.
     * @return newBuffer the decompressed buffer.
     */
    public static byte[] decompress(byte[] buffer) {
        byte[]  newBuffer = new byte[(buffer.length) * 4];    // ? danger multiply
        int     high, low,
                count     = 0;
        boolean dataFlag  = true;

        for (int i = 0; i < buffer.length; ) {
            high = (int) buffer[i++];

            if (dataFlag) {
                dataFlag = false;
            } else {
                dataFlag = true;
            }

            // last byte found
            if ((i == buffer.length) && (count > 0)) {
                newBuffer[count - 1] = buffer[i - 1];    // clear last space before EOT

                break;
            }

            // here we recognize if there was
            // negative couple of bytes
            low = (int) buffer[i++] & 0x00FF;    // positive

            int val = (high * 256) + low;

            // we need this if state for compatibility
            // between communication with 911 and image2
            if (((val & 0xC000) == 0xC000) && (val != -1) && (dataFlag == false)) {
                val &= 0xFFFF;
            }

            byte[] ba = String.valueOf(val).getBytes();

            for (byte b : ba) {
                newBuffer[count++] = b;
            }

            newBuffer[count++] = ' ';
        }

        newBuffer = clearGarbageFrom(newBuffer, count);

        return newBuffer;
    }

    /**
     * Return buffer without unused bytes in tail of array of bytes.
     * @param buffer the buffer.
     * @param length the length of buffer that we need .
     * @return cleanBuffer the clear buffer.
     */
    public static byte[] clearGarbageFrom(byte[] buffer, int length) {
        byte[] cleanBuffer = new byte[length];

        System.arraycopy(buffer, 0, cleanBuffer, 0, cleanBuffer.length);

        return cleanBuffer;
    }
}


//~ Formatted by Jindent --- http://www.jindent.com
