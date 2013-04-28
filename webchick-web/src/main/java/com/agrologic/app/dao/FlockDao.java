package com.agrologic.app.dao;

import com.agrologic.app.model.FlockDto;



import java.sql.SQLException;

import java.util.List;
import java.util.Map;

public interface FlockDao {
    public void insert(FlockDto flock) throws SQLException;

    public void update(FlockDto flock) throws SQLException;

    public void remove(Long flockId) throws SQLException;

    public void close(Long flockId, String endDate) throws SQLException;

    public FlockDto getById(Long flockId) throws SQLException;

    public List<FlockDto> getAllFlocksByController(Long controllerId) throws SQLException;

    public Integer getResetTime(Long flockId) throws SQLException;

    public Integer getResetTime(Long flockId, Integer growDay) throws SQLException;

    public Map<Integer, String> getAllHistoryByFlock(Long flockId) throws SQLException;

    public Map<Integer, String> getAllHistoryByFlock(Long flockId, int fromDay, int toDay) throws SQLException;

    public List<Integer> getHistory24GrowDays(Long flockId) throws SQLException;

    public String getHistory24(Long flockId, Integer growDay, String dn) throws SQLException;

    public String getDNHistory24(String dn) throws SQLException;

//  //    void insert(FlockDto flock) throws SQLException;
//  //    void update(FlockDto flock) throws SQLException;
//  //    void remove(Long flockId) throws SQLException;
//  //
//  //    Map<Integer,String> getHistoryByGrowDay(Long flockId,int... growDays) throws SQLException;
//  //    Map<Integer,String> getAllHistoryByFlock(Long flockId) throws SQLException;
//  //    Map<Integer,String> getAllHistoryByFlock(Long flockId, int fromDay, int toDay) throws SQLException;
//  //    FlockDto getById(Long flockId) throws SQLException;
//  //    List<FlockDto> getAllFlocksByController(Long controllerId) throws SQLException;
//  //    List<Integer> getGrowDaysFromHistory(Long flockId) throws SQLException;
//
//      public void insert(FlockDto flock) throws SQLException;
//      public void update(FlockDto flock) throws SQLException;
//      public void remove(Long flockId) throws SQLException;
//      public FlockDto getById(Long flockId) throws SQLException;
//
//      public List<FlockDto> getAllFlocksByController(Long controllerId) throws SQLException;
//      public Integer getResetTime(Long flockId) throws SQLException;
//      public Integer getResetTime(Long flockId, Integer growDay)  throws SQLException;
//
//      public Map<Integer,String> getAllHistoryByFlock(Long flockId) throws SQLException;
//      public Map<Integer,String> getAllHistoryByFlock(Long flockId, int fromDay, int toDay) throws SQLException;
//      public List<Integer> getHistory24GrowDays(Long flockId)  throws SQLException;
//      public String getHistory24(Long flockId, Integer growDay, String dn) throws SQLException;
}



