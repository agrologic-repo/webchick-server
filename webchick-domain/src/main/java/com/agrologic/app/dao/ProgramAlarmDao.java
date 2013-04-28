
/*
* To change this template, choose Tools | Templates
* and open the template in the editor.
 */
package com.agrologic.app.dao;


import com.agrologic.app.model.ProgramAlarm;


import java.sql.SQLException;

import java.util.List;
import java.util.Map;

/**
 * Title: IProgramAlarmDao <br> Description: <br> Copyright: Copyright (c) 2009 <br> Company: AgroLogic LTD. <br>
 *
 * @author Valery Manakhimov <br>
 * @version 1.1 <br>
 */
public interface ProgramAlarmDao {
    public void insert(ProgramAlarm programAlarm) throws SQLException;

    public void update(ProgramAlarm programAlarm) throws SQLException;

    public void remove(Long dataId, Integer digitNumber, Long programId) throws SQLException;

    public void insertAlarms(Long programId, Map<Long, Map<Integer, String>> alarmMap) throws SQLException;

    public List<ProgramAlarm> getAllProgramAlarms(Long programId) throws SQLException;

    public List<ProgramAlarm> getAllProgramAlarms(Long programId, Long langId) throws SQLException;

    public List<ProgramAlarm> getAllProgramAlarms(Long programId, String[] text) throws SQLException;
}


