package com.agrologic.app.dao.mysql.impl;

import com.agrologic.app.dao.DaoFactory;
import com.agrologic.app.dao.VersionDao;
import org.slf4j.Logger;

import java.sql.*;

public class VersionDaoImpl implements VersionDao {

    protected DaoFactory dao;
    private Logger logger = org.slf4j.LoggerFactory.getLogger(VersionDaoImpl.class);

    public VersionDaoImpl(DaoFactory daoFactory) {
        dao = daoFactory;
    }

    @Override
    public String getVersion() {
        String sqlQuery = "SELECT VERSION()";
        Statement stmt = null;
        Connection con = null;
        String version = "";

        try {
            System.out.println(" url : " + ((MySqlDaoFactory) dao).URL);
            System.out.println(" user : " + ((MySqlDaoFactory) dao).USER);
            System.out.println(" pass : " + ((MySqlDaoFactory) dao).PASS);
            con = DriverManager.getConnection(((MySqlDaoFactory) dao).URL,
                    ((MySqlDaoFactory) dao).USER,
                    ((MySqlDaoFactory) dao).PASS);
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
            } catch (SQLException ex) {
            }

            try {
                con.close();
            } catch (SQLException ex) {
            }
        }
        version = version.substring(0, version.lastIndexOf("."));

        return version;
    }
}
