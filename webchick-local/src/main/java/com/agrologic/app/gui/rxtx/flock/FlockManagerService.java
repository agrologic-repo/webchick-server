package com.agrologic.app.gui.rxtx.flock;

import com.agrologic.app.config.Configuration;
import com.agrologic.app.dao.*;
import com.agrologic.app.model.*;

import javax.swing.*;
import java.sql.SQLException;
import java.util.*;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FlockManagerService {

    private Long cellinkId;
    private FuelDao fuelDao;
    private GasDao gasDao;
    private SpreadDao spreadDao;
    private FeedDao feedDao;
    private FeedTypeDao feedTypeDao;
    private WorkerDao workerDao;
    private LaborDao laborDao;
    private MedicineDao medicineDao;
    private TransactionDao transactionDao;
    private DistribDao distribDao;
    private DataDao dataDao;
    private FlockDao flockDao;
    private ControllerDao controllerDao;

    private List<Flock> flocks = new ArrayList<Flock>();
    private Collection<Controller> controllers = new ArrayList<Controller>();

    public FlockManagerService() {
        Configuration conf = new Configuration();
        cellinkId = Long.parseLong(conf.getCellinkId());
        fuelDao = DbImplDecider.use(DaoType.DERBY).getDao(FuelDao.class);
        gasDao = DbImplDecider.use(DaoType.DERBY).getDao(GasDao.class);
        spreadDao = DbImplDecider.use(DaoType.DERBY).getDao(SpreadDao.class);
        feedDao = DbImplDecider.use(DaoType.DERBY).getDao(FeedDao.class);
        feedTypeDao = DbImplDecider.use(DaoType.DERBY).getDao(FeedTypeDao.class);
        workerDao = DbImplDecider.use(DaoType.DERBY).getDao(WorkerDao.class);
        laborDao = DbImplDecider.use(DaoType.DERBY).getDao(LaborDao.class);
        medicineDao = DbImplDecider.use(DaoType.DERBY).getDao(MedicineDao.class);
        transactionDao = DbImplDecider.use(DaoType.DERBY).getDao(TransactionDao.class);
        distribDao = DbImplDecider.use(DaoType.DERBY).getDao(DistribDao.class);
        dataDao = DbImplDecider.use(DaoType.DERBY).getDao(DataDao.class);
        flockDao = DbImplDecider.use(DaoType.DERBY).getDao(FlockDao.class);
        controllerDao = DbImplDecider.use(DaoType.DERBY).getDao(ControllerDao.class);
    }

    public void addFlock(Flock flock) {
        try {
            flockDao.insert(flock);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null,
                    "Can not add flock because flock for this houses already exist.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    public void saveFlock(Flock flock) {
        try {
            flockDao.update(flock);
        } catch (SQLException ex) {
            Logger.getLogger(FlockManagerService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void saveFlockDetail(Flock flock) {
        try {
            flockDao.updateFlockDetail(flock);
        } catch (SQLException ex) {
            Logger.getLogger(FlockManagerService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void removeFlock(Flock flock) {
        try {
            flockDao.removeAllHistoryInFlock(flock.getFlockId());
            flockDao.removeAllHistoryOf24hourInFlock(flock.getFlockId());
            flockDao.remove(flock.getFlockId());
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Flock does not exist", "Error", JOptionPane.ERROR);
        }
    }

    public Long generateFlockId() {
        long id = 1;
        try {
            List<Flock> flocks = (List<Flock>) flockDao.getAll();
            if (flocks.isEmpty()) {
                return id;
            } else {
                for (Flock f : flocks) {
                    long fid = f.getFlockId();
                    if (id < fid) {
                        id = fid;
                    }
                }
                id++;
            }
            return id;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return id;
    }

    public Integer getLastUpdatedGrowDay(Long flockId) {
        try {
            return flockDao.getUpdatedGrowDayHistory(flockId);
        } catch (SQLException ex) {
            return 1;
        }
    }

    public List<Flock> getFlocks() {
        try {
            flocks = (List<Flock>) flockDao.getAll();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return flocks;
    }

    public List<Flock> getOpenFlocks() {
        flocks.clear();
        getControllers();
        for (Controller c : controllers) {
            try {
                Flock flock = flockDao.getOpenFlockByController(c.getId());
                if (flock != null && flock.getFlockId() != null) {
                    flocks.add(flock);
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        return flocks;
    }

    public Collection<Controller> getControllers() {
        try {
            if (controllers == null || controllers.isEmpty()) {
                controllers = controllerDao.getAllByCellink(cellinkId);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return controllers;
    }

    public Map<String, Long> getControllerMap() {
        Map<String, Long> contrMap = new HashMap<String, Long>();
        getControllers();
        for (Controller c : controllers) {
            contrMap.put(c.getTitle(), c.getId());
        }
        return contrMap;
    }

    public Map<Integer, String> getFlockHistoryTable(Long flockId) {
        Map<Integer, String> updatedMap = new HashMap<Integer, String>();
        if (flockId == null) {
            return updatedMap;
        }
        try {
            updatedMap = flockDao.getAllHistoryByFlock(flockId);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return updatedMap;
    }

    public boolean isExist(Long dataId, String values) {
        boolean isExist = false;
        StringTokenizer st = new StringTokenizer(values, " ");
        int count = st.countTokens();
        if (count < 2) {
            return false;
        }
        for (int i = 0; i < count; i += 2) {
            try {
                String id = st.nextToken();
                String vl = st.nextToken();
                Long lid = Long.parseLong(id);
                if (dataId.equals(lid) && !vl.equals("-1")) {
                    return true;
                }
            } catch (NoSuchElementException ex) {
                ex.printStackTrace();
            }
        }
        return isExist;
    }

    public List<HistoryEntry> createHistoryEntryList(Long currFlockId) {
        List<HistoryEntry> allHistoryEntryList = new ArrayList<HistoryEntry>();
        try {
            List<Data> dataList = (List<Data>) dataDao.getAllBySpecial(Data.HISTORY);
            for (Data d : dataList) {
                allHistoryEntryList.add(new HistoryEntry(d.getType(), d.getLabel(), d.getFormat()));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        Map<Integer, String> flockHistory = getFlockHistoryTable(currFlockId);
        List<HistoryEntry> historyList = new ArrayList<HistoryEntry>();
        if (flockHistory.isEmpty() || (getFirstGrowDayWithNotEmptyHistory(flockHistory) == -1)) {
            allHistoryEntryList.clear();
            return allHistoryEntryList;
        } else {
            Integer firstNotEmpty = getFirstGrowDayWithNotEmptyHistory(flockHistory);
            for (HistoryEntry historyEntry : allHistoryEntryList) {
                String values = flockHistory.get(firstNotEmpty);
                if (historyEntry.getId() instanceof Long) {
                    Long id = (Long) historyEntry.getId();
                    if (isExist(id, values)) {
                        historyEntry.setValues(getHistoryMap(flockHistory, id, historyEntry.getFormat()));
                        historyList.add(historyEntry);
                    }
                }
            }
        }
        return historyList;
    }

    private Map<Integer, Double> getHistoryMap(Map<Integer, String> flockHistory, Long dataId, Integer format) {
        Set<Entry<Integer, String>> entries = flockHistory.entrySet();
        Map<Integer, Double> table = new HashMap<Integer, Double>();
        for (Entry entry : entries) {
            Integer growDay = (Integer) entry.getKey();
            String values = (String) entry.getValue();
            StringTokenizer st = new StringTokenizer(values, " ");
            int count = st.countTokens();
            for (int i = 0; i < count; i += 2) {
                try {
                    String id = st.nextToken();
                    String vl = st.nextToken();
                    Long lid = Long.parseLong(id);
                    if (dataId.equals(lid) && !vl.equals("-1")) {
                        Long value = Long.parseLong(vl);
                        if (DataFormat.TIME == format) {
                            long h = value / 100;
                            long m = value % 100;
                            long t = h * 60 + m;
                            table.put(growDay, (double) t);
                        } else {
                            vl = DataFormat.formatToStringValue(format, value);
                            table.put(growDay, Double.parseDouble(vl));
                        }
                    }
                } catch (NoSuchElementException ex) {
                    table.put(growDay, Double.parseDouble("0"));
                }
            }
        }
        return table;
    }

    private Integer getFirstGrowDayWithNotEmptyHistory(Map<Integer, String> flockHistory) {
        Set<Entry<Integer, String>> entries = flockHistory.entrySet();
        Integer firstElement = -1;
        for (Entry<Integer, String> entry : entries) {
            if (!entry.getValue().equals("-1")) {
                firstElement = entry.getKey();
                break;
            }
        }
        return firstElement;
    }

    public List<HistoryEntry> createHistoryEntry24List(Long currFlockId) {
        List<HistoryEntry> allHistoryEntryList = new ArrayList<HistoryEntry>();
        Map<String, String> map = flockDao.getHistoryN();
        Set<Entry<String, String>> entries = map.entrySet();
        for (Entry e : entries) {
            try {
                Map<Integer, String> values24 = flockDao.getAllHistory24ByFlockAndDnum(currFlockId, (String) e.getKey());
                HistoryEntry he = new HistoryEntry(e.getKey(), (String) e.getValue());
                he.setValues24Map(values24);
                allHistoryEntryList.add(he);
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        return allHistoryEntryList;
    }

    public void addGas(Gas gas) {
        try {
            gasDao.insert(gas);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void removeGas(Long gasId) {
        try {
            gasDao.remove(gasId);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public List<Gas> getAllGas(Long flockId) {
        try {
            return gasDao.getAllByFlockId(flockId);
        } catch (SQLException ex) {
            Logger.getLogger(FlockManagerService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new ArrayList<Gas>();
    }

    public Gas getGasById(Long id) {
        try {
            return gasDao.getById(id);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return new Gas();
    }

    public Fuel getFuelById(Long id) {
        try {
            return fuelDao.getById(id);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return new Fuel();
    }

    public void addFuel(Fuel fuel) {
        try {
            fuelDao.insert(fuel);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    void removeFuel(Long id) {
        try {
            fuelDao.remove(id);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public List<Fuel> getAllFuel(Long flockId) {
        try {
            return fuelDao.getAllByFlockId(flockId);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return new ArrayList<Fuel>();
    }

    public void addSpread(Spread spread) {
        try {
            spreadDao.insert(spread);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void removeSpread(Long spreadId) {
        try {
            spreadDao.remove(spreadId);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public List<Spread> getAllSpread(Long flockId) {
        try {
            return spreadDao.getAllByFlockId(flockId);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return new ArrayList<Spread>();
    }

    public Spread getSpreadById(Long id) {
        try {
            return spreadDao.getById(id);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return new Spread();
    }

    public void addFeed(Feed feed) {
        try {
            feedDao.insert(feed);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void removeFeed(Long feedId) {
        try {
            feedDao.remove(feedId);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public List<Feed> getAllFeed(Long flockId) {
        try {
            return feedDao.getAllByFlockId(flockId);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return new ArrayList<Feed>();
    }

    public Feed getFeedById(Long id) {
        try {
            return feedDao.getById(id);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return new Feed();
    }

    public FeedType getFeedTypeById(Long id) {
        try {
            return feedTypeDao.getById(id);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return new FeedType();
    }

    public void addFeedType(FeedType feedType) {
        try {
            feedTypeDao.insert(feedType);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public List<FeedType> getAllFeedType(Long cellinkId) {
        try {
            return feedTypeDao.getAllByCellinkId(cellinkId);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return new ArrayList<FeedType>();
    }

    public void removeFeedType(Long id) {
        try {
            feedTypeDao.remove(id);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void addWorker(Worker worker) {
        try {
            workerDao.insert(worker);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

    }

    public void removeWorker(Long id) {
        try {
            workerDao.remove(id);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public List<Worker> getAllWorker(Long cellinkId) {
        try {
            return workerDao.getAllByCellinkId(cellinkId);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return new ArrayList<Worker>();
    }

    public Worker getWorkerById(Long id) {
        try {
            return workerDao.getById(id);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return new Worker();
    }

    public void addLabor(Labor labor) {
        try {
            laborDao.insert(labor);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

    }

    public void removeLabor(Long id) {
        try {
            laborDao.remove(id);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public List<Labor> getAllLabor(Long flockId) {
        try {
            return laborDao.getAllByFlockId(flockId);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return new ArrayList<Labor>();
    }

    public Labor getLaborById(Long id) {
        try {
            return laborDao.getById(id);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return new Labor();
    }

    public List<Medicine> getAllMedicine(Long flockId) {
        try {
            return medicineDao.getAllByFlockId(flockId);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return new ArrayList<Medicine>();
    }

    public void removeMedicine(Long id) {
        try {
            medicineDao.remove(id);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void addMedicine(Medicine medicine) {
        try {
            medicineDao.insert(medicine);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public Medicine getMedicineById(Long id) {
        try {
            return medicineDao.getById(id);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return new Medicine();
    }

    public List<Transaction> getAllTransaction(Long flockId) {
        try {
            return transactionDao.getAllByFlockId(flockId);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return new ArrayList<Transaction>();
    }

    public void removeTransaction(Long id) {
        try {
            transactionDao.remove(id);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void addTransaction(Transaction transaction) {
        try {
            transactionDao.insert(transaction);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public Transaction getTransactionById(Long id) {
        try {
            return transactionDao.getById(id);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return new Transaction();
    }

    public Controller getControllerById(Long controllerId) {
        try {
            return controllerDao.getById(controllerId);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return new Controller();
    }

    public void saveControllerArea(Long controllerId, Integer area) {
        try {
            Controller controller = controllerDao.getById(controllerId);
            controller.setArea(area);
            controllerDao.update(controller);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public List<Distrib> getAllDistrib(Long flockId) {
        try {
            return distribDao.getAllByFlockId(flockId);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return new ArrayList<Distrib>();
    }

    public Distrib getDistribById(Long id) {
        try {
            return distribDao.getById(id);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return new Distrib();
    }

    public void removeDistrib(Long id) {
        try {
            distribDao.remove(id);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}
