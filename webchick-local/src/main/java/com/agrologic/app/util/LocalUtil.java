package com.agrologic.app.util;

import com.agrologic.app.gui.rxtx.WCSLWindow;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;

public class LocalUtil {
    private static File file;


    /**
     * Not recommended. Test should not sleep, but should wait for some condition instead.
     *
     * @param milliseconds Time to sleep in milliseconds
     */
    public static void sleep(long milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public static void restartApplication() throws URISyntaxException, IOException {
        final String javaBin = System.getProperty("java.home") + File.separator + "bin" + File.separator + "java";
        final File currentJar = new File(WCSLWindow.class.getProtectionDomain().getCodeSource().getLocation().toURI());

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
    }

}
