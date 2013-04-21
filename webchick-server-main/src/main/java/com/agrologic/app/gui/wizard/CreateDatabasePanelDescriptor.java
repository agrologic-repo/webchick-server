package com.agrologic.app.gui.wizard;

//~--- non-JDK imports --------------------------------------------------------
import com.agrologic.app.dao.service.impl.DatabaseManager;
import java.sql.SQLException;

public class CreateDatabasePanelDescriptor extends WizardPanelDescriptor {

    public static final String IDENTIFIER = "CREATE_DATABASE_PANEL";
    CreateDatabasePanel panel3;

    public CreateDatabasePanelDescriptor() {
        panel3 = new CreateDatabasePanel();
        setPanelDescriptorIdentifier(IDENTIFIER);
        setPanelComponent(panel3);
    }

    @Override
    public Object getNextPanelDescriptor() {
        return FINISH;
    }

    @Override
    public Object getBackPanelDescriptor() {
        return ChooseDirectoryPanelDescriptor.IDENTIFIER;
    }

    @Override
    public void aboutToDisplayPanel() {
        panel3.setProgressValue(0);
        panel3.setProgressText("Creating embedded databseto Server...");
        getWizard().setNextFinishButtonEnabled(false);
        getWizard().setBackButtonEnabled(false);
    }

    @Override
    public void displayingPanel() {
        Thread t = new Thread() {
            @Override
            public void run() {
                try {
                    DatabaseManager dbMgr = new DatabaseManager();
                    String userId = System.getProperty("database.user.id");
                    String cellinkId = System.getProperty("database.cellink.id");

                    dbMgr.doLoadTableData(userId, cellinkId);
                    panel3.setProgressValue(5);
                    panel3.setProgressText("Loaded user database... ");
                    panel3.setProgressValue(15);
                    panel3.setProgressText("Preparing and creating database... ");
                    dbMgr.runCreateTablesTask();

                    panel3.setProgressValue(65);
                    panel3.setProgressText("Database created successfully... ");
                    Thread.sleep(1000);
                    panel3.setProgressValue(70);
                    panel3.setProgressText("Start inserting data into the tables ...");
                    Thread.sleep(1000);
                    dbMgr.runInsertLoadedData();

                    Thread.sleep(1000);
                    panel3.setProgressValue(100);
                    panel3.setProgressText("Data Successfully Created .");
                    getWizard().setNextFinishButtonEnabled(true);
                    getWizard().setBackButtonEnabled(true);
                    dbMgr.finish();
                    dbMgr = null;
                } catch (SQLException ex) {
                    ex.printStackTrace();
                } catch (InterruptedException e) {
                    panel3.setProgressValue(0);
                    panel3.setProgressText("An Error Has Occurred");
                    getWizard().setBackButtonEnabled(true);
                }
            }
        };
        t.start();
    }

    @Override
    public void aboutToHidePanel() {
        // Can do something here, but we've chosen not not.

    }
}


//~ Formatted by Jindent --- http://www.jindent.com
