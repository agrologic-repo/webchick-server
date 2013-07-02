package com.agrologic.app.dao;

import com.agrologic.app.model.ControllerDto;

import java.sql.SQLException;
import java.util.List;

public interface ControllerDao {

    public void insert(ControllerDto controller) throws SQLException;

    public void update(ControllerDto controller) throws SQLException;

    public void remove(Long id) throws SQLException;

    public void sendNewDataValueToController(Long controllerId, Long dataId, Long value) throws SQLException;

    public void saveNewDataValueOnController(Long controllerId, Long dataId, Long value) throws SQLException;

    public void removeControllerData(Long controllerId) throws SQLException;

    public String getControllerGraph(Long controllerId) throws SQLException;

    public boolean isDataReady(Long userId) throws SQLException;

    public boolean isControllerDataReady(Long controllerId) throws SQLException;

    public ControllerDto getById(Long id) throws SQLException;

    public List<String> getControllerNames() throws SQLException;

    public List<ControllerDto> getAll() throws SQLException;

    public List<ControllerDto> getAllByCellinkId(Long cellinkId) throws SQLException;

    public List<ControllerDto> getAllActiveByCellinkId(Long cellinkId) throws SQLException;
}