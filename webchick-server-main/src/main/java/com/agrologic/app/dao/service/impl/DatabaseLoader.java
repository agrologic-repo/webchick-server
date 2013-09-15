
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.agrologic.app.dao.service.impl;

import com.agrologic.app.config.Configuration;
import com.agrologic.app.dao.service.DatabaseAccessor;
import com.agrologic.app.dao.service.DatabaseLoadAccessor;
import com.agrologic.app.dao.service.DatabaseLoadable;
import com.agrologic.app.model.*;
import org.apache.log4j.Logger;

import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.CopyOnWriteArraySet;

public class DatabaseLoader implements DatabaseLoadable, DatabaseLoadAccessor, ThreadCompleteListener {

    private static volatile Integer threadCount = 0;
    private static final long DEFAULT_LANG_ID = 1;
    private Long langId = DEFAULT_LANG_ID;
    private Logger logger;
    private DatabaseAccessor dba;
    private User user;
    private Collection<Alarm> alarms;
    private Collection<Data> dataTable;
    private Collection<Language> languages;
    private Collection<Program> programs;
    private Collection<Relay> relays;
    private Collection<SystemState> systemStates;

    public DatabaseLoader(DatabaseAccessor dba) {
        setDatabaseAccessor(dba);
        programs = new HashSet<Program>();
        logger = Logger.getLogger(DatabaseLoader.class.getName());
    }

    public synchronized boolean isLoading() {
        return threadCount <= 0 ? true : false;
    }

    public void setDatabaseAccessor(DatabaseAccessor dba) {
        this.dba = dba;
    }

    public void pullDataFromConstantTables() throws SQLException {
        languages = dba.getLanguageDao().geAll();
        dataTable = dba.getDataDao().getAllWithTranslation();
        alarms = dba.getAlarmDao().getAllWithTranslation();
        relays = dba.getRelayDao().getAllWithTranslation();
        systemStates = dba.getSystemStateDao().getAllWithTranslation();
    }

    @Override
    public void loadAllDataByUserAndCellink(Long userId, Long cellinkId) throws SQLException {
        try {
            Configuration config = new Configuration();
            // load constant table data
            pullDataFromConstantTables();
            String language = config.getLanguage();
            for (Language lang : languages) {
                if (lang.getLanguage().equals(language)) {
                    langId = lang.getId();
                    break;
                }
            }

//            logger.debug("Alarm size : " + dba.getAlarmDao().getAll().size());

            user = dba.getUserDao().getById(userId);
            if (cellinkId == -1) {
                // get cellinks and add to user cellink list
                Collection<Cellink> cellinks = dba.getCellinkDao().getAllUserCellinks(user.getId());
                user.setCellinks(cellinks);
            } else {
                Cellink cellink = dba.getCellinkDao().getById(cellinkId);
                user.addCellink(cellink);
            }

            for (Cellink cellink : user.getCellinks()) {
                // get controllers of cellink and add to cellink controller list
                Collection<Controller> controllers = dba.getControllerDao().getActiveCellinkControllers(cellink.getId());
                cellink.setControllers((Collection<Controller>) controllers);
                for (Controller controller : controllers) {
                    // get controller program and add to controller object
                    Program program = dba.getProgramDao().getById(controller.getProgramId());
                    controller.setProgram(program);
                    // get program relays and add to program object
                    program.setProgramRelays((List) dba.getProgramRelayDao()
                            .getAllProgramRelays(controller.getProgramId(), langId));
                    // get program alarms and add to program object
                    program.setProgramAlarms((List) dba.getProgramAlarmDao()
                            .getAllProgramAlarms(controller.getProgramId(), langId));
                    // get program system states and add to program object
                    program.setProgramSystemStates((List) dba.getProgramSystemStateDao()
                            .getAllProgramSystemStates(controller.getProgramId(), langId));
                    // get program screens and add to program object
                    Collection<Screen> screenList = dba.getScreenDao().getAllProgramScreens(program.getId(), langId);
                    program.setScreens((List<Screen>) screenList);
                    for (Screen screen : screenList) {
                        // get screen tables and add to screen object
                        List<Table> tableList = (List<Table>) dba.getTableDao()
                                .getScreenTables(screen.getProgramId(), screen.getId(), langId, false);
                        for (Table table : tableList) {
                            // get table data and add to table object

                            List<Data> dataList = (List<Data>) dba.getDataDao()
                                    .getOnScreenDataList(
                                            table.getProgramId(),
                                            table.getScreenId(),
                                            table.getId(),
                                            langId);
                            table.setDataList(dataList);
                        }
                        screen.setTables(tableList);
                    }

                    List<Data> specialdataList = (List<Data>) dba.getDataDao()
                            .getSpecialData(controller.getProgramId(), langId);
                    program.setSpecialList(specialdataList);

                    Collection<Data> dataList = dba.getDataDao().getControllerDataValues(controller.getId());
                    Map<Long, Data> dataValues = new HashMap<Long, Data>();
                    for (Data d : dataList) {
                        dataValues.put(d.getId(), d);
                    }
                    controller.initOnlineData(dataValues);
                    controller.setProgram(program);
                    programs.add(program);
                }
            }
            logger.info(" Controller data was successfully loaded .");
        } catch (SQLException e) {
            // show error message
            logger.info("Error during creation dao objects and/or loading data from database .\n", e);
            throw new SQLException("Error during creation dao objects and/or loading data from database .\n", e);
        } catch (Exception e) {
            logger.info("Error during creation dao objects and/or loading data from database .\n", e);
            throw new SQLException("Error during creation dao objects and/or loading data from database .\n", e);
        }
    }

