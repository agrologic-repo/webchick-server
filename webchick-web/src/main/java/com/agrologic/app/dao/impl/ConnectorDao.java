
/*
* To change this template, choose Tools | Templates
* and open the template in the editor.
 */
package com.agrologic.app.dao.impl;



import com.agrologic.app.utils.PropertyFileUtil;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import org.apache.log4j.Logger;
import org.apache.tomcat.dbcp.dbcp.BasicDataSource;

public class ConnectorDao {
    private final static String APP_CONFIG_FILE  = "config/application.properties";
    private final static String jdbcDriver       = PropertyFileUtil.getProperty(APP_CONFIG_FILE, "jdbc.driver");
    private final static String dataBaseUser     = PropertyFileUtil.getProperty(APP_CONFIG_FILE, "database.user");
    private final static String dataBaseUrl      = PropertyFileUtil.getProperty(APP_CONFIG_FILE, "database.url");
    private final static String dataBasePassword = PropertyFileUtil.getProperty(APP_CONFIG_FILE, "database.password");

    static {
        try {
            Class.forName(jdbcDriver).newInstance();
        } catch (Exception ex) {}
    }

    private InitialContext ctx;
    private DataSource     dataSource;
    private Logger         logger;

    public ConnectorDao() {
        try {

            /**
             * Create logger with class name.
             */
            logger = Logger.getLogger(ConnectorDao.class);

            /**
             * Create Initial context object for access database
             */
            ctx = new InitialContext();

            /*
             * Lookup the DataSource, which will be backed by a pool
             * that the application server provides. DataSource instances
             * are also a good candidate for caching as an instance
             * variable, as JNDI lookups can be expensive as well.
             */
            dataSource = (DataSource) ctx.lookup("java:comp/env/jdbc/AgroDB");

            if (dataSource == null) {
                setDefaultDatasource();
            }

            /*
             * The following code is what would actually be in your
             * Servlet, JSP or EJB 'service' method...where you need
             * to work with a JDBC connection.
             */
        } catch (NamingException ex) {
            logger.error("NamingException :" + ex.getMessage());
        }
    }

    /**
     * Get DataSource object
     * @return dataSource the data source object
     */
    public DataSource getDataSource() {
        return dataSource;
    }

    /**
     * Set DataSource object.
     */
    protected void setDefaultDatasource() {
        BasicDataSource bds = new BasicDataSource();

        bds.setDriverClassName("com.mysql.jdbc.Driver");
        bds.setUrl("jdbc:mysql://localhost:3306/agrodb?autoReconnect=true");
        bds.setUsername("root");
        bds.setPassword("agrologic");
        dataSource = bds;
    }

    /**
     * Attempts to establish a connection with the data source that this
     * DataSource object represents.
     * @return a connection to the data source.
     * @throws SQLException if a database access error occurs.
     */
    protected Connection getConnection() throws SQLException {
        if (dataSource == null) {
            setDefaultDatasource();
        }

        return dataSource.getConnection();
    }

    /**
     * Calling the method close on a Connection object that is already
     * closed is a no-op.
     * @param con a connection to the data source.
     * @throws SQLException if a database access error occurs.
     */
    protected void closeConnection(final Connection con) throws SQLException {
        con.close();
    }

    /**
     * The following method outputs the SQLState, error code, error description,
     * and cause (if there is one) contained in the SQLException as well as
     * any other exception chained to it.
     * @param ex the sql exception.
     */
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

    public String getMySQLVersion() {
        String     sqlQuery = "SELECT VERSION()";
        Statement  stmt     = null;
        Connection con      = null;
        String     result   = "";

        try {
            con  = getConnection();
            stmt = con.createStatement();

            ResultSet rs = stmt.executeQuery(sqlQuery);

            if (rs.next()) {
                result = rs.getString(1);
            }
        } catch (SQLException e) {
            logger.error("Cannot Get MySQL Version", e);
        } finally {
            try {
                stmt.close();
                con.close();
            } catch (SQLException ex) {
                java.util.logging.Logger.getLogger(ConnectorDao.class.getName()).log(java.util.logging.Level.SEVERE,
                                                   null, ex);
            }
        }

        int pidx = result.lastIndexOf(".");

        result = result.substring(0, pidx);

        return result;
    }
}



