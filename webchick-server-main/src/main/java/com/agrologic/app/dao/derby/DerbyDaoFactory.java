
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.agrologic.app.dao.derby;

//~--- non-JDK imports --------------------------------------------------------
import com.agrologic.app.dao.*;
import com.agrologic.app.dao.derby.impl.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * {Insert class description here}
 *
 * @version $Revision: 1.1.1.1 $
 * @since Build {insert version here} (MM YYYY)
 * @author Valery Manakhimov
 * @author $Author: nbweb $, (this version)
 */
public class DerbyDaoFactory extends DaoFactory {

    private static final String DRIVER = "org.apache.derby.jdbc.EmbeddedDriver";
    private static final String SCHEMA = "AGRODB";
    private static final String URL = "jdbc:derby:agrodb;create=true";
    private static DerbyDaoFactory instanceObject = null;
    private static final long serialVersionUID = 1L;
    private static final boolean DEBUG = false;

    /**
     * This static block causes the class loader to load the jdbcDriver.
     */
    static {
        try {
            if (DEBUG) {
                System.out.println("Starting JDBC driver...");
            }
            Class.forName(DRIVER).newInstance();
        } catch (Exception ex) {
            if (DEBUG) {
                System.out.println("jdbc : Driver Class not found, " + ex.getMessage());
            }
        }
    }

    private DerbyDaoFactory() {
    }

    public static DerbyDaoFactory instance() {
        if (instanceObject == null) {
            synchronized (DerbyDaoFactory.class) {
                instanceObject = new DerbyDaoFactory();
            }
        }

        return instanceObject;
    }

    /**
     * Returns a connection to the DB from the connection pool.
     *
     * @return Connection object.
     */
    @Override
    public Connection getConnection() {
        return ConnectionPool.getInstance().getPoolConnection();
    }

    /**
     * Releases the connection into the connection pool.
     *
     * @param con The connection to be released
     */
    @Override
    public void closeConnection(Connection con) {
        ConnectionPool.getInstance().closeConnectionPool(con);
    }

    /**
     * Close all connection that in the connection pool
     */
    public void closeAllConnection() {
        ConnectionPool.getInstance().closeAllConnectionPool();
    }

    /**
     * The following method outputs the SQLState, error code, error description,
     * and cause (if there is one) contained in the SQLException as well as any
     * other exception chained to it.
     *
     * @param ex the sql exception.
     */
    @Override
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
    @Override
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

    /**
     * Retrieve the mysql installed version.
     *
     * @return version the of installed mysql
     */
    public static String getMySQLVersion() {
        String sqlQuery = "SELECT VERSION()";
        Statement stmt = null;
        Connection con = null;
        String version = "";

        try {
            con = DriverManager.getConnection("jdbc:derby:test;create=true");
            stmt = con.createStatement();

            ResultSet rs = stmt.executeQuery(sqlQuery);

            if (rs.next()) {
                version = rs.getString(1);
            }
        } catch (SQLException e) {
            System.out.println("Cannot Get MySQL Version" + e.getMessage());
        } finally {
            try {
                stmt.close();
                con.close();
            } catch (SQLException ex) {
            }
        }

        int pidx = version.lastIndexOf(".");

        version = version.substring(0, pidx);

        return version;
    }

    @Override
    public SchemaDao getSchemaDao() {
        return new DerbySchemaDaoImpl(this);
    }

    @Override
    public UserDao getUserDao() {
        return new DerbyUserDaoImpl(this);
    }

    @Override
    public CellinkDao getCellinkDao() {
        return new DerbyCellinkDaoImpl(this);
    }

    @Override
    public ControllerDao getControllerDao() {
        return new DerbyControllerDaoImpl(this);
    }

    @Override
    public ProgramDao getProgramDao() {
        return new DerbyProgramDaoImpl(this);
    }

    @Override
    public ScreenDao getScreenDao() {
        return new DerbyScreenDaoImpl(this);
    }

    @Override
    public TableDao getTableDao() {
        return new DerbyTableDaoImpl(this);
    }

    @Override
    public DataDao getDataDao() {
        return new DerbyDataDaoImpl(this);
    }

    @Override
    public AlarmDao getAlarmDao() {
        return new DerbyAlarmDaoImpl(this);
    }

    @Override
    public RelayDao getRelayDao() {
        return new DerbyRelayDaoImpl(this);
    }

