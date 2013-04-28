package com.agrologic.app.dao.mysql.impl;

import com.agrologic.app.dao.DaoFactory;
import com.agrologic.app.dao.RelayDao;
import com.agrologic.app.model.Data;
import com.agrologic.app.model.ProgramRelay;
import com.agrologic.app.model.Relay;
import com.agrologic.app.util.RelayUtil;

import java.sql.*;
import java.util.Collection;

public class RelayDaoImpl implements RelayDao {

    protected DaoFactory dao;

    public RelayDaoImpl(DaoFactory daoFactory) {
        dao = daoFactory;
    }

    public void getColumnsNames() {
        String query = "select * from programrelays";
        PreparedStatement prepstmt = null;
        Connection con = null;
        ResultSetMetaData rsmd;

        try {
            con = dao.getConnection();
            prepstmt = con.prepareStatement(query);
            rsmd = prepstmt.getMetaData();
            int numColumns = rsmd.getColumnCount();
            numColumns = numColumns;
        } catch (SQLException sqle) {
            System.out.println(sqle.toString());
        } catch (Exception e) {
            System.out.println("getColumnNames: " + e.toString());
            e.printStackTrace();
        }
    }

    @Override
    public void insert(Relay relay) throws SQLException {
        String sqlQuery = "insert into relaynames values (?,?)";
        PreparedStatement prepstmt = null;
        Connection con = null;

        try {
            con = dao.getConnection();
            prepstmt = con.prepareStatement(sqlQuery);
            prepstmt.setLong(1, relay.getId());
            prepstmt.setString(2, relay.getText());
            prepstmt.executeUpdate();
        } catch (SQLException e) {
            dao.printSQLException(e);

            throw new SQLException("Cannot Insert Relay To The DataBase", e);
        } finally {
            prepstmt.close();
            dao.closeConnection(con);
        }
    }

