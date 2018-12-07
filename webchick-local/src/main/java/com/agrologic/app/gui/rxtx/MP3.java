/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.agrologic.app.gui.rxtx;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.util.Timer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;

/**
 *
 * @author kristina
 */
public class MP3 {
    private String filename;
    private Player player; 
    private Thread thread;
    
    // constructor that takes the name of an MP3 file
    public MP3(String filename) {
        this.filename = filename;
    }

    public void close() { if (player != null) player.close(); }

    // play the MP3 file to the sound card
    public void play() {
        try {
            FileInputStream fis = new FileInputStream(filename);
            BufferedInputStream bis = new BufferedInputStream(fis);
            player = new Player(bis);
        }
        catch (Exception e) {
            System.out.println("Problem playing file " + filename);
            System.out.println(e);
        }
//        try {
//            player.play();
//        } catch (JavaLayerException ex) {
//            Logger.getLogger(MP3.class.getName()).log(Level.SEVERE, null, ex);
//        }
        
//                    run in new thread to play in background
            new Thread() {
                public void run() {
                    try { 
                        player.play();
//                        Timer timer = new Timer();
//                        timer.wait(50000);
                    }
                    catch (Exception e) { System.out.println(e); }
                }
            }.start();
    }
}
