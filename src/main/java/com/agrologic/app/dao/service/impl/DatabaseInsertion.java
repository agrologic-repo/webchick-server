
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.agrologic.app.dao.service.impl;

//~--- non-JDK imports --------------------------------------------------------
import com.agrologic.app.dao.service.DatabaseAccessor;
import com.agrologic.app.dao.service.DatabaseInsertable;
import com.agrologic.app.dao.service.DatabaseLoadAccessor;
import com.agrologic.app.model.*;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Collection;
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
public class DatabaseInsertion implements DatabaseInsertable {

    private final Logger log = Logger.getLogger(DatabaseInsertion.class.getName());
    private DatabaseAccessor dba;
    private DatabaseLoadAccessor dla;
    private Long langId;

    public DatabaseInsertion(DatabaseAccessor dba, DatabaseLoadAccessor dla) {
        this.dba = dba;
        this.dla = dla;
    }

    @Override
    public void insertLoadedData() {
        log.info(" Start inserting data into the tables .");
        langId = dla.getLangId();
        try {
            dba.getLanguageDao().insert(dla.getLanguages());
            log.info("the data inserted successfully to the language table");
            dba.getAlarmDao().insert(dla.getAlarms());
            dba.getAlarmDao().insertTranslation(dla.getAlarms());
            log.info("the data inserted successfully to the alarm and alarmbylanguage table");
            dba.getRelayDao().insert(dla.getRelays());
            dba.getRelayDao().insertTranslation(dla.getRelays());
            log.info("the data inserted successfully to the relays and relaysbylanguage table");
            dba.getSystemStateDao().insert(dla.getSystemStates());
            dba.getSystemStateDao().insertTranslation(dla.getSystemStates());
            log.info("the data inserted successfully to the systemStates and systemstatesbylanguage table");
            dba.getDataDao().insert(dla.getDataTable());
            dba.getDataDao().insertTranslation(dla.getDataTable());
            log.info("the data inserted successfully to the datatable and databylanguage table");
            dba.getUserDao().insert(dla.getUser());
            log.info("the data inserted successfully to the users table");

            for (Cellink cellink : dla.getUser().getCellinks()) {
                insertNewCellink(cellink);
                log.info("the data inserted successfully to the cellinks table");
                List<Controller> controllers = cellink.getControllers();
                for (Controller controller : controllers) {
                    insertNewController(controller);
                    log.info("the data inserted successfully to the controller and controllerdata table");
                }
            }

            for (Program program : dla.getPrograms()) {
                if (!dba.getProgramDao().programExist(program.getId())) {
                    dba.getProgramDao().insert(program);
                    log.info("the data inserted successfully to the program table");
                    dba.getAlarmDao().insertProgramAlarms(program.getProgramAlarms());
                    log.info("the program alarms inserted successfully to the programalarm table");
                    dba.getRelayDao().insertProgramRelays(program.getProgramRelays());
                    log.info("the program relay inserted successfully to the programrelay table");
                    dba.getSystemStateDao().insertProgramSystemState(program.getProgramSystemStates());
                    log.info("the program system state inserted successfully to the programsysstate table");

                    Collection<Screen> screenList = program.getScreens();
                    dba.getScreenDao().insert(screenList);
                    log.info("the data inserted successfully to the screens table");
                    dba.getScreenDao().insertTranslation(screenList, langId);
                    log.info("the data inserted successfully to the screenbylanguage table");

                    for (Screen screen : screenList) {
                        List<Table> tableList = screen.getTables();
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

            log.info(" Finished inserting data .");
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
    }

    public void insertNewUser() {
        try {
            dba.getUserDao().insert(dla.getUser());
            for (Cellink cellink : dla.getUser().getCellinks()) {
                insertNewCellink(cellink);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void insertNewCellink() {
        Cellink newCellink = new Cellink();
        newCellink.setId((long) 1);
        newCellink.setName("yatir67");
        newCellink.setPassword("paul");
        newCellink.setUserId((long) 1);
        newCellink.setTime(new Timestamp(System.currentTimeMillis()));
        newCellink.setPort(8800);
        newCellink.setIp("0.0.0.0");
        newCellink.setState(0);
        newCellink.setScreenId((long) 1);
        newCellink.setActual(true);
        try {
            dba.getCellinkDao().insert(newCellink);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void insertNewCellinks() {
        for (Cellink cellink : dla.getUser().getCellinks()) {
            insertNewCellink(cellink);
        }
    }

    public void insertNewCellink(Cellink cellink) {
        try {
            dba.getCellinkDao().insert(cellink);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void insertNewControllers() {
        for (Cellink cellink : dla.getUser().getCellinks()) {
            List<Controller> controllers = cellink.getControllers();

            for (Controller c : controllers) {
                insertNewController(c);
            }
        }
    }

    public void insertNewController(Controller c) {
        try {
            dba.getControllerDao().insert(c);
        } catch (Exception e) {
            log.info(e);
        }
    }
}


//~ Formatted by Jindent --- http://www.jindent.com
