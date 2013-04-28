package com.agrologic.app.dao;

import com.agrologic.app.model.HistorySettingDto;

import java.sql.SQLException;
import java.util.List;


public interface HistorySettingDao {
    List<HistorySettingDto> getHistorySetting(Long programId) throws SQLException;

    List<HistorySettingDto> getSelectedHistorySetting(Long programId) throws SQLException;

    void saveHistorySetting(List<HistorySettingDto> hsl) throws SQLException;
}



