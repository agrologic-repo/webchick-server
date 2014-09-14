/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.agrologic.app.splashscreen;


import javax.swing.*;
import java.awt.*;

public class SplashDemo extends JWindow {

    public SplashDemo() {
        EventQueue.invokeLater(new Runnable() {

            @Override
            public void run() {
                try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (UnsupportedLookAndFeelException e) {
                    e.printStackTrace();
                }
                showSplash();

            }
        });
    }

    public static void main(String[] args) {
        new SplashDemo();
    }

    public void showSplash() {

        JPanel content = (JPanel) getContentPane();
        content.setOpaque(false);
        content.setBackground(new Color(0, 0, 0, 0));

        // Set the window's bounds, centering the window
        int width = 250;
        int height = 200;
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (screen.width - width) / 2;
        int y = (screen.height - height) / 2;
        setBounds(x, y, width, height);

        // Build the splash screen
        JLabel label = new JLabel(new ImageIcon(getClass().getResource("/images/ecm12.png")));
        content.add(label, BorderLayout.NORTH);
        ImageIcon wait = new ImageIcon(getClass().getResource("/images/wait.gif"));
        content.add(new JLabel(wait), BorderLayout.SOUTH);

        // Display it
        setVisible(true);
        toFront();

        new ResourceLoader().execute();
    }

    public class ResourceLoader extends SwingWorker<Object, Object> {

        @Override
        protected Object doInBackground() throws Exception {

            // Wait a little while, maybe while loading resources
            try {
                Thread.sleep(10000);
            } catch (Exception e) {
            }

            return null;

        }

        @Override
        protected void done() {
            setVisible(false);
        }
    }
}