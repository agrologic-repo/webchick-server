package com.agrologic.app.dao.mysql.impl;

import com.agrologic.app.config.Configuration;
import com.agrologic.app.dao.ControllerDao;
import com.agrologic.app.dao.DaoFactory;
import com.agrologic.app.dao.DataDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MySqlDaoFactory extends DaoFactory {
    private static final Configuration CONFIG = new Configuration();
    public static final String DRIVER = CONFIG.getDbDriver();
    public static final String URL = CONFIG.getDbUrl();
    public static final String USER = CONFIG.getDbUser();
    public static final String PASS = CONFIG.getDbPassword();
    private static final boolean DEBUG = false;
    private static MySqlDaoFactory instance = null;
    private static Logger logger = LoggerFactory.getLogger(MySqlDaoFactory.class);

    /**
     * This static block causes the class loader to load the jdbcDriver.
     */
    static {
        try {
            if (DEBUG) {
                logger.info("Starting JDBC driver...");
            }
            Class.forName(DRIVER).newInstance();
        } catch (Exception ex) {
            if (DEBUG) {
                logger.error("jdbc : Driver Class not found, ", ex.getMessage());
            }
        }
    }

    private MySqlDaoFactory() {
    }

    public static MySqlDaoFactory instance() {
        if (instance == null) {
            synchronized (MySqlDaoFactory.class) {
                instance = new MySqlDaoFactory();
            }
        }

        return instance;
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
    @Override
    public void closeAllConnection() {
        ConnectionPool.getInstance().closeAllConnectionPool();
    }

    /**
     * The following method outputs the SQLState, error code, error description, and cause (if there is one) contained
     * in the SQLException as well as any other exception chained to it.
     *
     * @param ex the sql exception.
     */
    @Override
    public void printSQLException(final SQLException ex) {
        for (Throwable e : ex) {
            if (e instanceof SQLException) {
                if (ignoreSQLException(((SQLException) e).getSQLState()) == false) {
                    e.printStackTrace(System.err);
                    logger.error("SQLState: " + ((SQLException) e).getSQLState());
                    logger.error("Error Code: " + ((SQLException) e).getErrorCode());
                    logger.error("Message: " + ((SQLException) e).getMessage());

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

    @Override
    public ControllerDao getControllerDao() {
        return new ControllerDaoImpl(this);
    }

    @Override
    public DataDao getDataDao() {
        return new DataDaoImpl(this);
    }

    /**
     * The ConnectionPool class is an inner static class implements which is a Singleton class that manages the
     * connection pool architecture.
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
                    if (DEBUG) {
                        logger.debug("pool : Connection pool singleton object was created.");
                    }

                    instance = new ConnectionPool();
                }
            }

            return instance;
        }

        /**
         * Acquires a connection object from the pool. In case the pool is empty creating a new connection object
         *
         * @return Connection object
         */
        public synchronized Connection getPoolConnection() {
            if ((pool.isEmpty()) && (currentConnectionsInSystem < MAX_CONNECTIONS)) {
                if (DEBUG) {
                    logger.debug("pool : Current connections in system = " + currentConnectionsInSystem);
                }

                if ((currentConnectionsInSystem + 10) >= MAX_CONNECTIONS) {
                    logger.warn("pool : Pool size is nearly full.  Please enlarge pool size or lower system load.");
                }

                try {
                    con = DriverManager.getConnection(URL, USER, PASS);

                    // con = DriverManager.getConnection(dataBaseUrl, dataBaseUser, dataBasePassword);
                    currentConnectionsInSystem++;

                    return con;
                } catch (Exception ex) {
                    logger.error("pool : Cannot establish a connection to the DB. DB URL = " + URL + " DB USER= "
                            + USER + " DB Password = " + PASS);

                    return null;
                }
            } else if (!pool.isEmpty()) {
                if (DEBUG) {
                    logger.debug("pool : Getting current connection from the pool.");
                }

                con = pool.get(pool.size() - 1);
                pool.remove(pool.size() - 1);

                return con;
            } else {
                logger.warn("pool : Pool size has reached his limits, thus can't get a connection immediately.");

                if (DEBUG) {
                    logger.debug("pool : Waiting in a loop until a connection will be close.");
                }

                while (true) {
                    if (!pool.isEmpty()) {
                        logger.debug("pool : Exit infinite loop, and get a connection from pool.");
                        con = pool.get(pool.size() - 1);
                        pool.remove(pool.size() - 1);

                        return con;
                    }
                }
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
                }
            }
            pool.clear();
            currentConnectionsInSystem = 0;
        }
    }
}
