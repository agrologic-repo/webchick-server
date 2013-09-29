package com.agrologic.app.network;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ClientSessionsSizeMonitor implements Runnable {
    private final Logger logger = LoggerFactory.getLogger(ClientSessionsSizeMonitor.class);
    private ClientSessions clientSessions;

    public ClientSessionsSizeMonitor(ServerThread serverThread) {
        this.clientSessions = serverThread.getClientSessions();
    }

    @Override
    public void run() {
        int size = clientSessions.getSessions().size();
        while (!Thread.currentThread().interrupted()) {
            if (size != clientSessions.getSessions().size()) {
                logger.debug("Current thread in system =  : " + clientSessions.getSessions().size());
                size = clientSessions.getSessions().size();
            }
        }
    }
}
