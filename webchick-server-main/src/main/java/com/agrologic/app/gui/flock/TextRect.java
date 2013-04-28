/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.agrologic.app.gui.flock;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.Hashtable;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import javax.swing.JPanel;

/**
 * {Insert class description here}
 *
 * @version $Revision: 1.1.1.1 $
 * @since Build {insert version here} (MM YYYY)
 * @author Valery Manakhimov
 * @author $Author: nbweb $, (this version)
 */
public class TextRect extends JPanel {

    private static Map<Color, Number> map = new Hashtable<Color, Number>();

    @Override
    protected void paintComponent(Graphics g1) {
        super.paintComponent(g1);
        Graphics2D g = (Graphics2D) g1;

        int height = 10;
        int width  = 10;
        int offsetX = 10, offsetY = 25, i = 0;
        Set<Entry<Color, Number>> entries = map.entrySet();
        for (Entry entry : entries) {
            g.setColor((Color)entry.getKey());
            g.drawRect(0 + offsetX , offsetY + i * height , width, height);
            g.fillRect(0 + offsetX, offsetY + i * height , width, height);
            g.setColor((Color.black));
            g.drawString(((Number)entry.getValue()).toString(), width + + offsetX + 10   , offsetY + i * height + width);
            i++;
        }
    }

    public synchronized void set(Color c, Number n) {
        map.put(c, n);
        this.repaint();
    }

    public synchronized void clearSet() {
        map.clear();
        this.repaint();
    }

}
