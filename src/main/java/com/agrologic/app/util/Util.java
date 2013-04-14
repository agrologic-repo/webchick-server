/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.agrologic.app.util;

import com.agrologic.app.gui.ConfigurationDialog;
import com.agrologic.app.gui.WCSWindow;
import com.agrologic.app.gui.rxtx.WCSLWindow;
import java.io.File;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

/**
 * {Insert class description here}
 *
 * @version $Revision: 1.1.1.1 $
 * @since Build {insert version here} (MM YYYY)
 * @author Valery Manakhimov
 * @author $Author: nbweb $, (this version)
 */
public class Util {

    private static File file;

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

    public static void restartApplication() {
        try {
            file = getJarFile(Util.class);
            Runtime.getRuntime().exec("java -jar " + file.getCanonicalPath());
            String path = new File(".").getCanonicalPath();
            Runtime.getRuntime().exec("java -jar WebchickServer.jar");
            System.exit(0);
        } catch (IOException ex) {
            java.util.logging.Logger.getLogger(WCSLWindow.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static File getJarFile(Class main) {
        try {
            File f = new File(main.getProtectionDomain().getCodeSource().getLocation().toURI());
            return f;
        } catch (URISyntaxException ex) {
            return null;
        }
    }

    public void openJARFile() {
        try {
            String path = new File(".").getCanonicalPath();
            path = path + "\\lib\\WebchickConfig.jar";
            File file = new File(path);
            URL url = file.toURL();
            URL[] urls = new URL[]{url};
            ClassLoader cl = new URLClassLoader(urls);
            Class clazz = cl.loadClass("webchickconfig.ui.ConfigurationDialog");
            ConfigurationDialog cfg = (ConfigurationDialog) clazz.newInstance();
        } catch (IOException ex) {
            java.util.logging.Logger.getLogger(WCSLWindow.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(WCSWindow.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(WCSWindow.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalArgumentException ex) {
            java.util.logging.Logger.getLogger(WCSWindow.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SecurityException ex) {
            java.util.logging.Logger.getLogger(WCSWindow.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(WCSWindow.class.getName()).log(Level.SEVERE, null, ex);
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
