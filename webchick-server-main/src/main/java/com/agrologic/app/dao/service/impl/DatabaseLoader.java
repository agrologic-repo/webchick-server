
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.agrologic.app.dao.service.impl;

import com.agrologic.app.config.Configuration;
import com.agrologic.app.dao.TableDao;
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
    private Logger log;
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
        log = Logger.getLogger(DatabaseLoader.class.getName());
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
                }
            }

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
                cellink.setControllers((List<Controller>) controllers);
                for (Controller controller : controllers) {
                    // get controller program and add to controller object
                    Program prog = dba.getProgramDao().getById(controller.getProgramId());
                    controller.setProgram(prog);
                    // get program relays and add to program object
                    prog.setProgramRelays((List) dba.getRelayDao().getSelectedProgramRelays(controller.getProgramId(), langId));
                    // get program alarms and add to program object
                    prog.setProgramAlarms((List) dba.getAlarmDao().getSelectedProgramAlarms(controller.getProgramId(), langId));
                    // get program system states and add to program object
                    prog.setProgramSystemStates((List) dba.getSystemStateDao().getAllProgramSystemStates(controller.getProgramId(), langId));
                    // get program screens and add to program object
                    Collection<Screen> screenList = dba.getScreenDao().getAllProgramScreens(controller.getProgramId(), langId);
                    prog.setScreens((List<Screen>) screenList);
                    for (Screen screen : screenList) {
                        // get screen tables and add to screen object
                        List<Table> tableList = (List<Table>) dba.getTableDao().getAllScreenTables(controller.getProgramId(),
                                screen.getId(), langId, TableDao.SHOW_CHECKED);
                        for (Table table : tableList) {
                            // get table data and add to table object
                            List<Data> dataList = (List<Data>) dba.getDataDao().getOnScreenDataList(controller.getProgramId(), screen.getId(), table.getId(), langId);
                            table.setDataList(dataList);
                        }
                        screen.setTables(tableList);
                    }
                    List<Data> specialdataList = (List<Data>) dba.getDataDao().getSpecialData(controller.getProgramId(), langId);
                    prog.setSpecialList(specialdataList);

                    Collection<Data> dataList = dba.getDataDao().getControllerDataValues(controller.getId(), controller.getProgramId());
                    Map<Long, Data> dataValues = new HashMap<Long, Data>();
                    for (Data d : dataList) {
                        dataValues.put(d.getId(), d);
                    }
                    controller.initOnlineData(dataValues);
                    controller.setProgram(prog);
                    programs.add(prog);
                }
            }
            log.info(" Controller data was successfully loaded .");
        } catch (SQLException e) {
            // show error message
            log.info("Error during creation dao objects and/or loading data from database .\n", e);
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
            List<Controller> controllers =
                    (List<Controller>) dba.getControllerDao().getActiveCellinkControllers(cellink.getId());
            for (Controller controller : controllers) {
                Collection<Data> dataList = dba.getDataDao().getControllerDataValues(controller.getId(),
                        controller.getProgramId());
                Map<Long, Data> dataValues = new HashMap<Long, Data>();
                for (Data d : dataList) {
                    dataValues.put(d.getId(), d);
                }
                controller.initOnlineData(dataValues);
                cellink.addController(controller);
            }
            user.addCellink(cellink);
        } catch (SQLException ex) {
            log.error(ex);
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
        System.out.println("Threads in system" + threadCount);
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
                System.out.println("Threads in system : " + threadCount);
                doRun();
            } finally {
                notifyListeners();
            }
        }

        public abstract void doRun();
    }
}
