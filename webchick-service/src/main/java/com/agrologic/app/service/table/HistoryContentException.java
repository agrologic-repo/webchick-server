package com.agrologic.app.service.table;

import com.agrologic.app.exception.GeneralException;

/**
 * Created by Valery on 1/7/14.
 */
public class HistoryContentException extends GeneralException {

    public HistoryContentException(String message, Exception e) {
        super(message);
        setStackTrace(e.getStackTrace());
    }
}
