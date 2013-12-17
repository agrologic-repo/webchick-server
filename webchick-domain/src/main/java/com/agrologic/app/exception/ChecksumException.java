package com.agrologic.app.exception;

/**
 * Throws if  there is mismatch between sum from received buffer and checksum that was calculated .
 *
 * @author Valery Manakhimov
 */
public class ChecksumException extends Exception {
    public static final String CHECKSUM_NUMBERS_ARE_MISMATCHED = "Checksum numbers are mismatched . "
            + "Received checksum was %d calculated checksum was %d ";

    public ChecksumException(int recvCHS, int calcCHS) {
        super(String.format(CHECKSUM_NUMBERS_ARE_MISMATCHED, recvCHS, calcCHS));
    }
}
