
package com.agrologic.app.dao;

import com.agrologic.app.model.ProgramDto;



import java.sql.SQLException;

import java.util.List;

public interface ProgramDao {
    public void insert(ProgramDto program) throws SQLException;

    public void update(ProgramDto program) throws SQLException;

    public void remove(Long id) throws SQLException;

    public int count() throws SQLException;

    public boolean checkNewProgramId(Long id) throws SQLException;

    public ProgramDto getById(Long id) throws SQLException;

    public List<ProgramDto> getAll() throws SQLException;

    public List<ProgramDto> getAll(String searchText) throws SQLException;

    public List<ProgramDto> getAllByUserId(String searchText, Long userId) throws SQLException;

    public List<ProgramDto> getAllByUserCompany(String searchText, String company) throws SQLException;

    public List<ProgramDto> getAll(String searchText, String index) throws SQLException;
}



