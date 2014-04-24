package com.agrologic.app.gui.rxtx;

import javax.swing.filechooser.FileFilter;
import java.io.File;

/**
 * Created by Valery on 1/20/14.
 */
public abstract class FileSuffixFilter extends FileFilter {

    final String suffix;

    public FileSuffixFilter(String suffix) {
        this.suffix = suffix;
    }

    @Override
    public boolean accept(File f) {
        return f.getName().toLowerCase().endsWith(suffix) || f.isDirectory();
    }

    public String getDescription() {
        return "Excel files (*.xls)";
    }

    public String getExtension() {
        return "xls";
    }
}