
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.agrologic.app.dao.service;

//~--- non-JDK imports --------------------------------------------------------
import com.agrologic.app.model.*;
import java.util.Collection;

/**
 *
 * @author Administrator
 */
public interface DatabaseLoadAccessor {

    Long getLangId();

    void setLangId(Long langId);

    User getUser();

    Collection<Alarm> getAlarms();

    Collection<Data> getDataTable();

    Collection<Language> getLanguages();

    Collection<Relay> getRelays();

    Collection<SystemState> getSystemStates();

    Collection<Program> getPrograms();

    Collection<ProgramAlarm> getProgramAlarms();

    Collection<ProgramRelay> getProgramRelays();

    Collection<ProgramSystemState> getProgramSystemStates();
}



