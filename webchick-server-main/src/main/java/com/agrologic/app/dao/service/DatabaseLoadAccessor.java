
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.agrologic.app.dao.service;

import com.agrologic.app.model.*;

import java.util.Collection;

public interface DatabaseLoadAccessor {

    public Long getLangId();

    public User getUser();

    public Collection<Alarm> getAlarms();

    public Collection<Data> getDataTable();

    public Collection<Language> getLanguages();

    public Collection<Relay> getRelays();

    public Collection<SystemState> getSystemStates();

    public Collection<Program> getPrograms();
}



