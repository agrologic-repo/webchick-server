package com.agrologic.app.util;

import com.agrologic.app.gui.rxtx.WCSLWindow;

import java.io.File;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

/**
 * Created with IntelliJ IDEA.
 * User: Valery
 * Date: 4/21/13
 * Time: 4:23 PM
 * To change this template use File | Settings | File Templates.
 */
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

    public static void restartApplications() throws IOException {
        List<String> arguments = ManagementFactory.getRuntimeMXBean().getInputArguments();
        List<String> fullRestart = new ArrayList<String>();
        fullRestart.add(System.getProperty("java.home") + "/bin/java");
        fullRestart.addAll(arguments);
        fullRestart.add("-cp");
        fullRestart.add(System.getProperty("java.class.path"));
        // Assuming that 'Application' contains the main method:
        fullRestart.add(WCSLWindow.class.getName());
        ProcessBuilder pb = new ProcessBuilder(fullRestart);
        pb.directory(new File(".").getParentFile());
        System.out.println("Starting app - arguments: " + fullRestart);
        pb.start();

    }
    public static void restartApplicationss() throws URISyntaxException, IOException {
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
