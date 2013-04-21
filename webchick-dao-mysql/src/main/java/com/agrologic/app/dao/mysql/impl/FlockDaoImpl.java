/**
 * To change this template, choose Tools | Templates and open the template in the editor.
 */
package com.agrologic.app.dao.mysql.impl;

//~--- non-JDK imports --------------------------------------------------------
import com.agrologic.app.dao.DaoFactory;
import com.agrologic.app.dao.DaoType;
import com.agrologic.app.dao.FlockDao;
import com.agrologic.app.model.Flock;
import java.sql.*;
import java.util.*;

/**
 * Title: FlockDaoImpl.java <br> Description: <br> Copyright: Copyright � 2010 <br> Company: AgroLogic Ltd. �<br>
 *
 * @author Valery Manakhimov <br>
 * @version 0.1.1 <br>
 */
public class FlockDaoImpl implements FlockDao {

    protected DaoFactory dao;

    public FlockDaoImpl(DaoFactory daoFactory) {
        dao = daoFactory;
    }

    /**
     * Help to create flock from result set .
     *
     * @param rs a result set
     * @return flock an objects that encapsulates a flock attributes
     * @throws java.sql.SQLException
     */
    private Flock makeFlock(ResultSet rs) throws SQLException {
        Flock flock = new Flock();
        flock.setFlockId(rs.getLong("FlockID"));
        flock.setControllerId(rs.getLong("ControllerId"));
        flock.setFlockName(rs.getString("Name"));
        flock.setStatus(rs.getString("Status"));
        flock.setStartDate(rs.getString("StartDate"));
        flock.setEndDate(rs.getString("EndDate"));
        try {
            flock.setQuantityMale(rs.getInt("QuantityMale"));
        } catch (SQLException ex) {
            //
        }
        try {
            flock.setCostChickMale(rs.getFloat("CostChickMale"));
        } catch (SQLException ex) {
            //;
        }
        try {
            flock.setQuantityFemale(rs.getInt("QuantityFemale"));
        } catch (SQLException ex) {
            //;
        }
        try {
            flock.setCostChickFemale(rs.getFloat("CostChickFemale"));
        } catch (SQLException ex) {
            ;
        }
        try {
            flock.setQuantityFemale(rs.getInt("QuantityFemale"));
        } catch (SQLException ex) {
            ;
        }
        try {
            flock.setCostChickFemale(rs.getFloat("CostChickFemale"));
        } catch (SQLException ex) {
            ;
        }
        try {
            flock.setTotalChicks(rs.getFloat("TotalChicks"));
        } catch (SQLException ex) {
            ;
        }
        try {
            flock.setGasBegin(rs.getInt("GasBegin"));
        } catch (SQLException ex) {
            ;
        }
        try {
            flock.setGasEnd(rs.getInt("GasEnd"));
        } catch (SQLException ex) {
            ;
        }
        try {

            flock.setCostGas(rs.getFloat("CostGas"));
        } catch (SQLException ex) {
            ;
        }
        try {
            flock.setCostGasEnd(rs.getFloat("CostGasEnd"));
        } catch (SQLException ex) {
            ;
        }

        try {
            flock.setGasAdd(rs.getInt("GasAdd"));
        } catch (SQLException ex) {
            ;
        }

        try {
            flock.setTotalGas(rs.getFloat("TotalGas"));
        } catch (SQLException ex) {
            ;
        }

        try {
            flock.setFuelBegin(rs.getInt("FuelBegin"));
        } catch (SQLException ex) {
            ;
        }
        try {
            flock.setFuelEnd(rs.getInt("FuelEnd"));
        } catch (SQLException ex) {
            ;
        }
        try {
            flock.setCostFuel(rs.getFloat("CostFuel"));
        } catch (SQLException ex) {
            ;
        }
        try {
            flock.setCostFuelEnd(rs.getFloat("CostFuelEnd"));
        } catch (SQLException ex) {
            ;
        }
        try {
            flock.setFuelAdd(rs.getInt("FuelAdd"));
        } catch (SQLException ex) {
            ;
        }
        try {
            flock.setTotalFuel(rs.getFloat("TotalFuel"));
        } catch (SQLException ex) {
            ;
        }

        try {
            flock.setWaterBegin(rs.getInt("WaterBegin"));
        } catch (SQLException ex) {
            ;
        }
        try {
            flock.setWaterEnd(rs.getInt("WaterEnd"));
        } catch (SQLException ex) {
            ;
        }

        try {
            flock.setFeedAdd(rs.getInt("FeedAdd"));
        } catch (SQLException ex) {
            ;
        }
        try {
            flock.setTotalFeed(rs.getFloat("TotalFeed"));
        } catch (SQLException ex) {
            ;
        }
        try {
            flock.setCostWater(rs.getFloat("CostWater"));
        } catch (SQLException ex) {
            ;
        }
        try {
            flock.setQuantityWater(rs.getInt("QuantityWater"));
        } catch (SQLException ex) {
            ;
        }
        try {
            flock.setTotalWater(rs.getFloat("TotalWater"));
        } catch (SQLException ex) {
            ;
        }
        try {
            flock.setElectBegin(rs.getInt("ElectBegin"));
        } catch (SQLException ex) {
            ;
        }
        try {
            flock.setElectEnd(rs.getInt("ElectEnd"));
        } catch (SQLException ex) {
            ;
        }
        try {
            flock.setCostElect(rs.getFloat("CostElect"));
        } catch (SQLException ex) {
            ;
        }
        try {
            flock.setQuantityElect(rs.getInt("QuantityElect"));
        } catch (SQLException ex) {
            ;
        }
        try {
            flock.setTotalElect(rs.getFloat("TotalElect"));
        } catch (SQLException ex) {
            ;
        }
        try {
            flock.setSpreadAdd(rs.getInt("SpreadAdd"));
        } catch (SQLException ex) {
            ;
        }
        try {
            flock.setTotalSpread(rs.getFloat("TotalSpread"));
        } catch (SQLException ex) {
            ;
        }
        try {
            flock.setTotalLabor(rs.getFloat("TotalLabor"));
        } catch (SQLException ex) {
            ;
        }
        try {
            flock.setTotalMedic(rs.getFloat("TotalMedic"));
        } catch (SQLException ex) {
            ;
        }
        try {
            flock.setExpenses(rs.getFloat("Expenses"));
        } catch (SQLException ex) {
            ;
        }
        try {
            flock.setRevenues(rs.getFloat("Revenues"));
        } catch (SQLException ex) {
            ;
        }
        try {
            flock.setCurrency(rs.getString("Currency"));
        } catch (SQLException ex) {
            ;
        }
        return flock;
    }

