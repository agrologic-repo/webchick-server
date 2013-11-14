package com.agrologic.app.dao.derby.impl;

import com.agrologic.app.dao.CreatebleDao;
import com.agrologic.app.dao.DropableDao;
import com.agrologic.app.dao.RemovebleDao;
import com.agrologic.app.dao.mysql.impl.MedicineDaoImpl;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DerbyMedicineDaoImpl extends MedicineDaoImpl implements CreatebleDao, DropableDao, RemovebleDao {

    public DerbyMedicineDaoImpl(JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate);
    }

    @Override
    public boolean tableExist() throws SQLException {
        try {
            DatabaseMetaData dbmd = jdbcTemplate.getDataSource().getConnection().getMetaData();

            ResultSet rs = dbmd.getTables(null, "APP", "MEDICINE", null);

            if (!rs.next()) {
                return false;
            }

        } catch (SQLException e) {
            throw new SQLException("Cannot get table MEDICINE from DataBase", e);
        }

        return true;
    }

    @Override
    public void createTable() throws SQLException {
        String sql = "CREATE TABLE MEDICINE ( "
                + "ID INT  NOT NULL PRIMARY KEY GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1) , "
                + "FLOCKID INT  NOT NULL, "
                + "AMOUNT INT  NOT NULL, "
                + "NAME VARCHAR(50) NOT NULL, "
                + "PRICE DOUBLE NOT NULL, "
                + "TOTAL DOUBLE NOT NULL "
                + ")";
        jdbcTemplate.execute(sql);
    }


    @Override
    public void dropTable() throws SQLException {
        String sql = "DROP TABLE APP.MEDICINE ";
        jdbcTemplate.execute(sql);
    }

//    @Override
//    public void insert(Medicine medicnie) throws SQLException {
//        String sql = "INSERT INTO MEDICINE ( FLOCKID, AMOUNT,NAME,PRICE, TOTAL ) "
//                + " VALUES (?,?,?,?,?) ";
//        PreparedStatement prepstmt = null;
//        Connection con = null;
//
//        try {
//            con = dao.getConnection();
//            prepstmt = con.prepareStatement(sql);
//            prepstmt.setLong(1, medicnie.getFlockId());
//            prepstmt.setInt(2, medicnie.getAmount());
//            prepstmt.setString(3, medicnie.getName());
//            prepstmt.setFloat(4, medicnie.getPrice());
//            prepstmt.setFloat(5, medicnie.getTotal());
//            prepstmt.executeUpdate();
//        } catch (SQLException e) {
//            dao.printSQLException(e);
//            throw new SQLException("Cannot Insert Medicine To The DataBase");
//        } finally {
//            prepstmt.close();
//            dao.closeConnection(con);
//        }
//    }

    @Override
    public void deleteFromTable() throws SQLException {
        String sql = "DELETE  FROM APP.MEDICINE ";
        jdbcTemplate.execute(sql);
    }
}



