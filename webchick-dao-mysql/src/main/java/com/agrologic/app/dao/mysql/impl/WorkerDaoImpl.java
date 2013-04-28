package com.agrologic.app.dao.mysql.impl;

import com.agrologic.app.dao.DaoFactory;
import com.agrologic.app.dao.WorkerDao;
import com.agrologic.app.model.Worker;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class WorkerDaoImpl implements WorkerDao {

    protected DaoFactory dao;

    public WorkerDaoImpl(DaoFactory daoFactory) {
        dao = daoFactory;
    }

    private Worker makeWorker(ResultSet rs) throws SQLException {
        Worker worker = new Worker();

        worker.setId(rs.getLong("ID"));
        worker.setName(rs.getString("Name"));
        worker.setDefine(rs.getString("Define"));
        worker.setPhone(rs.getString("Phone"));
        worker.setHourCost(rs.getFloat("HourCost"));
        worker.setCellinkId(rs.getLong("CellinkID"));

        return worker;
    }

    private List<Worker> makeWorkerList(ResultSet rs) throws SQLException {
        List<Worker> workerList = new ArrayList<Worker>();

        while (rs.next()) {
            workerList.add(makeWorker(rs));
        }

        return workerList;
    }

    @Override
    public void insert(Worker worker) throws SQLException {
        String sqlQuery = "insert into workers values (?,?,?,?,?,?)";
        PreparedStatement prepstmt = null;
        Connection con = null;

        try {
            con = dao.getConnection();
            prepstmt = con.prepareStatement(sqlQuery);
            prepstmt.setObject(1, null);
            prepstmt.setString(2, worker.getName());
            prepstmt.setString(3, worker.getDefine());
            prepstmt.setString(4, worker.getPhone());
            prepstmt.setFloat(5, worker.getHourCost());
            prepstmt.setFloat(6, worker.getCellinkId());
            prepstmt.executeUpdate();
        } catch (SQLException e) {
            dao.printSQLException(e);

            throw new SQLException("Cannot Insert Worker To The DataBase", e.getMessage());
        } finally {
            prepstmt.close();
            dao.closeConnection(con);
        }
    }

    @Override
    public void remove(Long id) throws SQLException {
        String sqlQuery = "delete from workers where ID=?";
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
    public Worker getById(Long id) throws SQLException {
        String sqlQuery = "select * from workers where ID=?";
        PreparedStatement prepstmt = null;
        Connection con = null;

        try {
            con = dao.getConnection();
            prepstmt = con.prepareStatement(sqlQuery);
            prepstmt.setLong(1, id);

            ResultSet rs = prepstmt.executeQuery();

            if (rs.next()) {
                return makeWorker(rs);
            } else {
                return null;
            }
        } catch (SQLException e) {
            dao.printSQLException(e);

            throw new SQLException("Cannot Retrieve Worker " + id + " From DataBase");
        } finally {
            prepstmt.close();
            dao.closeConnection(con);
        }
    }

    @Override
    public List<Worker> getAllByCellinkId(Long cellinkId) throws SQLException {
        String sqlQuery = "select * from workers where CellinkID=?";
        PreparedStatement prepstmt = null;
        Connection con = null;

        try {
            con = dao.getConnection();
            prepstmt = con.prepareStatement(sqlQuery);
            prepstmt.setLong(1, cellinkId);

            ResultSet rs = prepstmt.executeQuery();

            return makeWorkerList(rs);
        } catch (SQLException e) {
            dao.printSQLException(e);

            throw new SQLException("Cannot Retrieve All Workers", e.getMessage());
        } finally {
            prepstmt.close();
            dao.closeConnection(con);
        }
    }

    @Override
    public String getCurrencyById(Long id) throws SQLException {
        String sqlQuery = "select * from currency where ID=?";
        PreparedStatement prepstmt = null;
        Connection con = null;

        try {
            con = dao.getConnection();
            prepstmt = con.prepareStatement(sqlQuery);
            prepstmt.setLong(1, id);

            ResultSet rs = prepstmt.executeQuery();

            if (rs.next()) {
                return rs.getString("Symbol");
            } else {
                return null;
            }
        } catch (SQLException e) {
            dao.printSQLException(e);

            throw new SQLException("Cannot Retrieve Worker " + id + " From DataBase");
        } finally {
            prepstmt.close();
            dao.closeConnection(con);
        }
    }
}
