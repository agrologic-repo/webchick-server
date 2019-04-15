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
            JOptionPane.showMessageDialog(null, "Can not add flock because flock for this houses already exist.", "Error", JOptionPane.ERROR_MESSAGE);
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

    public Integer getFirstUpdatedGrowDay(Long flockId) {
        try {
            return flockDao.getUpdatedGrowDayHistoryMin(flockId);
        } catch (SQLException ex) {
            return 1;
        }
    }

    public Integer getFirstUpdatedGrowDay24(Long flockId) {
        try {
            return flockDao.getUpdatedGrowDayHistory24Min(flockId);
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
            updatedMap = flockDao.getFlockPerDayNotParsedReports(flockId);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return updatedMap;
    }

    public List<HistoryEntry> createHistoryEntryList(Long currFlockId) {
        Map<Integer, String> historyDataByGrowDayMap;
        List<Data> perAllDayHistoryList;
        String dataIdString;
        String valueString;
        long dataId;
        int value;
        int type;
        HistoryEntry historyEntryTest;
        Map<Integer, Double> valuesTest;
        List<HistoryEntry> historyListTest = null;
        List <Data> actualHistData;
        StringTokenizer token;
        int count;
        int highValue;
        int lowValue;

        try {
            historyDataByGrowDayMap = getFlockHistoryTable(currFlockId);
            perAllDayHistoryList = (List<Data>) dataDao.getAllBySpecial(Data.HISTORY);
            historyListTest = new ArrayList<HistoryEntry>();
            actualHistData = new ArrayList<Data>();
            count = 0;

            for (Map.Entry<Integer, String> entry : historyDataByGrowDayMap.entrySet()) {
                if (count == 0) {
                    count++;
                    if (!entry.getValue().equals("-1")) {
                        token = new StringTokenizer(entry.getValue(), " ");
                        while (token.hasMoreElements() && token.countTokens() >= 4) {
                            dataIdString = (String) token.nextElement();
                            valueString = (String) token.nextElement();
                            dataId = Long.parseLong(dataIdString);
                            value = Integer.parseInt(valueString);

                            type = (int) dataId;
                            if ((type & 0xC000) == 0) {
                                dataId = (type & 0xFFFF);
                            } else if ((type & 0xC000) != 0xC000) {
                                dataId = (type & 0xFFF);
                            } else {
                                dataId = (type & 0xFFFF);
                            }

//                            if (dataId < 0){
//                                dataId = dataId + 65536;
//                            }

                            for (Data data : perAllDayHistoryList) {
                                if (data.getId().equals(Long.valueOf(dataId)) || data.getType().equals(Long.valueOf(dataId))) {
                                    actualHistData.add(data);
                                }
                            }
                        }
                    } else {
                        count = 0;
                    }
                } else {
                    break;
                }
            }
        for (Map.Entry<Integer, String> entry : historyDataByGrowDayMap.entrySet()) {
            for (Data data : actualHistData) {
                if (data.getId() == 800) {
                    data.setValue(entry.getKey().longValue());
                } else {
                    if (!entry.getValue().equals("-1")) {
                        token = new StringTokenizer(entry.getValue(), " ");
                        while (token.hasMoreElements() && token.countTokens() >= 4) {
                            try {
                                dataIdString = (String) token.nextElement();
                                valueString = (String) token.nextElement();
                                dataId = Long.parseLong(dataIdString);
                                value = Integer.parseInt(valueString);

//                                if (dataId < 0){
//                                    dataId = dataId + 65536;
//                                }
                                type = (int) dataId;
                                if ((type & 0xC000) == 0) {
                                    dataId = (type & 0xFFFF);
                                } else if ((type & 0xC000) != 0xC000) {
                                    dataId = (type & 0xFFF);
                                } else {
                                    dataId = (type & 0xFFFF);
                                }
                                if (data.getId().equals(Long.valueOf(dataId)) || data.getType().equals(Long.valueOf(dataId))) {
                                    if(data.getFormatForLocalGraphs() == 8){
                                        token.nextToken();
                                        highValue = value;
                                        valueString = token.nextToken();
                                        value = Integer.parseInt(valueString);
                                        lowValue = value;
                                        value = ((highValue << 16) & 0xFFFF0000) | (lowValue & 0x0000FFFF);
                                    }
                                    data.setValue(Long.valueOf(value));
//                                            values += String.valueOf(data.getId()) + " " + data.getValue() + " ";
                                    if (isExistHistoryEntryIdIntoHistoryList(Long.valueOf(data.getId()), historyListTest)) {
                                        ////////////////////////////////////////////////////////////////////////////////
                                        double val=0.0;
                                        if (DataFormat.TIME == data.getFormat()) {
                                            long h = value / 100;
                                            long m = value % 100;
                                            long t = h * 60 + m;
                                            value = (int)t;
////                                            table.put(growDay, (double) t);
                                        } else {
                                              val = Double.parseDouble(DataFormat.formatToStringValue(data.getFormat(), value));
//                                            table.put(growDay, Double.parseDouble(vl));
                                        }
                                        ////////////////////////////////////////////////////////////////////////////////
//                                        historyListTest = setValuesToHistoryEntryFromHistoryList(data.getId(), entry.getKey(), value, historyListTest);////
                                        historyListTest = setValuesToHistoryEntryFromHistoryList(data.getId(), entry.getKey(), val, historyListTest);////
                                    } else {
                                        historyEntryTest = new HistoryEntry();
                                        valuesTest = new HashMap<Integer, Double>();
                                        historyEntryTest.setId(data.getId());
                                        historyEntryTest.setTitle(data.getLabel());
                                        historyEntryTest.setFormat(data.getFormat());
                                        ////////////////////////////////////////////////////////////////////////////////
                                        Double vl = 0.0;
                                        if (DataFormat.TIME == data.getFormat()) {
                                            long h = value / 100;
                                            long m = value % 100;
                                            long t = h * 60 + m;
                                            value = (int)t;
//                                            table.put(growDay, (double) t);
                                        } else {
                                              vl = Double.parseDouble(DataFormat.formatToStringValue(data.getFormat(), value));
//                                            table.put(growDay, Double.parseDouble(vl));
                                        }
                                        valuesTest.put(entry.getKey(), (double) vl);////
                                        ////////////////////////////////////////////////////////////////////////////////
//                                        valuesTest.put(entry.getKey(), (double) value);////
                                        historyEntryTest.setValues(valuesTest);
                                        historyListTest.add(historyEntryTest);
                                        historyEntryTest = null;
                                        valuesTest = null;
                                    }
                                }
                            } catch (NoSuchElementException e) {
                            // skip this exception
                            }
                        }
                    }
                }
            }
//                values = "";
        }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return historyListTest;
    }

    private boolean isExistHistoryEntryIdIntoHistoryList(Long dataId, List<HistoryEntry> historyListTest){
        boolean returnflag;
        String str;
        Long idLong;
        returnflag = false;
        for(HistoryEntry hisEnt : historyListTest){
            str = String.valueOf(hisEnt.getId());
            idLong = Long.valueOf(str);
            if(dataId.equals(idLong)) {
                returnflag = true;
            }
        }
        return returnflag;
    }

    private List <HistoryEntry> setValuesToHistoryEntryFromHistoryList(Long dataId, int growDay, double value, List<HistoryEntry> histiryListTest){
        String str;
        Long idLong;
        Map <Integer, Double> val;

        for(HistoryEntry he: histiryListTest){
            str = String.valueOf(he.getId());
            idLong = Long.valueOf(str);
            if(idLong.equals(dataId)){
                val = he.getValues();
                val.put(growDay, value);
                he.setValues(val);
            }
        }
        return histiryListTest;
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

    public List<HistoryEntry> createHistoryEntry24List(Long currFlockId, Integer growDay, Long langId) {
        List <HistoryEntry> histEntryLst = null;
        Collection<Data> perHourHistData;
        Map<Integer, String> values24;
        HistoryEntry he;

        try {
            histEntryLst = new ArrayList<HistoryEntry>();
            perHourHistData = flockDao.getFlockPerHourHistoryData(currFlockId, growDay, langId);
            for (Data data : perHourHistData){//3
                values24 = flockDao.getAllHistory24ByFlockAndDnum(currFlockId, data.getHistoryHourDNum());
                he = new HistoryEntry(data.getId(), data.getLabel(), data.getFormat());
                he.setValues24Map(values24);
                histEntryLst.add(he);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return histEntryLst;
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

////////////////////////////////////////////////////////////////////////////////////////////////////
//    private static int twosCompliment(int val) {
//        final int HIGH_32BIT_OFF_MASK = 0x0000FFFF;
//        int tVal = val;
//        if (tVal != -1) {
//            //two's compliment action
//            tVal = Math.abs(tVal);
//            tVal = ~tVal;
//            tVal &= HIGH_32BIT_OFF_MASK;
//            tVal += 1;
//        }
//        return tVal;
//    }
////////////////////////////////////////////////////////////////////////////////////////////////////////