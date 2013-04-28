
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
import org.apache.log4j.Logger;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * {Insert class description here}
 *
 * @author Valery Manakhimov
 * @author $Author: nbweb $, (this version)
 * @version $Revision: 1.1.1.1 $
 * @since Build {insert version here} (MM YYYY)
 */
public class DatabaseCreator implements DatabaseCreatable {

    private final Logger logger = Logger.getLogger(DatabaseCreator.class.getName());
    /**
     * List of Creatable objects.
     */
    private List<Object> createbleList;
    /**
     * Interface that provide access to created dao objects
     */
    private DatabaseAccessor dba;

    public DatabaseCreator(DatabaseAccessor dba) {
        super();
        this.dba = dba;
        createbleList = new ArrayList<Object>();
        initCreatableList();
    }

    private void initCreatableList() {
        createbleList.add(dba.getProgramDao());
        createbleList.add(dba.getScreenDao());
        createbleList.add(dba.getTableDao());
        createbleList.add(dba.getDataDao());
        createbleList.add(dba.getAlarmDao());
        createbleList.add(dba.getRelayDao());
        createbleList.add(dba.getSystemStateDao());
        createbleList.add(dba.getLanguageDao());
        createbleList.add(dba.getUserDao());
        createbleList.add(dba.getCellinkDao());
        createbleList.add(dba.getControllerDao());
        createbleList.add(dba.getFlockDao());
        createbleList.add(dba.getDistribDao());
        createbleList.add(dba.getFeedDao());
        createbleList.add(dba.getFeedTypeDao());
        createbleList.add(dba.getFuelDao());
        createbleList.add(dba.getGasDao());
        createbleList.add(dba.getLaborDao());
        createbleList.add(dba.getMedicineDao());
        createbleList.add(dba.getSpreadDao());
        createbleList.add(dba.getTransactionDao());
        createbleList.add(dba.getWorkerDao());
    }

    @Override
    public final void createAllTables() {
        logger.info(" Start creating database tables    ");
        logger.info(" This will take several minutes... ");

        Iterator<Object> iter = createbleList.iterator();
        while (iter.hasNext()) {
            try {
                Object creator = iter.next();
                if (!((CreatebleDao) creator).tableExist()) {
                    logger.info("Create " + creator.getClass().getName() + " table ... ");
                    ((CreatebleDao) creator).createTable();
                }
            } catch (SQLException ex) {
                logger.info(" Error during creating tables ", ex);
            }
        }
    }

    @Override
    public final void dropAllTables() {
        logger.info(" Start dropping database tables    ");
        logger.info(" This will take several minutes... ");

        Iterator<Object> iter = createbleList.iterator();
        while (iter.hasNext()) {
            try {
                Object droper = iter.next();
                if (((CreatebleDao) droper).tableExist()) {
                    logger.info("Drop " + droper.getClass().getName() + " table ... ");
                    ((DropableDao) droper).dropTable();
                }
            } catch (SQLException ex) {
                logger.info("Error during dropping tables ", ex);
            }
        }
    }

    @Override
    public void removeAllTables() {
        logger.info(" Start removing database tables    ");
        logger.info(" This will take several minutes... ");

        Iterator<Object> iter = createbleList.iterator();
        while (iter.hasNext()) {
            try {
                Object dropper = iter.next();
                if (((RemovebleDao) dropper).tableExist()) {
                    logger.info("Drop " + dropper.getClass().getName() + " table ... ");
                    ((RemovebleDao) dropper).removeFromTable();
                }
            } catch (SQLException ex) {
                logger.info("Error during remove all data from tables ", ex);
            }
        }
    }
}