
/**
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.agrologic.app.dao.impl;



import com.agrologic.app.dao.MedicineDao;
import com.agrologic.app.model.MedicineDto;



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
public class MedicineDaoImpl extends ConnectorDao implements MedicineDao {
    private MedicineDto makeMedicine(ResultSet rs) throws SQLException {
        MedicineDto medicine = new MedicineDto();

        medicine.setId(rs.getLong("ID"));
        medicine.setFlockId(rs.getLong("FlockID"));
        medicine.setAmount(rs.getInt("Amount"));
        medicine.setName(rs.getString("Name"));
        medicine.setPrice(rs.getFloat("Price"));
        medicine.setTotal(rs.getFloat("Total"));

        return medicine;
    }

    private List<MedicineDto> makeMedicineList(ResultSet rs) throws SQLException {
        List<MedicineDto> medicineList = new ArrayList<MedicineDto>();

        while (rs.next()) {
            medicineList.add(makeMedicine(rs));
        }

        return medicineList;
    }

    @Override
    public void insert(MedicineDto medicine) throws SQLException {
        String            sqlQuery = "insert into medicine values (?,?,?,?,?,?)";
        PreparedStatement prepstmt = null;
        Connection        con      = null;

        try {
            con      = getConnection();
            prepstmt = con.prepareStatement(sqlQuery);
            prepstmt.setObject(1, null);
            prepstmt.setLong(2, medicine.getFlockId());
            prepstmt.setInt(3, medicine.getAmount());
            prepstmt.setString(4, medicine.getName());
            prepstmt.setFloat(5, medicine.getPrice());
            prepstmt.setFloat(6, medicine.getTotal());
            prepstmt.executeUpdate();
        } catch (SQLException e) {
            printSQLException(e);

            throw new SQLException("Cannot Insert Medicine To The DataBase");
        } finally {
            prepstmt.close();
            closeConnection(con);
        }
    }

    @Override
    public void remove(Long id) throws SQLException {
        String            sqlQuery = "delete from medicine where ID=?";
        PreparedStatement prepstmt = null;
        Connection        con      = null;

        try {
            con      = getConnection();
            prepstmt = con.prepareStatement(sqlQuery);
            prepstmt.setLong(1, id);
            prepstmt.executeUpdate();
        } catch (SQLException e) {
            printSQLException(e);

            throw new SQLException("Cannot Delete Controller From DataBase");
        } finally {
            prepstmt.close();
            closeConnection(con);
        }
    }

    @Override
    public MedicineDto getById(Long id) throws SQLException {
        String            sqlQuery = "select * from medicine where ID=?";
        PreparedStatement prepstmt = null;
        Connection        con      = null;

        try {
            con      = getConnection();
            prepstmt = con.prepareStatement(sqlQuery);
            prepstmt.setLong(1, id);

            ResultSet rs = prepstmt.executeQuery();

            if (rs.next()) {
                return makeMedicine(rs);
            } else {
                return null;
            }
        } catch (SQLException e) {
            printSQLException(e);

            throw new SQLException("Cannot Retrieve Medicine " + id + " From DataBase");
        } finally {
            prepstmt.close();
            closeConnection(con);
        }
    }

    @Override
    public List<MedicineDto> getAllByFlockId(Long flockId) throws SQLException {
        String            sqlQuery = "select * from medicine where FlockID=?";
        PreparedStatement prepstmt = null;
        Connection        con      = null;

        try {
            con      = getConnection();
            prepstmt = con.prepareStatement(sqlQuery);
            prepstmt.setLong(1, flockId);

            ResultSet rs = prepstmt.executeQuery();

            return makeMedicineList(rs);
        } catch (SQLException e) {
            printSQLException(e);

            throw new SQLException("Cannot Retrieve All Medicine of Flock " + flockId + " From DataBase");
        } finally {
            prepstmt.close();
            closeConnection(con);
        }
    }

    @Override
    public String getCurrencyById(Long id) throws SQLException {
        String            sqlQuery = "select * from currency where ID=?";
        PreparedStatement prepstmt = null;
        Connection        con      = null;

        try {
            con      = getConnection();
            prepstmt = con.prepareStatement(sqlQuery);
            prepstmt.setLong(1, id);

            ResultSet rs = prepstmt.executeQuery();

            if (rs.next()) {
                return rs.getString("Symbol");
            } else {
                return null;
            }
        } catch (SQLException e) {
            printSQLException(e);

            throw new SQLException("Cannot Retrieve Medicine " + id + " From DataBase");
        } finally {
            prepstmt.close();
            closeConnection(con);
        }
    }
}



