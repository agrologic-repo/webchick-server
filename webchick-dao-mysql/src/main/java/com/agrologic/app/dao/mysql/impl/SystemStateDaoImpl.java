package com.agrologic.app.dao.mysql.impl;

import com.agrologic.app.dao.DaoFactory;
import com.agrologic.app.dao.SystemStateDao;
import com.agrologic.app.model.ProgramSystemState;
import com.agrologic.app.model.SystemState;
import com.agrologic.app.util.SystemStateUtil;

import java.sql.*;
import java.util.Collection;

public class SystemStateDaoImpl implements SystemStateDao {
    protected DaoFactory dao;

    public SystemStateDaoImpl(DaoFactory daoFactory) {
        dao = daoFactory;
    }

    @Override
    public void insert(SystemState systemstate) throws SQLException {
        String sqlQuery = "insert into systemstatenames values (?,?)";
        PreparedStatement prepstmt = null;
        Connection con = null;

        try {
            con = dao.getConnection();
            prepstmt = con.prepareStatement(sqlQuery);
            prepstmt.setLong(1, systemstate.getId());
            prepstmt.setString(2, systemstate.getText());
            prepstmt.executeUpdate();
        } catch (SQLException e) {
            dao.printSQLException(e);
            throw new SQLException("Cannot Insert System State To The DataBase", e);
        } finally {
            prepstmt.close();
            dao.closeConnection(con);
        }
    }

    @Override
    public void insert(Collection<SystemState> systemStateList) throws SQLException {
        // there is duplicate SystemState elements in systemStateList we need only unique elements
        Collection<SystemState> uniqueSystemStateList = SystemStateUtil.getUniqueElements(systemStateList);
        String sqlQuery = "INSERT INTO SYSTEMSTATENAMES (ID, NAME) VALUES(?,?)";
        PreparedStatement prepstmt = null;
        Connection con = null;

        try {
            con = dao.getConnection();
            // turn off autocommit
            con.setAutoCommit(false);
            int i = 0;
            prepstmt = con.prepareStatement(sqlQuery);
            for (SystemState systemState : uniqueSystemStateList) {
                prepstmt.setLong(1, systemState.getId());
                prepstmt.setString(2, systemState.getText());
                prepstmt.addBatch();
                if ((i + 1) % 200 == 0) {
                    prepstmt.executeBatch();    // Execute every 200 items.
                    System.out.print(".");
                }
                i++;
            }
            prepstmt.executeBatch();
            con.commit();
            con.setAutoCommit(true);
        } catch (SQLException e) {
            dao.printSQLException(e);
            if (con != null) {
                try {
                    con.rollback();
                } catch (SQLException ex) {
                    dao.printSQLException(ex);
                    throw new SQLException("Transaction is being rolled back");
                }
            }
        } finally {
            prepstmt.close();
            dao.closeConnection(con);
        }
    }

    @Override
    public void insertTranslation(Long systemstateId, Long langId, String translation) throws SQLException {
        String sqlQuery =
                "insert into systemstatebylanguage values (?,?,?) on duplicate key update UnicodeName=values(UnicodeName)";
        PreparedStatement prepstmt = null;
        Connection con = null;

        try {
            con = dao.getConnection();
            prepstmt = con.prepareStatement(sqlQuery);
            prepstmt.setLong(1, systemstateId);
            prepstmt.setLong(2, langId);
            prepstmt.setString(3, translation);
            prepstmt.executeUpdate();
        } catch (SQLException e) {
            dao.printSQLException(e);
            throw new SQLException("Cannot Insert row into SystemStateByLanguage Table ", e);
        } finally {
            prepstmt.close();
            dao.closeConnection(con);
        }
    }

    @Override
    public void insertTranslation(Collection<SystemState> systemStateList) throws SQLException {
        String sqlQuery = "insert into systemStatebylanguage values (?,?,?) ";
//      + "on duplicate key update UnicodeName=values(UnicodeName)";
        PreparedStatement prepstmt = null;
        Connection con = null;

        try {
            con = dao.getConnection();
            // turn off autocommit
            con.setAutoCommit(false);
            int i = 0;
            prepstmt = con.prepareStatement(sqlQuery);
            for (SystemState systemState : systemStateList) {
                prepstmt.setLong(1, systemState.getId());
                prepstmt.setLong(2, systemState.getLangId());
                prepstmt.setString(3, systemState.getUnicodeText());
                prepstmt.addBatch();
                if ((i + 1) % 200 == 0) {
                    prepstmt.executeBatch();    // Execute every 200 items.
                    System.out.print(".");
                }
                i++;
            }
            prepstmt.executeBatch();
            con.commit();
            con.setAutoCommit(true);
        } catch (SQLException e) {
            dao.printSQLException(e);

            if (con != null) {
                try {
                    con.rollback();
                } catch (SQLException ex) {
                    dao.printSQLException(ex);
                    throw new SQLException("Transaction is being rolled back", ex);
                }
            }
        } finally {
            prepstmt.close();
            dao.closeConnection(con);
        }
    }

