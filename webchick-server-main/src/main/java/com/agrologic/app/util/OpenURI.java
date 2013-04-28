
/*
* To change this template, choose Tools | Templates
* and open the template in the editor.
 */
package com.agrologic.app.util;



import java.awt.Desktop;

public class OpenURI {
//    public static void main(String[] args) {
//        System.setProperty("java.classpath", "C:\\Program Files\\Java\\jdk1.7.0\\bin");
//        System.out.println(System.getProperty("java.classpath"));
//
//        if (!java.awt.Desktop.isDesktopSupported()) {
//            System.err.println("Desktop is not supported (fatal)");
//            System.exit(1);
//        }
//
//        if (args.length == 0) {
//            System.out.println("Usage: OpenURI [URI [URI ... ]]");
//            System.exit(0);
//        }
//
//        java.awt.Desktop desktop = java.awt.Desktop.getDesktop();
//
//        if (!desktop.isSupported(java.awt.Desktop.Action.BROWSE)) {
//            System.err.println("Desktop doesn't support the browse action (fatal)");
//            System.exit(1);
//        }
//
//        for (String arg : args) {
//            try {
//                java.net.URI uri = new java.net.URI(arg);
//
//                desktop.browse(uri);
//            } catch (Exception e) {
//                System.err.println(e.getMessage());
//                e.printStackTrace();
//            }
//        }
//    }

    public static void openURI(String uriString) {
        if (!java.awt.Desktop.isDesktopSupported()) {
            System.err.println("Desktop is not supported (fatal)");
            System.exit(1);
        }

        if (uriString.length() == 0) {
            System.out.println("Usage: OpenURI [URI [URI ... ]]");
            System.exit(0);
        }

        java.awt.Desktop desktop = java.awt.Desktop.getDesktop();

        if (!desktop.isSupported(java.awt.Desktop.Action.BROWSE)) {
            System.err.println("Desktop doesn't support the browse action (fatal)");
            System.exit(1);
        }

        try {
            java.net.URI uri = new java.net.URI(uriString);

            desktop.browse(uri);
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getMessage());
        }
    }
}



