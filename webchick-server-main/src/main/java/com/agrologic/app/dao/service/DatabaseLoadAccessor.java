
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.agrologic.app.dao.service;

import com.agrologic.app.model.*;
import java.util.Collection;

public interface DatabaseLoadAccessor {

    Long getLangId();

    User getUser();

    Collection<Alarm> getAlarms();

    Collection<Data> getDataTable();

    Collection<Language> getLanguages();

    Collection<Relay> getRelays();

    Collection<SystemState> getSystemStates();

    Collection<Program> getPrograms();
}



