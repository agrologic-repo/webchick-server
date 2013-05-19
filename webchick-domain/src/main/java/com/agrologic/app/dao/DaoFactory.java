package com.agrologic.app.dao;

import java.sql.Connection;
import java.sql.SQLException;

public abstract class DaoFactory {

    /**
     * Returns a connection to the DB from the connection pool.
     *
     * @return Connection object.
     */
    public abstract Connection getConnection();

    /**
     * Releases the connection into the connection pool.
     *
     * @param con The connection to be released
     */
    public abstract void closeConnection(Connection con);

    /**
     *  Close all connection that in connection pool
     */
    public abstract void closeAllConnection();


    /**
     * The following method outputs the SQLState, error code, error description, and cause (if there is one) contained
     * in the SQLException as well as any other exception chained to it.
     *
     * @param ex the sql exception.
     */
    public void printSQLException(final SQLException ex) {
        if (ex == null) {
            throw new NullPointerException();
        }

        for (Throwable e : ex) {
            if (e instanceof SQLException) {
                if (ignoreSQLException(((SQLException) e).getSQLState()) == false) {
                    e.printStackTrace(System.err);
                    System.out.println("SQLState: " + ((SQLException) e).getSQLState());
                    System.out.println("Error Code: " + ((SQLException) e).getErrorCode());
                    System.out.println("Message: " + ((SQLException) e).getMessage());
                    Throwable t = ex.getCause();
                    while (t != null) {
                        System.out.println("Cause: " + t);
                        t = t.getCause();
                    }
                }
            }
        }
    }

    /**
     * Retrieve the SQLState then process the SQLException accordingly.
     *
     * @param sqlState the state for sql exception
     * @return true if jar file or table exist, otherwise return false
     */
    public boolean ignoreSQLException(final String sqlState) {

        // X0Y32: Jar file already exists in schema
        if (sqlState.equalsIgnoreCase("X0Y32")) {
            return true;
        }

        // 42Y55: Table already exists in schema
        if (sqlState.equalsIgnoreCase("42Y55")) {
            return true;
        }

        return false;
    }
    public abstract ControllerDao getControllerDao();
    public abstract DataDao getDataDao();
}
