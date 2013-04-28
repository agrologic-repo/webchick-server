
/*
* To change this template, choose Tools | Templates
* and open the template in the editor.
 */
package com.agrologic.app.dao.impl;



import com.agrologic.app.dao.ProgramDao;
import com.agrologic.app.model.ProgramDto;



import java.sql.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Title: ProgramDaoImpl <br>
 * Description: <br>
 * Copyright:   Copyright (c) 2009 <br>
 * Company:     AgroLogic LTD. <br>
 * @author      Valery Manakhimov <br>
 * @version     1.1 <br>
 */
public class ProgramDaoImpl extends ConnectorDao implements ProgramDao {
    private ProgramDto makeProgram(ResultSet rs) throws SQLException {
        ProgramDto program = new ProgramDto();

        program.setId(rs.getLong("ProgramID"));
        program.setName(rs.getString("Name"));
        program.setCreatedDate(rs.getString("Created"));
        program.setModifiedDate(rs.getString("Modified"));

        return program;
    }

    private List<ProgramDto> makeProgramList(ResultSet rs) throws SQLException {
        List<ProgramDto> programs = new ArrayList<ProgramDto>();

        while (rs.next()) {
            programs.add(makeProgram(rs));
        }

        return programs;
    }

    @Override
    public void insert(ProgramDto program) throws SQLException {
        String            sqlQuery = "insert into programs values (?,?,?,?)";
        PreparedStatement prepstmt = null;
        Connection        con      = null;

        try {
            con      = getConnection();
            prepstmt = con.prepareStatement(sqlQuery);
            prepstmt.setObject(1, program.getId());
            prepstmt.setString(2, program.getName());
            prepstmt.setString(3, program.getCreatedDate());
            prepstmt.setString(4, program.getModifiedDate());
            prepstmt.executeUpdate();
        } catch (SQLException e) {
            printSQLException(e);

            throw new SQLException("Cannot Insert Program To The DataBase");
        } finally {
            prepstmt.close();
            closeConnection(con);
        }
    }

    @Override
    public void update(ProgramDto program) throws SQLException {
        String            sqlQuery = "update programs set Name=?, Modified=? where ProgramID=?";
        PreparedStatement prepstmt = null;
        Connection        con      = null;

        try {
            con      = getConnection();
            prepstmt = con.prepareStatement(sqlQuery);
            prepstmt.setString(1, program.getName());
            prepstmt.setString(2, program.getModifiedDate());
            prepstmt.setLong(3, program.getId());
            prepstmt.executeUpdate();
        } catch (SQLException e) {
            printSQLException(e);

            throw new SQLException("Cannot Update Program In DataBase");
        } finally {
            prepstmt.close();
            closeConnection(con);
        }
    }

    @Override
    public void remove(Long id) throws SQLException {
        String            sqlQuery = "delete from programs where ProgramID=?";
        PreparedStatement prepstmt = null;
        Connection        con      = null;

        try {
            con      = getConnection();
            prepstmt = con.prepareStatement(sqlQuery);
            prepstmt.setLong(1, id);
            prepstmt.executeUpdate();
        } catch (SQLException e) {
            printSQLException(e);

            throw new SQLException("Cannot Delete Program From DataBase");
        } finally {
            prepstmt.close();
            closeConnection(con);
        }
    }

    @Override
    public int count() throws SQLException {
        String     sqlQuery = "select count(*) as count from programs";
        Statement  stmt     = null;
        Connection con      = null;

        try {
            con  = getConnection();
            stmt = con.createStatement();

            ResultSet rs = stmt.executeQuery(sqlQuery);

            if (rs.next()) {
                return rs.getInt("count");
            } else {
                return 0;
            }
        } catch (SQLException e) {
            printSQLException(e);

            throw new SQLException("Cannot Count Programs From DataBase");
        } finally {
            stmt.close();
            closeConnection(con);
        }
    }

    @Override
    public ProgramDto getById(Long id) throws SQLException {
        String            sqlQuery = "select * from programs where ProgramID=?";
        PreparedStatement prepstmt = null;
        Connection        con      = null;

        try {
            con      = getConnection();
            prepstmt = con.prepareStatement(sqlQuery);
            prepstmt.setLong(1, id);

            ResultSet rs = prepstmt.executeQuery();

            if (rs.next()) {
                return makeProgram(rs);
            } else {
                return null;
            }
        } catch (SQLException e) {
            printSQLException(e);

            throw new SQLException("Cannot Retrieve Programs From DataBase");
        } finally {
            prepstmt.close();
            closeConnection(con);
        }
    }

