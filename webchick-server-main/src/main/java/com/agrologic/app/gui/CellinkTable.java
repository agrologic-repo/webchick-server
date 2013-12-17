package com.agrologic.app.gui;

import com.agrologic.app.dao.CellinkDao;
import com.agrologic.app.dao.DaoType;
import com.agrologic.app.dao.DbImplDecider;
import com.agrologic.app.model.Cellink;
import com.agrologic.app.model.CellinkState;
import org.apache.log4j.Logger;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public final class CellinkTable extends JTable {
    private static final long serialVersionUID = 1234432112344321L;
    private JPopupMenu popupTableMenu;
    private JMenuItem startedState;
    private JMenuItem stoppedState;
    private Logger logger = Logger.getLogger(CellinkTable.class);
    private transient CellinkDao cellinkDao;

    private ScheduledExecutorService executor;

    public CellinkTable() {
        this(new CellinkTableModel());
    }

    public CellinkTable(CellinkTableModel model) {
        super(model);
        initColumnSizes();
        getTableHeader().setReorderingAllowed(false);
        setAutoCreateRowSorter(false);
        setFont(new java.awt.Font("Tahoma", Font.PLAIN, 12));
        setDefaultRenderer(CellinkState.class, new CellinkStateCellRenderer());

        popupTableMenu = new JPopupMenu();
        startedState = new JMenuItem("Start");
        startedState.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                int row = getSelectedRow();
                setState(row, CellinkState.STATE_START);
            }
        });

        popupTableMenu.add(startedState);
        stoppedState = new JMenuItem("Stop");
        stoppedState.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                int row = getSelectedRow();
                setState(row, CellinkState.STATE_STOP);
            }
        });
        popupTableMenu.add(stoppedState);
        addMouseListener(new MouseAdapter() {

            @Override
            public void mousePressed(MouseEvent e) {
                if (e.isMetaDown()) {
                    popupTableMenu.show(CellinkTable.this, e.getX(), e.getY());
                    // get the coordinates of the mouse click
                    Point p = e.getPoint();
                    // get the row index that contains that coordinate
                    int rowNumber = CellinkTable.this.rowAtPoint(p);
                    // Get the ListSelectionModel of the JTable
                    ListSelectionModel model = CellinkTable.this.getSelectionModel();
                    // set the selected interval of rows. Using the "rowNumber"
                    // variable for the beginning and end selects only that one row.
                    model.setSelectionInterval(rowNumber, rowNumber);
                }
            }
        });
    }

    /**
     * This method picks good column sizes. If all column heads are wider than the column's cells' contents, then you
     * can just use column.sizeWidthToFit().
     */
    public void initColumnSizes() {
        getColumnModel().getColumn(CellinkTableModel.COL_ID).setResizable(false);
        getColumnModel().getColumn(CellinkTableModel.COL_ID).setPreferredWidth(50);
        getColumnModel().getColumn(CellinkTableModel.COL_NAME).setResizable(false);
        getColumnModel().getColumn(CellinkTableModel.COL_NAME).setPreferredWidth(200);
        getColumnModel().getColumn(CellinkTableModel.COL_USER).setResizable(false);
        getColumnModel().getColumn(CellinkTableModel.COL_USER).setPreferredWidth(180);
        getColumnModel().getColumn(CellinkTableModel.COL_IP_ADDRESS).setResizable(false);
        getColumnModel().getColumn(CellinkTableModel.COL_IP_ADDRESS).setPreferredWidth(180);
        getColumnModel().getColumn(CellinkTableModel.COL_PORT).setResizable(false);
        getColumnModel().getColumn(CellinkTableModel.COL_PORT).setPreferredWidth(70);
        getColumnModel().getColumn(CellinkTableModel.COL_TIME).setResizable(false);
        getColumnModel().getColumn(CellinkTableModel.COL_TIME).setPreferredWidth(250);
        getColumnModel().getColumn(CellinkTableModel.COL_STATUS).setResizable(false);
        getColumnModel().getColumn(CellinkTableModel.COL_STATUS).setPreferredWidth(100);
        getColumnModel().getColumn(CellinkTableModel.COL_WITH_LOGGING).setResizable(false);
        getColumnModel().getColumn(CellinkTableModel.COL_WITH_LOGGING).setPreferredWidth(100);
    }

    public void startMonitoring() {

        if (executor == null || executor.isShutdown()) {
            /**
             * Update cellink list in CellinkTableModel task.
             */
            Runnable task = new Runnable() {

                private CellinkTableModel cellinkModel;

                public void run() {
                    try {
                        cellinkModel = ((CellinkTableModel) getModel());
                        if (cellinkModel.size() == 0) {
                            cellinkModel.addAll(retrieveCellinks());
                        } else {
                            int countCellinksInMemory = ((CellinkTableModel) getModel()).size();
                            int countCellinksInDatabase = countCellinks();

                            if (countCellinksInMemory != countCellinksInDatabase) {
                                List<Cellink> cellinkListTemp = (List<Cellink>) retrieveCellinks();
                                int length = (countCellinksInMemory < countCellinksInDatabase)
                                        ? cellinkModel.size()
                                        : cellinkListTemp.size();
                                cellinkModel.addAndRemoveAbsent(cellinkListTemp);
                                cellinkModel.setReloadChanges(length, cellinkListTemp);
                            } else {
                                List<Cellink> cellinkListTemp = (List<Cellink>) retrieveCellinks();
                                int length = (countCellinksInMemory < countCellinksInDatabase)
                                        ? cellinkModel.size()
                                        : cellinkListTemp.size();
                                cellinkModel.setReloadChanges(length, cellinkListTemp);
                            }
                        }
                        validate();
                        repaint();
                        invalidate();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            };

            executor = Executors.newSingleThreadScheduledExecutor();
            executor.scheduleAtFixedRate(task, 0, 1, TimeUnit.SECONDS);
        }
    }

    /**
     * Retrieves all cellinks from database
     *
     * @return cellink list from database or empty list
     */
    private Collection<Cellink> retrieveCellinks() {
        if (cellinkDao == null) {
            cellinkDao = DbImplDecider.use(DaoType.MYSQL).getDao(CellinkDao.class);
        }

        try {

            return cellinkDao.getAll();
        } catch (SQLException ex) {
            logger.error("Load data error: " + ex.getMessage());
            return new ArrayList<Cellink>();
        }
    }

    private synchronized int countCellinks() {
        if (cellinkDao == null) {
            cellinkDao = DbImplDecider.use(DaoType.MYSQL).getDao(CellinkDao.class);
        }
        try {
            return cellinkDao.count();

        } catch (SQLException ex) {
            logger.error("Load data error: " + ex.getMessage());
            return 0;
        }
    }

    /**
     * Set state of specified cellink by using on selected event in table
     *
     * @param row   the selected row
     * @param state the cellink state to change
     */
    public void setState(int row, int state) {
        List<Cellink> list = ((CellinkTableModel) getModel()).getData();
        for (int i = 0; i < list.size(); i++) {
            if (i == row) {
                try {
                    Cellink cellink = list.get(i);
                    cellink.setState(state);
                    cellinkDao.update(cellink);
                    list.get(i).setState(state);
                    break;
                } catch (SQLException e) {
                    JOptionPane.showMessageDialog(CellinkTable.this, "Database Error\nFor details check log file .");
                    logger.error(e);
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(CellinkTable.this, "Unknown Error\nFor details check log file .");
                    logger.error(e);
                }
            }
        }
    }

    /**
     * Return counter of current online cellink .
     *
     * @return onlineCount the online counter.
     */
    public synchronized int onlineCellinks() {
        int onlineCount = 0;
        CellinkTableModel ctb = ((CellinkTableModel) getModel());

        synchronized (ctb.getData()) {
            for (Cellink c : ctb.getData()) {
                if ((c.getState() != CellinkState.STATE_OFFLINE) && (c.getState() != CellinkState.STATE_STOP)) {
                    onlineCount++;
                }
            }
        }

        return onlineCount;
    }

    /**
     * Return counter of current total cellink .
     *
     * @return totalCellinks the total cellink .
     */
    public int totalCellinks() {
        CellinkTableModel cellinkTableModel = ((CellinkTableModel) getModel());
        synchronized (cellinkTableModel.getData()) {
            return cellinkTableModel.getData().size();
        }
    }

    public Iterator<Cellink> iterator() {
        return ((CellinkTableModel) getModel()).getData().iterator();
    }
}



