package com.agrologic.app.dao.service.impl;

import com.agrologic.app.dao.service.DatabaseAccessor;
import com.agrologic.app.dao.service.DatabaseInsertable;
import com.agrologic.app.dao.service.DatabaseLoadAccessor;
import com.agrologic.app.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.Collection;

public class DatabaseInsertion implements DatabaseInsertable {

    private DatabaseAccessor dba;
    private DatabaseLoadAccessor dla;
    private final Logger logger = LoggerFactory.getLogger(DatabaseInsertion.class);

    public DatabaseInsertion(DatabaseAccessor dba, DatabaseLoadAccessor dla) {
        this.dba = dba;
        this.dla = dla;
    }

    @Override
    public void insertLoadedData() {
        logger.info(" Start inserting data into the tables .");
        Long langId = dla.getLangId();
        try {
            dba.getLanguageDao().insert(dla.getLanguages());
            logger.info("the data inserted successfully to the language table");
            dba.getAlarmDao().insert(dla.getAlarms());
            dba.getAlarmDao().insertTranslation(dla.getAlarms());
            logger.info("the data inserted successfully to the alarm and alarmbylanguage table");
            dba.getRelayDao().insert(dla.getRelays());
            dba.getRelayDao().insertTranslation(dla.getRelays());
            logger.info("the data inserted successfully to the relays and relaysbylanguage table");
            dba.getSystemStateDao().insert(dla.getSystemStates());
            dba.getSystemStateDao().insertTranslation(dla.getSystemStates());
            logger.info("the data inserted successfully to the systemStates and systemstatesbylanguage table");
            dba.getActionSetDao().insert(dla.getActionSets());
            dba.getActionSetDao().insertTranslation(dla.getActionSets());
            logger.info("the data inserted successfully to the systemStates and actionsetbylanguage table");

            dba.getDataDao().insert(dla.getDataTable());
            dba.getDataDao().insertTranslation(dla.getDataTable());
            logger.info("the data inserted successfully to the datatable and databylanguage table");
            dba.getUserDao().insert(dla.getUser());
            logger.info("the data inserted successfully to the users table");

            for (Cellink cellink : dla.getUser().getCellinks()) {
                insertNewCellink(cellink);
                logger.info("the data inserted successfully to the cellinks table");
                Collection<Controller> controllers = cellink.getControllers();
                for (Controller controller : controllers) {
                    insertNewController(controller);
                    logger.info("the data inserted successfully to the controller and controllerdata table");
                }
            }

            for (Program program : dla.getPrograms()) {
                if (!dba.getProgramDao().isProgramWithGivenIdExist(program.getId())) {
                    dba.getProgramDao().insert(program);
                    logger.info("the data inserted successfully to the program table");
                    dba.getProgramAlarmDao().insert(program.getProgramAlarms());
                    logger.info("the program alarms inserted successfully to the programalarm table");
                    dba.getProgramRelayDao().insert(program.getProgramRelays());
                    logger.info("the program relay inserted successfully to the programrelay table");
                    dba.getProgramSystemStateDao().insertProgramSystemState(program.getProgramSystemStates());
                    logger.info("the program system state inserted successfully to the programsysstate table");

                    dba.getProgramActionSetDao().insertProgramActionSetList(program.getProgramActionSet());
                    logger.info("the program action buttons inserted successfully to the programactionset table");

                    Collection<Screen> screenList = program.getScreens();
                    dba.getScreenDao().insert(screenList);
                    logger.info("the data inserted successfully to the screens table");
                    dba.getScreenDao().insertTranslation(screenList, langId);
                    logger.info("the data inserted successfully to the screenbylanguage table");

                    for (Screen screen : screenList) {
                        Collection<Table> tableList = screen.getTables();
                        dba.getTableDao().insert(tableList);
                        dba.getTableDao().insertTranslation(tableList);
                        for (Table table : tableList) {
                            Collection<Data> dataList = table.getDataList();
                            dba.getDataDao().insertTableData(table.getId(), table.getScreenId(), table.getProgramId(), dataList);
                        }
                        screen.setTables(tableList);
                    }
                    dba.getDataDao().insertSpecialList(program.getSpecialList(), program.getId(), langId);
                }
            }

            logger.info(" Finished inserting data .");
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void insertNewCellink(Cellink cellink) {
        try {
            dba.getCellinkDao().insert(cellink);
        } catch (Exception e) {
            logger.info("Cannot insert cellink to the database ", e);
        }
    }

    @Override
    public void insertNewController(Controller c) {
        try {
            dba.getControllerDao().insert(c);
            dba.getControllerDao().insertControllerDataValues(c.getId(), c.getDataValues());
        } catch (Exception e) {
            logger.info("Cannot insert controller to the database ", e);
        }
    }
}



