/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.agrologic.app.messaging;

import java.util.*;

/**
 * {Insert class description here}
 *
 * @version $Revision: 1.1.1.1 $
 * @since Build {insert version here} (MM YYYY)
 * @author Valery Manakhimov
 * @author $Author: nbweb $, (this version)
 */
public class ActiveHistoryList {
    List active;
    Map<String,Boolean> defaultList = new TreeMap<String,Boolean>();

    public ActiveHistoryList() {
        defaultList.put("DN1", true);
        defaultList.put("DN2", true);
        defaultList.put("DN3", true);
        defaultList.put("DN4", true);
        defaultList.put("DN5", true);
        defaultList.put("DN6", true);
        defaultList.put("DN7", true);
        defaultList.put("DN8", true);
        defaultList.put("DN9", true);
        defaultList.put("DN10", true);
    }

    public void randomInit() {
        Set set = defaultList.keySet();
        Iterator iter = set.iterator();
        Random rnd = new Random();
        while(iter.hasNext()) {
            String  key = (String) iter.next();
            boolean result = rnd.nextBoolean();
            defaultList.put(key, result);
        }
    }

    public List getActive() {
        active = new ArrayList();
        Iterator iter = defaultList.keySet().iterator();
        while(iter.hasNext()) {
            String key = (String)iter.next();
            Boolean val = (Boolean) defaultList.get(key);
            if(val == true) {
                active.add(key);
            }
        }
        return active;
    }

    public void printAll() {
        for (Object o:active) {
            System.out.println(o);
        }
    }

    public static void main(String args[]) {
        ActiveHistoryList ahl = new ActiveHistoryList();
        ahl.randomInit();
        ahl.getActive();
        ahl.printAll();
    }
}
