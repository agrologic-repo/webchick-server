
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.agrologic.app.dao.service.impl;

import com.agrologic.app.dao.CreatebleDao;
import com.agrologic.app.dao.DropableDao;
import com.agrologic.app.dao.RemovebleDao;
import com.agrologic.app.dao.service.DatabaseAccessor;
import com.agrologic.app.dao.service.DatabaseCreatable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class DatabaseCreator implements DatabaseCreatable {

    private final Logger logger = LoggerFactory.getLogger(DatabaseCreator.class);
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
        createbleList.add(dba.getProgramAlarmDao());
        createbleList.add(dba.getProgramRelayDao());
        createbleList.add(dba.getProgramSystemStateDao());
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
                logger.info("Create " + creator.getClass().getName() + " table ... ");
                if (!((CreatebleDao) creator).tableExist()) {
                    logger.info("Created " + creator.getClass().getName() + " table ... ");
                    ((CreatebleDao) creator).createTable();
                }
            } catch (SQLException ex) {
                logger.info(" Error during creating tables ", ex);
            } catch (Exception ex) {
                ex.printStackTrace();
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
                    ((RemovebleDao) dropper).deleteFromTable();
                }
            } catch (SQLException ex) {
                logger.info("Error during remove all data from tables ", ex);
            }
        }
    }
}