package com.agrologic.app.dao.service.impl;

import com.agrologic.app.config.Configuration;
import com.agrologic.app.dao.service.DatabaseAccessor;
import com.agrologic.app.dao.service.DatabaseLoadAccessor;
import com.agrologic.app.dao.service.DatabaseLoadable;
import com.agrologic.app.exception.DatabaseNotFound;
import com.agrologic.app.exception.WrongDatabaseException;
import com.agrologic.app.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class DatabaseLoader implements DatabaseLoadable, DatabaseLoadAccessor {

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
    private Collection<ActionSet> actionSets;

    public DatabaseLoader(DatabaseAccessor dba) {
        setDatabaseAccessor(dba);
        programs = new HashSet<Program>();
        logger = LoggerFactory.getLogger(DatabaseLoader.class);
    }

    public void setDatabaseAccessor(DatabaseAccessor dba) {
        this.dba = dba;
    }

    public DatabaseAccessor getDatabaseAccessor() { // added 13/09/2017
        return dba;
    } // added 13/09/2017

    @Override
    public void loadControllersAndProgramsByUserAndCellink(Long userId, Long cellinkId) throws WrongDatabaseException,
            DatabaseNotFound {
        try {
            user = dba.getUserDao().getById(userId);
            logger.info("User exist : ", user);
            if (user.getValidate() == false) {
                Collection<User> users = dba.getUserDao().getAll();
                if (users.size() > 0) {
                    User u = users.iterator().next();
                    logger.info("The User ID in current database is {} ", u.getId());
                }
                logger.error("Wrong database User ID not found .");
                throw new WrongDatabaseException();
            }
        } catch (WrongDatabaseException e) {
            logger.error(e.getMessage(), e);
            throw new WrongDatabaseException();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new DatabaseNotFound();
        }

        try {
            // load common data that not depend on user id and cellink id .
            languages = dba.getLanguageDao().geAll();
            dataTable = dba.getDataDao().getAllWithTranslation();//
            alarms = dba.getAlarmDao().getAllWithTranslation();
            relays = dba.getRelayDao().getAllWithTranslation(); //
            systemStates = dba.getSystemStateDao().getAllWithTranslation();
            // it should run also with old version of database
            // that for this try and catch used for
            try {
                actionSets = dba.getActionSetDao().getAllWithTranslation();
            } catch (Exception e) {
                logger.error("Error during loading action set data . This is the old version of database ");
            }


            Configuration config = new Configuration();
            String language = config.getLanguage();
            for (Language lang : languages) {
                if (lang.getLanguage().equals(language)) {
                    langId = lang.getId();
                    break;
                }
            }

            if (cellinkId == -1) {
                // get cellink and add to user cellink list
                Collection<Cellink> cellinks = dba.getCellinkDao().getAllUserCellinks(user.getId());
                user.setCellinks(cellinks);
            } else {
                Cellink cellink = dba.getCellinkDao().getById(cellinkId);
                user.addCellink(cellink);
            }

            for (Cellink cellink : user.getCellinks()) {
                // get controllers of cellink and add to cellink controller list
                Collection<Controller> controllers = dba.getControllerDao().getActiveCellinkControllers(cellink.getId());
                cellink.setControllers(controllers);

                for (Controller controller : controllers) {
                    // get controller program and add to controller object
                    Program program = dba.getProgramDao().getById(controller.getProgramId());
                    if (!programs.contains(program)) {
                        logger.info("program {} not exist in set of programs ", program.getName());
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

                        try {

                            program.setProgramActionSet((List) dba.getProgramActionSetDao()
                                    .getAllOnScreen(controller.getProgramId(), langId));
                        } catch (Exception e) {
                            logger.error("Error during loading program action set data . This is the old version of database ");
                        }

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

                        List<Data> specialData = (List<Data>) dba.getDataDao().getSpecialData(controller.getProgramId(), langId);
                        program.setSpecialList(specialData);

                        controller.setProgram(program);
                        programs.add(program);
                    } else {
                        logger.info("program is exist in set of programs ");
                        for (Program p : programs) {
                            if (p.getId().equals(controller.getProgramId())) {
                                controller.setProgram(p);
                            }
                        }
                    }

                    Collection<Data> dataList = dba.getDataDao().getControllerDataValues(controller.getId());
                    Map<Long, Data> dataValues = new HashMap<Long, Data>();
                    for (Data d : dataList) {
                        dataValues.put(d.getId(), d);//////////////////////////////////////////////////////////////////////////////////////////////////
                    }
                    controller.initOnlineData(dataValues);
                }
            }
            logger.info(" Controller data was successfully loaded .");
        } catch (Exception e) {
            logger.info(e.getMessage(), e);
            throw new WrongDatabaseException();
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
    public Collection<ActionSet> getActionSets() {
        return actionSets;
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
}