    @Override
    public void insert(Collection<Relay> relayList) throws SQLException {

        Collection<Relay> uniqueRelayList = RelayUtil.getUniqueElements(relayList);
        String sqlQuery = "INSERT INTO RELAYNAMES (ID, NAME) VALUES (?,?)";
        PreparedStatement prepstmt = null;
        Connection con = null;

        try {
            con = dao.getConnection();
            // turn off autocommit
            con.setAutoCommit(false);
            int i = 0;
            prepstmt = con.prepareStatement(sqlQuery);
            for (Relay relay : uniqueRelayList) {
                prepstmt.setLong(1, relay.getId());
                prepstmt.setString(2, relay.getText());
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
    public void insertTranslation(Long relayId, Long langId, String translation) throws SQLException {
        String sqlQuery =
                "insert into relaybylanguage values (?,?,?) on duplicate key update UnicodeText=values(UnicodeText)";
        PreparedStatement prepstmt = null;
        Connection con = null;

        try {
            con = dao.getConnection();
            prepstmt = con.prepareStatement(sqlQuery);
            prepstmt.setLong(1, relayId);
            prepstmt.setLong(2, langId);
            prepstmt.setString(3, translation);
            prepstmt.executeUpdate();
        } catch (SQLException e) {
            dao.printSQLException(e);

            throw new SQLException("Cannot Insert Translation To DataBase", e);
        } finally {
            prepstmt.close();
            dao.closeConnection(con);
        }
    }

    @Override
    public void insertTranslation(Collection<Relay> relayList) throws SQLException {
        String sqlQuery = "insert into relaybylanguage values (?,?,?) ";

//      + "on duplicate key update UnicodeText=values(UnicodeText)";
        PreparedStatement prepstmt = null;
        Connection con = null;

        try {
            con = dao.getConnection();

            // turn off autocommit
            con.setAutoCommit(false);
            int i = 0;
            prepstmt = con.prepareStatement(sqlQuery);
            for (Relay relay : relayList) {
                prepstmt.setLong(1, relay.getId());
                prepstmt.setLong(2, relay.getLangId());
                prepstmt.setString(3, relay.getUnicodeText());
                prepstmt.addBatch();

                if ((i + 1) % 200 == 0) {
                    prepstmt.executeBatch();    // Execute every 200 items.
                    System.out.print(".");
                }

                i++;
            }

            System.out.print("");
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
    public void insertProgramRelays(Collection<ProgramRelay> programRelayList) throws SQLException {
        String sqlQuery = "insert into programrelays values (?,?,?,?,?,?)";
        PreparedStatement prepstmt = null;
        Connection con = null;

        try {
            con = dao.getConnection();
            con.setAutoCommit(false);
            prepstmt = con.prepareStatement(sqlQuery);

            for (ProgramRelay programRelay : programRelayList) {
                prepstmt.setLong(1, programRelay.getDataId());
                prepstmt.setInt(2, programRelay.getBitNumber());
                prepstmt.setString(3, programRelay.getText());
                prepstmt.setLong(4, programRelay.getProgramId());
                prepstmt.setInt(5, programRelay.getRelayNumber());
                prepstmt.setLong(6, programRelay.getRelayTextId());
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
    public void update(Relay relay) throws SQLException {
        String sqlQuery = "update relaynames set Name=? where ID=?";
        PreparedStatement prepstmt = null;
        Connection con = null;

        try {
            con = dao.getConnection();
            prepstmt = con.prepareStatement(sqlQuery);
            prepstmt.setString(1, relay.getText());
            prepstmt.setLong(2, relay.getId());
            prepstmt.executeUpdate();
        } catch (SQLException e) {
            dao.printSQLException(e);

            throw new SQLException("Cannot Update Relay In DataBase");
        } finally {
            prepstmt.close();
            dao.closeConnection(con);
        }
    }

    @Override
    public void remove(Long id) throws SQLException {
        String sqlQuery = "delete from relaynames where ID=?";
        PreparedStatement prepstmt = null;
        Connection con = null;

        try {
            con = dao.getConnection();
            prepstmt = con.prepareStatement(sqlQuery);
            prepstmt.setLong(1, id);
            prepstmt.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException("Cannot Remove Relay From DataBase", e);
        } finally {
            prepstmt.close();
            dao.closeConnection(con);
        }
    }

    public void update(Collection<Data> relayDatas) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Relay getById(Long id) throws SQLException {
        String sqlQuery = "select * from relaynames where ID=?";
        PreparedStatement prepstmt = null;
        Connection con = null;

        try {
            con = dao.getConnection();
            prepstmt = con.prepareStatement(sqlQuery);
            prepstmt.setLong(1, id);
            ResultSet rs = prepstmt.executeQuery();
            if (rs.next()) {
                return RelayUtil.makeRelay(rs);
            } else {
                return null;
            }
        } catch (SQLException e) {
            throw new SQLException("Cannot Retrieve Relay From DataBase", e);
        } finally {
            prepstmt.close();
            dao.closeConnection(con);
        }
    }

    @Override
    public Collection<Relay> getAll() throws SQLException {
        String sqlQuery = "select * from relaynames order by ID,Name";
        Statement stmt = null;
        Connection con = null;

        try {
            con = dao.getConnection();
            stmt = con.createStatement();

            ResultSet rs = stmt.executeQuery(sqlQuery);

            return RelayUtil.makeRelayList(rs);
        } catch (SQLException e) {
            System.out.println("Caught SQL SQLException : " + e.getMessage());

            throw new SQLException("Cannot Retrieve Users From DataBase");
        } finally {
            stmt.close();
            dao.closeConnection(con);
        }
    }

    @Override
    public Collection<Relay> getAll(Long langId) throws SQLException {
        String sqlQuery = "select r1.id, r1.name, r2.relayid, r2.langid, r2.unicodetext from relaynames r1 "
                + "left join relaybylanguage r2 on r1.id=r2.relayid and langid=" + langId;
        Statement stmt = null;
        Connection con = null;

        try {
            con = dao.getConnection();
            stmt = con.createStatement();

            ResultSet rs = stmt.executeQuery(sqlQuery);

            return RelayUtil.makeRelayList(rs);
        } catch (SQLException e) {
            throw new SQLException("Cannot Retrieve Relay From DataBase", e);
        } finally {
            stmt.close();
            dao.closeConnection(con);
        }
    }

    @Override
    public Collection<Relay> getAllWithTranslation() throws SQLException {
        String sqlQuery = "select * from relaynames "
                + "join relaybylanguage on relaynames.id=relaybylanguage.relayid "
                + "order by relaybylanguage.langid , relaybylanguage.relayid";
        Statement stmt = null;
        Connection con = null;

        try {
            con = dao.getConnection();
            stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sqlQuery);
            return RelayUtil.makeRelayList(rs);
        } catch (SQLException e) {
            dao.printSQLException(e);

            throw new SQLException("Cannot Retrieve Relay From DataBase");
        } finally {
            stmt.close();
            dao.closeConnection(con);
        }
    }

    @Override
    public Collection<ProgramRelay> getAllProgramRelays(Long programId) throws SQLException {
        String sqlQuery = "select * from programrelays where ProgramID=? ";
        PreparedStatement prepstmt = null;
        Connection con = null;

        try {
            con = dao.getConnection();
            prepstmt = con.prepareStatement(sqlQuery);
            prepstmt.setLong(1, programId);
            return RelayUtil.makeProgramRelayList(prepstmt.executeQuery());
        } catch (SQLException e) {
            throw new SQLException("Cannot Retrieve ProgramRelays From DataBase", e);
        } finally {
            prepstmt.close();
            dao.closeConnection(con);
        }
    }

    @Override
    public Collection<ProgramRelay> getSelectedProgramRelays(Long programId) throws SQLException {
        String sqlQuery =
                "select * from programrelays "
                        + "where ProgramID=? and TEXT not Like '%None%' and Text not Like '%Damy%' order by DataID,BitNumber";
        PreparedStatement prepstmt = null;
        Connection con = null;

        try {
            con = dao.getConnection();
            prepstmt = con.prepareStatement(sqlQuery);
            prepstmt.setLong(1, programId);
            return RelayUtil.makeProgramRelayList(prepstmt.executeQuery());
        } catch (SQLException e) {
            throw new SQLException("Cannot Retrieve ProgramRelays From DataBase", e);
        } finally {
            prepstmt.close();
            dao.closeConnection(con);
        }
    }

    @Override
    public Collection<ProgramRelay> getSelectedProgramRelays(Long programId, Long langId) throws SQLException {
//        String sqlQuery = "select * from programrelays as progrelay "
//                + "join (select RelayID,LangID,UnicodeText from relaybylanguage) as rbl "
//                + "where rbl.RelayID = progrelay.RelayTextID and rbl.LangID=? and ProgramID=? "
//                + "and Text not Like '%None%' and Text not Like '%Damy%' order by DataID,BitNumber";
        String sqlQuery = "select * from programrelays "
                + "left join relaybylanguage on relaybylanguage.RelayID=programrelays.RelayTextID "
                + "and relaybylanguage.langid=? "
                + "where programrelays.programid=? "
                + "and Text not Like '%None%' and Text not Like '%Damy%' order by DataID,BitNumber";

        PreparedStatement prepstmt = null;
        Connection con = null;

        try {
            con = dao.getConnection();
            prepstmt = con.prepareStatement(sqlQuery);
            prepstmt.setLong(1, langId);
            prepstmt.setLong(2, programId);
            ResultSet rs = prepstmt.executeQuery();
            return RelayUtil.makeProgramRelayList(rs);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException("Cannot Retrieve ProgramRelays From DataBase", e);
        } finally {
            prepstmt.close();
            dao.closeConnection(con);
        }
    }
}
