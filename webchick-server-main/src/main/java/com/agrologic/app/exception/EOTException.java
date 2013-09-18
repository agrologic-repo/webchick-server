package com.agrologic.app.exception;

public class EOTException extends Exception {
    static final String EOT_ERROR = "End of transmission character was not received during reading response bytes.";

    public EOTException(String message) {
        super(message);
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }
}



