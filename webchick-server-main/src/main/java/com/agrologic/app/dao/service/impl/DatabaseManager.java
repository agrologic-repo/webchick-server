package com.agrologic.app.dao.service.impl;

import com.agrologic.app.config.Configuration;
import com.agrologic.app.dao.DaoType;
import com.agrologic.app.dao.RemovebleDao;
import com.agrologic.app.exception.DatabaseNotFound;
import com.agrologic.app.exception.WrongDatabaseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DatabaseManager {

    private final Logger logger = LoggerFactory.getLogger(DatabaseManager.class);
    private DatabaseCreator databaseCreator;
    private DatabaseGeneralService databaseGeneralService;
    private DatabaseInsertion databaseInsertion;
    private DatabaseLoader databaseLoader;
    private final ExecutorService executorService;

    /**
     * Creates a new {@link DatabaseManager}.
     */
    public DatabaseManager() {
        this(DaoType.DERBY);
    }

    /**
     * Creates a new {@link DatabaseManager}.
     *
     * @param daoType the dao type
     */
    public DatabaseManager(DaoType daoType) {
        this.databaseGeneralService = new DatabaseGeneralService(daoType);
        this.databaseLoader = new DatabaseLoader(databaseGeneralService);
        this.executorService = Executors.newSingleThreadExecutor();
    }

    public synchronized void doLoadTableData() throws DatabaseNotFound, WrongDatabaseException {
        Configuration config = new Configuration();
        Long userId = Long.parseLong(config.getUserId());
        Long cellinkId = Long.parseLong(config.getCellinkId());
        if (userId.equals((long) -1) || cellinkId.equals((long) -1)) {
            logger.info("UserID or CellinkID argument error : User ID : {} and Cellink ID: {} "
                    , new Object[]{userId, cellinkId});
            throw new IllegalArgumentException();
        }
        databaseLoader.loadControllersAndProgramsByUserAndCellink(userId, cellinkId);
    }

    public synchronized void runCreateTablesTask() {
        databaseGeneralService.setDaoType(DaoType.DERBY);
        databaseGeneralService.initDaoByType();
        databaseCreator = new DatabaseCreator(databaseGeneralService);
        databaseCreator.createAllTables();
    }

    public synchronized void runInsertLoadedData() {
        if (databaseInsertion == null) {
            databaseInsertion = new DatabaseInsertion(databaseGeneralService, databaseLoader);
        }
        databaseInsertion.insertLoadedData();
    }

    public synchronized void runRemoveOldData() {
        try {
            ((RemovebleDao) databaseGeneralService.getControllerDao()).deleteFromTable();
            ((RemovebleDao) databaseGeneralService.getCellinkDao()).deleteFromTable();
            ((RemovebleDao) databaseGeneralService.getUserDao()).deleteFromTable();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public DatabaseLoader getDatabaseLoader() {
        return databaseLoader;
    }

    public DatabaseGeneralService getDatabaseGeneralService() {
        return databaseGeneralService;
    }

    public void finish() {
//        databaseGeneralService.closeAll();
        databaseGeneralService = null;
        databaseCreator = null;
        databaseInsertion = null;
        databaseLoader = null;
        executorService.shutdownNow();
        System.gc();
    }
}
