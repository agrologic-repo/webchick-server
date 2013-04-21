
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.agrologic.app.dao.service.impl;

//~--- non-JDK imports --------------------------------------------------------
import com.agrologic.app.dao.*;
import com.agrologic.app.dao.service.DatabaseAccessor;
import java.io.Serializable;
import org.apache.log4j.Logger;

/**
 * {Insert class description here}
 *
 * @version $Revision: 1.1.1.1 $
 * @since Build {insert version here} (MM YYYY)
 * @author Valery Manakhimov
 * @author $Author: nbweb $, (this version)
 */
public class DatabaseGeneralService implements DatabaseAccessor, Serializable {

    protected final Logger log = Logger.getLogger(DatabaseGeneralService.class.getName());
    protected AlarmDao alarmDao;
    protected CellinkDao cellinkDao;
    protected ControllerDao controllerDao;
    protected FlockDao flockDao;
    protected DaoType daoType;
    protected DataDao dataDao;
    protected DistribDao distribDao;
    protected FeedDao feedDao;
    protected FeedTypeDao feedTypeDao;
    protected FuelDao fuelDao;
    protected LaborDao laborDao;
    protected GasDao gasDao;
    protected MedicineDao medicineDao;
    protected SpreadDao spreadDao;
    protected TransactionDao transactionDao;
    protected WorkerDao workerDao;
    protected LanguageDao languageDao;
    protected ProgramDao programDao;
    protected RelayDao relayDao;
    protected SchemaDao schemaDao;
    protected ScreenDao screenDao;
    protected SystemStateDao systemStateDao;
    protected TableDao tableDao;
    protected UserDao userDao;

    public DatabaseGeneralService(DaoType daoType) {
        this.daoType = daoType;
        initDaoByType();
    }

    protected void printLogbyDaoType(String message) {
        StringBuilder buffer = new StringBuilder();
        buffer.append(message);
        switch (daoType) {
            default:
            case MYSQL:
                buffer.append("  for mysql database ");
                break;

            case DERBY:
                buffer.append("  for derby database ");
                break;
        }
        log.info(buffer.toString());
    }

    public DaoType getDaoType() {
        return daoType;
    }

    public void setDaoType(DaoType daoType) {
        this.daoType = daoType;
    }

    /**
     * Initialize dao interface by dao type
     */
    public final void initDaoByType() {
        printLogbyDaoType("Initializating dao interface");
        schemaDao = DbImplDecider.getDaoFactory(daoType).getSchemaDao();
        userDao = DbImplDecider.getDaoFactory(daoType).getUserDao();
        cellinkDao = DbImplDecider.getDaoFactory(daoType).getCellinkDao();
        controllerDao = DbImplDecider.getDaoFactory(daoType).getControllerDao();
        flockDao = DbImplDecider.getDaoFactory(daoType).getFlockDao();
        programDao = DbImplDecider.getDaoFactory(daoType).getProgramDao();
        screenDao = DbImplDecider.getDaoFactory(daoType).getScreenDao();
        tableDao = DbImplDecider.getDaoFactory(daoType).getTableDao();
        dataDao = DbImplDecider.getDaoFactory(daoType).getDataDao();
        alarmDao = DbImplDecider.getDaoFactory(daoType).getAlarmDao();
        relayDao = DbImplDecider.getDaoFactory(daoType).getRelayDao();
        systemStateDao = DbImplDecider.getDaoFactory(daoType).getSystemStateDao();
        languageDao = DbImplDecider.getDaoFactory(daoType).getLanguageDao();
        distribDao = DbImplDecider.getDaoFactory(daoType).getDistribDao();
        feedDao = DbImplDecider.getDaoFactory(daoType).getFeedDao();
        feedTypeDao = DbImplDecider.getDaoFactory(daoType).getFeedTypeDao();
        fuelDao = DbImplDecider.getDaoFactory(daoType).getFuelDao();
        laborDao = DbImplDecider.getDaoFactory(daoType).getLaborDao();
        gasDao = DbImplDecider.getDaoFactory(daoType).getGasDao();
        medicineDao = DbImplDecider.getDaoFactory(daoType).getMedicineDao();
        spreadDao = DbImplDecider.getDaoFactory(daoType).getSpreadDao();
        transactionDao = DbImplDecider.getDaoFactory(daoType).getTransactionDao();
        workerDao = DbImplDecider.getDaoFactory(daoType).getWorkerDao();
    }

    /**
     * Set database directory folder to the system user home path folder collection.
     */
    public void setDefaultDatabaseDir() {
        String userHomeDir = System.getProperty("user.home", ".");
        String systemDir = userHomeDir + "\\agrologic\\database";
        setDatabaseDir(systemDir);
    }

