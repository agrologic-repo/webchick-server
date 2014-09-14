
/*
* To change this template, choose Tools | Templates
* and open the template in the editor.
 */
package com.agrologic.app.dao;


import java.sql.SQLException;

/**
 * DAO for the domain string . It provides operations to work with domain string
 *
 * @author Valery Manakhimov
 */
public interface DomainDao {
    /**
     * Return string that represent domain .
     *
     * @return domain the string that represent domain
     * @throws SQLException if failed to get domain from database .
     */
    String getDomain() throws SQLException;

    /**
     * Return path to logo by specified domain name
     *
     * @param domain the domain name
     * @return string to path of logo
     * @throws SQLException if failed to get path to logo  from database .
     */
    String getLogoPath(String domain) throws SQLException;

    /**
     * Return company name by specified domain name .
     *
     * @param domain the domain name
     * @return the company
     * @throws SQLException if failed to get company from database .
     */
    String getCompany(String domain) throws SQLException;
}



