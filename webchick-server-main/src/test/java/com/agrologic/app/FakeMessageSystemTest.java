package com.agrologic.app;

import com.agrologic.app.messaging.Message;
import org.apache.commons.io.IOUtils;
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
        String answer = IOUtils.toString(inputStream);
        assertEquals(new String(new byte[]{22, '3', '\r'}), answer);
    }

    private byte[] createKeepAlive() {
        return new byte[]{Message.STX, 'c', 't', 'b',
                Message.RS, 'c', 't', 'b',
                Message.RS, '5', '.', '0', '1', 'W', 'e', '*',
                Message.RS, Message.ETX};
    }
}