    /**
     * Set embedded database directory folder .
     *
     * @param dir the directory to set
     */
    public void setDatabaseDir(String dir) {
        System.setProperty("derby.system.home", dir);
        log.info("Derby database path : " + dir);
    }

    public AlarmDao getAlarmDao() {
        return alarmDao;
    }

    @Override
    public void setAlarmDao(AlarmDao alarmDao) {
        this.alarmDao = alarmDao;
    }

    @Override
    public CellinkDao getCellinkDao() {
        return cellinkDao;
    }

    @Override
    public void setCellinkDao(CellinkDao cellinkDao) {
        this.cellinkDao = cellinkDao;
    }

    @Override
    public ControllerDao getControllerDao() {
        return controllerDao;
    }

    @Override
    public void setControllerDao(ControllerDao controllerDao) {
        this.controllerDao = controllerDao;
    }

    @Override
    public DataDao getDataDao() {
        return dataDao;
    }

    @Override
    public void setDataDao(DataDao dataDao) {
        this.dataDao = dataDao;
    }

    @Override
    public LanguageDao getLanguageDao() {
        return languageDao;
    }

    @Override
    public void setLanguageDao(LanguageDao languageDao) {
        this.languageDao = languageDao;
    }

    @Override
    public ProgramDao getProgramDao() {
        return programDao;
    }

    @Override
    public void setProgramDao(ProgramDao programDao) {
        this.programDao = programDao;
    }

    @Override
    public RelayDao getRelayDao() {
        return relayDao;
    }

    public void setRelayDao(RelayDao relayDao) {
        this.relayDao = relayDao;
    }

    public SchemaDao getSchemaDao() {
        return schemaDao;
    }

    public void setSchemaDao(SchemaDao schemaDao) {
        this.schemaDao = schemaDao;
    }

    public ScreenDao getScreenDao() {
        return screenDao;
    }

    public void setScreenDao(ScreenDao screenDao) {
        this.screenDao = screenDao;
    }

    public SystemStateDao getSystemStateDao() {
        return systemStateDao;
    }

    public void setSystemStateDao(SystemStateDao systemStateDao) {
        this.systemStateDao = systemStateDao;
    }

    public TableDao getTableDao() {
        return tableDao;
    }

    public void setTableDao(TableDao tableDao) {
        this.tableDao = tableDao;
    }

    public UserDao getUserDao() {
        return userDao;
    }

    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    public FlockDao getFlockDao() {
        return flockDao;
    }

    public void setFlockDao(FlockDao flockDao) {
        this.flockDao = flockDao;
    }

    public DistribDao getDistribDao() {
        return distribDao;
    }

    public void setDistribDao(DistribDao distribDao) {
        this.distribDao = distribDao;
    }

    public FeedDao getFeedDao() {
        return feedDao;
    }

    public void setFeedDao(FeedDao feedDao) {
        this.feedDao = feedDao;
    }

    public FeedTypeDao getFeedTypeDao() {
        return feedTypeDao;
    }

    public void setFeedTypeDao(FeedTypeDao feedTypeDao) {
        this.feedTypeDao = feedTypeDao;
    }

    public FuelDao getFuelDao() {
        return fuelDao;
    }

    public void setFuelDao(FuelDao fuelDao) {
        this.fuelDao = fuelDao;
    }

    public GasDao getGasDao() {
        return gasDao;
    }

    public void setGasDao(GasDao gasDao) {
        this.gasDao = gasDao;
    }

    public LaborDao getLaborDao() {
        return laborDao;
    }

    public void setLaborDao(LaborDao laborDao) {
        this.laborDao = laborDao;
    }

    public MedicineDao getMedicineDao() {
        return medicineDao;
    }

    public void setMedicineDao(MedicineDao medicineDao) {
        this.medicineDao = medicineDao;
    }

    public SpreadDao getSpreadDao() {
        return spreadDao;
    }

    public void setSpreadDao(SpreadDao spreadDao) {
        this.spreadDao = spreadDao;
    }

    public TransactionDao getTransactionDao() {
        return transactionDao;
    }

    public void setTransactionDao(TransactionDao transactionDao) {
        this.transactionDao = transactionDao;
    }

    public WorkerDao getWorkerDao() {
        return workerDao;
    }

    public void setWorkerDao(WorkerDao workerDao) {
        this.workerDao = workerDao;
    }

    public void closeAll() {
        DbImplDecider.getDaoFactory(daoType).closeAllConnection();
    }
}


//~ Formatted by Jindent --- http://www.jindent.com
