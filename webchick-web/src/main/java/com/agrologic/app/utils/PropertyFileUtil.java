
/*
* To change this template, choose Tools | Templates
* and open the template in the editor.
 */
package com.agrologic.app.utils;

//~--- JDK imports ------------------------------------------------------------

import java.io.*;
import java.util.Locale;
import java.util.Properties;
import java.util.Set;

/**
 * Title: PropertyFileUtil <br>
 * Decription: <br>
 * Copyright:   Copyright (c) 2008 <br>
 *
 * @version 1.0 <br>
 */
public class PropertyFileUtil {

    /**
     * Ccreates a new, empty property file named by this filename if
     * and only if a file with this name does not yet exist.
     *
     * @param fileName the property file name
     */
    public static void createPropertyFile(String fileName) {
        File propertyFile = new File(fileName);

        if (!propertyFile.exists()) {
            try {
                propertyFile.createNewFile();
            } catch (IOException ex) {
            }
        } else {
            System.out.println("Exist");
        }
    }

    /**
     * Set property key with giving value, at giving filename.
     *
     * @param fileName the property file name
     * @param comments the property comments
     * @param key      the property to set
     * @param value    the value to set
     * @return true if ok, fasle otherwise.
     */
    public static boolean setProperty(String fileName, String comments, String key, String value) {
        try {
            Properties props = new Properties();

            props.load(new FileInputStream(fileName));
            props.put(key, value);
            props.store(new FileOutputStream(new File(fileName)), comments);
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();

            return false;
        } catch (IOException ex) {
            return false;
        }

        return true;
    }

    /**
     * Get property by key from  property file.
     *
     * @param fileName the property file name
     * @param key      the property to get
     * @return value of key.
     */
    public static String getProperty(String fileName, String key) {
        String value = "";

        try {
            Properties props = new Properties();

            props.load(new FileInputStream(fileName));
            value = props.getProperty(key);
        } catch (FileNotFoundException ex) {
        } catch (IOException ex) {
        }

        return value;
    }

    /**
     * Delete property key from property file
     *
     * @param fileName the property file name
     * @param comments the comment of property file
     * @param key      the property key to delete
     * @return true if ok , false otherwise
     */
    public static boolean deleteProperty(String fileName, String comments, String key) {
        try {
            Properties props = new Properties();

            props.load(new FileInputStream(fileName));
            props.remove(key);
            props.store(new FileOutputStream(new File(fileName)), comments);
        } catch (FileNotFoundException ex) {
            return false;
        } catch (IOException ex) {
            return false;
        }

        return true;
    }

    public static void main(String[] args) {
        String path = System.getProperty("user.home");
        Properties prop = System.getProperties();

        System.out.print("The path : ");
        System.out.print(path);

        Set<String> propNames = prop.stringPropertyNames();

        System.out.println("\n======================================================\t");

        for (String pn : propNames) {
            System.out.print(pn + "\t");
            System.out.println(prop.getProperty(pn));
        }

        Locale locale = Locale.US;
        String fileName = "GraphData_" + locale.toString() + ".properties";
        String p = "../ggggggggggggg.properties";

        PropertyFileUtil.createPropertyFile(p);
    }
}



