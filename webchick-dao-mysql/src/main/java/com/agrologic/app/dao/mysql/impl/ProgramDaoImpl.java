
/*
* To change this template, choose Tools | Templates
* and open the template in the editor.
 */
package com.agrologic.app.dao.mysql.impl;

//~--- non-JDK imports --------------------------------------------------------

import com.agrologic.app.dao.DaoFactory;
import com.agrologic.app.dao.DaoType;
import com.agrologic.app.dao.ProgramDao;
import com.agrologic.app.model.Program;
import com.agrologic.app.util.ProgramUtil;
import java.sql.*;
import java.util.Collection;

/**
 * Title: ProgramDaoImpl
 * <br> Description:
 * <br> Copyright: Copyright (c) 2009
 * <br> Company: AgroLogic LTD.
 * <br>
 *
 * @author Valery Manakhimov <br>
 * @version 1.1 <br>
 */
public class ProgramDaoImpl implements ProgramDao {
    protected DaoFactory dao;

    public ProgramDaoImpl(DaoFactory daoFactory) {
        dao = daoFactory;
    }

    @Override
    public void insert(Program program) throws SQLException {
        String            sqlQuery = "insert into programs values (?,?,?,?)";
        PreparedStatement prepstmt = null;
        Connection        con      = null;

        try {
            con      = dao.getConnection();
            prepstmt = con.prepareStatement(sqlQuery);
            prepstmt.setObject(1, program.getId());
            prepstmt.setString(2, program.getName());
            prepstmt.setString(3, program.getCreatedDate());
            prepstmt.setString(4, program.getModifiedDate());
            prepstmt.executeUpdate();
        } catch (SQLException e) {
            dao.printSQLException(e);

            throw new SQLException("Cannot Insert Program " + program + " To The DataBase");
        } finally {
            prepstmt.close();
            dao.closeConnection(con);
        }
    }

    @Override
    public void update(Program program) throws SQLException {
        String            sqlQuery = "update programs set Name=?, Modified=? where ProgramID=?";
        PreparedStatement prepstmt = null;
        Connection        con      = null;

        try {
            con      = dao.getConnection();
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
        String            sqlQuery = "delete from programs where ProgramID=?";
        PreparedStatement prepstmt = null;
        Connection        con      = null;

        try {
            con      = dao.getConnection();
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
    public Program getById(Long id) throws SQLException {
        String            sqlQuery = "select * from programs where ProgramID=?";
        PreparedStatement prepstmt = null;
        Connection        con      = null;

        try {
            con      = dao.getConnection();
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
        String     sqlQuery = "select * from programs";
        Statement  stmt     = null;
        Connection con      = null;

        try {
            con  = dao.getConnection();
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
    public boolean programExist(Long id) throws SQLException {
        Program p = getById(id);

        return p != null;
    }
}


//~ Formatted by Jindent --- http://www.jindent.com
