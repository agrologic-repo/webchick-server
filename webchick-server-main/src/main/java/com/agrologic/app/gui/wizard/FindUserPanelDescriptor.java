
/*
* To change this template, choose Tools | Templates
* and open the template in the editor.
 */
package com.agrologic.app.gui.wizard;

//~--- JDK imports ------------------------------------------------------------

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.SwingUtilities;

/**
 * {Insert class description here}
 *
 * @version $Revision: 1.1.1.1 $
 * @since Build {insert version here} (MM YYYY)
 * @author Valery Manakhimov
 * @author $Author: nbweb $, (this version)
 */
public class FindUserPanelDescriptor extends WizardPanelDescriptor implements KeyListener, ActionListener {
    public static final String IDENTIFIER = "FIND_USER_PANEL";
    private FindUserPanel      findUserPanel;

    public FindUserPanelDescriptor() {
        findUserPanel = new FindUserPanel();
        findUserPanel.addTextChangedActionListener(this);
        findUserPanel.addFindUserActionListener(this);
        findUserPanel.addSelectCellinkActionListener(this);
        setPanelDescriptorIdentifier(IDENTIFIER);
        setPanelComponent(findUserPanel);
    }

    @Override
    public Object getNextPanelDescriptor() {
        return ChooseDirectoryPanelDescriptor.IDENTIFIER;
    }

    @Override
    public Object getBackPanelDescriptor() {
        return null;
    }

    @Override
    public void aboutToDisplayPanel() {
        setNextButtonAccordingUserIDText();
    }

    @Override
    public void aboutToHidePanel() {
        System.setProperty("database.user.id", findUserPanel.getUserId());
        System.setProperty("database.cellink.id", findUserPanel.getCellinkId());
    }

    @Override
    public void keyTyped(KeyEvent e) {

        // throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void keyPressed(KeyEvent e) {

        // throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void keyReleased(KeyEvent e) {
        setNextButtonAccordingFindUserAndCellink();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        setNextButtonAccordingFindUserAndCellink();
    }

    private void setNextButtonAccordingUserIDText() {
        FindUserPanel component = (FindUserPanel) getPanelComponent();
        if (component.isTextEmpty()) {
            getWizard().setNextFinishButtonEnabled(false);
        } else {
            getWizard().setNextFinishButtonEnabled(true);
        }
    }

    private void setNextButtonAccordingFindUserAndCellink() {
        final FindUserPanel component = (FindUserPanel) getPanelComponent();

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                if (component.getFindProccessProgress() == FindUserPanel.FINISH_FIND) {
                    if ((component.isUserExist() == Boolean.TRUE) && component.cellinkSelected()) {
                        getWizard().setNextFinishButtonEnabled(true);
                    } else {
                        getWizard().setNextFinishButtonEnabled(false);
                    }
                }
            }
        });
    }
}


//~ Formatted by Jindent --- http://www.jindent.com
