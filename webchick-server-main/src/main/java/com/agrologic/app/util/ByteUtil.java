package com.agrologic.app.util;


import org.apache.commons.lang.Validate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ByteUtil {
    public static int indexOf(byte[] buffer, byte input) {
        Validate.notNull(buffer, "buffer can not be null");
        int count = 0;
        int i = -1;

        for (byte b : buffer) {
            if (b == input) {
                i = count;

                break;
            }

            count++;
        }

        return i;
    }

    public static int lastIndexOf(byte[] buffer, byte input) {
        Validate.notNull(buffer, "buffer can not be null");
        Validate.notNull(input, "input can not be null");

        int count = 0;
        int i = -1;

        for (byte b : buffer) {
            if (b == input) {
                i = count;
            }

            count++;
        }

        return i;
    }

    public static List<byte[]> split(byte[] buffer, byte input) {
        ArrayList<byte[]> matchList = new ArrayList<byte[]>();
        int matchCount = 0;
        int from = 0;
        int to = 0;
        byte[] tmp = new byte[buffer.length];

        for (byte b : buffer) {
            tmp[to] = b;

            if (b == input) {
                byte[] match = Arrays.copyOfRange(tmp, from, to);

                matchList.add(match);
                from = to + 1;
                matchCount++;
            }

            to++;
        }

        byte[] match = Arrays.copyOfRange(tmp, from, to);

        matchList.add(match);
        matchCount++;

        return matchList.subList(0, matchCount);
    }

    public static byte[] substring(byte[] buffer, int beginIndex, int endIndex) {
        if (beginIndex < 0) {
            throw new StringIndexOutOfBoundsException(beginIndex);
        }

        if (endIndex > buffer.length) {
            throw new StringIndexOutOfBoundsException(endIndex);
        }

        if (beginIndex > endIndex) {
            throw new StringIndexOutOfBoundsException(endIndex - beginIndex);
        }

        int size = endIndex - beginIndex;
        byte[] newBuffer = new byte[size];

        for (int i = 0; i < size; i++) {
            newBuffer[i] = buffer[i + beginIndex];
        }

        return newBuffer;
    }

    public static void printResult(byte[] buf) {
        for (byte b : buf) {
            System.out.println(b);
        }
    }
}



