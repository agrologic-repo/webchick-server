package com.agrologic.app.dao.mysql.impl;

import com.agrologic.app.dao.DaoFactory;
import com.agrologic.app.dao.FeedDao;
import com.agrologic.app.model.Feed;
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

public class FeedDaoImpl implements FeedDao {
    protected final DaoFactory dao;
    private final Logger logger = LoggerFactory.getLogger(FeedDaoImpl.class);
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;

    public FeedDaoImpl(JdbcTemplate jdbcTemplate, DaoFactory dao) {
        this.jdbcTemplate = jdbcTemplate;
        this.jdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
        this.jdbcInsert.setTableName("feed");
        this.dao = dao;
    }

    private Feed makeFeed(ResultSet rs) throws SQLException {
        Feed feed = new Feed();
        feed.setId(rs.getLong("ID"));
        feed.setFlockId(rs.getLong("FlockID"));
        feed.setType(rs.getLong("Type"));
        feed.setAmount(rs.getInt("Amount"));
        feed.setDate(rs.getString("Date"));
        feed.setNumberAccount(rs.getInt("AccountNumber"));
        feed.setTotal(rs.getFloat("Total"));
        return feed;
    }

    private List<Feed> makeFeedList(ResultSet rs) throws SQLException {
        List<Feed> feedList = new ArrayList<Feed>();

        while (rs.next()) {
            feedList.add(makeFeed(rs));
        }

        return feedList;
    }

    @Override
    public void insert(Feed feed) throws SQLException {
        String sqlQuery = "insert into feed values (?,?,?,?,?,?,?)";
        PreparedStatement prepstmt = null;
        Connection con = null;

        try {
            con = dao.getConnection();
            prepstmt = con.prepareStatement(sqlQuery);
            prepstmt.setObject(1, null);
            prepstmt.setLong(2, feed.getFlockId());
            prepstmt.setLong(3, feed.getType());
            prepstmt.setInt(4, feed.getAmount());
            prepstmt.setString(5, feed.getDate());
            prepstmt.setInt(6, feed.getNumberAccount());
            prepstmt.setFloat(7, feed.getTotal());
            prepstmt.executeUpdate();
        } catch (SQLException e) {
            dao.printSQLException(e);
            throw new SQLException("Cannot Insert Feed To The DataBase");
        } finally {
            prepstmt.close();
            dao.closeConnection(con);
        }
    }

    @Override
    public void remove(Long id) throws SQLException {
        String sqlQuery = "delete from feed where ID=?";
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
    public Feed getById(Long id) throws SQLException {
        String sqlQuery = "select * from feed where ID=?";
        PreparedStatement prepstmt = null;
        Connection con = null;

        try {
            con = dao.getConnection();
            prepstmt = con.prepareStatement(sqlQuery);
            prepstmt.setLong(1, id);

            ResultSet rs = prepstmt.executeQuery();

            if (rs.next()) {
                return makeFeed(rs);
            } else {
                return null;
            }
        } catch (SQLException e) {
            dao.printSQLException(e);

            throw new SQLException("Cannot Retrieve Feed " + id + " From DataBase");
        } finally {
            prepstmt.close();
            dao.closeConnection(con);
        }
    }

    @Override
    public List<Feed> getAllByFlockId(Long flockId) throws SQLException {
        String sqlQuery = "select * from feed where FlockID=?";
        PreparedStatement prepstmt = null;
        Connection con = null;

        try {
            con = dao.getConnection();
            prepstmt = con.prepareStatement(sqlQuery);
            prepstmt.setLong(1, flockId);

            ResultSet rs = prepstmt.executeQuery();

            return makeFeedList(rs);
        } catch (SQLException e) {
            dao.printSQLException(e);

            throw new SQLException("Cannot Retrieve All Feed of Flock " + flockId + " From DataBase");
        } finally {
            prepstmt.close();
            dao.closeConnection(con);
        }
    }
}
