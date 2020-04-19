package com.agrologic.app.network;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by valery on 2/1/20.
 */
public class SocketThreadDebugLogger {
    private Logger logger = LoggerFactory.getLogger(SocketThreadDebugLogger.class);

    public void printLog(String logString) {
        logger.info(logString);
    }
}