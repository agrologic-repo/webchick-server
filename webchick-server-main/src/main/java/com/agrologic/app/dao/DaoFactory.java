
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.agrologic.app.dao;

//~--- non-JDK imports --------------------------------------------------------
import com.agrologic.app.dao.derby.DerbyDaoFactory;
import com.agrologic.app.dao.mysql.MySqlDaoFactory;

//~--- JDK imports ------------------------------------------------------------

import java.sql.Connection;
import java.sql.SQLException;

/**
 * {Insert class description here}
 *
 * @version $Revision: 1.1.1.1 $
 * @since Build {insert version here} (MM YYYY)
 * @author Valery Manakhimov
 * @author $Author: nbweb $, (this version)
 */
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

    // List of DAO types supported by the factory
    // There will be a method for each DAO that can be
    // created. The concrete factories will have to
    // implement these methods.
    // There will be a method for each DAO that can be
    // created. The concrete factories will have to
    // implement these methods.
    public static DaoFactory getDaoFactory(DaoType daoType) {
        switch (daoType) {
            case DERBY:
                return DerbyDaoFactory.instance();

            case MYSQL:
                return MySqlDaoFactory.instance();

            default:
                return null;
        }
    }

    public abstract SchemaDao getSchemaDao();

    public abstract UserDao getUserDao();

    public abstract CellinkDao getCellinkDao();

    public abstract ControllerDao getControllerDao();

    public abstract ProgramDao getProgramDao();

    public abstract ScreenDao getScreenDao();

    public abstract TableDao getTableDao();

    public abstract DataDao getDataDao();

    public abstract AlarmDao getAlarmDao();

    public abstract RelayDao getRelayDao();

    public abstract SystemStateDao getSystemStateDao();

    public abstract LanguageDao getLanguageDao();

    public abstract FlockDao getFlockDao();

    public abstract DistribDao getDistribDao();

    public abstract FeedDao getFeedDao();

    public abstract FeedTypeDao getFeedTypeDao();

    public abstract FuelDao getFuelDao();

    public abstract LaborDao getLaborDao();

    public abstract GasDao getGasDao();

    public abstract MedicineDao getMedicineDao();

    public abstract SpreadDao getSpreadDao();

    public abstract TransactionDao getTransactionDao();

    public abstract WorkerDao getWorkerDao();
}


//~ Formatted by Jindent --- http://www.jindent.com
