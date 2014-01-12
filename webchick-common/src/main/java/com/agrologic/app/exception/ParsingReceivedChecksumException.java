package com.agrologic.app.exception;

/**
 * Is thrown by parsing response buffer to indicate problems with parsing checksum .
 *
 * @author Valery Manakhimov
 */
public class ParsingReceivedChecksumException extends NumberFormatException {

    public static final String PARSING_CHECKSUM_ERROR = "Can not parsing %s ";

    /**
     * @param param
     */
    public ParsingReceivedChecksumException(String param) {
        super(String.format(PARSING_CHECKSUM_ERROR, param));
    }
}



