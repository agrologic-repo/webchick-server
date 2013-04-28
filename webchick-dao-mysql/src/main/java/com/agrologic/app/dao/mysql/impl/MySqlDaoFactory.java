package com.agrologic.app.dao.mysql.impl;

import com.agrologic.app.config.Configuration;
import com.agrologic.app.dao.*;
import org.apache.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MySqlDaoFactory extends DaoFactory {
    private static final Configuration     CONFIG         = new Configuration();
    private static final String            DRIVER         = CONFIG.getDbDriver();
    private static final String            URL            = CONFIG.getDbUrl();
    private static final String            USER           = CONFIG.getDbUser();
    private static final String            PASS           = CONFIG.getDbPassword();
    private static final boolean           DEBUG          = false;
    private static MySqlDaoFactory         instance       = null;
    private static Logger                  logger         = Logger.getRootLogger();

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
                logger.fatal("jdbc : Driver Class not found, " + ex.getMessage());
            }
        }
    }

    private MySqlDaoFactory() {}

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

    /**
     * Retrieve the mysql installed version;
     *
     * @return version the of installed mysql
     */
    public static String getMySQLVersion() {
        String     sqlQuery = "SELECT VERSION()";
        Statement  stmt     = null;
        Connection con      = null;
        String     version  = "";

        try {
            con  = DriverManager.getConnection("jdbc:mysql://localhost:3306/", USER, PASS);
            stmt = con.createStatement();

            ResultSet rs = stmt.executeQuery(sqlQuery);

            if (rs.next()) {
                version = rs.getString(1);
            }
        } catch (SQLException e) {
            logger.error("Cannot Get MySQL Version", e);
        } finally {
            try {
                stmt.close();
            } catch (SQLException ex) {}

            try {
                con.close();
            } catch (SQLException ex) {}
        }

        int pidx = version.lastIndexOf(".");

        version = version.substring(0, pidx);

        return version;
    }

    @Override
    public SchemaDao getSchemaDao() {
        return new SchemaDaoImpl(this);
    }

    @Override
    public UserDao getUserDao() {
        return new UserDaoImpl(this);
    }

    @Override
    public CellinkDao getCellinkDao() {
        return new CellinkDaoImpl(this);
    }

    @Override
    public ControllerDao getControllerDao() {
        return new ControllerDaoImpl(this);
    }

    @Override
    public ProgramDao getProgramDao() {
        return new ProgramDaoImpl(this);
    }

    @Override
    public ScreenDao getScreenDao() {
        return new ScreenDaoImpl(this);
    }

    @Override
    public TableDao getTableDao() {
        return new TableDaoImpl(this);
    }

    @Override
    public DataDao getDataDao() {
        return new DataDaoImpl(this);
    }

    @Override
    public AlarmDao getAlarmDao() {
        return new AlarmDaoImpl(this);
    }

    @Override
    public RelayDao getRelayDao() {
        return new RelayDaoImpl(this);
    }

    @Override
    public SystemStateDao getSystemStateDao() {
        return new SystemStateDaoImpl(this);
    }

    @Override
    public LanguageDao getLanguageDao() {
        return new LanguageDaoImpl(this);
    }

    @Override
    public FlockDao getFlockDao() {
        return new FlockDaoImpl(this);
    }

    @Override
    public DistribDao getDistribDao() {
        return new DistribDaoImpl(this);
    }

    @Override
    public FeedDao getFeedDao() {
        return new FeedDaoImpl(this);
    }

    @Override
    public FeedTypeDao getFeedTypeDao() {
        return new FeedTypeDaoImpl(this);
    }

    @Override
    public FuelDao getFuelDao() {
        return new FuelDaoImpl(this);
    }

    @Override
    public LaborDao getLaborDao() {
        return new LaborDaoImpl(this);
    }

    @Override
    public GasDao getGasDao() {
        return new GasDaoImpl(this);
    }

    @Override
    public MedicineDao getMedicineDao() {
        return new MedicineDaoImpl(this);
    }

    @Override
    public SpreadDao getSpreadDao() {
        return new SpreadDaoImpl(this);
    }

    @Override
    public TransactionDao getTransactionDao() {
        return new TransactionDaoImpl(this);
    }

    @Override
    public WorkerDao getWorkerDao() {
        return new WorkerDaoImpl(this);
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
        private static final int        MAX_CONNECTIONS            = 200;
        private static Connection       con                        = null;
        private static int              currentConnectionsInSystem = 0;
        private static List<Connection> pool                       = new ArrayList<Connection>();
        private static ConnectionPool   instance;

        /**
         * Default Constructor.
         */
        private ConnectionPool() {}

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
                    logger.fatal("pool : Cannot establish a connection to the DB. DB URL = " + URL + " DB USER= "
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