    /**
     * Help to create list of flocks from result set
     *
     * @param rs a result set
     * @return users a list of Flock objects
     * @throws java.sql.SQLException
     */
    private List<Flock> makeFlockList(ResultSet rs) throws SQLException {
        List<Flock> flocks = new ArrayList<Flock>();

        while (rs.next()) {
            flocks.add(makeFlock(rs));
        }

        return flocks;
    }

    @Override
    public void insert(Flock flock) throws SQLException {
        String sqlQuery =
                "insert into flocks (FlockID,ControllerID,Name,Status,StartDate,EndDate) values (?,?,?,?,?,?)";
        PreparedStatement prepstmt = null;
        Connection con = null;

        try {
            con = dao.getConnection();
            prepstmt = con.prepareStatement(sqlQuery);
            prepstmt.setObject(1, null);
            prepstmt.setLong(2, flock.getControllerId());
            prepstmt.setString(3, flock.getFlockName());
            prepstmt.setString(4, flock.getStatus());
            prepstmt.setString(5, flock.getStartTime());
            prepstmt.setString(6, flock.getEndTime());
            prepstmt.executeUpdate();
        } catch (SQLException e) {
            dao.printSQLException(e);
            throw new SQLException("Can not Add New Flcok To The DataBase");
        } finally {
            prepstmt.close();
            dao.closeConnection(con);
        }
    }

    @Override
    public void update(Flock flock) throws SQLException {
        String sqlQuery =
                "update flocks set ControllerId=?, Name=?, Status=?,StartDate=?,EndDate=? where FlockID=?";
        PreparedStatement prepstmt = null;
        Connection con = null;

        try {
            con = dao.getConnection();
            prepstmt = con.prepareStatement(sqlQuery);
            prepstmt.setLong(1, flock.getControllerId());
            prepstmt.setString(2, flock.getFlockName());
            prepstmt.setString(3, flock.getStatus());
            prepstmt.setString(4, flock.getStartTime());
            prepstmt.setString(5, flock.getEndTime());
            prepstmt.setLong(6, flock.getFlockId());
            prepstmt.executeUpdate();
        } catch (SQLException e) {
            dao.getConnection();

            throw new SQLException("Cannot Update Flock In DataBase", e);
        } finally {
            prepstmt.close();
            dao.closeConnection(con);
        }
    }

