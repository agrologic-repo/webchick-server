package com.agrologic.app.dao;

import com.agrologic.app.model.HistorySetting;

import java.sql.SQLException;
import java.util.List;


public interface HistorySettingDao {
    List<HistorySetting> getHistorySetting(Long programId) throws SQLException;

    List<HistorySetting> getSelectedHistorySetting(Long programId) throws SQLException;

    void saveHistorySetting(List<HistorySetting> hsl) throws SQLException;
}



