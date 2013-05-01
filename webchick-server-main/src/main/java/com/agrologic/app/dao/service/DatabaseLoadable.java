
/*
* To change this template, choose Tools | Templates
* and open the template in the editor.
 */
package com.agrologic.app.dao.service;

public interface DatabaseLoadable {

    void loadAllDataByUserAndCellink(Long userId, Long cellinkId) throws Exception;

    void loadControllersByUserAndCellink(long userId, long cellinkId);
}



