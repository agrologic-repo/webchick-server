
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.agrologic.app.dao;

//~--- non-JDK imports --------------------------------------------------------
import com.agrologic.app.model.Flock;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Map;

/**
 * Title: FlockDao <br> Description: <br> Copyright: Copyright (c) 2009 <br> Company: Agro Logic LTD. <br>
 *
 * @author Valery Manakhimov <br>
 * @version 1.1 <br>
 */
public interface FlockDao {

    void insert(Flock flock) throws SQLException;

    void update(Flock flock) throws SQLException;

    void updateFlockDetail(Flock flock) throws SQLException;

    void remove(Long flockId) throws SQLException;

    Flock getById(Long flockId) throws SQLException;

    Flock getOpenFlockByController(Long controllerId) throws SQLException;

    Integer getUpdatedGrowDayHistory(Long flockId) throws SQLException;

    Integer getUpdatedGrowDayHistory24(Long flockId) throws SQLException;

    public Map<String, String> getHistoryN();// throws SQLException;

    void updateHistoryByGrowDay(Long flockId, Integer growDay, String values) throws SQLException;

    void updateHistory24ByGrowDay(Long flockId, Integer growDay, String dnum, String values) throws SQLException;

    void removeHistoryByGrowDay(Long flockId, Integer growDay) throws SQLException;

    void removeHistoryByFlock(Long flockId) throws SQLException;

    void removeHistory24ByFlock(Long flockId) throws SQLException;

    Collection<Flock> getAll() throws SQLException;

    Map<Integer, String> getAllHistoryByFlock(Long flockId) throws SQLException;

    Map<Integer, String> getAllHistoryByFlock(Long flockId, int fromDay, int toDay) throws SQLException;

    Map<Integer, String> getAllHistory24ByFlockAndDnum(Long flockId, String dnum) throws SQLException;
}
//~ Formatted by Jindent --- http://www.jindent.com
