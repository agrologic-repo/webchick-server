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
     * Search program by program id and return true if program exist , otherwise false;
     *
     * @param id the program id
     * @return true if program exist , otherwise false
     * @throws SQLException if failed to get the program from the database
     */
    boolean programExist(Long id) throws SQLException;

    /**
     * Gets program by it id
     *
     * @param id the program id
     * @return program an objects that encapsulates a program attributes
     * @throws SQLException if failed to retrieve program from the database
     */
    Program getById(Long id) throws SQLException;

    /**
     * Retrieves all Program in no special order .
     *
     * @return Program a vector of Program objects, each object reflects a row in table Program.
     * @throws SQLException if failed to retrieve all Program from the database
     */
    Collection<Program> getAll() throws SQLException;
}


