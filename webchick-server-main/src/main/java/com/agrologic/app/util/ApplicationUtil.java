package com.agrologic.app.util;

import com.agrologic.app.exception.RestartApplicationException;
import com.agrologic.app.gui.Application;

import javax.swing.*;
import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class ApplicationUtil {
    /**
     * Not recommended. Test should not sleep , but should wait for some condition instead.
     *
     * @param milliseconds time to sleep in milliseconds
     */
    public static void sleep(long milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Not recommended. Test should not sleepMilliSeconds, but should wait for some condition instead.
     *
     * @param milliseconds Time to milliseconds in milliseconds
     */
    public static void sleepMilliSeconds(long milliseconds) {

        try {
            Thread.sleep(TimeUnit.MICROSECONDS.toMillis(milliseconds));
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Not recommended. Test should not sleepMilliSeconds, but should wait for some condition instead.
     *
     * @param milliseconds Time to milliseconds in seconds
     */
    public static void sleepSeconds(long milliseconds) {

        try {
            Thread.sleep(TimeUnit.SECONDS.toMillis(milliseconds));
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Return <code>ImageIcon</code> object with loaded icon from resource
     *
     * @param name the icon name
     * @return icon
     */
    public static ImageIcon getIcon(String name) {
        ImageIcon icon = new javax.swing.ImageIcon(Application.class.getResource("/images/" + name));
        return icon;
    }

    /**
     * Restart application . Class name is the name of application .
     *
     * @param className the application name
     * @throws RestartApplicationException
     */
    public static void restartApplication(Class className) throws RestartApplicationException {
        try {
            final String javaBin = System.getProperty("java.home") + File.separator + "bin" + File.separator + "java";
            final File currentJar = new File(className.getProtectionDomain().getCodeSource().getLocation().toURI());

            /*
             * is it a jar file?
             */
            if (!currentJar.getName().endsWith(".jar")) {
                return;
            }

            /*
             * Build command: java -jar application.jar
             */
            final ArrayList<String> command = new ArrayList<String>();
            command.add(javaBin);
            command.add("-jar");
            command.add(currentJar.getPath());

            final ProcessBuilder builder = new ProcessBuilder(command);
            builder.start();
            System.exit(0);
        } catch (Exception e) {
            throw new RestartApplicationException(className.getName());
        }
    }
}
