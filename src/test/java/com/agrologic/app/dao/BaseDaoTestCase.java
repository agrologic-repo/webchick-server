/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.agrologic.app.dao;

import org.apache.log4j.Logger;
import org.junit.Ignore;

/**
 * Base class for running Dao tests.
 *
 * @author mraible
 */
@Ignore
public abstract class BaseDaoTestCase {

    protected final Logger log = Logger.getLogger(getClass().getName());

    public BaseDaoTestCase() {
        // Since a ResourceBundle is not required for each class, just
        // do a simple check to see if one exists
        String className = this.getClass().getName();
        getDefaultDatabaseDir();
    }

    protected String getDefaultDatabaseDir() {
        String userHomeDir = System.getProperty("user.home", ".");
        String systemDir = userHomeDir + "\\collection";
        return System.getProperty("derby.system.home", systemDir);
    }
}