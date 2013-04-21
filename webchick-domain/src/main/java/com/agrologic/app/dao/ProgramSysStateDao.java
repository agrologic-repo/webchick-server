
/*
* To change this template, choose Tools | Templates
* and open the template in the editor.
 */
package com.agrologic.app.dao;

//~--- non-JDK imports --------------------------------------------------------

import com.agrologic.app.model.ProgramSystemState;

//~--- JDK imports ------------------------------------------------------------

import java.sql.SQLException;

import java.util.List;
import java.util.Map;
import java.util.SortedMap;

/**
 * Title: IProgSysStateDao <br> Description: <br> Copyright: Copyright (c) 2009 <br> Company: AgroLogic LTD. <br>
 *
 * @author Valery Manakhimov <br>
 * @version 1.1 <br>
 */
public interface ProgramSysStateDao {
    public void insert(ProgramSystemState programSystemState) throws SQLException;

    public void update(ProgramSystemState programSystemState) throws SQLException;

    public void remove(Long dataId, Integer number, Long programId) throws SQLException;

    public void insertSystemStates(Long programId, SortedMap<Long, Map<Integer, String>> systemStateMap)
            throws SQLException;

    public List<ProgramSystemState> getAllProgramSystemStates(Long programId) throws SQLException;

    public List<ProgramSystemState> getAllProgramSystemStates(Long programId, Long langId) throws SQLException;

    public List<ProgramSystemState> getAllProgramSystemStates(Long programId, String[] text) throws SQLException;
}


//~ Formatted by Jindent --- http://www.jindent.com
