package com.agrologic.app.dao;

import com.agrologic.app.model.Data;
import com.agrologic.app.model.ProgramRelay;
import com.agrologic.app.model.Relay;
import java.sql.SQLException;
import java.util.Collection;

public interface RelayDao {

    /**
     * Insert a new relay row to table relay names .
     *
     * @param relay an objects that encapsulates an relay attributes .
     * @throws SQLException if failed to insert new relay to the database .
     */
    void insert(Relay relay) throws SQLException;

    /**
     * Insert relay names
     *
     * @param relayList the relay list
     * @throws SQLException if failed to insert to the relay table
     */
    void insert(Collection<Relay> relayList) throws SQLException;

    /**
     * Insert relay translation row to relay by language table. <br> This is a dictionary of relay types .
     *
     * @param relayId the id of relays
     * @param langId the id of language
     * @param translation the translation text
     * @throws SQLException if failed to insert to the relay by language table
     */
    void insertTranslation(Long relayId, Long langId, String translation) throws SQLException;

    /**
     * Insert relay list translation to relay by language table. <br> This is a dictionary of relay types .
     *
     * @param relayList the relay list
     * @throws SQLException if failed to insert to the relay by language table
     */
    void insertTranslation(Collection<Relay> relayList) throws SQLException;

    /**
     * Insert program relays to program relay table .
     *
     * @param programRelayList the program relays
     * @throws SQLException if failed to insert to the program relay table
     */
    void insertProgramRelays(Collection<ProgramRelay> programRelayList) throws SQLException;

    /**
     * Updates an existing relay row in table relay
     *
     * @param relay an object that encapsulates an relay attributes
     * @throws SQLException if failed to update the relay in the database
     */
    void update(Relay relay) throws SQLException;

    /**
     * Update relay data
     *
     * @param relayDatas
     * @throws SQLException
     */
    void update(Collection<Data> relayDatas) throws SQLException;

    /**
     * Removes an relay from the database
     *
     * @param id the id of the relay to be removed from the database
     * @throws SQLException if failed to remove the relay from the database
     */
    void remove(Long id) throws SQLException;

    /**
     * Get the relay by id
     *
     * @param id the id of relay object
     * @return relay an objects that encapsulates an relay attributes
     * @throws SQLException if failed to insert to the relay by language table
     */
    Relay getById(Long id) throws SQLException;

    /**
     * Retrieves all relays
     *
     * @return relays a Collection of Relay objects, each object reflects a row in table relay
     * @throws SQLException if failed to retrieve all relay from the database
     */
    Collection<Relay> getAll() throws SQLException;

    /**
     * Retrieves all relays by language
     *
     * @return relays a Collection of Relay objects, each object reflects a row in table relay
     * @throws SQLException if failed to retrieve all relay from the database
     */
    Collection<Relay> getAll(Long langId) throws SQLException;

    /**
     * Retrieves all relays with translation
     *
     * @return relays a Collection of Relay objects, each object reflects a row in table relay
     * @throws SQLException if failed to retrieve all relay from the database
     */
    Collection<Relay> getAllWithTranslation() throws SQLException;

    /**
     * Retrieves program relays by program id.
     *
     * @param programId the program id
     * @return programRelays a Collection of ProgramRelay objects, each object reflects a row of program relay
     * @throws SQLException if failed to retrieve all program relay from the database
     */
    Collection<ProgramRelay> getAllProgramRelays(Long programId) throws SQLException;

    /**
     * Retrieves selected program relays by program id that was selected .
     *
     * @param programId the program id
     * @return programRelays a Collection of ProgramRelay object , each object reflects a row in table programrelays
     * @throws SQLException if failed to retrieve all program relays from the database
     */
    Collection<ProgramRelay> getSelectedProgramRelays(Long programId) throws SQLException;

    /**
     * Retrieves selected program relays by program id and language id.
     *
     * @param langId the language id
     * @param programId the program id
     * @return programRelays a Collection of ProgramRelay object , each object reflects a row in table programrelays
     * @throws SQLException if failed to retrieve all program relays from the database
     */
    Collection<ProgramRelay> getSelectedProgramRelays(Long programId, Long langId) throws SQLException;
}


