package com.agrologic.app.gui.wizard;

//~--- JDK imports ------------------------------------------------------------

import java.util.concurrent.Callable;

import javax.swing.JFrame;

public class WizardRunner implements Callable<Object> {

//  Wizard wizard = new Wizard();
    JFrame jframe;

    public WizardRunner(JFrame jframe) {
        this.jframe = jframe;
    }

    public static void main(String[] args) {
        Wizard wizard = new Wizard();

//      Windows.centerOnScreen(wizard.getDialog());
        wizard.getDialog().setTitle("Create Database Wizard Dialog");

//      WizardPanelDescriptor descriptor1 = new TestPanel1Descriptor();
//      wizard.registerWizardPanel(TestPanel1Descriptor.IDENTIFIER, descriptor1);
//
//      WizardPanelDescriptor descriptor2 = new TestPanel2Descriptor();
//      wizard.registerWizardPanel(TestPanel2Descriptor.IDENTIFIER, descriptor2);
//
//      WizardPanelDescriptor descriptor3 = new TestPanel3Descriptor();
//      wizard.registerWizardPanel(TestPanel3Descriptor.IDENTIFIER, descriptor3);
//
//      wizard.setCurrentPanel(TestPanel1Descriptor.IDENTIFIER);
        WizardPanelDescriptor descriptor1 = new FindUserPanelDescriptor();

        wizard.registerWizardPanel(FindUserPanelDescriptor.IDENTIFIER, descriptor1);

        WizardPanelDescriptor descriptor2 = new ChooseDirectoryPanelDescriptor();

        wizard.registerWizardPanel(ChooseDirectoryPanelDescriptor.IDENTIFIER, descriptor2);

        WizardPanelDescriptor descriptor3 = new CreateDatabasePanelDescriptor();

        wizard.registerWizardPanel(CreateDatabasePanelDescriptor.IDENTIFIER, descriptor3);
        wizard.setCurrentPanel(FindUserPanelDescriptor.IDENTIFIER);
        wizard.showModalDialog();

//      int ret = wizard.showModalDialog();

//      System.out.println("Dialog return code is (0=Finish,1=Cancel,2=Error): " + ret);
//      System.out.println("Second panel selection is: "
//              + (((FindUserPanel) descriptor2.getPanelComponent()).getRadioButtonSelected()));
        System.exit(0);
    }

    @Override
    public Object call() throws Exception {
        Wizard wizard = new Wizard();

//      Windows.centerOnScreen(wizard.getDialog());
        wizard.getDialog().setTitle("Create Database Wizard Dialog");

        WizardPanelDescriptor descriptor1 = new FindUserPanelDescriptor();

        wizard.registerWizardPanel(FindUserPanelDescriptor.IDENTIFIER, descriptor1);

        WizardPanelDescriptor descriptor2 = new ChooseDirectoryPanelDescriptor();

        wizard.registerWizardPanel(ChooseDirectoryPanelDescriptor.IDENTIFIER, descriptor2);

        WizardPanelDescriptor descriptor3 = new CreateDatabasePanelDescriptor();

        wizard.registerWizardPanel(CreateDatabasePanelDescriptor.IDENTIFIER, descriptor3);
        wizard.setCurrentPanel(FindUserPanelDescriptor.IDENTIFIER);

        int ret = wizard.showModalDialog();
        return 1;
    }
}


//~ Formatted by Jindent --- http://www.jindent.com
