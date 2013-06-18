package com.agrologic.app.dao.service.impl;

import com.agrologic.app.config.Configuration;
import com.agrologic.app.dao.DaoType;
import com.agrologic.app.dao.RemovebleDao;
import org.apache.log4j.Logger;

import java.sql.SQLException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DatabaseManager {

    private final Logger logger = Logger.getLogger(DatabaseManager.class);
    private DatabaseCreator databaseCreator;
    private DatabaseGeneralService databaseGeneralService;
    private DatabaseInsertion databaseInsertion;
    private DatabaseLoader databaseLoader;
    private Long userId;
    private Long cellinkId;
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

    public synchronized void doLoadTableData() throws SQLException {
        Configuration config = new Configuration();
        doLoadTableData(config.getUserId(), config.getCellinkId());
    }

    public synchronized void doLoadTableData(String suserId, String scellinkId) throws SQLException {
        userId = Long.parseLong(suserId);
        cellinkId = Long.parseLong(scellinkId);
        try {
            if (userId.equals((long) -1) || cellinkId.equals((long) -1)) {
                logger.trace("UserID or CellinkID argument error : userId= " + userId + "; cellinkId= " + cellinkId + ";");
                throw new IllegalArgumentException();
            }
            databaseLoader.loadAllDataByUserAndCellink(userId, cellinkId);
        } catch (Exception e) {
            throw new SQLException(e.getMessage(), e);
        }
    }

    public synchronized void runCreateTablesTask() {
        databaseGeneralService.setDaoType(DaoType.DERBY);
        databaseGeneralService.initDaoByType();
        databaseCreator = new DatabaseCreator(databaseGeneralService);
//        databaseCreator.dropAllTables();
//        runRemoveOldData();
        databaseCreator.createAllTables();
    }

    public synchronized void runInsertLoadedData() {
        databaseGeneralService.setDaoType(DaoType.DERBY);
        databaseGeneralService.initDaoByType();
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
        userId = null;
        cellinkId = null;
        databaseGeneralService.closeAll();
        databaseGeneralService = null;
        databaseCreator = null;
        databaseInsertion = null;
        databaseLoader = null;
        executorService.shutdownNow();
        System.gc();
    }
}
