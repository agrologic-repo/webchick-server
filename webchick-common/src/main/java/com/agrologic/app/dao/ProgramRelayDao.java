package com.agrologic.app.dao;

import com.agrologic.app.model.ProgramRelay;

import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * DAO for the {@link  com.agrologic.app.model.ProgramRelay}. It provides all CRUD operations to work with
 * {@link com.agrologic.app.model.ProgramRelay} objects.
 *
 * @author Valery Manakhimov
 */

public interface ProgramRelayDao {
    /**
     * Insert a new program relay row to table program relays .
     *
     * @param programRelay an objects that encapsulates an relay attributes .
     * @throws java.sql.SQLException if failed to insert new relay to the database .
     */
    public void insert(ProgramRelay programRelay) throws SQLException;

    /**
     * Update program relay
     *
     * @param programRelay the program relay with new data
     * @throws java.sql.SQLException if failed to update the program relays
     */
    public void update(ProgramRelay programRelay) throws SQLException;

    /**
     * Remove program relay with given program id,  data id and digit number .
     *
     * @param dataId    the data id of program relay
     * @param bitNumber the digit number
     * @param programId the program id
     * @throws java.sql.SQLException if failed to remove the program relays
     */
    public void remove(Long dataId, Integer bitNumber, Long programId) throws SQLException;

    /**
     * Save program relays that was assigned by user to given program .
     *
     * @param programId the program id
     * @param relayMap  the map with program relay id and selected bit numbers .
     * @throws java.sql.SQLException
     */
    public void assignRelaysToGivenProgram(Long programId, Map<Long, Map<Integer, String>> relayMap) throws SQLException;

    /**
     * Insert program relays collection
     *
     * @param programRelays the list of program relays
     * @throws java.sql.SQLException if failed to insert to the program relays collection
     */
    public void insert(Collection<ProgramRelay> programRelays) throws SQLException;

//    public List<Long> getRelayNumberTypes(Long programId) throws SQLException;

    /**
     * Retrieves program relays by program id.
     *
     * @param programId the program id
     * @return programAlarms a collection the list of program relays
     * @throws java.sql.SQLException if failed to retrieve all program relay from the database
     */
    public List<ProgramRelay> getAllProgramRelays(Long programId) throws SQLException;

    /**
     * Retrieves selected program relay by program id and language id.
     *
     * @param programId the program id
     * @param langId    the language id
     * @return Collection of ProgramRelay object , each object reflects a row in table program relays
     * @throws java.sql.SQLException
     */
    public List<ProgramRelay> getAllProgramRelays(Long programId, Long langId) throws SQLException;

    /**
     * Retrieves selected program relay by program id and text
     *
     * @param programId the program id
     * @param text
     * @return the list of program relays
     * @throws java.sql.SQLException if failed to retrieve all program relay from the database
     */
    public List<ProgramRelay> getAllProgramRelays(Long programId, String[] text) throws SQLException;
}