    private boolean skipScreen(Screen screen) {
        if (screen.getTitle().equals("Main")
                // || screen.getTitle().equals("Graphs")
                || screen.getTitle().equals("Action Buttons")) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void loadControllersByUserAndCellink(long userId, long cellinkId) {
        try {
            user = dba.getUserDao().getById(userId);
            Cellink cellink = dba.getCellinkDao().getById(cellinkId);
            Collection<Controller> controllers = dba.getControllerDao().getActiveCellinkControllers(cellink.getId());
            for (Controller controller : controllers) {
                Collection<Data> dataList = dba.getDataDao().getControllerDataValues(controller.getId());
                Map<Long, Data> dataValues = new HashMap<Long, Data>();
                for (Data d : dataList) {
                    dataValues.put(d.getId(), d);
                }
                controller.initOnlineData(dataValues);
                cellink.addController(controller);
            }
            user.addCellink(cellink);
        } catch (SQLException ex) {
            logger.error(ex);
        }
    }

    @Override
    public Collection<Alarm> getAlarms() {
        return alarms;
    }

    @Override
    public Collection<Data> getDataTable() {
        return dataTable;
    }

    @Override
    public Collection<Language> getLanguages() {
        return languages;
    }

    @Override
    public Collection<Program> getPrograms() {
        return programs;
    }

    @Override
    public Collection<Relay> getRelays() {
        return relays;
    }

    @Override
    public Collection<SystemState> getSystemStates() {
        return systemStates;
    }

    @Override
    public User getUser() {
        return user;
    }

    @Override
    public Long getLangId() {
        return langId;
    }

    public void setLangId(Long langId) {
        this.langId = langId;
    }

    @Override
    public void notifyOfThreadComplete(Thread thread) {
        threadCount--;
    }

    abstract class NotifyingThread extends Thread {

        private final Set<ThreadCompleteListener> listeners = new CopyOnWriteArraySet<ThreadCompleteListener>();

        NotifyingThread(final ThreadCompleteListener listener) {
            this.listeners.add(listener);
        }

        public final void addListener(final ThreadCompleteListener listener) {
            listeners.add(listener);
        }

        public final void removeListener(final ThreadCompleteListener listener) {
            listeners.remove(listener);
        }

        private final void notifyListeners() {
            for (ThreadCompleteListener listener : listeners) {
                listener.notifyOfThreadComplete(this);
            }
        }

        @Override
        public final void run() {
            try {
                threadCount++;
                doRun();
            } finally {
                notifyListeners();
            }
        }

        public abstract void doRun();
    }
}
