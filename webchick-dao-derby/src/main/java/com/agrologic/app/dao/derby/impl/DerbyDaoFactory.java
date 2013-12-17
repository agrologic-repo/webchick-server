package com.agrologic.app.dao.derby.impl;

import com.agrologic.app.dao.DaoFactory;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class DerbyDaoFactory extends DaoFactory {

    protected static final String PROP_FILENAME = "derby.properties";
    protected static Properties props = null;

    public static final String DRIVER = "jdbc.driver";
    public static final String URL = "jdbc.url";
    public static final String USER = "jdbc.user";
    public static final String PASSWORD = "jdbc.password";
    private static DerbyDaoFactory instanceObject = null;
    private static final boolean DEBUG = true;

    /**
     * This static block causes the class loader to load the jdbcDriver.
     */
    static {
        try {
            ClassLoader loader = DerbyDaoFactory.class.getClassLoader();
            InputStream is = loader.getResourceAsStream(PROP_FILENAME);
            props = new Properties();
            props.load(is);
            if (DEBUG) {
                System.out.println("Starting JDBC driver...");
            }


            Class.forName(props.getProperty(DRIVER)).newInstance();
        } catch (Exception ex) {
            if (DEBUG) {
                System.out.println("jdbc : Driver Class not found, " + ex.getMessage());
            }
        }
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

//    @Override
//    public ControllerDao getControllerDao() {
//        return new DerbyControllerDaoImpl(this);
//    }
//
//    @Override
//    public DataDao getDataDao() {
//        return new DerbyDataDaoImpl(this);
//    }

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
                    con = DriverManager.getConnection(props.getProperty(URL));
                    currentConnectionsInSystem++;
                    return con;
                } catch (Exception ex) {
                    ex.printStackTrace();
                    System.out.println("pool : Cannot establish a connection to the DB. DB URL = "
                            + props.getProperty(URL));
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