    @Override
    public void remove(Long flockId) throws SQLException {
        String sqlQuery = "delete from flocks where FlockID=?";
        PreparedStatement prepstmt = null;
        Connection con = null;

        try {
            con = dao.getConnection();
            prepstmt = con.prepareStatement(sqlQuery);
            prepstmt.setLong(1, flockId);
            prepstmt.executeUpdate();
        } catch (SQLException e) {
            dao.printSQLException(e);

            throw new SQLException("Cannot Remove Flock From DataBase");
        } finally {
            prepstmt.close();
            dao.closeConnection(con);
        }
    }

    @Override
    public Flock getById(Long flockId) throws SQLException {
        String sqlQuery = "select * from flocks where FlockID=?";
        PreparedStatement prepstmt = null;
        Connection con = null;

        try {
            con = dao.getConnection();
            prepstmt = con.prepareStatement(sqlQuery);
            prepstmt.setLong(1, flockId);
            ResultSet rs = prepstmt.executeQuery();
            if (rs.next()) {
                return makeFlock(rs);
            } else {
                return null;
            }
        } catch (SQLException e) {
            dao.printSQLException(e);

            throw new SQLException("Cannot Retrieve Users From DataBase", e);
        } finally {
            prepstmt.close();
            dao.closeConnection(con);
        }
    }

    @Override
    public Flock getOpenFlockByController(Long controllerId) throws SQLException {
        String sqlQuery = "select * from flocks where ControllerID=? and Status='Open'";
        PreparedStatement prepstmt = null;
        Connection con = null;

        try {
            con = dao.getConnection();
            prepstmt = con.prepareStatement(sqlQuery);
            prepstmt.setLong(1, controllerId);

            ResultSet rs = prepstmt.executeQuery();

            if (rs.next()) {
                return makeFlock(rs);
            } else {
                return null;
            }
        } catch (SQLException e) {
            dao.printSQLException(e);
            throw new SQLException("Cannot Retrieve Users From DataBase");
        } finally {
            prepstmt.close();
            dao.closeConnection(con);
        }
    }

    @Override
    public Integer getUpdatedGrowDayHistory(Long flockId) throws SQLException {
        String sqlQuery = "select growday from flockhistory "
                + "where flockid=? and growday = "
                + "(select max(GrowDay) from flockhistory where FlockID=?)";
        PreparedStatement prepstmt = null;
        Connection con = null;

        try {
            con = dao.getConnection();
            prepstmt = con.prepareStatement(sqlQuery);
            prepstmt.setLong(1, flockId);
            prepstmt.setLong(2, flockId);

            ResultSet rs = prepstmt.executeQuery();

            if (rs.next()) {
                return Integer.parseInt(rs.getString("growday"));
            } else {
                return null;
            }
        } catch (SQLException e) {
            dao.printSQLException(e);

            throw new SQLException("Cannot Execute Query");
        } finally {
            prepstmt.close();
            dao.closeConnection(con);
        }
    }

    @Override
    public Integer getUpdatedGrowDayHistory24(Long flockId) throws SQLException {
        String sqlQuery = "select max(growday) as growday from flockhistory24 "
                + " where flockid=? and DNum ="
                + " (select max(DNum) from flockhistory24 where FlockID=?)";
        PreparedStatement prepstmt = null;
        Connection con = null;

        try {
            con = dao.getConnection();
            prepstmt = con.prepareStatement(sqlQuery);
            prepstmt.setLong(1, flockId);
            prepstmt.setLong(2, flockId);
            ResultSet rs = prepstmt.executeQuery();

            if (rs.next()) {
                try {
                    return Integer.parseInt(rs.getString("growday"));
                } catch (Exception e) {
                    return null;
                }
            } else {
                return null;
            }
        } catch (SQLException e) {
            dao.printSQLException(e);

            throw new SQLException("Cannot Retrieve Users From DataBase");
        } finally {
            prepstmt.close();
            dao.closeConnection(con);
        }
    }

