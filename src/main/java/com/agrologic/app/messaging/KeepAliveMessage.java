package com.agrologic.app.messaging;

import com.agrologic.app.util.ByteUtil;

import java.util.Arrays;
import java.util.List;

public class KeepAliveMessage {
    private final String username;
    private final String password;
    private final String version;


    public KeepAliveMessage( String username, String password, String version) {
        this.version = version;
        this.password = password;
        this.username = username;
    }

    public String getVersion() {
        return version;
    }

    public String getPassword() {
        return password;
    }

    public String getUsername() {
        return username;
    }

    public static KeepAliveMessage parseIncomingBytes(byte[] payload) throws WrongMessageFormatException {
        int stx = ByteUtil.indexOf(payload, Message.STX);
        int etx = ByteUtil.indexOf(payload, Message.ETX);
        if (stx < 0 || etx < 0) {
            throw new WrongMessageFormatException("Keep alive of wrong format came");
        }
        byte[] data = Arrays.copyOfRange(payload, stx + 1, etx);
        List<byte[]> dataList = ByteUtil.split(data, Message.RS);

        int PASS_INDEX = 0;
        int NAME_INDEX = 1;
        int VERS_INDEX = 2;
        data = dataList.get(PASS_INDEX);
        String psswd = new String(data, 0, data.length);
        data = dataList.get(NAME_INDEX);
        String name = new String(data, 0, data.length);
        String vers = "N/A";
        if (dataList.size() > VERS_INDEX) {
            data = dataList.get(VERS_INDEX);
            vers = new String(data, 0, data.length);
        }
        return new KeepAliveMessage(name, psswd, vers);
    }
}
