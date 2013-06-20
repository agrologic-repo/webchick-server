package com.agrologic.app;

import com.agrologic.app.messaging.Message;
import org.junit.Before;
import org.junit.Test;

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
        outputStream.write(createKeepAlive());
        Thread.sleep(1000);
        byte[] readBuffer = new byte[256];
        int length = inputStream.read(readBuffer);
        String answer = new String(readBuffer, 0, length);
        assertEquals(new String(new byte[]{22, '3', '\r'}), answer);
    }

    private byte[] createKeepAlive() {
        return new byte[]{Message.ProtocolBytes.STX.getValue(), 'c', 't', 'b',
                Message.ProtocolBytes.RS.getValue(), 'c', 't', 'b',
                Message.ProtocolBytes.RS.getValue(), '5', '.', '0', '1', 'W', 'e', '*',
                Message.ProtocolBytes.RS.getValue(), Message.ProtocolBytes.ETX.getValue()};
    }
}
