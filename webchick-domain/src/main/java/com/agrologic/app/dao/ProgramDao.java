package com.agrologic.app.dao;

import com.agrologic.app.model.Program;

import java.sql.SQLException;
import java.util.Collection;

public interface ProgramDao {

    /**
     * Inserts a new program row to table program
     *
     * @param program an objects that encapsulates a program attributes
     * @throws SQLException if failed to insert new program to the database
     */
    void insert(Program program) throws SQLException;

    /**
     * Updates an existing program row in table program
     *
     * @param program an object that encapsulates a program attributes
     * @throws SQLException if failed to update the program in the database
     */
    void update(Program program) throws SQLException;

    /**
     * Removes a program from the database
     *
     * @param id the id of the program to be removed from the database
     * @throws SQLException if failed to remove the program from the database
     */
    void remove(Long id) throws SQLException;

    /**
     * Return number of programs in database
     *
     * @return number of programs
     * @throws SQLException if failed to execute query
     */
    Integer count() throws SQLException;

    /**
     * Return number of programs in database with searched text
     *
     * @param searchText the search text
     * @return number of programs
     * @throws SQLException if failed to execute query
     */
    Integer count(String searchText) throws SQLException;

    /**
     * Return true if program with given id exist in database
     *
     * @param id program id
     * @return true if program exist , otherwise false
     * @throws SQLException if failed to execute query
     */
    Boolean isProgramWithGivenIdExist(Long id) throws SQLException;

    /**
     * Gets program by it id
     *
     * @param id the program id
     * @return program an objects that encapsulates a program attributes
     * @throws SQLException if failed to retrieve program from the database
     */
    Program getById(Long id) throws SQLException;

    /**
     * Retrieves all programs in no special order .
     *
     * @return Program a collection of Program objects, each object reflects a row in table Program.
     * @throws SQLException if failed to retrieve all Program from the database
     */
    Collection<Program> getAll() throws SQLException;

    Collection<Program> getAll(String searchText) throws SQLException;

    Collection<Program> getAllByUserId(String searchText, Long userId) throws SQLException;

    Collection<Program> getAllByUserCompany(String searchText, String company) throws SQLException;

    Collection<Program> getAll(String searchText, String index) throws SQLException;

}


