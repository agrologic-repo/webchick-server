package com.agrologic.app.messaging;

import com.agrologic.app.util.ByteUtil;
import org.apache.commons.lang.Validate;

import java.util.Arrays;
import java.util.List;

public class KeepAliveMessage {

    public static final String BUUFER_IS_NULL_MSG = "Buffer must not be null";
    public static final String WRONG_FORMAT_MSG = "Message does not match the required format";
    private final String username;
    private final String password;
    private final String version;

    public KeepAliveMessage(String username, String password, String version) {
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

    public static KeepAliveMessage parseIncomingBytes(byte[] payload) throws WrongMessageFormatException,
            IllegalArgumentException {

        Validate.notNull(payload, BUUFER_IS_NULL_MSG);

        int stx = ByteUtil.indexOf(payload, Message.STX);
        int etx = ByteUtil.indexOf(payload, Message.ETX);
        if (stx < 0 || etx < 0) {
            throw new WrongMessageFormatException(WRONG_FORMAT_MSG);
        }
        byte[] data = Arrays.copyOfRange(payload, stx + 1, etx);
        List<byte[]> dataList = ByteUtil.split(data, Message.RS);

        if (dataList.size() < 2) {
            throw new WrongMessageFormatException(WRONG_FORMAT_MSG);
        }

        int PASS_INDEX = 0;
        int NAME_INDEX = 1;
        int VERS_INDEX = 2;
        String psswd, name, vers = "N/A";
        data = dataList.get(PASS_INDEX);
        psswd = new String(data, 0, data.length);
        data = dataList.get(NAME_INDEX);
        name = new String(data, 0, data.length);
        if (dataList.size() > VERS_INDEX) {
            data = dataList.get(VERS_INDEX);
            vers = new String(data, 0, data.length);
        }

        return new KeepAliveMessage(name, psswd, vers);
    }
}
