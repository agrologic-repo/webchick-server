
/*
* To change this template, choose Tools | Templates
* and open the template in the editor.
 */
package com.agrologic.app.dao;



import java.sql.SQLException;


public interface DomainDao {
    String getDomain() throws SQLException;

    String getLogoPath(String domain) throws SQLException;

    String getCompany(String domain) throws SQLException;
}



