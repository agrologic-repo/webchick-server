package com.agrologic.app.service.graph;

import com.agrologic.app.service.history.FlockHistoryService;
import com.agrologic.app.service.history.HistoryService;
import com.agrologic.app.service.history.transaction.FlockHistoryServiceImpl;
import com.agrologic.app.service.history.transaction.HistoryServiceImpl;

/**
 * Created by Valery on 1/12/14.
 */
public class FlockGraphService {
    private HistoryService historyService;
    private FlockHistoryService flockHistoryService;

    public FlockGraphService() {
        historyService = new HistoryServiceImpl();
        flockHistoryService = new FlockHistoryServiceImpl();
    }
}