    @Override
    public void updateHistoryByGrowDay(Long flockId, Integer growDay, String values) throws SQLException {
        String sqlQuery = "insert into flockhistory (FlockID,GrowDay,HistoryData) "
                + "VALUES (?,?,?) on duplicate key update HistoryData=VALUES(HistoryData)";
        PreparedStatement prepstmt = null;
        Connection con = null;

        try {
            con = dao.getConnection();
            prepstmt = con.prepareStatement(sqlQuery);
            prepstmt.setLong(1, flockId);
            prepstmt.setInt(2, growDay);
            prepstmt.setString(3, values);
            prepstmt.executeUpdate();
        } catch (SQLException e) {
            dao.printSQLException(e);

            throw new SQLException("Update flock history error ", e);
        } finally {
            prepstmt.close();
            dao.closeConnection(con);
        }
    }

    @Override
    public void updateHistory24ByGrowDay(Long flockId, Integer growDay, String dnum, String values) throws SQLException {
        String sqlQuery = "insert into flockhistory24 (FlockID, GrowDay, DNum, HistoryData) "
                + "values (?,?,?,?) on duplicate key update HistoryData=values(HistoryData)";
        PreparedStatement prepstmt = null;
        Connection con = null;

        try {
            con = dao.getConnection();
            prepstmt = con.prepareStatement(sqlQuery);
            prepstmt.setLong(1, flockId);
            prepstmt.setInt(2, growDay);
            prepstmt.setString(3, dnum);
            prepstmt.setString(4, values);
            prepstmt.executeUpdate();
        } catch (SQLException e) {
            dao.printSQLException(e);

            throw new SQLException("Update flock history 24 hours error.", e);
        } finally {
            prepstmt.close();
            dao.closeConnection(con);
        }
    }

    public Integer getUpdatedGrowDay(Long flockId) throws SQLException {
        String sqlQuery = "select growday from flockhistory where growday = "
                + "(select max(GrowDay) from flockhistory where FlockID=?)";
        PreparedStatement prepstmt = null;
        Connection con = null;

        try {
            con = dao.getConnection();
            prepstmt = con.prepareStatement(sqlQuery);
            prepstmt.setLong(1, flockId);

            ResultSet rs = prepstmt.executeQuery();

            if (rs.next()) {
                return Integer.parseInt(rs.getString("growday"));
            } else {
                return null;
            }
        } catch (SQLException e) {
            dao.printSQLException(e);

            throw new SQLException("Cannot Retrieve Users From DataBase");
        } finally {
            prepstmt.close();
            dao.closeConnection(con);
        }
    }

    @Override
    public Map<String, String> getHistoryN() {
        Map<String, String> historyNums = new HashMap<String, String>();
        historyNums.put("D18", "Inside Temp");
        historyNums.put("D19", "Outside Temp");
        historyNums.put("D20", "Humidity");
        historyNums.put("D21", "Water");
        historyNums.put("D72", "Feed");
        return historyNums;
    }

