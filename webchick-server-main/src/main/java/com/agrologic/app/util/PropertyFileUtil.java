package com.agrologic.app.util;

import java.io.*;
import java.util.Properties;

public class PropertyFileUtil {

    /**
     * Creates a new, empty property file named by this filename if
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
     * @param comments the coment of property file
     * @param key      the property key to delete
     * @return true if ok, false otherwise
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

    public static String getProgramPath() {
        String currDir = System.getProperty("user.dir");
        File dir = new File(currDir);

        return dir.getAbsolutePath();
    }
}



