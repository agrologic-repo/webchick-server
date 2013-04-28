/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.agrologic.app.dao.impl;

import com.agrologic.app.dao.FlockDao;
import com.agrologic.app.model.FlockDto;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class FlockDaoImpl extends ConnectorDao implements FlockDao {

    /**
     * Help to create flock from result set .
     * @param resultSet a result set
     * @return flock an objects that encapsulates a flock attributes
     * @throws java.sql.SQLException
     */
    private FlockDto makeFlock(ResultSet resultSet) throws SQLException {
        FlockDto flock = new FlockDto();
        flock.setFlockId(resultSet.getLong("FlockID"));
        flock.setControllerId(resultSet.getLong("ControllerID"));
        flock.setFlockName(resultSet.getString("Name"));
        flock.setStatus(resultSet.getString("Status"));
        flock.setStartDate(resultSet.getString("StartDate"));
        flock.setEndDate(resultSet.getString("EndDate"));
        flock.setProgramId(resultSet.getLong("ProgramID"));
        try {
            flock.setQuantityMale(resultSet.getInt("QuantityMale"));
        } catch (SQLException ex) {
            printSQLException(ex);
        }
        try {
            flock.setCostChickMale(resultSet.getFloat("CostChickMale"));
        } catch (SQLException ex) {
            printSQLException(ex);
        }
        try {
            flock.setQuantityFemale(resultSet.getInt("QuantityFemale"));
        } catch (SQLException ex) {
            printSQLException(ex);
        }
        try {
            flock.setCostChickFemale(resultSet.getFloat("CostChickFemale"));
        } catch (SQLException ex) {
            printSQLException(ex);
        }
        try {
            flock.setQuantityFemale(resultSet.getInt("QuantityFemale"));
        } catch (SQLException ex) {
            printSQLException(ex);
        }
        try {
            flock.setCostChickFemale(resultSet.getFloat("CostChickFemale"));
        } catch (SQLException ex) {
            printSQLException(ex);
        }
        try {
            flock.setTotalChicks(resultSet.getFloat("TotalChicks"));
        } catch (SQLException ex) {
            printSQLException(ex);
        }
        try {
            flock.setGasBegin(resultSet.getInt("GasBegin"));
        } catch (SQLException ex) {
            printSQLException(ex);
        }
        try {
            flock.setGasEnd(resultSet.getInt("GasEnd"));
        } catch (SQLException ex) {
            printSQLException(ex);
        }
        try {

            flock.setCostGas(resultSet.getFloat("CostGas"));
        } catch (SQLException ex) {
            printSQLException(ex);
        }
        try {
            flock.setCostGasEnd(resultSet.getFloat("CostGasEnd"));
        } catch (SQLException ex) {
            printSQLException(ex);
        }

        try {
            flock.setGasAdd(resultSet.getInt("GasAdd"));
        } catch (SQLException ex) {
            printSQLException(ex);
        }

        try {
            flock.setTotalGas(resultSet.getFloat("TotalGas"));
        } catch (SQLException ex) {
            printSQLException(ex);
        }

        try {
            flock.setFuelBegin(resultSet.getInt("FuelBegin"));
        } catch (SQLException ex) {
            printSQLException(ex);
        }
        try {
            flock.setFuelEnd(resultSet.getInt("FuelEnd"));
        } catch (SQLException ex) {
            printSQLException(ex);
        }
        try {
            flock.setCostFuel(resultSet.getFloat("CostFuel"));
        } catch (SQLException ex) {
            printSQLException(ex);
        }
        try {
            flock.setCostFuelEnd(resultSet.getFloat("CostFuelEnd"));
        } catch (SQLException ex) {
            printSQLException(ex);
        }
        try {
            flock.setFuelAdd(resultSet.getInt("FuelAdd"));
        } catch (SQLException ex) {
            printSQLException(ex);
        }
        try {
            flock.setTotalFuel(resultSet.getFloat("TotalFuel"));
        } catch (SQLException ex) {
            printSQLException(ex);
        }

        try {
            flock.setWaterBegin(resultSet.getInt("WaterBegin"));
        } catch (SQLException ex) {
            printSQLException(ex);
        }
        try {
            flock.setWaterEnd(resultSet.getInt("WaterEnd"));
        } catch (SQLException ex) {
            printSQLException(ex);
        }

        try {
            flock.setFeedAdd(resultSet.getInt("FeedAdd"));
        } catch (SQLException ex) {
            printSQLException(ex);
        }
        try {
            flock.setTotalFeed(resultSet.getFloat("TotalFeed"));
        } catch (SQLException ex) {
            printSQLException(ex);
        }
        try {
            flock.setCostWater(resultSet.getFloat("CostWater"));
        } catch (SQLException ex) {
            printSQLException(ex);
        }
        try {
            flock.setQuantityWater(resultSet.getInt("QuantityWater"));
        } catch (SQLException ex) {
            printSQLException(ex);
        }
        try {
            flock.setTotalWater(resultSet.getFloat("TotalWater"));
        } catch (SQLException ex) {
            printSQLException(ex);
        }
        try {
            flock.setElectBegin(resultSet.getInt("ElectBegin"));
        } catch (SQLException ex) {
            printSQLException(ex);
        }
        try {
            flock.setElectEnd(resultSet.getInt("ElectEnd"));
        } catch (SQLException ex) {
            printSQLException(ex);
        }
        try {
            flock.setCostElect(resultSet.getFloat("CostElect"));
        } catch (SQLException ex) {
            printSQLException(ex);
        }
        try {
            flock.setQuantityElect(resultSet.getInt("QuantityElect"));
        } catch (SQLException ex) {
            printSQLException(ex);
        }
        try {
            flock.setTotalElect(resultSet.getFloat("TotalElect"));
        } catch (SQLException ex) {
            printSQLException(ex);
        }
        try {
            flock.setSpreadAdd(resultSet.getInt("SpreadAdd"));
        } catch (SQLException ex) {
            printSQLException(ex);
        }
        try {
            flock.setTotalSpread(resultSet.getFloat("TotalSpread"));
        } catch (SQLException ex) {
            printSQLException(ex);
        }
        try {
            flock.setTotalLabor(resultSet.getFloat("TotalLabor"));
        } catch (SQLException ex) {
            printSQLException(ex);
        }
        try {
            flock.setTotalMedic(resultSet.getFloat("TotalMedic"));
        } catch (SQLException ex) {
            printSQLException(ex);
        }
        try {
            flock.setExpenses(resultSet.getFloat("Expenses"));
        } catch (SQLException ex) {
            printSQLException(ex);
        }
        try {
            flock.setRevenues(resultSet.getFloat("Revenues"));
        } catch (SQLException ex) {
            printSQLException(ex);
        }
        try {
            flock.setCurrency(resultSet.getString("Currency"));
        } catch (SQLException ex) {
            printSQLException(ex);
        }
        return flock;
    }

    /**
     * Help to create list of cellinks from result set
     * @param resultSet a result set
     * @return users a list of FlockDto objects
     * @throws java.sql.SQLException
     */
    private List<FlockDto> makeFlockList(ResultSet resultSet) throws SQLException {
        List<FlockDto> flocks = new ArrayList<FlockDto>();
        while (resultSet.next()) {
            flocks.add(makeFlock(resultSet));
        }
        return flocks;
    }

    @Override
    public void insert(FlockDto flock) throws SQLException {
        String sqlQuery = "insert into flocks (FlockID,ControllerID,Name,Status,StartDate,EndDate,ProgramID) values (?,?,?,?,?,?,?)";
        PreparedStatement prepstmt = null;
        Connection con = null;

        try {
            con = getConnection();
            prepstmt = con.prepareStatement(sqlQuery);
            prepstmt.setObject(1, null);
            prepstmt.setLong(2, flock.getControllerId());
            prepstmt.setString(3, flock.getFlockName());
            prepstmt.setString(4, flock.getStatus());
            prepstmt.setString(5, flock.getStartTime());
            prepstmt.setString(6, flock.getEndTime());
            prepstmt.setLong(7, flock.getProgramId());
            prepstmt.executeUpdate();
        } catch (SQLException e) {
            printSQLException(e);
            throw new SQLException("Can not Add New Flock To The DataBase");
        } finally {
            prepstmt.close();
            closeConnection(con);
        }
    }

    @Override
    public void update(FlockDto flock) throws SQLException {
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
            con = getConnection();
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
            printSQLException(e);
            throw new SQLException("Cannot Update FlockDto In DataBase");
        } finally {
            prepstmt.close();
            closeConnection(con);
        }
    }

    @Override
    public void remove(Long flockId) throws SQLException {
        String sqlQuery = "delete from flocks where FlockID=?";
        PreparedStatement prepstmt = null;
        Connection con = null;

        try {
            con = getConnection();
            prepstmt = con.prepareStatement(sqlQuery);
            prepstmt.setLong(1, flockId);
            prepstmt.executeUpdate();
        } catch (SQLException e) {
            printSQLException(e);
            throw new SQLException("Cannot Remove Flock From DataBase", e);
        } finally {
            prepstmt.close();
            closeConnection(con);
        }
    }

    @Override
    public void close(Long flockId, String endDate) throws SQLException {
        String sqlQuery = "update flocks set Status='Close' , EndDate=? where FlockID=? ";
        PreparedStatement prepstmt = null;
        Connection con = null;

        try {
            con = getConnection();
            prepstmt = con.prepareStatement(sqlQuery);
            prepstmt.setLong(2, flockId);
            prepstmt.setString(1, endDate);
            prepstmt.executeUpdate();
        } catch (SQLException e) {
            printSQLException(e);
            throw new SQLException("Cannot Close Flock In DataBase", e);
        } finally {
            prepstmt.close();
            closeConnection(con);
        }
    }

    @Override
    public FlockDto getById(Long flockId) throws SQLException {
        String sqlQuery = "select * from flocks where FlockID=?";
        PreparedStatement prepstmt = null;
        Connection con = null;

        try {
            con = getConnection();
            prepstmt = con.prepareStatement(sqlQuery);
            prepstmt.setLong(1, flockId);
            ResultSet rs = prepstmt.executeQuery();

            if (rs.next()) {
                return makeFlock(rs);
            } else {
                return null;
            }
        } catch (SQLException e) {
            printSQLException(e);
            throw new SQLException("Cannot Retrieve Users From DataBase", e);
        } finally {
            prepstmt.close();
            closeConnection(con);
        }
    }

    @Override
    public List<FlockDto> getAllFlocksByController(Long controllerId) throws SQLException {
        String sqlQuery = "select * from flocks where ControllerID=?";
        PreparedStatement prepstmt = null;
        Connection con = null;

        try {
            con = getConnection();
            prepstmt = con.prepareStatement(sqlQuery);
            prepstmt.setLong(1, controllerId);
            ResultSet rs = prepstmt.executeQuery();
            return makeFlockList(rs);
        } catch (SQLException e) {
            printSQLException(e);
            throw new SQLException("Cannot Retrieve Users From DataBase");
        } finally {
            prepstmt.close();
            closeConnection(con);
        }
    }

    @Override
    public Map<Integer, String> getAllHistoryByFlock(Long flockId) throws SQLException {
        String sqlQuery = "select * from flockhistory where flockid=?";
        PreparedStatement prepstmt = null;
        Connection con = null;

        try {
            con = getConnection();
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
            printSQLException(e);
            throw new SQLException("Cannot Retrieve Users From DataBase", e);
        } finally {
            prepstmt.close();
            closeConnection(con);
        }
    }

    @Override
    public String getHistory24(Long flockId, Integer growDay, String dn) throws SQLException {
        String sqlQuery = "select * from flockhistory24 where flockid=? and growday=? and dnum=?";
        PreparedStatement prepstmt = null;
        Connection con = null;

        try {
            con = getConnection();
            prepstmt = con.prepareStatement(sqlQuery);
            prepstmt.setLong(1, flockId);
            prepstmt.setInt(2, growDay);
            prepstmt.setString(3, dn);
            ResultSet rs = prepstmt.executeQuery();
            if (rs.next()) {
                return rs.getString("HistoryData");
            } else {
                return "";
            }
        } catch (SQLException e) {
            printSQLException(e);
            throw new SQLException("Cannot Retrieve values from History24 table", e);
        } finally {
            prepstmt.close();
            closeConnection(con);
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
            con = getConnection();
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
            printSQLException(e);
            throw new SQLException("Cannot Retrieve Users From DataBase", e);
        } finally {
            prepstmt.close();
            closeConnection(con);
        }
    }

    @Override
    public Integer getResetTime(Long flockId) throws SQLException {
        return getResetTime(flockId, 1);
    }

    @Override
    public Integer getResetTime(Long flockId, Integer growDay) throws SQLException {
        String sqlQuery = "select historydata from flockhistory24 where FlockID=? "
                + "and GrowDay=? and DNum='D70'";
        PreparedStatement prepstmt = null;
        Connection con = null;

        try {
            con = getConnection();
            prepstmt = con.prepareStatement(sqlQuery);
            prepstmt.setLong(1, flockId);
            prepstmt.setLong(2, growDay);
            ResultSet rs = prepstmt.executeQuery();

            if (rs.next()) {
                Integer resetTime = rs.getInt("HistoryData");
                return resetTime;
            } else {
                return 0;
            }
        } catch (SQLException e) {
            printSQLException(e);
            throw new SQLException("Cannot Retrieve Reset Time From flockhistory24 table");
        } finally {
            prepstmt.close();
            closeConnection(con);
        }
    }

    @Override
    public List<Integer> getHistory24GrowDays(Long flockId) throws SQLException {
        String sqlQuery = "select distinct growday from flockhistory24 where flockid=?";
        PreparedStatement prepstmt = null;
        Connection con = null;

        try {
            con = getConnection();
            prepstmt = con.prepareStatement(sqlQuery);
            prepstmt.setLong(1, flockId);
            ResultSet rs = prepstmt.executeQuery();

            List<Integer> growDays = new ArrayList<Integer>();
            while (rs.next()) {
                Integer growDay = rs.getInt("GrowDay");
                growDays.add(growDay);
            }
            return growDays;
        } catch (SQLException e) {
            printSQLException(e);
            throw new SQLException("Cannot Retrieve Users From DataBase");
        } finally {
            prepstmt.close();
            closeConnection(con);
        }
    }
