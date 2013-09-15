package com.agrologic.app.dao.mysql.impl;

import com.agrologic.app.dao.DaoFactory;
import com.agrologic.app.dao.GasDao;
import com.agrologic.app.model.Gas;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class GasDaoImpl implements GasDao {
    protected final DaoFactory dao;
    private final Logger logger = LoggerFactory.getLogger(GasDaoImpl.class);
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;

    public GasDaoImpl(JdbcTemplate jdbcTemplate, DaoFactory dao) {
        this.jdbcTemplate = jdbcTemplate;
        this.jdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
        this.jdbcInsert.setTableName("gas");
        this.dao = dao;
    }

    private Gas makeGas(ResultSet rs) throws SQLException {
        Gas gas = new Gas();
        gas.setId(rs.getLong("ID"));
        gas.setFlockId(rs.getLong("FlockID"));
        gas.setAmount(rs.getInt("Amount"));
        gas.setDate(rs.getString("Date"));
        gas.setNumberAccount(rs.getInt("NumberAccount"));
        gas.setPrice(rs.getFloat("Price"));
        gas.setTotal(rs.getFloat("Total"));
        return gas;
    }

    private List<Gas> makeGasList(ResultSet rs) throws SQLException {
        List<Gas> gasList = new ArrayList<Gas>();

        while (rs.next()) {
            gasList.add(makeGas(rs));
        }

        return gasList;
    }

    @Override
    public void insert(Gas gas) throws SQLException {
        String sqlQuery = "insert into gas values (?,?,?,?,?,?,?)";
        PreparedStatement prepstmt = null;
        Connection con = null;

        try {
            con = dao.getConnection();
            prepstmt = con.prepareStatement(sqlQuery);
            prepstmt.setObject(1, null);
            prepstmt.setLong(2, gas.getFlockId());
            prepstmt.setInt(3, gas.getAmount());
            prepstmt.setString(4, gas.getDate());
            prepstmt.setInt(5, gas.getNumberAccount());
            prepstmt.setFloat(6, gas.getPrice());
            prepstmt.setFloat(7, gas.getTotal());
            prepstmt.executeUpdate();
        } catch (SQLException e) {
            dao.printSQLException(e);

            throw new SQLException("Cannot Insert Gas To The DataBase");
        } finally {
            prepstmt.close();
            dao.closeConnection(con);
        }
    }

    @Override
    public void remove(Long id) throws SQLException {
        String sqlQuery = "delete from gas where ID=?";
        PreparedStatement prepstmt = null;
        Connection con = null;

        try {
            con = dao.getConnection();
            prepstmt = con.prepareStatement(sqlQuery);
            prepstmt.setLong(1, id);
            prepstmt.executeUpdate();
        } catch (SQLException e) {
            dao.printSQLException(e);

            throw new SQLException("Cannot Delete Controller From DataBase");
        } finally {
            prepstmt.close();
            dao.closeConnection(con);
        }
    }

    @Override
    public Gas getById(Long id) throws SQLException {
        String sqlQuery = "select * from gas where ID=?";
        PreparedStatement prepstmt = null;
        Connection con = null;

        try {
            con = dao.getConnection();
            prepstmt = con.prepareStatement(sqlQuery);
            prepstmt.setLong(1, id);

            ResultSet rs = prepstmt.executeQuery();
            if (rs.next()) {
                return makeGas(rs);
            } else {
                return null;
            }
        } catch (SQLException e) {
            dao.printSQLException(e);

            throw new SQLException("Cannot Retrieve Gas " + id + " From DataBase");
        } finally {
            prepstmt.close();
            dao.closeConnection(con);
        }
    }

    @Override
    public List<Gas> getAllByFlockId(Long flockId) throws SQLException {
        String sqlQuery = "select * from gas where FlockID=?";
        PreparedStatement prepstmt = null;
        Connection con = null;

        try {
            con = dao.getConnection();
            prepstmt = con.prepareStatement(sqlQuery);
            prepstmt.setLong(1, flockId);

            ResultSet rs = prepstmt.executeQuery();

            return makeGasList(rs);
        } catch (SQLException e) {
            dao.printSQLException(e);

            throw new SQLException("Cannot Retrieve All Gas of Flock " + flockId + " From DataBase");
        } finally {
            prepstmt.close();
            dao.closeConnection(con);
        }
    }
}
