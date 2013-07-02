package com.agrologic.app.messaging;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class KeepAliveMessageTest {

    private final static byte[] PAY_LOAD = new byte[]{Message.ProtocolBytes.STX.getValue(), 'c', 't', 'b',
            Message.ProtocolBytes.RS.getValue(), 'c', 't', 'b',
            Message.ProtocolBytes.RS.getValue(), '5', '.', '0', '1', 'W', 'e', '*',
            Message.ProtocolBytes.RS.getValue(), Message.ProtocolBytes.ETX.getValue()};
    private final static byte[] WRONG_FORMAT_PAY_LOAD = new byte[]{Message.ProtocolBytes.STX.getValue(),
            'c', 't', 'b', 'c', 't', 'b', Message.ProtocolBytes.ETX.getValue()};

    private final static byte[] PAY_LOAD_3G = new byte[]{Message.ProtocolBytes.STX.getValue(), 'c', 't', 'b',
            Message.ProtocolBytes.RS.getValue(), 'c', 't', 'b',
            Message.ProtocolBytes.RS.getValue(), '5', '.', '0', '1', '3', 'G', 'e', '*',
            Message.ProtocolBytes.RS.getValue(), Message.ProtocolBytes.ETX.getValue()};

    @Test
    public void testParseIncomingBytes() throws Exception {
        KeepAliveMessage kam = KeepAliveMessage.parseIncomingBytes(PAY_LOAD);
        assertEquals(kam.getUsername(), ("ctb"));
        assertEquals(kam.getPassword(), ("ctb"));
        assertEquals(kam.getVersion(), ("5.01We*"));
    }

    @Test
    public void testParseIncomingBytes3G() throws Exception {
        KeepAliveMessage kam = KeepAliveMessage.parseIncomingBytes(PAY_LOAD_3G);
        assertEquals(kam.getUsername(), ("ctb"));
        assertEquals(kam.getPassword(), ("ctb"));
        assertEquals(kam.getVersion(), ("5.013Ge*"));
    }


    @Test
    public void testParseIncomingBytesIfBufferIsNull() throws WrongMessageFormatException {
        byte[] nullArg = null;
        try {
            KeepAliveMessage.parseIncomingBytes(nullArg);
        } catch (IllegalArgumentException e) {
            assert (e.getMessage().equals(KeepAliveMessage.BUFFER_MUST_NOT_BE_NULL));
        }
    }

    @Test
    public void testParseIncomingBytesIfFormatIsWrong() {
        try {
            KeepAliveMessage.parseIncomingBytes(WRONG_FORMAT_PAY_LOAD);
        } catch (WrongMessageFormatException e) {
            assert (e.getMessage().equals(KeepAliveMessage.MESSAGE_DOES_NOT_MATCH_THE_REQUIRED_FORMAT));
        }
    }
}