//
//    /**
//     * Help to create flock from result set .
//     * @param resultSet a result set
//     * @return flock an objects that encapsulates a flock attributes
//     * @throws java.sql.SQLException
//     */
//    private FlockDto makeFlock(ResultSet resultSet) throws SQLException {
//        FlockDto flock =  new FlockDto();
//        flock.setFlockId(resultSet.getLong("FlockID"));
//        flock.setFlockName(resultSet.getString("Name"));
//        flock.setStatus(resultSet.getString("Status"));
//        flock.setStartDate(resultSet.getString("StartDate"));
//        flock.setEndDate(resultSet.getString("EndDate"));
//        flock.setProgramId(resultSet.getLong("ProgramID"));
//        try {
//            flock.setQuantityMale(resultSet.getInt("QuantityMale"));
//        } catch (SQLException ex) {
//            printSQLException(ex);
//        }
//        try {
//            flock.setPriceChickMale(resultSet.getFloat("PriceChickMale"));
//        } catch (SQLException ex) {
//            printSQLException(ex);
//        }
//        try {
//            flock.setQuantityFemale(resultSet.getInt("QuantityMale"));
//        } catch (SQLException ex) {
//            printSQLException(ex);
//        }
//        try {
//            flock.setPriceChickFemale(resultSet.getFloat("PriceChickMale"));
//        } catch (SQLException ex) {
//            printSQLException(ex);
//        }
//        try {
//            flock.setQuantityFemale(resultSet.getInt("QuantityMale"));
//        } catch (SQLException ex) {
//            printSQLException(ex);
//        }
//        try {
//            flock.setPriceChickFemale(resultSet.getFloat("PriceChickMale"));
//        } catch (SQLException ex) {
//            printSQLException(ex);
//        }
//        try {
//            flock.setTotalChicks(resultSet.getInt("TotalChicks"));
//        } catch (SQLException ex) {
//            printSQLException(ex);
//        }
//        try {
//            flock.setWaterBegin(resultSet.getInt("WaterBegin"));
//        } catch (SQLException ex) {
//            printSQLException(ex);
//        }
//        try {
//            flock.setWaterEnd(resultSet.getInt("WaterEnd"));
//        } catch (SQLException ex) {
//            printSQLException(ex);
//        }
//        try {
//            flock.setPriceWater(resultSet.getInt("PriceWater"));
//        } catch (SQLException ex) {
//            printSQLException(ex);
//        }
//        try {
//            flock.setTotalWater(resultSet.getInt("TotalWater"));
//        } catch (SQLException ex) {
//            printSQLException(ex);
//        }
//        try {
//            flock.setElectBegin(resultSet.getInt("ElectBegin"));
//        } catch (SQLException ex) {
//            printSQLException(ex);
//        }
//        try {
//            flock.setElectEnd(resultSet.getInt("ElectEnd"));
//        } catch (SQLException ex) {
//            printSQLException(ex);
//        }
//        try {
//            flock.setPriceElect(resultSet.getInt("PriceElect"));
//        } catch (SQLException ex) {
//            printSQLException(ex);
//        }
//        try {
//            flock.setTotalElect(resultSet.getInt("TotalElect"));
//        } catch (SQLException ex) {
//            printSQLException(ex);
//        }
//
//        return flock;
//    }
//
//    /**
//     * Help to create list of cellinks from result set
//     * @param resultSet a result set
//     * @return users a list of FlockDto objects
//     * @throws java.sql.SQLException
//     */
//    private List<FlockDto> makeFlockList(ResultSet resultSet) throws SQLException {
//        List<FlockDto> flocks = new ArrayList<FlockDto>();
//        while (resultSet.next()) {
//            flocks.add(makeFlock(resultSet));
//        }
//        return flocks;
//    }
//
//    @Override
//    public void insert(FlockDto flock) throws SQLException {
//        String sqlQuery = "insert into flocks (FlockID,ControllerID,Name,Status,StartDate,EndDate,ProgramID) values (?,?,?,?,?,?,?)";
//        PreparedStatement prepstmt = null;
//        Connection con = null;
//
//        try {
//            con = getConnection();
//            prepstmt = con.prepareStatement(sqlQuery);
//            prepstmt.setObject(1,null);
//            prepstmt.setLong(2,flock.getControllerId());
//            prepstmt.setString(3,flock.getFlockName());
//            prepstmt.setString(4,flock.getStatus());
//            prepstmt.setString(5,flock.getStartTime());
//            prepstmt.setString(6,flock.getEndTime());
//            prepstmt.setLong(7,flock.getProgramId());
//            prepstmt.executeUpdate();
//        } catch (SQLException e) {
//            printSQLException(e);
//            throw new SQLException("Can not Add New Flock To The DataBase");
//        } finally {
//            prepstmt.close();
//            closeConnection(con);
//        }
//    }
//
//    @Override
//    public void update(FlockDto flock) throws SQLException {
//        String sqlQuery = "update flocks set QuantityMale=?, QuantityFemale=?, TotalChicks=?, PriceChickMale=?, PriceChickFemale=?, " +
//                                            "ElectBegin=?, ElectEnd=?, PriceElect=?, TotalElect=?, " +
//                                            "WaterBegin=?, WaterEnd=?, PriceWater=?, TotalWater=?, " +
//                                            " where FlockID=?";
//        PreparedStatement prepstmt = null;
//        Connection con = null;
//
//        try {
//            con  = getConnection();
//            prepstmt = con.prepareStatement(sqlQuery);
//            prepstmt.setInt(1,flock.getQuantityMale());
//            prepstmt.setInt(2,flock.getQuantityFemale());
//            prepstmt.setInt(3,flock.getTotalChicks());
//            prepstmt.setFloat(4,flock.getPriceChickMale());
//            prepstmt.setFloat(5,flock.getPriceChickFemale());
//            prepstmt.setInt(6,flock.getElectBegin());
//            prepstmt.setInt(7,flock.getElectEnd());
//            prepstmt.setFloat(8,flock.getPriceElect());
//            prepstmt.setInt(9,flock.getTotalElect());
//            prepstmt.setInt(10,flock.getWaterBegin());
//            prepstmt.setInt(11,flock.getWaterEnd());
//            prepstmt.setFloat(12,flock.getPriceWater());
//            prepstmt.setInt(13,flock.getTotalWater());
//            prepstmt.setLong(14,flock.getFlockId());
//            prepstmt.executeUpdate();
//        } catch (SQLException e) {
//            printSQLException(e);
//            throw new SQLException("Cannot Update Flock table In DataBase");
//        } finally {
//            prepstmt.close();
//            closeConnection(con);
//        }
//    }
//
//    @Override
//    public void remove(Long flockId) throws SQLException {
//        String sqlQuery = "delete from flocks where FlockID=?";
//        PreparedStatement prepstmt = null;
//        Connection con = null;
//
//        try {
//            con  = getConnection();
//            prepstmt = con.prepareStatement(sqlQuery);
//            prepstmt.setLong(1, flockId);
//            prepstmt.executeUpdate();
//        } catch (SQLException e) {
//            printSQLException(e);
//            throw new SQLException("Cannot Remove Flock From DataBase", e);
//        } finally {
//            prepstmt.close();
//            closeConnection(con);
//        }
//    }
//
//    @Override
//    public FlockDto getById(Long flockId) throws SQLException {
//        String sqlQuery = "select * from flocks where FlockID=?";
//        PreparedStatement prepstmt = null;
//        Connection con = null;
//
//        try {
//            con  = getConnection();
//            prepstmt = con.prepareStatement(sqlQuery);
//            prepstmt.setLong(1, flockId);
//            ResultSet rs = prepstmt.executeQuery();
//
//            if (rs.next()) {
//                return makeFlock(rs);
//            } else {
//                return null;
//            }
//        } catch (SQLException e) {
//            printSQLException(e);
//            throw new SQLException("Cannot Retrieve Users From DataBase", e);
//        } finally {
//            prepstmt.close();
//            closeConnection(con);
//        }
//    }
//
//    @Override
//    public List<FlockDto> getAllFlocksByController(Long controllerId) throws SQLException {
//        String sqlQuery = "select * from flocks where ControllerID=?";
//        PreparedStatement prepstmt = null;
//        Connection con = null;
//
//        try {
//            con = getConnection();
//            prepstmt = con.prepareStatement(sqlQuery);
//            prepstmt.setLong(1, controllerId);
//            ResultSet rs = prepstmt.executeQuery();
//            return makeFlockList(rs);
//        } catch (SQLException e) {
//            printSQLException(e);
//            throw new SQLException("Cannot Retrieve Users From DataBase");
//        } finally {
//            prepstmt.close();
//            closeConnection(con);
//        }
//    }
//
//    @Override
//    public Map<Integer, String> getAllHistoryByFlock(Long flockId) throws SQLException {
//        String sqlQuery = "select * from flockhistory where flockid=?" ;
//        PreparedStatement prepstmt = null;
//        Connection con = null;
//
//        try {
//            con  = getConnection();
//            prepstmt = con.prepareStatement(sqlQuery);
//            prepstmt.setLong(1, flockId);
//            ResultSet rs = prepstmt.executeQuery();
//            Map<Integer,String> historyByGrowDay = new TreeMap<Integer, String>();
//            while (rs.next()) {
//                Integer growDay = rs.getInt("GrowDay");
//                String history = rs.getString("HistoryData");
//                historyByGrowDay.put(growDay, history);
//            }
//            return historyByGrowDay;
//        } catch (SQLException e) {
//            printSQLException(e);
//            throw new SQLException("Cannot Retrieve Users From DataBase", e);
//        } finally {
//            prepstmt.close();
//            closeConnection(con);
//        }
//    }
//
//    @Override
//    public String getHistory24(Long flockId, Integer growDay, String dn) throws SQLException {
//        String sqlQuery = "select * from flockhistory24 where flockid=? and growday=? and dnum=?" ;
//        PreparedStatement prepstmt = null;
//        Connection con = null;
//
//        try {
//            con  = getConnection();
//            prepstmt = con.prepareStatement(sqlQuery);
//            prepstmt.setLong(1, flockId);
//            prepstmt.setInt(2, growDay);
//            prepstmt.setString(3, dn);
//            ResultSet rs = prepstmt.executeQuery();
//            if (rs.next()) {
//                return rs.getString("HistoryData");
//            } else {
//                return "";
//            }
//        } catch (SQLException e) {
//            printSQLException(e);
//            throw new SQLException("Cannot Retrieve values from History24 table", e);
//        } finally {
//            prepstmt.close();
//            closeConnection(con);
//        }
//    }
//
//    @Override
//    public Map<Integer, String> getAllHistoryByFlock(Long flockId, int fromDay, int toDay) throws SQLException {
//        String sqlQuery = "select * from flockhistory where flockid=? and growday between ? and ?" ;
//        PreparedStatement prepstmt = null;
//        Connection con = null;
//
//        if ( fromDay == -1 && toDay == -1) {
//            fromDay = 0;
//            toDay   = 50;
//        }
//
//        try {
//            con  = getConnection();
//            prepstmt = con.prepareStatement(sqlQuery);
//            prepstmt.setLong(1, flockId);
//            prepstmt.setLong(2, fromDay);
//            prepstmt.setLong(3, toDay);
//
//            ResultSet rs = prepstmt.executeQuery();
//
//            Map<Integer,String> historyByGrowDay = new TreeMap<Integer, String>();
//            while (rs.next()) {
//                Integer growDay = rs.getInt("GrowDay");
//                String history = rs.getString("HistoryData");
//                historyByGrowDay.put(growDay, history);
//            }
//            return historyByGrowDay;
//        } catch (SQLException e) {
//            printSQLException(e);
//            throw new SQLException("Cannot Retrieve Users From DataBase", e);
//        } finally {
//            prepstmt.close();
//            closeConnection(con);
//        }
//    }
//
//    @Override
//    public Integer getResetTime(Long flockId) throws SQLException {
//            return getResetTime(flockId, 1);
//    }
//
//    @Override
//    public Integer getResetTime(Long flockId, Integer growDay) throws SQLException {
//        String sqlQuery = "select historydata from flockhistory24 where FlockID=? "
//                + "and GrowDay=? and DNum='D70'";
//        PreparedStatement prepstmt = null;
//        Connection con = null;
//
//        try {
//            con  = getConnection();
//            prepstmt = con.prepareStatement(sqlQuery);
//            prepstmt.setLong(1, flockId);
//            prepstmt.setLong(2, growDay);
//            ResultSet rs = prepstmt.executeQuery();
//
//            if (rs.next()) {
//                Integer resetTime = rs.getInt("HistoryData");
//                return resetTime;
//            } else {
//                return 0;
//            }
//        } catch (SQLException e) {
//            printSQLException(e);;
//            throw new SQLException("Cannot Retrieve Reset Time From flockhistory24 table");
//        } finally {
//            prepstmt.close();
//            closeConnection(con);
//        }
//    }
//
//    public List<Integer> getHistory24GrowDays(Long flockId)  throws SQLException {
//        String sqlQuery = "select distinct growday from flockhistory24 where flockid=?";
//        PreparedStatement prepstmt = null;
//        Connection con = null;
//
//        try {
//            con = getConnection();
//            prepstmt = con.prepareStatement(sqlQuery);
//            prepstmt.setLong(1, flockId);
//            ResultSet rs = prepstmt.executeQuery();
//
//            List<Integer> growDays = new ArrayList<Integer>();
//            while (rs.next()) {
//                Integer growDay = rs.getInt("GrowDay");
//                growDays.add(growDay);
//            }
//            return growDays;
//        } catch (SQLException e) {
//            printSQLException(e);
//            throw new SQLException("Cannot Retrieve Users From DataBase");
//        } finally {
//            prepstmt.close();
//            closeConnection(con);
//        }
//    }
////   /**
////     * Help to create flock from result set .
////     * @param resultSet a result set
////     * @return flock an objects that encapsulates a flock attributes
////     * @throws java.sql.SQLException
////     */
////    private FlockDto makeFlock(ResultSet resultSet) throws SQLException {
////        FlockDto flock =  new FlockDto();
////        flock.setFlockId(resultSet.getLong("FlockID"));
////        flock.setFlockName(resultSet.getString("Name"));
////        flock.setStatus(resultSet.getString("Status"));
////        flock.setStartDate(resultSet.getString("StartDate"));
////        flock.setEndDate(resultSet.getString("EndDate"));
////        flock.setProgramId(resultSet.getLong("ProgramID"));
////        try {
////            flock.setQuantityMale(resultSet.getInt("QuantityMale"));
////        } catch (SQLException ex) {
////            printSQLException(ex);
////        }
////        try {
////            flock.setPriceChickMale(resultSet.getFloat("PriceChickMale"));
////        } catch (SQLException ex) {
////            printSQLException(ex);
////        }
////        try {
////            flock.setQuantityFemale(resultSet.getInt("QuantityMale"));
////        } catch (SQLException ex) {
////            printSQLException(ex);
////        }
////        try {
////            flock.setPriceChickFemale(resultSet.getFloat("PriceChickMale"));
////        } catch (SQLException ex) {
////            printSQLException(ex);
////        }
////        try {
////            flock.setQuantityFemale(resultSet.getInt("QuantityMale"));
////        } catch (SQLException ex) {
////            printSQLException(ex);
////        }
////        try {
////            flock.setPriceChickFemale(resultSet.getFloat("PriceChickMale"));
////        } catch (SQLException ex) {
////            printSQLException(ex);
////        }
////        try {
////            flock.setTotalChicks(resultSet.getInt("TotalChicks"));
////        } catch (SQLException ex) {
////            printSQLException(ex);
////        }
////        try {
////            flock.setWaterBegin(resultSet.getInt("WaterBegin"));
////        } catch (SQLException ex) {
////            printSQLException(ex);
////        }
////        try {
////            flock.setWaterEnd(resultSet.getInt("WaterEnd"));
////        } catch (SQLException ex) {
////            printSQLException(ex);
////        }
////        try {
////            flock.setPriceWater(resultSet.getInt("PriceWater"));
////        } catch (SQLException ex) {
////            printSQLException(ex);
////        }
////        try {
////            flock.setTotalWater(resultSet.getInt("TotalWater"));
////        } catch (SQLException ex) {
////            printSQLException(ex);
////        }
////        try {
////            flock.setElectBegin(resultSet.getInt("ElectBegin"));
////        } catch (SQLException ex) {
////            printSQLException(ex);
////        }
////        try {
////            flock.setElectEnd(resultSet.getInt("ElectEnd"));
////        } catch (SQLException ex) {
////            printSQLException(ex);
////        }
////        try {
////            flock.setPriceElect(resultSet.getInt("PriceElect"));
////        } catch (SQLException ex) {
////            printSQLException(ex);
////        }
////        try {
////            flock.setTotalElect(resultSet.getInt("TotalElect"));
////        } catch (SQLException ex) {
////            printSQLException(ex);
////        }
////
////        return flock;
////    }
////
////    /**
////     * Help to create list of cellinks from result set
////     * @param resultSet a result set
////     * @return users a list of FlockDto objects
////     * @throws java.sql.SQLException
////     */
////    private List<FlockDto> makeFlockList(ResultSet resultSet) throws SQLException {
////        List<FlockDto> flocks = new ArrayList<FlockDto>();
////        while (resultSet.next()) {
////            flocks.add(makeFlock(resultSet));
////        }
////        return flocks;
////    }
////
////    @Override
////    public void insert(FlockDto flock) throws SQLException {
////        String sqlQuery = "insert into flocks (FlockID,ControllerID,Name,Status,StartDate,EndDate,ProgramID) values (?,?,?,?,?,?,?)";
////        PreparedStatement prepstmt = null;
////        Connection con = null;
////
////        try {
////            con = getConnection();
////            prepstmt = con.prepareStatement(sqlQuery);
////            prepstmt.setObject(1,null);
////            prepstmt.setLong(2,flock.getControllerId());
////            prepstmt.setString(3,flock.getFlockName());
////            prepstmt.setString(4,flock.getStatus());
////            prepstmt.setString(5,flock.getStartTime());
////            prepstmt.setString(6,flock.getEndTime());
////            prepstmt.setLong(7,flock.getProgramId());
////            prepstmt.executeUpdate();
////        } catch (SQLException e) {
////            printSQLException(e);
////            throw new SQLException("Can not Add New Flock To The DataBase");
////        } finally {
////            prepstmt.close();
////            closeConnection(con);
////        }
////    }
////
////    @Override
////    public void update(FlockDto flock) throws SQLException {
////        String sqlQuery = "update controllers set QuantityMale=?, QuantityFemale=?, TotalChicks=?, PriceChickMale=?, PriceChickFemale=?, " +
////                                            "ElectBegin=?, ElectEnd=?, PriceElect=?, TotalElect=?, " +
////                                            "WaterBegin=?, WaterEnd=?, PriceWater=?, TotalWater=?, " +
////                                            " where FlockID=?";
////        PreparedStatement prepstmt = null;
////        Connection con = null;
////
////        try {
////            con  = getConnection();
////            prepstmt = con.prepareStatement(sqlQuery);
////            prepstmt.setInt(1,flock.getQuantityMale());
////            prepstmt.setInt(2,flock.getQuantityFemale());
////            prepstmt.setInt(3,flock.getTotalChicks());
////            prepstmt.setFloat(4,flock.getPriceChickMale());
////            prepstmt.setFloat(5,flock.getPriceChickFemale());
////            prepstmt.setInt(6,flock.getElectBegin());
////            prepstmt.setInt(7,flock.getElectEnd());
////            prepstmt.setFloat(8,flock.getPriceElect());
////            prepstmt.setInt(9,flock.getTotalElect());
////            prepstmt.setInt(10,flock.getWaterBegin());
////            prepstmt.setInt(11,flock.getWaterEnd());
////            prepstmt.setFloat(12,flock.getPriceWater());
////            prepstmt.setInt(13,flock.getTotalWater());
////            prepstmt.setLong(14,flock.getFlockId());
////            prepstmt.executeUpdate();
////        } catch (SQLException e) {
////            printSQLException(e);
////            throw new SQLException("Cannot Update ControllerDto In DataBase");
////        } finally {
////            prepstmt.close();
////            closeConnection(con);
////        }
////    }
////
////    @Override
////    public void remove(Long flockId) throws SQLException {
////        String sqlQuery = "delete from flocks where FlockID=?";
////        PreparedStatement prepstmt = null;
////        Connection con = null;
////
////        try {
////            con  = getConnection();
////            prepstmt = con.prepareStatement(sqlQuery);
////            prepstmt.setLong(1, flockId);
////            prepstmt.executeUpdate();
////        } catch (SQLException e) {
////            printSQLException(e);
////            throw new SQLException("Cannot Remove Flock From DataBase", e);
////        } finally {
////            prepstmt.close();
////            closeConnection(con);
////        }
////    }
////
////    @Override
////    public FlockDto getById(Long flockId) throws SQLException {
////        String sqlQuery = "select * from flocks where FlockID=?";
////        PreparedStatement prepstmt = null;
////        Connection con = null;
////
////        try {
////            con  = getConnection();
////            prepstmt = con.prepareStatement(sqlQuery);
////            prepstmt.setLong(1, flockId);
////            ResultSet rs = prepstmt.executeQuery();
////
////            if (rs.next()) {
////                return makeFlock(rs);
////            } else {
////                return null;
////            }
////        } catch (SQLException e) {
////            printSQLException(e);
////            throw new SQLException("Cannot Retrieve Users From DataBase", e);
////        } finally {
////            prepstmt.close();
////            closeConnection(con);
////        }
////    }
////
////    @Override
////    public List<FlockDto> getAllFlocksByController(Long controllerId) throws SQLException {
////        String sqlQuery = "select * from flocks where ControllerID=?";
////        PreparedStatement prepstmt = null;
////        Connection con = null;
////
////        try {
////            con = getConnection();
////            prepstmt = con.prepareStatement(sqlQuery);
////            prepstmt.setLong(1, controllerId);
////            ResultSet rs = prepstmt.executeQuery();
////            return makeFlockList(rs);
////        } catch (SQLException e) {
////            printSQLException(e);
////            throw new SQLException("Cannot Retrieve Users From DataBase");
////        } finally {
////            prepstmt.close();
////            closeConnection(con);
////        }
////    }
////
////
////    @Override
////    public Map<Integer,String> getHistoryByGrowDay(Long flockId,int... growDays) throws SQLException {
////        //SELECT * FROM flockhistory where flockid=2 and historydata <> '-1'  LIMIT 0, 50
////        String sqlQuery = "select * from flocks as f" +
////                          " join (select * from flockhistory where flockid=? and growday in (?) and historydata <> '-1')" +
////                          " as fh where f.flockid=fh.flockid" ;
////        PreparedStatement prepstmt = null;
////        Connection con = null;
////
////        int len = growDays.length;
////        StringBuilder sb = new StringBuilder();
////        sb.append("(");
////        for(int i = 0; i < len; i++) {
////            sb.append("?").append(",");
////        }
////        sb.replace(sb.length()-1, sb.length(), ")");
////        sqlQuery = sqlQuery.replace("(?)", sb.toString());
////        try {
////            con  = getConnection();
////            prepstmt = con.prepareStatement(sqlQuery);
////            prepstmt.setLong(1, flockId);
////            int i = 0;
////            for(int growDay:growDays) {
////                prepstmt.setInt(i+2, growDay);
////                i++;
////            }
////
////            ResultSet rs = prepstmt.executeQuery();
////            Map<Integer,String> historyByGrowDay = new HashMap<Integer, String>();
////            while (rs.next()) {
////                Integer growDay = rs.getInt("GrowDay");
////                String history = rs.getString("HistoryData");
////                historyByGrowDay.put(growDay, history);
////            }
////            return historyByGrowDay;
////        } catch (SQLException e) {
////            printSQLException(e);
////            throw new SQLException("Cannot Retrieve Users From DataBase", e);
////        } finally {
////            prepstmt.close();
////            closeConnection(con);
////        }
////    }
////
////    @Override
////    public List<Integer> getGrowDaysFromHistory(Long flockId) throws SQLException {
////        String sqlQuery = "select growday from flockhistory where FlockID=?";
////        PreparedStatement prepstmt = null;
////        Connection con = null;
////
////        try {
////            con  = getConnection();
////            prepstmt = con.prepareStatement(sqlQuery);
////            prepstmt.setLong(1, flockId);
////            ResultSet rs = prepstmt.executeQuery();
////
////            List<Integer> growDayList = new ArrayList<Integer>();
////            while (rs.next()) {
////                Integer growDay = rs.getInt("GrowDay");
////                growDayList.add(growDay);
////            }
////            return growDayList;
////        } catch (SQLException e) {
////            printSQLException(e);;
////            throw new SQLException("Cannot Retrieve Users From DataBase");
////        } finally {
////            prepstmt.close();
////            closeConnection(con);
////        }
////    }
////
////
////    @Override
////    public Map<Integer, String> getAllHistoryByFlock(Long flockId) throws SQLException {
////        /***/
////        String sqlQuery = "select * from flockhistory where flockid=?" ;
//////        /***/
//////        String sqlQuery2 = "select count(*) from flockhistory as a where flockid=?" ;
////        PreparedStatement prepstmt = null;
////        Connection con = null;
////
////        try {
////            con  = getConnection();
////            prepstmt = con.prepareStatement(sqlQuery);
////            prepstmt.setLong(1, flockId);
////            ResultSet rs = prepstmt.executeQuery();
////            Map<Integer,String> historyByGrowDay = new TreeMap<Integer, String>();
////            while (rs.next()) {
////                Integer growDay = rs.getInt("GrowDay");
////                String history = rs.getString("HistoryData");
////                historyByGrowDay.put(growDay, history);
////            }
////            return historyByGrowDay;
////        } catch (SQLException e) {
////            printSQLException(e);
////            throw new SQLException("Cannot Retrieve Users From DataBase", e);
////        } finally {
////            prepstmt.close();
////            closeConnection(con);
////        }
////    }
////
////    @Override
////    public Map<Integer, String> getAllHistoryByFlock(Long flockId, int fromDay, int toDay) throws SQLException {
////        String sqlQuery = "select * from flockhistory where flockid=? and growday between ? and ?" ;
////        PreparedStatement prepstmt = null;
////        Connection con = null;
////
////        if ( fromDay == -1 && toDay == -1) {
////            fromDay = 0;
////            toDay   = 50;
////        }
////
////        try {
////            con  = getConnection();
////            prepstmt = con.prepareStatement(sqlQuery);
////            prepstmt.setLong(1, flockId);
////            prepstmt.setLong(2, fromDay);
////            prepstmt.setLong(3, toDay);
////
////            ResultSet rs = prepstmt.executeQuery();
////
////            Map<Integer,String> historyByGrowDay = new TreeMap<Integer, String>();
////            while (rs.next()) {
////                Integer growDay = rs.getInt("GrowDay");
////                String history = rs.getString("HistoryData");
////                historyByGrowDay.put(growDay, history);
////            }
////            return historyByGrowDay;
////        } catch (SQLException e) {
////            printSQLException(e);
////            throw new SQLException("Cannot Retrieve Users From DataBase", e);
////        } finally {
////            prepstmt.close();
////            closeConnection(con);
////        }
////    }
////
////    public static void main(String args[]) throws SQLException{
////        FlockDaoImpl fdao = new FlockDaoImpl();
////        Map<Integer,String> history = fdao.getHistoryByGrowDay(new Long(2), 1,2,3,4,5,9,12);
////        Iterator iter = history.keySet().iterator();
////        while(iter.hasNext()) {
////            Integer key = (Integer)iter.next();
////            String value = history.get(key);
////            System.out.println("\t" + key + "\t"+value);
////        }
////    }


//SELECT SUBSTRING_INDEX(SUBSTRING_INDEX( `historydata` , ' ', 2 ),' ',2) AS b from flockhistory where flockid=2

    @Override
    public String getDNHistory24(String dn) throws SQLException {
        String sqlQuery = "select name from historyn where dn=?";
        PreparedStatement prepstmt = null;
        Connection con = null;

        try {
            con = getConnection();
            prepstmt = con.prepareStatement(sqlQuery);
            prepstmt.setString(1, dn);
            ResultSet rs = prepstmt.executeQuery();
            if (rs.next()) {
                return rs.getString("name");
            } else {
                return "";
            }
        } catch (SQLException e) {
            printSQLException(e);
            throw new SQLException("Cannot Retrieve values from History24 table", e);
        } finally {
            prepstmt.close();
            closeConnection(con);
        }

    }
}
