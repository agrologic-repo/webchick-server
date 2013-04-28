
/*
* To change this template, choose Tools | Templates
* and open the template in the editor.
 */
package com.agrologic.app.gui;

//~--- non-JDK imports --------------------------------------------------------

import java.awt.Color;
import java.awt.Image;
import java.awt.Toolkit;
import java.io.StringWriter;
import java.net.URL;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.StringTokenizer;
import javax.swing.ImageIcon;
import javax.swing.JTextPane;
import javax.swing.text.*;
import org.apache.log4j.*;
import org.apache.log4j.helpers.QuietWriter;
import org.apache.log4j.spi.LoggingEvent;
import org.apache.log4j.spi.ThrowableInformation;

/**
 * <b>Experimental</b> TextPaneAppender. <br> Created: Sat Feb 26 18:50:27 2000 <br>
 *
 * @author Sven Reimers
 */
public class TextPaneAppender extends AppenderSkeleton {
    final String           COLOR_OPTION_BACKGROUND = "Color.Background";
    final String           COLOR_OPTION_DEBUG      = "Color.Debug";
    final String           COLOR_OPTION_ERROR      = "Color.Error";
    final String           COLOR_OPTION_FATAL      = "Color.Emerg";
    final String           COLOR_OPTION_INFO       = "Color.Info";
    final String           COLOR_OPTION_WARN       = "Color.Warn";
    final String           FANCY_OPTION            = "Fancy";
    final String           FONT_NAME_OPTION        = "Font.Name";
    final String           FONT_SIZE_OPTION        = "Font.Size";
    final String           LABEL_OPTION            = "Label";
    private PatternLayout  layout                  = new PatternLayout("%d{dd/MM/yyyy hh:mm:ss} %5p  - %m%n");
    private Hashtable      attributes;
    private StyledDocument doc;
    private boolean        fancy;
    private Hashtable      icons;
    private String         label;
    private int            maxBufSize;
    private QuietWriter    qw;
    private StringWriter   sw;
    private JTextPane      textpane;

    public TextPaneAppender() {
        super();
        maxBufSize = 32000;
        setTextPane(new JTextPane());
        createAttributes();
        createIcons();
        this.label = "";
        this.sw    = new StringWriter();
        this.qw    = new QuietWriter(getSw(), errorHandler);
        this.fancy = true;
    }

    public TextPaneAppender(String name) {
        this();

//      this.layout = layout;
        this.name = name;
        setTextPane(new JTextPane());
        createAttributes();
        createIcons();
    }

    public TextPaneAppender(Layout layout, String name, int maxBufSize) {

//      this(layout, name);
        this.maxBufSize = maxBufSize;
    }

    public static Image loadIcon(String path) {
        if (path == null) {
            throw new NullPointerException();
        }

        Image img = null;

        try {
            URL url = TextPaneAppender.class.getResource(path);
            if (url != null) {
                img = (Image) (Toolkit.getDefaultToolkit()).getImage(url);
            } else {
                // System.out.println("Unable to get image from " + path);
            }
        } catch (Exception e) {

            // System.out.println("Exception occured: " + e.getMessage() + " - " + e);
        }

        return img;
    }

    public void close() {}

    private void createAttributes() {
        setAttributes(new Hashtable());

        MutableAttributeSet att = new SimpleAttributeSet();

        getAttributes().put(Level.FATAL, att);
        StyleConstants.setFontSize(att, 14);
        att = new SimpleAttributeSet();
        getAttributes().put(Level.ERROR, att);
        StyleConstants.setFontSize(att, 14);
        att = new SimpleAttributeSet();
        getAttributes().put(Level.WARN, att);
        StyleConstants.setFontSize(att, 14);
        att = new SimpleAttributeSet();
        getAttributes().put(Level.INFO, att);
        StyleConstants.setFontSize(att, 14);
        att = new SimpleAttributeSet();
        getAttributes().put(Level.DEBUG, att);
        StyleConstants.setFontSize(att, 14);

        StyleConstants.setForeground((MutableAttributeSet) getAttributes().get(Level.FATAL), Color.red);
        StyleConstants.setForeground((MutableAttributeSet) getAttributes().get(Level.ERROR), Color.red);
        StyleConstants.setForeground((MutableAttributeSet) getAttributes().get(Level.WARN), new Color(255, 128, 64));
        StyleConstants.setForeground((MutableAttributeSet) getAttributes().get(Level.INFO), Color.blue);
        StyleConstants.setForeground((MutableAttributeSet) getAttributes().get(Level.DEBUG), Color.black);
    }

    private void createIcons() {
        setIcons(new Hashtable());

        try {
            getIcons().put(Level.FATAL, new ImageIcon(loadIcon("/icons/red.gif")));
            getIcons().put(Level.ERROR, new ImageIcon(loadIcon("/icons/red.gif")));
            getIcons().put(Level.WARN, new ImageIcon(loadIcon("/icons/yellow.gif")));
            getIcons().put(Level.INFO, new ImageIcon(loadIcon("/icons/green.gif")));
            getIcons().put(Level.DEBUG, new ImageIcon(loadIcon("/icons/black.gif")));
        } catch (NullPointerException e) {

            // System.out.println("TextPaneAppender: Unable to load icons");
        }
    }