    @Override
    public SystemStateDao getSystemStateDao() {
        return new DerbySystemStateDaoImpl(this);
    }

    @Override
    public LanguageDao getLanguageDao() {
        return new DerbyLanguageDaoImpl(this);
    }

    @Override
    public FlockDao getFlockDao() {
        return new DerbyFlockDaoImpl(this);
    }

    @Override
    public DistribDao getDistribDao() {
        return new DerbyDistribDaoImpl(this);
    }

    @Override
    public FeedDao getFeedDao() {
        return new DerbyFeedDaoImpl(this);
    }

    @Override
    public FeedTypeDao getFeedTypeDao() {
        return new DerbyFeedTypeDaoImpl(this);
    }

    @Override
    public FuelDao getFuelDao() {
        return new DerbyFuelDaoImpl(this);
    }

    @Override
    public LaborDao getLaborDao() {
        return new DerbyLaborDaoImpl(this);
    }

    @Override
    public GasDao getGasDao() {
        return new DerbyGasDaoImpl(this);
    }

    @Override
    public MedicineDao getMedicineDao() {
        return new DerbyMedicineDaoImpl(this);
    }

    @Override
    public SpreadDao getSpreadDao() {
        return new DerbySpreadDaoImpl(this);
    }

    @Override
    public TransactionDao getTransactionDao() {
        return new DerbyTransactionDaoImpl(this);
    }

    @Override
    public WorkerDao getWorkerDao() {
        return new DerbyWorkerDaoImpl(this);
    }

    /**
     * The ConnectionPool class is an inner static class implements which is a
     * Singleton class that manages the connection pool architecture.
     *
     * @author Valery
     * @version 1.0
     */
    static final class ConnectionPool {

        // Max connections in SQL server 200 unless we configure it differently
        private static final int MAX_CONNECTIONS = 200;
        private static Connection con = null;
        private static int currentConnectionsInSystem = 0;
        private static List<Connection> pool = new ArrayList<Connection>();
        private static ConnectionPool instance;

        /**
         * Default Constructor.
         */
        private ConnectionPool() {
        }

        /**
         * Gets a reference of the connection pool.
         *
         * @return ConnectionPool object.
         */
        public static ConnectionPool getInstance() {
            synchronized (ConnectionPool.class) {
                if (instance == null) {
                    instance = new ConnectionPool();
                }
            }

            return instance;
        }

        /**
         * Acquires a connection object from the pool. In case the pool is empty
         * creating a new connection object
         *
         * @return Connection object
         */
        public synchronized Connection getPoolConnection() {
            if ((pool.isEmpty()) && (currentConnectionsInSystem < MAX_CONNECTIONS)) {
                if ((currentConnectionsInSystem + 10) >= MAX_CONNECTIONS) {
                    System.out.println(
                            "pool : Pool size is nearly full.  Please enlarge pool size or lower system load.");
                }

                try {
                    con = DriverManager.getConnection(URL);
                    currentConnectionsInSystem++;
                    return con;
                } catch (Exception ex) {
                    ex.printStackTrace();
                    System.out.println("pool : Cannot establish a connection to the DB. DB URL = " + URL);
                    return null;
                }
            } else if (pool.isEmpty()) {
                System.out.println("pool : Pool size has reached his limits, thus can't get a connection immediately.");
                if (DEBUG) {
                    System.out.println("pool : Waiting in a loop until a connection will be close.");
                }

                while (true) {
                    if (!pool.isEmpty()) {
                        System.out.println("pool : Exit infinite loop, and get a connection from pool.");
                        con = pool.get(pool.size() - 1);
                        pool.remove(pool.size() - 1);
                        return con;
                    }
                }
            } else {
                if (DEBUG) {
                    System.out.println("pool : Getting current connection from the pool.");
                }

                con = pool.get(pool.size() - 1);
                pool.remove(pool.size() - 1);

                return con;
            }
        }

        /**
         * Add a connection to the pool.
         *
         * @param con to be closed, and the next con to replace him in the pool.
         */
        public synchronized void closeConnectionPool(Connection con) {
            pool.add(con);
        }

        public synchronized void closeAllConnectionPool() {
            for (Connection c : pool) {
                try {
                    c.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            pool.clear();
            currentConnectionsInSystem = 0;
        }
    }
}


//~ Formatted by Jindent --- http://www.jindent.com
