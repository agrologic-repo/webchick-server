package com.agrologic.app.dao.mysql.impl;

import com.agrologic.app.dao.DaoFactory;
import com.agrologic.app.dao.ProgramDao;
import com.agrologic.app.dao.mappers.ProgramUtil;
import com.agrologic.app.model.Program;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import java.sql.*;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class ProgramDaoImpl implements ProgramDao {
    protected final DaoFactory dao;
    private final Logger logger = LoggerFactory.getLogger(AlarmDaoImpl.class);
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;

    public ProgramDaoImpl(JdbcTemplate jdbcTemplate, DaoFactory dao) {
        this.jdbcTemplate = jdbcTemplate;
        this.jdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
        this.jdbcInsert.setTableName("programs");
        this.dao = dao;
    }

    @Override
    public void insert(Program program) throws SQLException {
        logger.debug("Creating program with id [{}]", program.getId());
        Map<String, Object> valuesToInsert = new HashMap<String, Object>();
        valuesToInsert.put("programid", program.getId());
        valuesToInsert.put("name", program.getName());
        valuesToInsert.put("created", program.getCreatedDate());
        valuesToInsert.put("modified", program.getModifiedDate());
        jdbcInsert.execute(valuesToInsert);
    }

    @Override
    public void update(Program program) throws SQLException {
        String sqlQuery = "update programs set Name=?, Modified=? where ProgramID=?";
        PreparedStatement prepstmt = null;
        Connection con = null;

        try {
            con = dao.getConnection();
            prepstmt = con.prepareStatement(sqlQuery);
            prepstmt.setString(1, program.getName());
            prepstmt.setString(2, program.getModifiedDate());
            prepstmt.setLong(3, program.getId());
            prepstmt.executeUpdate();
        } catch (SQLException e) {
            dao.printSQLException(e);
            throw new SQLException("Cannot Update Program In DataBase");
        } finally {
            prepstmt.close();
            dao.closeConnection(con);
        }
    }

    @Override
    public void remove(Long id) throws SQLException {
        String sqlQuery = "delete from programs where ProgramID=?";
        PreparedStatement prepstmt = null;
        Connection con = null;

        try {
            con = dao.getConnection();
            prepstmt = con.prepareStatement(sqlQuery);
            prepstmt.setLong(1, id);
            prepstmt.executeUpdate();
        } catch (SQLException e) {
            dao.printSQLException(e);

            throw new SQLException("Cannot Delete Program " + id + "From DataBase");
        } finally {
            prepstmt.close();
            dao.closeConnection(con);
        }
    }

    @Override
    public int count() throws SQLException {
        String sqlQuery = "select count(*) as count from programs";
        Statement stmt = null;
        Connection con = null;

        try {
            con = dao.getConnection();
            stmt = con.createStatement();

            ResultSet rs = stmt.executeQuery(sqlQuery);

            if (rs.next()) {
                return rs.getInt("count");
            } else {
                return 0;
            }
        } catch (SQLException e) {
            dao.printSQLException(e);

            throw new SQLException("Cannot Count Programs From DataBase");
        } finally {
            stmt.close();
            dao.closeConnection(con);
        }
    }

    @Override
    public boolean isProgramWithGivenIdExist(Long id) throws SQLException {
        String sqlQuery = "select * from programs where ProgramID = ?";
        PreparedStatement prepstmt = null;
        Connection con = null;

        try {
            con = dao.getConnection();
            prepstmt = con.prepareStatement(sqlQuery);
            prepstmt.setLong(1, id);

            ResultSet rs = prepstmt.executeQuery();

            if (rs.next()) {
                return true;
            } else {
                return false;
            }
        } catch (SQLException e) {
            dao.printSQLException(e);

            throw new SQLException("Cannot Validate Program ID In DataBase");
        } finally {
            prepstmt.close();
            dao.closeConnection(con);
        }
    }

    @Override
    public boolean programExist(Long id) throws SQLException {
        Program p = getById(id);

        return p != null;
    }

    @Override
    public Program getById(Long id) throws SQLException {
        String sqlQuery = "select * from programs where ProgramID=?";
        PreparedStatement prepstmt = null;
        Connection con = null;

        try {
            con = dao.getConnection();
            prepstmt = con.prepareStatement(sqlQuery);
            prepstmt.setLong(1, id);

            ResultSet rs = prepstmt.executeQuery();

            if (rs.next()) {
                return ProgramUtil.makeProgram(rs);
            } else {
                return null;
            }
        } catch (SQLException e) {
            dao.printSQLException(e);

            throw new SQLException("Cannot Retrieve Program " + id + " From DataBase");
        } finally {
            prepstmt.close();
            dao.closeConnection(con);
        }
    }

    @Override
    public Collection<Program> getAll() throws SQLException {
        String sqlQuery = "select * from programs";
        Statement stmt = null;
        Connection con = null;

        try {
            con = dao.getConnection();
            stmt = con.createStatement();

            ResultSet rs = stmt.executeQuery(sqlQuery);

            return ProgramUtil.makeProgramList(rs);
        } catch (SQLException e) {
            dao.printSQLException(e);

            throw new SQLException("Cannot Retrieve Programs From DataBase");
        } finally {
            stmt.close();
            dao.closeConnection(con);
        }
    }

    @Override
    public Collection<Program> getAll(String searchText) throws SQLException {
        String sqlQuery = "select * from programs where name like '%" + searchText + "%'";
        Statement stmt = null;
        Connection con = null;

        try {
            con = dao.getConnection();
            stmt = con.createStatement();

            ResultSet rs = stmt.executeQuery(sqlQuery);

            return ProgramUtil.makeProgramList(rs);
        } catch (SQLException e) {
            dao.printSQLException(e);

            throw new SQLException("Cannot Retrieve Programs From DataBase");
        } finally {
            stmt.close();
            dao.closeConnection(con);
        }
    }

    @Override
    public Collection<Program> getAllByUserId(String searchText, Long userId) throws SQLException {
        String sqlQuery = "select * from programs where programid in "
                + "(select programid from controllers where cellinkid in "
                + "(select cellinkid from cellinks where userid=?)) and name like '%" + searchText + "%'";
        PreparedStatement prepstmt = null;
        Connection con = null;

        try {
            con = dao.getConnection();
            prepstmt = con.prepareStatement(sqlQuery);
            prepstmt.setLong(1, userId);

            ResultSet rs = prepstmt.executeQuery();

            return ProgramUtil.makeProgramList(rs);
        } catch (SQLException e) {
            dao.printSQLException(e);

            throw new SQLException("Cannot Retrieve Programs From DataBase");
        } finally {
            prepstmt.close();
            dao.closeConnection(con);
        }
    }

    @Override
    public Collection<Program> getAllByUserCompany(String searchText, String company) throws SQLException {
        String sqlQuery = "select * from programs where programid in "
                + "(select distinct programid from controllers where cellinkid in "
                + "(select cellinkid from cellinks where userid in "
                + "(select userid from users where company=?) and programid<>1)) and name like '%"
                + searchText + "%'";
        PreparedStatement prepstmt = null;
        Connection con = null;

        try {
            con = dao.getConnection();
            prepstmt = con.prepareStatement(sqlQuery);
            prepstmt.setString(1, company);

            ResultSet rs = prepstmt.executeQuery();

            return ProgramUtil.makeProgramList(rs);
        } catch (SQLException e) {
            dao.printSQLException(e);

            throw new SQLException("Cannot Retrieve Programs From DataBase");
        } finally {
            prepstmt.close();
            dao.closeConnection(con);
        }
    }

    @Override
    public Collection<Program> getAll(String searchText, String index) throws SQLException {
        String sqlQuery = "select * from programs where name like '%" + searchText + "%' limit ?,25 ";
        PreparedStatement prepstmt = null;
        Connection con = null;

        try {
            con = dao.getConnection();
            prepstmt = con.prepareStatement(sqlQuery);
            prepstmt.setInt(1, Integer.parseInt(index));

            ResultSet rs = prepstmt.executeQuery();

            return ProgramUtil.makeProgramList(rs);
        } catch (SQLException e) {
            dao.printSQLException(e);

            throw new SQLException("Cannot Retrieve Programs From DataBase");
        } finally {
            prepstmt.close();
            dao.closeConnection(con);
        }
    }
}