    @Override
    public List<ProgramDto> getAll() throws SQLException {
        String     sqlQuery = "select * from programs";
        Statement  stmt     = null;
        Connection con      = null;

        try {
            con  = getConnection();
            stmt = con.createStatement();

            ResultSet rs = stmt.executeQuery(sqlQuery);

            return makeProgramList(rs);
        } catch (SQLException e) {
            printSQLException(e);

            throw new SQLException("Cannot Retrieve Programs From DataBase");
        } finally {
            stmt.close();
            closeConnection(con);
        }
    }

    @Override
    public boolean checkNewProgramId(Long id) throws SQLException {
        String            sqlQuery = "select * from programs where ProgramID = ?";
        PreparedStatement prepstmt = null;
        Connection        con      = null;

        try {
            con      = getConnection();
            prepstmt = con.prepareStatement(sqlQuery);
            prepstmt.setLong(1, id);

            ResultSet rs = prepstmt.executeQuery();

            if (rs.next()) {
                return false;
            } else {
                return true;
            }
        } catch (SQLException e) {
            printSQLException(e);

            throw new SQLException("Cannot Validate Program ID In DataBase");
        } finally {
            prepstmt.close();
            closeConnection(con);
        }
    }

    @Override
    public List<ProgramDto> getAll(String searchText) throws SQLException {
        String     sqlQuery = "select * from programs where name like '%" + searchText + "%'";
        Statement  stmt     = null;
        Connection con      = null;

        try {
            con  = getConnection();
            stmt = con.createStatement();

            ResultSet rs = stmt.executeQuery(sqlQuery);

            return makeProgramList(rs);
        } catch (SQLException e) {
            printSQLException(e);

            throw new SQLException("Cannot Retrieve Programs From DataBase");
        } finally {
            stmt.close();
            closeConnection(con);
        }
    }

    @Override
    public List<ProgramDto> getAllByUserId(String searchText, Long userId) throws SQLException {
        String sqlQuery = "select * from programs where programid in "
                          + "(select programid from controllers where cellinkid in "
                          + "(select cellinkid from cellinks where userid=?)) and name like '%" + searchText + "%'";
        PreparedStatement prepstmt = null;
        Connection        con      = null;

        try {
            con      = getConnection();
            prepstmt = con.prepareStatement(sqlQuery);
            prepstmt.setLong(1, userId);

            ResultSet rs = prepstmt.executeQuery();

            return makeProgramList(rs);
        } catch (SQLException e) {
            printSQLException(e);

            throw new SQLException("Cannot Retrieve Programs From DataBase");
        } finally {
            prepstmt.close();
            closeConnection(con);
        }
    }

    @Override
    public List<ProgramDto> getAllByUserCompany(String searchText, String company) throws SQLException {
        String sqlQuery = "select * from programs where programid in "
                          + "(select distinct programid from controllers where cellinkid in "
                          + "(select cellinkid from cellinks where userid in "
                          + "(select userid from users where company=?) and programid<>1)) and name like '%"
                          + searchText + "%'";
        PreparedStatement prepstmt = null;
        Connection        con      = null;

        try {
            con      = getConnection();
            prepstmt = con.prepareStatement(sqlQuery);
            prepstmt.setString(1, company);

            ResultSet rs = prepstmt.executeQuery();

            return makeProgramList(rs);
        } catch (SQLException e) {
            printSQLException(e);

            throw new SQLException("Cannot Retrieve Programs From DataBase");
        } finally {
            prepstmt.close();
            closeConnection(con);
        }
    }

    @Override
    public List<ProgramDto> getAll(String searchText, String index) throws SQLException {
        String            sqlQuery = "select * from programs where name like '%" + searchText + "%' limit ?,25 ";
        PreparedStatement prepstmt = null;
        Connection        con      = null;

        try {
            con      = getConnection();
            prepstmt = con.prepareStatement(sqlQuery);
            prepstmt.setInt(1, Integer.parseInt(index));

            ResultSet rs = prepstmt.executeQuery();

            return makeProgramList(rs);
        } catch (SQLException e) {
            printSQLException(e);

            throw new SQLException("Cannot Retrieve Programs From DataBase");
        } finally {
            prepstmt.close();
            closeConnection(con);
        }
    }
}



