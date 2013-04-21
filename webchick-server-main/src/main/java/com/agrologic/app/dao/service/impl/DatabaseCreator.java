
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.agrologic.app.dao.service.impl;

//~--- non-JDK imports --------------------------------------------------------
import com.agrologic.app.dao.CreatebleDao;
import com.agrologic.app.dao.DropableDao;
import com.agrologic.app.dao.RemovebleDao;
import com.agrologic.app.dao.service.DatabaseAccessor;
import com.agrologic.app.dao.service.DatabaseCreatable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.log4j.Logger;

/**
 * {Insert class description here}
 *
 * @version $Revision: 1.1.1.1 $
 * @since Build {insert version here} (MM YYYY)
 * @author Valery Manakhimov
 * @author $Author: nbweb $, (this version)
 */
public class DatabaseCreator implements DatabaseCreatable {

    private final Logger log = Logger.getLogger(DatabaseCreator.class.getName());
    /**
     * List of Createble objects.
     */
    private List<Object> createbleList;
    /**
     * Interface that provide access to created dao objects
     */
    private DatabaseAccessor dba;

    public DatabaseCreator() {
        super();
        createbleList = new ArrayList<Object>();
    }

    public DatabaseCreator(DatabaseAccessor dba) {
        super();
        this.dba = dba;
        createbleList = new ArrayList<Object>();
        initCreatebleList();
    }

    private void initCreatebleList() {
        createbleList.add((CreatebleDao) dba.getProgramDao());
        createbleList.add((CreatebleDao) dba.getScreenDao());
        createbleList.add((CreatebleDao) dba.getTableDao());
        createbleList.add((CreatebleDao) dba.getDataDao());
        createbleList.add((CreatebleDao) dba.getAlarmDao());
        createbleList.add((CreatebleDao) dba.getRelayDao());
        createbleList.add((CreatebleDao) dba.getSystemStateDao());
        createbleList.add((CreatebleDao) dba.getLanguageDao());
        createbleList.add((CreatebleDao) dba.getUserDao());
        createbleList.add((CreatebleDao) dba.getCellinkDao());
        createbleList.add((CreatebleDao) dba.getControllerDao());
        createbleList.add((CreatebleDao) dba.getFlockDao());
        createbleList.add((CreatebleDao) dba.getDistribDao());
        createbleList.add((CreatebleDao) dba.getFeedDao());
        createbleList.add((CreatebleDao) dba.getFeedTypeDao());
        createbleList.add((CreatebleDao) dba.getFuelDao());
        createbleList.add((CreatebleDao) dba.getGasDao());
        createbleList.add((CreatebleDao) dba.getLaborDao());
        createbleList.add((CreatebleDao) dba.getMedicineDao());
        createbleList.add((CreatebleDao) dba.getSpreadDao());
        createbleList.add((CreatebleDao) dba.getTransactionDao());
        createbleList.add((CreatebleDao) dba.getWorkerDao());
    }

    @Override
    public final void createAllTables() {
        log.info(" Start creating database tables    ");
        log.info(" This will take several minutes... ");

        Iterator<Object> iter = createbleList.iterator();
        while (iter.hasNext()) {
            try {
                Object creator = iter.next();
                if (!((CreatebleDao) creator).tableExist()) {
                    log.info("Create " + creator.getClass().getName() + " table ... ");
                    ((CreatebleDao) creator).createTable();
                }
            } catch (SQLException ex) {
                log.info(" Error during creating tables ", ex);
            }
        }
    }

    @Override
    public final void dropAllTables() {
        log.info(" Start dropping database tables    ");
        log.info(" This will take several minutes... ");

        Iterator<Object> iter = createbleList.iterator();
        while (iter.hasNext()) {
            try {
                Object droper = iter.next();
                if (((CreatebleDao) droper).tableExist()) {
                    log.info("Drop " + droper.getClass().getName() + " table ... ");
                    ((DropableDao) droper).dropTable();
                }
            } catch (SQLException ex) {
                log.info("Error during dropping tables ", ex);
            }
        }
    }

    @Override
    public void removeAllTables() {
        log.info(" Start removing database tables    ");
        log.info(" This will take several minutes... ");

        Iterator<Object> iter = createbleList.iterator();
        while (iter.hasNext()) {
            try {
                Object droper = iter.next();
                if (((RemovebleDao) droper).tableExist()) {
                    log.info("Drop " + droper.getClass().getName() + " table ... ");
                    ((RemovebleDao) droper).removeFromTable();
                }
            } catch (SQLException ex) {
                log.info("Error during remove all data from tables ", ex);
            }
        }
    }
}