package com.agrologic.app.model.history;

import java.util.Map;

/**
 * Created by Valery on 12/30/13.
 */
public interface HistoryData {

    /**
     * Return history data title
     *
     * @return the title
     */
    public String getTitle();

    /**
     * Map of history data values
     *
     * @return the
     */
    public Map<Integer, Integer> getValues();
}
