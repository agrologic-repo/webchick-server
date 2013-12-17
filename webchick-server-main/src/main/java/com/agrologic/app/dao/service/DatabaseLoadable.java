
/*
* To change this template, choose Tools | Templates
* and open the template in the editor.
 */
package com.agrologic.app.dao.service;

/**
 * Provide loading data operation that use for creating embedded database .
 *
 * @author Valery Manakhimov
 */
public interface DatabaseLoadable {

    /**
     * Load data from database that belongs to given user id and cellink id . These data uses to build local database
     * to create screens for controllers .
     *
     * @param userId    the user id
     * @param cellinkId the cellink id
     * @throws Exception if failed to retrieve any data from database.
     */
    public void loadControllersAndProgramsByUserAndCellink(Long userId, Long cellinkId) throws Exception;
}



