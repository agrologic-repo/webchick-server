package com.agrologic.app.gui.rxtx;

import javax.swing.*;
import java.io.File;

/**
 * Created by Valery on 1/20/14.
 */
public class FileChooser extends JFileChooser {
    public FileChooser() {
        setFileFilter(new ExcelFileFilter());
//        String path = configurationManager.getProperty(ApplicationConfigurationConstant.LAST_LOAD_LOCATION);
//        setCurrentDirectory(new File(path));
    }

    /**
     * Get file extension of selected file if it exist
     *
     * @param file the selected file
     * @return extension if exist otherwise null
     */
    public String getExtension(File file) {
        String extension = null;
        String s = file.getName();
        int i = s.lastIndexOf('.');
        if (i > 0 && i < s.length() - 1) {
            extension = s.substring(i + 1).toLowerCase();
        }
        return extension;
    }

    /**
     * @see #getSelectedFile
     */
    @Override
    public File getSelectedFile() {
        File file = super.getSelectedFile();
        if (file == null) {
            return null;
        }

        if (getExtension(file) == null) {
            if (getFileFilter() instanceof ExcelFileFilter) {
                return (new File(file.getPath() + "." + (new ExcelFileFilter()).getExtension()));
            }
        }

        return file;
    }
}
