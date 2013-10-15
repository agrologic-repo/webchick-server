package com.agrologic.app;

import com.agrologic.app.messaging.Message;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import static org.junit.Assert.assertEquals;

public class FakeMessageSystemTest {

    private InputStream inputStream;
    private OutputStream outputStream;

    @Before
    public void setUp() throws Exception {
        Socket socket = new Socket("192.168.1.101", 5500);
        socket.setTcpNoDelay(true);
        inputStream = socket.getInputStream();
        outputStream = socket.getOutputStream();
    }

    @Test
    public void testFakeMessage() throws Exception {
        String answer = sendKeepAlive();
        assertEquals(new String(new byte[]{22, '3', '\r'}), answer);
    }

    private String sendKeepAlive() throws IOException, InterruptedException {
        outputStream.write(createKeepAlive());
        Thread.sleep(1000);
        byte[] readBuffer = new byte[256];
        int length = inputStream.read(readBuffer);
        return new String(readBuffer, 0, length);
    }

    @Test
    public void requestFromServerAccepted() throws IOException, InterruptedException {
        sendKeepAlive();
        byte[] readBuffer = new byte[256];
        int length = inputStream.read(readBuffer);
        String request = new String(readBuffer, 0, length);
        assertEquals("V01%T901a 111\r", request);
    }

//    private byte[] sendAnswer() {
//
//    }

    private byte[] createKeepAlive() {
        return new byte[]{Message.ProtocolBytes.STX.getValue(), 'c', 't', 'b',
                Message.ProtocolBytes.RS.getValue(), 'c', 't', 'b',
                Message.ProtocolBytes.RS.getValue(), '1', '.', '0', '0', 'G', 'h', '*',
                Message.ProtocolBytes.RS.getValue(), Message.ProtocolBytes.ETX.getValue()};
    }
}
