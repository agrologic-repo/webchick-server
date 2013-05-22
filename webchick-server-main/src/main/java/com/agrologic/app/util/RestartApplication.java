/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.agrologic.app.util;

import com.agrologic.app.except.JarFileWasNotFound;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.jar.JarFile;

/**
 * {Insert class description here}
 *
 * @author Valery Manakhimov
 * @author $Author: nbweb $, (this version)
 * @version $Revision: 1.1.1.1 $
 * @since Build {insert version here} (MM YYYY)
 */
public class RestartApplication {

    private final static Logger logger = LoggerFactory.getLogger(RestartApplication.class);

    public static void resetUsingManagementFactoryAndRuntimeClass(String[] args) throws IOException {
        StringBuilder cmd = new StringBuilder();
        cmd.append(System.getProperty("java.home") + File.separator + "bin" + File.separator + "java ");
        for (String jvmArg : ManagementFactory.getRuntimeMXBean().getInputArguments()) {
            cmd.append(jvmArg + " ");
        }
        cmd.append("-cp ").append(ManagementFactory.getRuntimeMXBean().getClassPath()).append(" ");
        cmd.append(RestartApplication.class.getName()).append(" ");
        for (String arg : args) {
            cmd.append(arg).append(" ");
        }
        Runtime.getRuntime().exec(cmd.toString());
        System.exit(0);
    }

    public static void restartUsingFileClassAndProcessBuilder() throws JarFileWasNotFound, IOException {
        restartUsingFileClassAndProcessBuilder(RestartApplication.class);
    }

    public static void restartUsingFileClassAndProcessBuilder(Class className) throws JarFileWasNotFound {
        try {
            final String javaBin = System.getProperty("java.home") + File.separator + "bin" + File.separator + "java";
            final File applicationJarFile = getJarFileByGivenJarName(className.getName());

        /*
         * Build command: java -jar application.jar
         */
            final ArrayList<String> command = new ArrayList<String>();
            command.add(javaBin);
            command.add("-jar");
            command.add(applicationJarFile.getPath());

            final ProcessBuilder builder = new ProcessBuilder(command);
            builder.start();
            System.exit(0);
        } catch (IOException ex) {
            logger.error(JarFileWasNotFound.JAR_FILE_WAS_NOT_FOUND + " {} ", className.getName(), ex);
            throw new JarFileWasNotFound(className.getName());
        }
    }

    /**
     * @param jarName
     * @return
     * @throws IOException
     */
    public static File getJarFileByGivenJarName(String jarName) throws JarFileWasNotFound {
        String path = PropertyFileUtil.getProgramPath() + File.separator + jarName;
        logger.error("Path : {} ", path);
        JarFile jar = null;
        try {
            jar = new JarFile(path);
        } catch (IOException e) {
            throw new JarFileWasNotFound(path);
        }
        return new File(jar.getName());
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
}
