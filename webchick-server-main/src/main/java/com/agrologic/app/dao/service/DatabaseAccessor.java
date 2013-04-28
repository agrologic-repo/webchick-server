
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.agrologic.app.dao.service;

//~--- non-JDK imports --------------------------------------------------------
import com.agrologic.app.dao.*;

/**
 * {Insert class description here}
 *
 * @version $Revision: 1.1.1.1 $
 * @since Build {insert version here} (MM YYYY)
 * @author Valery Manakhimov
 * @author $Author: nbweb $, (this version)
 */
public interface DatabaseAccessor {

    AlarmDao getAlarmDao();

    void setAlarmDao(AlarmDao alarmDao);

    CellinkDao getCellinkDao();

    void setCellinkDao(CellinkDao cellinkDao);

    ControllerDao getControllerDao();

    void setControllerDao(ControllerDao controllerDao);

    DataDao getDataDao();

    void setDataDao(DataDao dataDao);

    LanguageDao getLanguageDao();

    void setLanguageDao(LanguageDao languageDao);

    ProgramDao getProgramDao();

    void setProgramDao(ProgramDao programDao);

    RelayDao getRelayDao();

    void setRelayDao(RelayDao relayDao);

    SchemaDao getSchemaDao();

    void setSchemaDao(SchemaDao schemaDao);

    ScreenDao getScreenDao();

    void setScreenDao(ScreenDao screenDao);

    SystemStateDao getSystemStateDao();

    void setSystemStateDao(SystemStateDao systemStateDao);

    TableDao getTableDao();

    void setTableDao(TableDao tableDao);

    UserDao getUserDao();

    void setUserDao(UserDao userDao);

    FlockDao getFlockDao();

    void setFlockDao(FlockDao flockDao);

    DistribDao getDistribDao();

    void setDistribDao(DistribDao distribDao);

    FeedDao getFeedDao();

    void setFeedDao(FeedDao feedDao);

    FeedTypeDao getFeedTypeDao();

    void setFeedTypeDao(FeedTypeDao feedTypeDao);

    FuelDao getFuelDao();

    void setFuelDao(FuelDao fuelDao);

    GasDao getGasDao();

    void setGasDao(GasDao gasDao);

    LaborDao getLaborDao();

    void setLaborDao(LaborDao laborDao);

    MedicineDao getMedicineDao();

    void setMedicineDao(MedicineDao medicineDao);

    SpreadDao getSpreadDao();

    void setSpreadDao(SpreadDao spreadDao);

    TransactionDao getTransactionDao();

    void setTransactionDao(TransactionDao transactionDao);

    WorkerDao getWorkerDao();

    void setWorkerDao(WorkerDao workerDao);
}