    @Override
    public void append(LoggingEvent event) {
        String               text = null;
        ThrowableInformation ti   = event.getThrowableInformation();

        if (ti != null) {
            StringBuilder exbuf   = new StringBuilder();
            String[]      excDesc = ti.getThrowableStrRep();

            for (int i = 0; i < excDesc.length; i++) {
                exbuf.append(excDesc[i]);
                exbuf.append("\n");
            }

            text = exbuf.toString();
        } else {
            text = this.layout.format(event);
        }

        try {
            int overBufferCount = getDoc().getLength() - getMaxBufSize();

            if (overBufferCount > 0) {
                getDoc().remove(0, overBufferCount);
                getDoc().insertString(0, "<< Snip >>", (MutableAttributeSet) getAttributes().get(Level.INFO));
            }

            if (fancy) {
                getTextpane().setCaretPosition(getDoc().getLength());
                getTextpane().insertIcon((ImageIcon) getIcons().get(event.getLevel()));
            }

            getDoc().insertString(getDoc().getLength(), text,
                                  (MutableAttributeSet) getAttributes().get(event.getLevel()));
        } catch (BadLocationException badex) {

            // System.err.println(badex);
        }

        getTextpane().setCaretPosition(getDoc().getLength());
    }

    public JTextPane getTextPane() {
        return getTextpane();
    }

    private static Color parseColor(String v) {
        StringTokenizer st  = new StringTokenizer(v, ",");
        int[]           val = { 255, 255, 255, 255 };
        int             i   = 0;

        while (st.hasMoreTokens()) {
            val[i] = Integer.parseInt(st.nextToken());
            i++;
        }

        return new Color(val[0], val[1], val[2], val[3]);
    }

    private static String colorToString(Color c) {

        // alpha component emitted only if not default (255)
        String res = c.getRed() + "," + c.getGreen() + "," + c.getBlue();

        return (c.getAlpha() >= 255)
               ? res
               : res + "," + c.getAlpha();
    }

    public void setTextPane(JTextPane textpane) {
        this.setTextpane(textpane);
        this.setDoc(textpane.getStyledDocument());
    }

    private void setColor(Priority p, String v) {
        StyleConstants.setForeground((MutableAttributeSet) getAttributes().get(p), parseColor(v));
    }

    private String getColor(Priority p) {
        Color c = StyleConstants.getForeground((MutableAttributeSet) getAttributes().get(p));

        return (c == null)
               ? null
               : colorToString(c);
    }

    // ///////////////////////////////////////////////////////////////////
    // option setters and getters
    public void setLabel(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    public void setColorEmerg(String color) {
        setColor(Priority.FATAL, color);
    }

    public String getColorEmerg() {
        return getColor(Priority.FATAL);
    }

    public void setColorError(String color) {
        setColor(Priority.ERROR, color);
    }

    public String getColorError() {
        return getColor(Priority.ERROR);
    }

    public void setColorWarn(String color) {
        setColor(Priority.WARN, color);
    }

    public String getColorWarn() {
        return getColor(Priority.WARN);
    }

    public void setColorInfo(String color) {
        setColor(Priority.INFO, color);
    }

    public String getColorInfo() {
        return getColor(Priority.INFO);
    }

    public void setColorDebug(String color) {
        setColor(Priority.DEBUG, color);
    }

    public String getColorDebug() {
        return getColor(Priority.DEBUG);
    }

    public void setColorBackground(String color) {
        getTextpane().setBackground(parseColor(color));
    }

    public String getColorBackground() {
        return colorToString(getTextpane().getBackground());
    }

    public void setFancy(boolean fancy) {
        this.fancy = fancy;
    }

    public boolean getFancy() {
        return fancy;
    }

    public void setFontSize(int size) {
        Enumeration e = getAttributes().elements();

        while (e.hasMoreElements()) {
            StyleConstants.setFontSize((MutableAttributeSet) e.nextElement(), size);
        }

        return;
    }

    public int getFontSize() {
        AttributeSet attrSet = (AttributeSet) getAttributes().get(Priority.INFO);

        return StyleConstants.getFontSize(attrSet);
    }

    public void setFontName(String name) {
        Enumeration e = getAttributes().elements();

        while (e.hasMoreElements()) {
            StyleConstants.setFontFamily((MutableAttributeSet) e.nextElement(), name);
        }

        return;
    }

    public String getFontName() {
        AttributeSet attrSet = (AttributeSet) getAttributes().get(Priority.INFO);

        return StyleConstants.getFontFamily(attrSet);
    }

    @Override
    public boolean requiresLayout() {
        return true;
    }

    /**
     * @return the textpane
     */
    public JTextPane getTextpane() {
        return textpane;
    }

    /**
     * @param textpane the textpane to set
     */
    public void setTextpane(JTextPane textpane) {
        this.textpane = textpane;
    }

    /**
     * @return the doc
     */
    public StyledDocument getDoc() {
        return doc;
    }

    /**
     * @param doc the doc to set
     */
    public void setDoc(StyledDocument doc) {
        this.doc = doc;
    }

    /**
     * @return the sw
     */
    public StringWriter getSw() {
        return sw;
    }

    /**
     * @param sw the sw to set
     */
    public void setSw(StringWriter sw) {
        this.sw = sw;
    }

    /**
     * @return the qw
     */
    public QuietWriter getQw() {
        return qw;
    }

    /**
     * @param qw the qw to set
     */
    public void setQw(QuietWriter qw) {
        this.qw = qw;
    }

    /**
     * @return the attributes
     */
    public Hashtable getAttributes() {
        return attributes;
    }

    /**
     * @param attributes the attributes to set
     */
    public void setAttributes(Hashtable attributes) {
        this.attributes = attributes;
    }

    /**
     * @return the icons
     */
    public Hashtable getIcons() {
        return icons;
    }

    /**
     * @param icons the icons to set
     */
    public void setIcons(Hashtable icons) {
        this.icons = icons;
    }

    /**
     * @return the maxBufSize
     */
    public int getMaxBufSize() {
        return maxBufSize;
    }

    /**
     * @param maxBufSize the maxBufSize to set
     */
    public void setMaxBufSize(int maxBufSize) {
        this.maxBufSize = maxBufSize;
    }
}    // TextPaneAppender



