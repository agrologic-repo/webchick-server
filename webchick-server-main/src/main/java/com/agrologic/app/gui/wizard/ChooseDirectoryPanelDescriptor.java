
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.agrologic.app.gui.wizard;

import java.io.File;

/**
 * {Insert class description here}
 *
 * @version $Revision: 1.1.1.1 $
 * @since Build {insert version here} (MM YYYY)
 * @author Valery Manakhimov
 * @author $Author: nbweb $, (this version)
 */
public class ChooseDirectoryPanelDescriptor extends WizardPanelDescriptor {

    public static final String IDENTIFIER = "CHOOSE_DIR_PANEL";
    private ChooseDirectoryPanel createDatabasePanel;

    public ChooseDirectoryPanelDescriptor() {
        createDatabasePanel = new ChooseDirectoryPanel();
        setPanelDescriptorIdentifier(IDENTIFIER);
        setPanelComponent(createDatabasePanel);
    }

    @Override
    public Object getNextPanelDescriptor() {
        return CreateDatabasePanelDescriptor.IDENTIFIER;
    }

    @Override
    public Object getBackPanelDescriptor() {
        return FindUserPanelDescriptor.IDENTIFIER;
    }

    @Override
    public void aboutToDisplayPanel() {
        super.aboutToDisplayPanel();
    }

    @Override
    public void aboutToHidePanel() {
        // check if database directory already exist
        String path = createDatabasePanel.getDatabaseDir();
        path += "\\agrodb";
        delete(new File(path));
        setDatabaseDir(createDatabasePanel.getDatabaseDir());
    }

    public void setDatabaseDir(String dir) {
        System.setProperty("derby.system.home", dir);
    }

    public void delete(File f) {
        if (f.isDirectory()) {
            for (File c : f.listFiles()) {
                delete(c);
            }
        }
        if (!f.delete()) {
            System.err.println("Failed to delete file: " + f);
        }
    }
}


//~ Formatted by Jindent --- http://www.jindent.com
