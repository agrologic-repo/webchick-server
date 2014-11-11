package com.agrologic.app;

import com.agrologic.app.dao.CellinkDao;
import com.agrologic.app.messaging.Message;
import com.agrologic.app.model.Cellink;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

public class FakeMessageSystemTest {
    private InputStream inputStream;
    private OutputStream outputStream;
    private CellinkDao cellinkDao = mock(CellinkDao.class);

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

//    @Test
//    public void requestFromServerAccepted() throws IOException, InterruptedException {
//        sendKeepAlive();
//        byte[] readBuffer = new byte[256];
//        int length = inputStream.read(readBuffer);
//        String request = new String(readBuffer, 0, length);
//        assertEquals("V01%T901a 111\r", request);
//    }

//    private byte[] sendAnswer() {
//
//    }

    private byte[] createKeepAlive() {
        return new byte[]{Message.ProtocolBytes.STX.getValue(), 'c', 't', 'b',
                Message.ProtocolBytes.RS.getValue(), 'D', 'E', 'M', 'O', '-', '2', 'G',
                Message.ProtocolBytes.RS.getValue(), '1', '.', '0', '0', 'G', 'h', '*',
                Message.ProtocolBytes.RS.getValue(), Message.ProtocolBytes.ETX.getValue()};
    }

    private Collection<byte[]> createKeepAliveList() throws SQLException {
        Collection<Cellink> cellinkCollection = cellinkDao.getAll();
        Collection<byte[]> keepAliveCollection = new ArrayList<byte[]>();


        for(Cellink cellink:cellinkCollection) {
            keepAliveCollection.add(createKeepAlive(cellink));
        }


        return keepAliveCollection;
    }

    private byte[] createKeepAlive(Cellink cellink) {
        String name = cellink.getName();
        String pass = cellink.getPassword();
        String version = "1.00Gh*";

        byte[] keepalive = new byte[256];

        int i = 0;
        keepalive[i++] = Message.ProtocolBytes.STX.getValue();

        keepalive[i++] = Message.ProtocolBytes.RS.getValue();
        for(byte c:pass.getBytes()) {
            keepalive[i++] = c;
        }

        keepalive[i++] = Message.ProtocolBytes.RS.getValue();
        for(byte c:name.getBytes()) {
            keepalive[i++] = c;
        }
        keepalive[i++] = Message.ProtocolBytes.RS.getValue();
        for(byte c:version.getBytes()) {
            keepalive[i++] = c;
        }
        keepalive[i++] = Message.ProtocolBytes.RS.getValue();
        keepalive[i] = Message.ProtocolBytes.ETX.getValue();

        return keepalive;
    }
}
