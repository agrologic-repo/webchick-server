package com.agrologic.app.dao.derby.impl;

import com.agrologic.app.dao.DaoFactory;
import com.agrologic.app.dao.VersionDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;

public class DerbyVersionDaoImpl implements VersionDao {

    protected DaoFactory dao;
    private Logger logger = LoggerFactory.getLogger(DerbyVersionDaoImpl.class);

    public DerbyVersionDaoImpl(DaoFactory daoFactory) {
        dao = daoFactory;
    }

    @Override
    public String getVersion() {
        String sqlQuery = "SELECT VERSION()";
        Statement stmt = null;
        Connection con = null;
        String version = "";

        try {
            con = DriverManager.getConnection(((DerbyDaoFactory) dao).URL);
            stmt = con.createStatement();

            ResultSet rs = stmt.executeQuery(sqlQuery);

            if (rs.next()) {
                version = rs.getString(1);
            }
        } catch (SQLException e) {
            logger.error("Cannot Get Derby Version", e);
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