    @Override
    public Collection<Flock> getAll() throws SQLException {
        String sqlQuery = "select * from flocks ";
        Statement stmt = null;
        Connection con = null;

        try {
            con = dao.getConnection();
            stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sqlQuery);
            return makeFlockList(rs);
        } catch (SQLException e) {
            dao.printSQLException(e);
            throw new SQLException("Cannot Retrieve Flocks From DataBase");
        } finally {
            stmt.close();
            dao.closeConnection(con);
        }
    }

    @Override
    public Map<Integer, String> getAllHistoryByFlock(Long flockId) throws SQLException {
        String sqlQuery = "select * from flockhistory where flockid=?";
        PreparedStatement prepstmt = null;
        Connection con = null;

        try {
            con = dao.getConnection();
            prepstmt = con.prepareStatement(sqlQuery);
            prepstmt.setLong(1, flockId);
            ResultSet rs = prepstmt.executeQuery();
            Map<Integer, String> historyByGrowDay = new TreeMap<Integer, String>();
            while (rs.next()) {
                Integer growDay = rs.getInt("GrowDay");
                String history = rs.getString("HistoryData");
                historyByGrowDay.put(growDay, history);
            }
            return historyByGrowDay;
        } catch (SQLException e) {
            dao.printSQLException(e);
            throw new SQLException("Cannot Retrieve FlockHistory From DataBase", e);
        } finally {
            prepstmt.close();
            dao.closeConnection(con);
        }
    }

    @Override
    public Map<Integer, String> getAllHistoryByFlock(Long flockId, int fromDay, int toDay) throws SQLException {
        String sqlQuery = "select * from flockhistory where flockid=? and growday between ? and ?";
        PreparedStatement prepstmt = null;
        Connection con = null;

        if (fromDay == -1 && toDay == -1) {
            fromDay = 0;
            toDay = 1000;
        }

        try {
            con = dao.getConnection();
            prepstmt = con.prepareStatement(sqlQuery);
            prepstmt.setLong(1, flockId);
            prepstmt.setLong(2, fromDay);
            prepstmt.setLong(3, toDay);

            ResultSet rs = prepstmt.executeQuery();

            Map<Integer, String> historyByGrowDay = new TreeMap<Integer, String>();
            while (rs.next()) {
                Integer growDay = rs.getInt("GrowDay");
                String history = rs.getString("HistoryData");
                historyByGrowDay.put(growDay, history);
            }
            return historyByGrowDay;
        } catch (SQLException e) {
            dao.printSQLException(e);
            throw new SQLException("Cannot Retrieve Users From DataBase", e);
        } finally {
            prepstmt.close();
            dao.closeConnection(con);
        }
    }

    @Override
    public Map<Integer, String> getAllHistory24ByFlockAndDnum(Long flockId, String dnum) throws SQLException {
        String sqlQuery = "select * from flockhistory24 where  flockid=? and dnum=?";
        PreparedStatement prepstmt = null;
        Connection con = null;

        try {
            con = dao.getConnection();
            prepstmt = con.prepareStatement(sqlQuery);
            prepstmt.setLong(1, flockId);
            prepstmt.setString(2, dnum);
            ResultSet rs = prepstmt.executeQuery();
            Map<Integer, String> historyByGrowDay = new TreeMap<Integer, String>();
            while (rs.next()) {
                Integer growDay = rs.getInt("GrowDay");
                String history = rs.getString("HistoryData");
                historyByGrowDay.put(growDay, history);
            }
            return historyByGrowDay;
        } catch (SQLException e) {
            dao.printSQLException(e);
            throw new SQLException("Cannot Retrieve FlockHistory From DataBase", e);
        } finally {
            prepstmt.close();
            dao.closeConnection(con);
        }
    }

    @Override
    public void removeHistoryByGrowDay(Long flockId, Integer growDay) throws SQLException {
        String sqlQuery = "delete from flockhistory where flockid=? and growday=?";
        PreparedStatement prepstmt = null;
        Connection con = null;

        try {
            con = dao.getConnection();
            prepstmt = con.prepareStatement(sqlQuery);
            prepstmt.setLong(1, flockId);
            prepstmt.setLong(2, growDay);
            prepstmt.executeUpdate();
        } catch (SQLException e) {
            dao.printSQLException(e);
            throw new SQLException("Cannot Remove Flock History On Grow Day " + growDay + " From DataBase");
        } finally {
            prepstmt.close();
            dao.closeConnection(con);
        }
    }

    @Override
    public void removeHistoryByFlock(Long flockId) throws SQLException {
        String sqlQuery = "delete from flockhistory where flockid=?";
        PreparedStatement prepstmt = null;
        Connection con = null;

        try {
            con = dao.getConnection();
            prepstmt = con.prepareStatement(sqlQuery);
            prepstmt.setLong(1, flockId);
            prepstmt.executeUpdate();
        } catch (SQLException e) {
            dao.printSQLException(e);
            throw new SQLException("Cannot Remove Flock History From DataBase");
        } finally {
            prepstmt.close();
            dao.closeConnection(con);
        }
    }

    @Override
    public void removeHistory24ByFlock(Long flockId) throws SQLException {
        String sqlQuery = "delete from flockhistory24 where flockid=?";
        PreparedStatement prepstmt = null;
        Connection con = null;

        try {
            con = dao.getConnection();
            prepstmt = con.prepareStatement(sqlQuery);
            prepstmt.setLong(1, flockId);
            prepstmt.executeUpdate();
        } catch (SQLException e) {
            dao.printSQLException(e);
            throw new SQLException("Cannot Remove Flock History 24 hour From DataBase");
        } finally {
            prepstmt.close();
            dao.closeConnection(con);
        }
    }

    @Override
    public void updateFlockDetail(Flock flock) throws SQLException {
        String sqlQuery = "update flocks set "
                + " QuantityMale=?, QuantityFemale=?, QuantityElect=?, QuantitySpread=?, QuantityWater=?, "
                + " ElectBegin=?, ElectEnd=?, FuelBegin=?, FuelEnd=?, GasBegin=?, GasEnd=?, WaterBegin=?, WaterEnd=?, "
                + " CostChickMale=?, CostChickFemale=?, CostElect=?, CostFuel=?, CostFuelEnd=?, CostGas=?, CostGasEnd=?,"
                + " CostWater=?, CostSpread=?, CostMaleKg=?,"
                + " FuelAdd=?, GasAdd=?, FeedAdd=?, SpreadAdd=?, "
                + " Expenses=?, Revenues=?, "
                + " TotalElect=?, TotalFuel=?, TotalGas=?, TotalWater=?, TotalSpread=?, TotalMedic=?, TotalChicks=?, "
                + " TotalLabor=?, TotalFeed=?, "
                + " Currency=? "
                + " where FlockID=? ";
        PreparedStatement prepstmt = null;
        Connection con = null;

        try {
            con = dao.getConnection();
            prepstmt = con.prepareStatement(sqlQuery);
            prepstmt.setInt(1, flock.getQuantityMale());
            prepstmt.setInt(2, flock.getQuantityFemale());
            prepstmt.setInt(3, flock.getQuantityElect());
            prepstmt.setInt(4, flock.getQuantitySpread());
            prepstmt.setInt(5, flock.getQuantityWater());
            prepstmt.setInt(6, flock.getElectBegin());
            prepstmt.setInt(7, flock.getElectEnd());
            prepstmt.setInt(8, flock.getFuelBegin());
            prepstmt.setInt(9, flock.getFuelEnd());
            prepstmt.setInt(10, flock.getGasBegin());
            prepstmt.setInt(11, flock.getGasEnd());
            prepstmt.setInt(12, flock.getWaterBegin());
            prepstmt.setInt(13, flock.getWaterEnd());
            prepstmt.setFloat(14, flock.getCostChickMale());
            prepstmt.setFloat(15, flock.getCostChickFemale());
            prepstmt.setFloat(16, flock.getCostElect());
            prepstmt.setFloat(17, flock.getCostFuel());
            prepstmt.setFloat(18, flock.getCostFuelEnd());
            prepstmt.setFloat(19, flock.getCostGas());
            prepstmt.setFloat(20, flock.getCostGasEnd());
            prepstmt.setFloat(21, flock.getCostWater());
            prepstmt.setFloat(22, flock.getCostSpread());
            prepstmt.setFloat(23, flock.getCostMaleKg());
            prepstmt.setInt(24, flock.getFuelAdd());
            prepstmt.setInt(25, flock.getGasAdd());
            prepstmt.setInt(26, flock.getFeedAdd());
            prepstmt.setFloat(27, flock.getSpreadAdd());
            prepstmt.setFloat(28, flock.getExpenses());
            prepstmt.setFloat(29, flock.getRevenues());
            prepstmt.setFloat(30, flock.getTotalElect());
            prepstmt.setFloat(31, flock.getTotalFuel());
            prepstmt.setFloat(32, flock.getTotalGas());
            prepstmt.setFloat(33, flock.getTotalWater());
            prepstmt.setFloat(34, flock.getTotalSpread());
            prepstmt.setFloat(35, flock.getTotalMedic());
            prepstmt.setFloat(36, flock.getTotalChicks());
            prepstmt.setFloat(37, flock.getTotalLabor());
            prepstmt.setFloat(38, flock.getTotalFeed());
            prepstmt.setString(39, flock.getCurrency());
            prepstmt.setLong(40, flock.getFlockId());
            prepstmt.executeUpdate();
        } catch (SQLException e) {
            dao.printSQLException(e);
            throw new SQLException("Cannot Update FlockDto In DataBase");
        } finally {
            prepstmt.close();
            dao.closeConnection(con);
        }
    }
}


//~ Formatted by Jindent --- http://www.jindent.com
