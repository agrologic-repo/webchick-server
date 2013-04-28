
/*
* To change this template, choose Tools | Templates
* and open the template in the editor.
 */
package com.agrologic.app.dao.service;

/**
 *
 * @author Administrator
 */
public interface DatabaseLoadable {

//    void loadAllDataByUser(Long userId);

    void loadAllDataByUserAndCellink(Long userId, Long cellinkId) throws Exception;

    void loadControllersByUserAndCellink(long userId, long cellinkId);

    void loadOtherScreens(Long userId, Long cellinkId);

    //    void loadMainScreens(Long userId, Long cellinkId);
}



