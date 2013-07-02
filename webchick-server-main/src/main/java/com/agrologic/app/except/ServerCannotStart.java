package com.agrologic.app.except;

public class ServerCannotStart extends Exception {
    static final String THE_ERROR_IN_THE_TCP_PROTOCOL = "The error in the TCP protocol";

    public ServerCannotStart() {
        this(THE_ERROR_IN_THE_TCP_PROTOCOL);
    }

    public ServerCannotStart(String message) {
        super(message);
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }
}
