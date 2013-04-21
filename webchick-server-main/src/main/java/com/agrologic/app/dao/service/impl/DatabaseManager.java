package com.agrologic.app.dao.service.impl;

//~--- non-JDK imports --------------------------------------------------------
import com.agrologic.app.config.Configuration;
import com.agrologic.app.dao.DaoType;
import com.agrologic.app.dao.RemovebleDao;
import com.agrologic.app.model.*;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.apache.log4j.Logger;

public class DatabaseManager {

    private final Logger log = Logger.getLogger(DatabaseManager.class.getName());
    private DatabaseCreator databaseCreator;
    private DatabaseGeneralService databaseGeneralService;
    private DatabaseInsertion databaseInsertion;
    private DatabaseLoader databaseLoader;
    private Long userId;
    private Long cellinkId;
    private final ExecutorService executorService;

    public DatabaseManager() {
        this(DaoType.MYSQL);
    }

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
                log.trace("UserID or CellinkID argument error : userId= " + userId + "; cellinkId= " + cellinkId + ";");
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
            ((RemovebleDao)databaseGeneralService.getControllerDao()).removeFromTable();
            ((RemovebleDao)databaseGeneralService.getCellinkDao()).removeFromTable();
            ((RemovebleDao)databaseGeneralService.getUserDao()).removeFromTable();
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
        cellinkId= null;
        databaseGeneralService.closeAll();
        databaseGeneralService = null;
        databaseCreator = null;
        databaseInsertion = null;
        databaseLoader = null;
        executorService.shutdownNow();
        System.gc();
    }

    public void printLoadedDatabase() throws SQLException {
        long langId = 1;

        System.out.println("============================= U S E R ==============================");
        System.out.println(databaseLoader.getUser());

        Collection<Cellink> cellinks = databaseLoader.getUser().getCellinks();

        for (Cellink cellink : cellinks) {
            System.out.println("\t============================ C E L L I N K S ============================");
            System.out.println("\t" + cellink);

            Collection<Controller> controllers = cellink.getControllers();

            System.out.println("\t\t============================ C O N T R O L L E R S ============================");

            for (Controller controller : controllers) {
                System.out.println("\t\t" + controller);
                System.out.println("\t\t============================ P R O G R A M ============================");

                Program program = controller.getProgram();

                System.out.println("\t\t" + program);

                Collection<Screen> screenList = program.getScreens();

                for (Screen screen : screenList) {
                    System.out.println("\t\t============================ S C R E E N ============================");
                    System.out.println("\t\t" + screen);

                    Collection<Table> tableList = screen.getTables();

                    for (Table table : tableList) {
                        System.out.println("\t\t============================ T A B L E ============================");
                        System.out.println("\t\t" + table);

                        Collection<Data> dataList = table.getDataList();

                        System.out.println("\t\t============================ DATA ITEMS ============================");

                        for (Data d : dataList) {
                            System.out.println("\t\t" + d);
                        }
                    }
                }
            }

            System.out.println("=====================================================================");
            System.out.println("");
        }
    }

    public static void main(String[] args) throws SQLException {
        DatabaseManager dbManager = new DatabaseManager(DaoType.DERBY);
        List<Data> dataList = (List<Data>) dbManager.getDatabaseGeneralService().getDataDao().getAllWithTranslation();
        dataList.size();
        for (Data d : dataList) {
            System.out.print(d);
            System.out.print(" " + d.getLangId());
            System.out.println("" + d.getUnicodeLabel());
        }
    }
}
