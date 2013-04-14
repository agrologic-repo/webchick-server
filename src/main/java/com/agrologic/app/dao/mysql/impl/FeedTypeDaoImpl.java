/**
 * To change this template, choose Tools | Templates and open the template in the editor.
 */
package com.agrologic.app.dao.mysql.impl;

//~--- non-JDK imports --------------------------------------------------------
import com.agrologic.app.dao.DaoFactory;
import com.agrologic.app.dao.DaoType;
import com.agrologic.app.dao.FeedTypeDao;
import com.agrologic.app.model.FeedType;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author JanL
 */
public class FeedTypeDaoImpl implements FeedTypeDao {

    protected DaoFactory dao;

    public FeedTypeDaoImpl() {
        this(DaoType.MYSQL);
    }

    public FeedTypeDaoImpl(DaoType daoType) {
        this.dao = DaoFactory.getDaoFactory(daoType);
    }

    private FeedType makeFeedType(ResultSet rs) throws SQLException {
        FeedType feed = new FeedType();
        feed.setId(rs.getLong("ID"));
        feed.setCellinkId(rs.getLong("CellinkID"));
        feed.setFeedType(rs.getString("FeedType"));
        feed.setPrice(rs.getFloat("Price"));
        return feed;
    }

    private List<FeedType> makeFeedTypeList(ResultSet rs) throws SQLException {
        List<FeedType> feedList = new ArrayList<FeedType>();
        while (rs.next()) {
            feedList.add(makeFeedType(rs));
        }
        return feedList;
    }

    @Override
    public void insert(FeedType feed) throws SQLException {
        String sqlQuery = "insert into feedtypes values (?,?,?,?)";
        PreparedStatement prepstmt = null;
        Connection con = null;

        try {
            con = dao.getConnection();
            prepstmt = con.prepareStatement(sqlQuery);
            prepstmt.setObject(1, null);
            prepstmt.setString(2, feed.getFeedType());
            prepstmt.setFloat(3, feed.getPrice());
            prepstmt.setLong(4, feed.getCellinkId());
            prepstmt.executeUpdate();
        } catch (SQLException e) {
            dao.printSQLException(e);

            throw new SQLException("Cannot Insert FeedType To The DataBase");
        } finally {
            prepstmt.close();
            dao.closeConnection(con);
        }
    }

    @Override
    public void remove(Long id) throws SQLException {
        String sqlQuery = "delete from feedtypes where ID=?";
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
    public FeedType getById(Long id) throws SQLException {
        String sqlQuery = "select * from feedtypes where ID=?";
        PreparedStatement prepstmt = null;
        Connection con = null;

        try {
            con = dao.getConnection();
            prepstmt = con.prepareStatement(sqlQuery);
            prepstmt.setLong(1, id);

            ResultSet rs = prepstmt.executeQuery();

            if (rs.next()) {
                return makeFeedType(rs);
            } else {
                return null;
            }
        } catch (SQLException e) {
            dao.printSQLException(e);
            throw new SQLException("Cannot Retrieve FeedType " + id + " From DataBase");
        } finally {
            prepstmt.close();
            dao.closeConnection(con);
        }
    }

    @Override
    public List<FeedType> getAllByCellinkId(Long cellinkId) throws SQLException {
        String sqlQuery = "select * from feedtypes where CellinkID=?";
        PreparedStatement prepstmt = null;
        Connection con = null;

        try {
            con = dao.getConnection();
            prepstmt = con.prepareStatement(sqlQuery);
            prepstmt.setLong(1, cellinkId);
            ResultSet rs = prepstmt.executeQuery();

            return makeFeedTypeList(rs);
        } catch (SQLException e) {
            dao.printSQLException(e);
            throw new SQLException("Cannot Retrieve All FeedType  From DataBase");
        } finally {
            prepstmt.close();
            dao.closeConnection(con);
        }
    }
}


//~ Formatted by Jindent --- http://www.jindent.com
