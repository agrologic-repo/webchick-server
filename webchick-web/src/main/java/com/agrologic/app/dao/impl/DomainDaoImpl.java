
/*
* To change this template, choose Tools | Templates
* and open the template in the editor.
 */
package com.agrologic.app.dao.impl;



import com.agrologic.app.dao.DomainDao;



import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author Administrator
 */
public class DomainDaoImpl extends ConnectorDao implements DomainDao {
    @Override
    public String getDomain() throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String getLogoPath(String domain) throws SQLException {
        String            sqlQuery = "select logopath from domains where domain=? ";
        PreparedStatement prepstmt = null;
        Connection        con      = null;

        try {
            con      = getConnection();
            prepstmt = con.prepareStatement(sqlQuery);
            prepstmt.setString(1, domain);

            ResultSet rs = prepstmt.executeQuery();

            if (rs.next()) {
                return rs.getString("logopath");
            } else {
                return "img/agrologiclogo.png";
            }
        } catch (SQLException e) {
            System.out.println("Caught SQL SQLException : " + e.getMessage());

            throw new SQLException("Cannot Retrieve Controllers From DataBase", e);
        } finally {
            prepstmt.close();
            closeConnection(con);
        }
    }

    @Override
    public String getCompany(String domain) throws SQLException {
        String            sqlQuery = "select company from domains where domain=? ";
        PreparedStatement prepstmt = null;
        Connection        con      = null;

        try {
            con      = getConnection();
            prepstmt = con.prepareStatement(sqlQuery);
            prepstmt.setString(1, domain);

            ResultSet rs = prepstmt.executeQuery();

            if (rs.next()) {
                return rs.getString("company");
            } else {
                return "Agrologic";
            }
        } catch (SQLException e) {
            System.out.println("Caught SQL SQLException : " + e.getMessage());

            throw new SQLException("Cannot Retrieve Controllers From DataBase", e);
        } finally {
            prepstmt.close();
            closeConnection(con);
        }
    }
}



