package com.agrologic.app.dao;

import com.agrologic.app.model.LanguageDto;

import java.sql.SQLException;
import java.util.List;

public interface LanguageDao {
    public Long getLanguageId(String l) throws SQLException;

    public LanguageDto getById(Long langId) throws SQLException;

    public List<LanguageDto> geAll() throws SQLException;
}



