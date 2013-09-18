package com.agrologic.app.exception;

public class ObjectDoesNotExist extends Exception {
    private static final long serialVersionUID = 1L;

    public ObjectDoesNotExist(String message) {
        super(message);
    }

    @Override
    public String toString() {
        return super.getMessage();
    }
}