    @Override
    public void insertProgramSystemState(Collection<ProgramSystemState> programSystemStates) throws SQLException {
        String sqlQuery = "insert into programsysstates values (?,?,?,?,?,?)";
        PreparedStatement prepstmt = null;
        Connection con = null;

        try {
            con = dao.getConnection();
            con.setAutoCommit(false);
            prepstmt = con.prepareStatement(sqlQuery);
            for (ProgramSystemState programSystemState : programSystemStates) {
                prepstmt.setLong(1, programSystemState.getDataId());
                prepstmt.setInt(2, programSystemState.getNumber());
                prepstmt.setString(3, programSystemState.getText());
                prepstmt.setLong(4, programSystemState.getProgramId());
                prepstmt.setInt(5, programSystemState.getSystemStateNumber());
                prepstmt.setLong(6, programSystemState.getSystemStateTextId());
                prepstmt.addBatch();
            }
            prepstmt.executeBatch();
            con.commit();
            con.setAutoCommit(true);
        } catch (SQLException e) {
            dao.printSQLException(e);

            if (con != null) {
                try {
                    con.rollback();
                } catch (SQLException ex) {
                    dao.printSQLException(ex);

                    throw new SQLException("Transaction is being rolled back", ex);
                }
            }
        } finally {
            prepstmt.close();
            dao.closeConnection(con);
        }
    }

    @Override
    public void update(SystemState systemstate) throws SQLException {
        String sqlQuery = "update systemstatenames set Name=? where ID=?";
        PreparedStatement prepstmt = null;
        Connection con = null;

        try {
            con = dao.getConnection();
            prepstmt = con.prepareStatement(sqlQuery);
            prepstmt.setString(1, systemstate.getText());
            prepstmt.setLong(2, systemstate.getId());
            prepstmt.executeUpdate();
        } catch (SQLException e) {
            dao.printSQLException(e);

            throw new SQLException("Cannot Update system state In DataBase");
        } finally {
            prepstmt.close();
            dao.closeConnection(con);
        }
    }

    @Override
    public void remove(Long id) throws SQLException {
        String sqlQuery = "delete from systemstatenames where ID=?";
        PreparedStatement prepstmt = null;
        Connection con = null;

        try {
            con = dao.getConnection();
            prepstmt = con.prepareStatement(sqlQuery);
            prepstmt.setLong(1, id);
            prepstmt.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException("Cannot Remove system states From DataBase", e);
        } finally {
            prepstmt.close();
            dao.closeConnection(con);
        }
    }

    @Override
    public SystemState getById(Long id) throws SQLException {
        String sqlQuery = "select * from systemstatenames where ID=?";
        PreparedStatement prepstmt = null;
        Connection con = null;

        try {
            con = dao.getConnection();
            prepstmt = con.prepareStatement(sqlQuery);
            prepstmt.setLong(1, id);
            ResultSet rs = prepstmt.executeQuery();
            if (rs.next()) {
                return SystemStateUtil.makeSystemState(rs);
            } else {
                return null;
            }
        } catch (SQLException e) {
            dao.printSQLException(e);

            throw new SQLException("Cannot Retrieve SystemState From DataBase", e);
        } finally {
            prepstmt.close();
            dao.closeConnection(con);
        }
    }

    @Override
    public Collection<SystemState> getAll() throws SQLException {
        String sqlQuery = "select * from systemstatenames order by ID,Name";
        Statement stmt = null;
        Connection con = null;

        try {
            con = dao.getConnection();
            stmt = con.createStatement();

            ResultSet rs = stmt.executeQuery(sqlQuery);

            return SystemStateUtil.makeSystemStateList(rs);
        } catch (SQLException e) {
            dao.printSQLException(e);

            throw new SQLException("Cannot Retrieve SystemState From DataBase", e);
        } finally {
            stmt.close();
            dao.closeConnection(con);
        }
    }

