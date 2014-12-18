package com.agrologic.app.dao;

import com.agrologic.app.model.Relay;

import java.sql.SQLException;
import java.util.Collection;

/**
 * DAO for the {@link  com.agrologic.app.model.Relay}. It provides all CRUD operations to work with
 * {@link com.agrologic.app.model.Relay} objects.
 *
 * @author Valery Manakhimov
 */
public interface RelayDao {

    /**
     * Insert a new relay row to table relay names .
     *
     * @param relay an objects that encapsulates an relay attributes .
     * @throws java.sql.SQLException if failed to insert new relay to the database .
     */
    void insert(Relay relay) throws SQLException;

    /**
     * Updates an existing relay row in table relay
     *
     * @param relay an object that encapsulates an relay attributes
     * @throws java.sql.SQLException if failed to update the relay in the database
     */
    void update(Relay relay) throws SQLException;

    /**
     * Removes an relay from the database
     *
     * @param id the id of the relay to be removed from the database
     * @throws java.sql.SQLException if failed to remove the relay from the database
     */
    void remove(Long id) throws SQLException;

    /**
     * Insert relay names
     *
     * @param relayList the relay list
     * @throws java.sql.SQLException if failed to insert to the relay table
     */
    void insert(Collection<Relay> relayList) throws SQLException;

    /**
     * Insert relay translation row to relay by language table. <br> This is a dictionary of relay types .
     *
     * @param relayId     the id of relays
     * @param langId      the id of language
     * @param translation the translation text
     * @throws java.sql.SQLException if failed to insert to the relay by language table
     */
    void insertTranslation(Long relayId, Long langId, String translation) throws SQLException;

    /**
     * Insert relay list translation to relay by language table. <br> This is a dictionary of relay types .
     *
     * @param relayList the relay list
     * @throws java.sql.SQLException if failed to insert to the relay by language table
     */
    void insertTranslation(Collection<Relay> relayList) throws SQLException;

    /**
     * Get the relay by id
     *
     * @param id the id of relay object
     * @return relay an objects that encapsulates an relay attributes
     * @throws java.sql.SQLException if failed to insert to the relay by language table
     */
    Relay getById(Long id) throws SQLException;

    /**
     * Retrieves all relays
     *
     * @return relays a Collection of Relay objects, each object reflects a row in table relay
     * @throws java.sql.SQLException if failed to retrieve all relay from the database
     */
    Collection<Relay> getAll() throws SQLException;

    /**
     * Retrieves all relays by language
     *
     * @return relays a Collection of Relay objects, each object reflects a row in table relay
     * @throws java.sql.SQLException if failed to retrieve all relay from the database
     */
    Collection<Relay> getAll(Long langId) throws SQLException;

    /**
     * Retrieves all relays with translation
     *
     * @return relays a Collection of Relay objects, each object reflects a row in table relay
     * @throws java.sql.SQLException if failed to retrieve all relay from the database
     */
    Collection<Relay> getAllWithTranslation() throws SQLException;

    /**
     * Copy relays of selected program
     *
     * @param newProgramId the id of added program
     * @param selectedProgramId the id of selected program to get data data from it
     * @throws java.sql.SQLException if failed to execute the query
     */
    void copyRelays(Long newProgramId, Long selectedProgramId);
}


