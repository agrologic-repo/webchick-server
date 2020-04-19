package com.agrologic.app.exception;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * Created by valery on 4/18/20.
 */
public class GraphLoadingException  extends Exception {
    public GraphLoadingException(String message) {
        super(message);
    }

    /**
     * Return stack trace with message,To format stack trace for logging, For easy viewing of text log
     *
     * @param message stack trace's message
     * @return stack trace with message
     */
    private String getStackTrace(String message) {
        StringWriter sw = new StringWriter();
        new Throwable(message).printStackTrace(new PrintWriter(sw));
        return sw.toString();
    }
}