    @Override
    public Collection<SystemState> getAll(Long langId) throws SQLException {
        String sqlQuery =
                "select s1.id, s1.name, s2.systemstateid, s2.langid, s2.unicodename from systemstatenames s1 "
                        + "left join systemstatebylanguage s2 on s1.id=s2.systemstateid and langid=" + langId;
        Statement stmt = null;
        Connection con = null;

        try {
            con = dao.getConnection();
            stmt = con.createStatement();

            ResultSet rs = stmt.executeQuery(sqlQuery);

            return SystemStateUtil.makeSystemStateList(rs);
        } catch (SQLException e) {
            dao.printSQLException(e);

            throw new SQLException("Cannot Retrieve SystemState From DataBase", e);
        } finally {
            stmt.close();
            dao.closeConnection(con);
        }
    }

    @Override
    public Collection<SystemState> getAllWithTranslation() throws SQLException {
        String sqlQuery = "select * from systemstatenames "
                + "join systemstatebylanguage on systemstatenames.id=systemstatebylanguage.systemstateid "
                + "order by systemstatebylanguage.langid , systemstatebylanguage.systemstateid";
        Statement stmt = null;
        Connection con = null;

        try {
            con = dao.getConnection();
            stmt = con.createStatement();

            ResultSet rs = stmt.executeQuery(sqlQuery);

            return SystemStateUtil.makeSystemStateList(rs);
        } catch (SQLException e) {
            dao.printSQLException(e);

            throw new SQLException("Cannot Retrieve SystemState From DataBase");
        } finally {
            stmt.close();
            dao.closeConnection(con);
        }
    }

    @Override
    public Collection<ProgramSystemState> getAllProgramSystemStates(Long programId) throws SQLException {
        String sqlQuery = "select * from programsysstates where ProgramID=? ";
        PreparedStatement prepstmt = null;
        Connection con = null;

        try {
            con = dao.getConnection();
            prepstmt = con.prepareStatement(sqlQuery);
            prepstmt.setLong(1, programId);
            return SystemStateUtil.makeProgramSystemStateList(prepstmt.executeQuery());
        } catch (SQLException e) {
            throw new SQLException("Cannot Retrieve ProgramSystemState From DataBase", e);
        } finally {
            prepstmt.close();
            dao.closeConnection(con);
        }
    }

    @Override
    public Collection<ProgramSystemState> getSelectedProgramSystemStates(Long programId) throws SQLException {
        String sqlQuery = "select * from programsysstates where ProgramID=? and TEXT not Like '%None%' "
                + "and Text not Like '%Damy%' order by DataID, SystemStateNumber";
        PreparedStatement prepstmt = null;
        Connection con = null;

        try {
            con = dao.getConnection();
            prepstmt = con.prepareStatement(sqlQuery);
            prepstmt.setLong(1, programId);
            return SystemStateUtil.makeProgramSystemStateList(prepstmt.executeQuery());
        } catch (SQLException e) {
            throw new SQLException("Cannot Retrieve ProgramSystemState From DataBase", e);
        } finally {
            prepstmt.close();
            dao.closeConnection(con);
        }
    }

    @Override
    public Collection<ProgramSystemState> getAllProgramSystemStates(Long programId, Long langId) throws SQLException {
        String sqlQuery =
                "select * from programsysstates as pss "
                        + " inner join systemstatebylanguage ssbl on "
                        + " ssbl.SystemStateID=pss.systemstatetextid and ssbl.langid=? and pss.programid=? and pss.text not like '%None%' "
                        + " and pss.Text not Like '%None%' and pss.Text not Like '%Damy%' order by pss.DataID,pss.Number ";
        PreparedStatement prepstmt = null;
        Connection con = null;

        try {
            con = dao.getConnection();
            prepstmt = con.prepareStatement(sqlQuery);
            prepstmt.setLong(1, langId);
            prepstmt.setLong(2, programId);
            return SystemStateUtil.makeProgramSystemStateList(prepstmt.executeQuery());
        } catch (SQLException e) {
            throw new SQLException("Cannot Retrieve ProgramSystemState From DataBase", e);
        } finally {
            prepstmt.close();
            dao.closeConnection(con);
        }
    }
}