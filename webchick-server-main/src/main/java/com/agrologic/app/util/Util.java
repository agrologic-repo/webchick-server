/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.agrologic.app.util;

import com.agrologic.app.gui.WCSWindow;

import java.io.File;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

/**
 * {Insert class description here}
 *
 * @author Valery Manakhimov
 * @author $Author: nbweb $, (this version)
 * @version $Revision: 1.1.1.1 $
 * @since Build {insert version here} (MM YYYY)
 */
public class Util {

    public static void restartApplication(String[] args) throws URISyntaxException, IOException {
        final String javaBin = System.getProperty("java.home") + File.separator + "bin" + File.separator + "java";
        final File currentJar = new File(Util.class.getProtectionDomain().getCodeSource().getLocation().toURI());

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


    public static void restartApplications() throws IOException {
        List<String> arguments = ManagementFactory.getRuntimeMXBean().getInputArguments();
        List<String> fullRestart = new ArrayList<String>();
        fullRestart.add(System.getProperty("java.home") + "/bin/java");
        fullRestart.addAll(arguments);
        fullRestart.add("-cp");
        fullRestart.add(System.getProperty("java.class.path"));
        // Assuming that 'Application' contains the main method:
        fullRestart.add(WCSWindow.class.getName());
        ProcessBuilder pb = new ProcessBuilder(fullRestart);
        pb.directory(new File(".").getParentFile());
        System.out.println("Starting app - arguments: " + fullRestart);
        pb.start();

    }

    public static void resetApplication(String[] args) throws IOException {
        StringBuilder cmd = new StringBuilder();
        cmd.append(System.getProperty("java.home") + File.separator + "bin" + File.separator + "java ");
        for (String jvmArg : ManagementFactory.getRuntimeMXBean().getInputArguments()) {
            cmd.append(jvmArg + " ");
        }
        cmd.append("-cp ").append(ManagementFactory.getRuntimeMXBean().getClassPath()).append(" ");
        cmd.append(Util.class.getName()).append(" ");
        for (String arg : args) {
            cmd.append(arg).append(" ");
        }
        Runtime.getRuntime().exec(cmd.toString());
        System.exit(0);
    }

    public static void restartApplicationss() throws URISyntaxException, IOException {
        final String javaBin = System.getProperty("java.home") + File.separator + "bin" + File.separator + "java";
        final File currentJar = new File(WCSWindow.class.getProtectionDomain().getCodeSource().getLocation().toURI());

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


    public static File getJarFile(Class main) {
        try {
            File f = new File(main.getProtectionDomain().getCodeSource().getLocation().toURI());
            return f;
        } catch (URISyntaxException ex) {
            return null;
        }
    }

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

    public static void main(String[] args) throws URISyntaxException, IOException {
        resetApplication(args);
    }
}
