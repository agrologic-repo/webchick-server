package com.agrologic.app.gui.rxtx.flock;

import com.agrologic.app.config.Configuration;
import com.agrologic.app.dao.*;
import com.agrologic.app.model.*;
import com.agrologic.app.service.history.FlockHistoryService;
import com.agrologic.app.service.history.HistoryContent;
import com.agrologic.app.service.history.HistoryContentCreator;
import com.agrologic.app.service.history.transaction.FlockHistoryServiceImpl;
import com.agrologic.app.service.table.HistoryContentException;

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
//    private static final int SHIFT_16_BIT = 16;
//    private static final int HIGH_16BIT_ON_MASK = 0x8000;
//
//    private List<HistoryEntry> history;
//    int counter;

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

//        history = new ArrayList<HistoryEntry>();
//        counter = 0;
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

//    public boolean isExist(Long dataId, String values) {
//        boolean isExist = false;
//        StringTokenizer st = new StringTokenizer(values, " ");
//        int count = st.countTokens();
//        if (count < 2) {
//            return false;
//        }
//        for (int i = 0; i < count; i += 2) {
//            try {
//                String id = st.nextToken();
//                String vl = st.nextToken();
//                Long lid = Long.parseLong(id);
//                if (dataId.equals(lid) && !vl.equals("-1")) {
//                    return true;
//                }
//            } catch (NoSuchElementException ex) {
//                ex.printStackTrace();
//            }
//        }
//        return isExist;
//    }



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

//    //07/01/2018
//    public List<HistoryEntry> createHistory24HourEntryList(Long currFlockId, int growDay){
//        List<HistoryEntry> historyList = null;
//
//        historyList = new ArrayList<HistoryEntry>();
//
//        return historyList;
//    }
//    //07/01/2018

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

///////////////////////////////////////////////////////////////////////////////////////////////////////
//    private String returnNewValues(String values) {
//
//        String newValues = "";
//        String idStr = "";
//        String valueStr = "";
//
//        StringTokenizer token = new StringTokenizer(values, " ");
//        while (token.hasMoreElements() && token.countTokens() >= 4) {
//            try {
//                String dataIdString = (String) token.nextElement();   //-31303
//                String valueString = (String) token.nextElement();    //0
//
//                long dataId = Long.parseLong(dataIdString); //(long) -31303
//                int value = Integer.parseInt(valueString); // (int) 0
//
//                int type = (int) dataId;// type of value (like 4096)   //(long) -31303
//                if ((type & 0xC000) != 0xC000) {
//                    dataId = (type & 0xFFF); // remove type to get an index 4096&0xFFF -> 0    // dataId=1465
//                } else {
//                    dataId = (type & 0xFFFF);
//                }
//
//                if (dataId == 1465) {
//                    dataId = 34233;
//                } else if (dataId == 1467) {
//                    dataId = 34235;
//                } else if (dataId == 1469) {
//                    dataId = 34237;
//                } else if (dataId == 1471) {
//                    dataId = 34239;
//                } else if (dataId == 1473) {
//                    dataId = 34241;
//                } else if (dataId == 1475) {
//                    dataId = 34243;
//                } else if (dataId == 2600) {
//                    dataId = 35368;
//                } else if (dataId == 2615) {
//                    dataId = 35383;
//                } else if (dataId == 2630) {
//                    dataId = 35398;
//                } else if (dataId == 2645) {
//                    dataId = 35413;
//                } else if (dataId == 2660) {
//                    dataId = 35428;
//                } else if (dataId == 2675) {
//                    dataId = 35443;
//                } else if (dataId == 1384) {
//                    dataId = 34152;
//                }
//                token.nextToken();// skip this key
//
//                int highValue = value;
//                boolean negative = ((highValue & HIGH_16BIT_ON_MASK) == 0) ? false : true;
//                if (negative) {
//                    // two's compliment action
//                    highValue = twosCompliment(highValue);
//                }
//                highValue <<= SHIFT_16_BIT;
//                valueString = token.nextToken();// get low value
//                value = Integer.parseInt(valueString);
//
//                int lowValue = value;
//                negative = ((lowValue & HIGH_16BIT_ON_MASK) == 0) ? false : true;
//                if (negative) {
//                    // two's compliment action
//                    lowValue = twosCompliment(lowValue);
//                }
//                value = highValue + lowValue;
////
//                idStr = String.valueOf(dataId);
//                valueStr = String.valueOf(value);
////
//                newValues += idStr + " " + valueStr + " ";
//            } catch (NoSuchElementException e) {
//                // skip this exception
//            }
//        }
//
//        return newValues;
//    }
//////////////////////////////////////////////////////////////////////////////////////////////////////

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


//8888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888
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
//8888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888


//    private Integer getFirstGrowDayWithNotEmptyHistory(Map<Integer, String> flockHistory) {
//        Set<Entry<Integer, String>> entries = flockHistory.entrySet();
//        Integer firstElement = -1;
//        for (Entry<Integer, String> entry : entries) {
//            if (!entry.getValue().equals("-1")) {
//                firstElement = entry.getKey();
//                break;
//            }
//        }
//        return firstElement;
//    }

//        private Integer getLastGrowDayWithNotEmptyHistory(Map<Integer, String> flockHistory) {
//        Set<Entry<Integer, String>> entries = flockHistory.entrySet();
//        Integer lastElement = null;
//        for (Entry<Integer, String> entry : entries) {
//            if (!(lastElement == null)) {
//                lastElement = flockHistory.size();
//                break;
//            }
//        }
//        return firstElement;
//    }

//    public List<HistoryEntry> createHistoryEntry24List(Long currFlockId) {
//        List<HistoryEntry> allHistoryEntryList = new ArrayList<HistoryEntry>();
//        Map<String, String> map = flockDao.getHistoryN();
//        Set<Entry<String, String>> entries = map.entrySet();
//        for (Entry e : entries) {
//            try {
//                Map<Integer, String> values24 = flockDao.getAllHistory24ByFlockAndDnum(currFlockId, (String) e.getKey());
//                HistoryEntry he = new HistoryEntry(e.getKey(), (String) e.getValue());
//                he.setValues24Map(values24);
//                allHistoryEntryList.add(he);
//            } catch (SQLException ex) {
//                ex.printStackTrace();
//            }
//        }
//        return allHistoryEntryList;
//    }

    public List<HistoryEntry> createHistoryEntry24List(Long currFlockId, Integer growDay, Long langId) {
        List <HistoryEntry> histEntryLst = null;
        Collection<Data> perHourHistData;
        Map<Integer, String> values24;
        HistoryEntry he;

        try {

            histEntryLst = new ArrayList<HistoryEntry>();
            perHourHistData = flockDao.getFlockPerHourHistoryData(currFlockId, growDay, langId);
            for (Data data : perHourHistData){
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




////////////////////////////////////
///////////////////////////////////
//    if (!entry.getValue().equals("-1")) {
//        StringTokenizer token = new StringTokenizer(entry.getValue(), " ");
//        while (token.hasMoreElements() && token.countTokens() >= 4) {
//            try {
//                String dataIdString = (String) token.nextElement();
//                String valueString = (String) token.nextElement();
//
//                long dataId = Long.parseLong(dataIdString);
//                int value = Integer.parseInt(valueString);
//
//                int type = (int) dataId;// type of value (like 4096)
//                if ((type & 0xC000) != 0xC000) {
//                    dataId = (type & 0xFFF); // remove type to get an index 4096&0xFFF -> 0
//                } else {
//                    dataId = (type & 0xFFFF);
//                }
//
//                if (data.getId().equals(Long.valueOf(dataId))) {
//                    if (data.isLongType()) {
//                        token.nextToken();// skip this key
//                        int highValue = value;
//                        valueString = token.nextToken();// get low value
//                        value = Integer.parseInt(valueString);
//
//                        int lowValue = value;
//                        value = ((highValue << 16)&0xFFFF0000) | (lowValue&0x0000FFFF);
//                    }
//                    data.setValue(Long.valueOf(value));
//                    // we need only data than not equals to -1
//                    if (!data.getValue().equals(-1L)) {
//                        actualPerDayHistoryList.add((Data) data.clone());
//                    }
//                }
//            } catch (NoSuchElementException e) {
//                // skip this exception
//            }
//        }
//    }
////////////////////////////////////
///////////////////////////////////


//    public List<HistoryEntry> createHistoryEntryList(Long currFlockId) {
//        /////////////////////////////////////
//        String valuesNew = null;
//        String values = null;
//        List<HistoryEntry> historyListReturn = null;
//        historyListReturn = new ArrayList<HistoryEntry>();
//        /////////////////////////////////////
//        List<HistoryEntry> allHistoryEntryList = null;
//        allHistoryEntryList = new ArrayList<HistoryEntry>();
//        try {
//            List<Data> dataList = (List<Data>) dataDao.getAllBySpecial(Data.HISTORY);
//            for (Data d : dataList) {
//                allHistoryEntryList.add(new HistoryEntry(d.getType(), d.getLabel(), d.getFormat()));
//            }
//        } catch (SQLException ex) {
//            ex.printStackTrace();
//        }
//        Map<Integer, String> flockHistory = getFlockHistoryTable(currFlockId);
//        List<HistoryEntry> historyList = new ArrayList<HistoryEntry>();
//        if (flockHistory.isEmpty() || (getFirstGrowDayWithNotEmptyHistory(flockHistory) == -1)) {
//            allHistoryEntryList.clear();
//            return allHistoryEntryList;
//        } else {
////            Integer firstNotEmpty = getFirstGrowDayWithNotEmptyHistory(flockHistory);
//            int firstNotEmpty = 1;
//            int size = flockHistory.size();
//            while (size > 0) {
//                values = flockHistory.get(firstNotEmpty);
////                values = "4096 252 1301 468 1302 70 1303 0 1304 0 1305 0 1328 8 1358 14 1825 2 1826 30 14162 0 14163 0 14164 0 14165 0 2163 0 15189 1056 15190 1077 2929 0 2930 0 2931 0 2932 0 15221 0 15222 0 15223 0 15224 0 2937 0 2938 0 2939 0 2940 0 15250 0 15251 0 15252 0 15253 0 2966 0 2967 0 2968 0 2969 0 7098 250 7099 250 7100 0 7101 700 3006 0 3007 0 3009 0 3017 0 3033 0 3038 0 3041 0 3042 0 3043 0 3044 0 3566 47 3567 7 7715 1 64210 250 64211 249 64224 22545 64225 23569 ";
//                for (HistoryEntry historyEntry : allHistoryEntryList) {/////////////////////////
//                        if (historyEntry.getId() instanceof Long) {
//                            Long id = (Long) historyEntry.getId();
//                            if (isExist(id, values)) {
////                        if (isExist(34233L, values)) {
//                                historyEntry.setValues(getHistoryMap(flockHistory, id, historyEntry.getFormat()));     ///
//                                historyList.add(historyEntry); ///
//                            } else {
//                                valuesNew = returnNewValues(values); ///
//                                valuesNew = values;
////                                valuesNew = "4096 252 1301 468 1302 70 1303 0 1304 0 1305 0 1328 8 1358 14 1825 2 1826 30 14162 0 14163 0 14164 0 14165 0 2163 0 15189 1056 15190 1077 2929 0 2930 0 2931 0 2932 0 15221 0 15222 0 15223 0 15224 0 2937 0 2938 0 2939 0 2940 0 15250 0 15251 0 15252 0 15253 0 2966 0 2967 0 2968 0 2969 0 7098 250 7099 250 7100 0 7101 700 3006 0 3007 0 3009 0 3017 0 3033 0 3038 0 3041 0 3042 0 3043 0 3044 0 3566 47 3567 7 7715 1 64210 250 64211 249 64224 22545 64225 23569 ";
//                                if (isExist(id, valuesNew)) {
//                                    if (flockHistory.containsKey(firstNotEmpty)) {
//                                        flockHistory.put(firstNotEmpty, valuesNew);
////                                    flockHistoryNew.put(firstNotEmpty, valuesNew);
//                                    }
////                                valuesHistory = valuesHistory(values, firstNotEmpty);
////                                historyEntry.setValues(valuesHistory);
//                                    historyEntry.setValues(getHistoryMap(flockHistory, id, historyEntry.getFormat()));     ///
//                                    historyList.add(historyEntry); ///
//                                }
//                            }
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//                        }
//
//                }////////////////////////////////////////////////
//                firstNotEmpty++; ////
//                size--;/////
//            }
//        }
//        for (int i = 0; i < 13; i++){
////        for (int i = 0; i < historyList.size(); i++){
//            HistoryEntry entry = historyList.get(i);
//            historyListReturn.add(entry);
//        }
//        historyList.clear();
////        return historyList;
//        return historyListReturn;
//    }
////////////////////////////////////////////!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!





//    public List<HistoryEntry> createHistoryEntryList(Long currFlockId) {
//        List<HistoryEntry> historyList = null;
//        String values = "";
//        Map<Integer, String> historyDataByGrowDayMap;
//        List<Data> perAllDayHistoryList;
//        HistoryEntry historyEntry;
//        String dataIdString;
//        String valueString;
//        long dataId;
//        int value;
//        int type;
//        boolean flag = true;
//        HistoryEntry historyEntryTest;
//        Map<Integer, Double> valuesTest;
//        List<HistoryEntry> historyListTest = null;
//
////        history = new ArrayList<HistoryEntry>();
//
//
//        try {
//            historyList = new ArrayList<HistoryEntry>();
//            historyDataByGrowDayMap = getFlockHistoryTable(currFlockId);
//            perAllDayHistoryList = (List<Data>) dataDao.getAllBySpecial(Data.HISTORY);
//            historyEntryTest = new HistoryEntry();
//            valuesTest = new HashMap<Integer, Double>();
//            historyListTest = new ArrayList<HistoryEntry>();
//
//            for (Map.Entry<Integer, String> entry : historyDataByGrowDayMap.entrySet()) {
//                for (Data data : perAllDayHistoryList) {
//                    if (data.getId() == 800) {
//                        data.setValue(entry.getKey().longValue());
//                    } else {
//                        if (!entry.getValue().equals("-1")) {
//                            StringTokenizer token = new StringTokenizer(entry.getValue(), " ");
//                            while (token.hasMoreElements() && token.countTokens() >= 4) {
//                                try {
//                                    dataIdString = (String) token.nextElement();
//                                    valueString = (String) token.nextElement();
//
//                                    dataId = Long.parseLong(dataIdString);
//                                    value = Integer.parseInt(valueString);
//
//                                    type = (int) dataId;
//                                    if((type & 0xC000) == 0){
//                                        dataId = (type & 0xFFFF);
//                                    }
//                                    else if ((type & 0xC000) != 0xC000) {
//                                        dataId = (type & 0xFFF);
//                                    }else {
//                                        dataId = (type & 0xFFFF);
//                                    }
//
//                                    if (data.getId().equals(Long.valueOf(dataId))) {
//                                        if (data.isLongType()) {
//                                            token.nextToken();
//                                            int highValue = value;
//                                            valueString = token.nextToken();
//                                            value = Integer.parseInt(valueString);
//                                            int lowValue = value;
//                                            value = ((highValue << 16)&0xFFFF0000) | (lowValue&0x0000FFFF);
//                                        }
//                                        data.setValue(Long.valueOf(value));
//
//                                        values += String.valueOf(data.getId()) + " " + data.getValue() + " ";
//                                        //xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
//                                        if(isExistHistoryEntryIdIntoHistoryList(Long.valueOf(dataId), historyListTest)){
//                                            historyListTest = setValuesToHistoryEntryFromHistoryList(dataId, entry.getKey(), value, historyListTest);
//                                        } else {
//                                            historyEntryTest = new HistoryEntry();
//                                            historyEntryTest.setId(dataId);
//                                            valuesTest.put(entry.getKey(), (double) value);
//                                            historyEntryTest.setValues(valuesTest);
//                                            historyListTest.add(historyEntryTest);
//                                            historyEntryTest = null;
//                                        }
//                                        //xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
//                                    } else {
//                                        data.setValue(Long.valueOf(value));
//                                        values += dataId + " " + value + " ";
//                                        //xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
//                                        if(isExistHistoryEntryIdIntoHistoryList(Long.valueOf(dataId), historyListTest)){
//                                            historyListTest = setValuesToHistoryEntryFromHistoryList(dataId, entry.getKey(), value, historyListTest);
//                                        } else {
//                                            historyEntryTest = new HistoryEntry();
//                                            valuesTest = new HashMap<Integer, Double>();
//                                            historyEntryTest.setId(dataId);
//                                            valuesTest.put(entry.getKey(), (double) value);
//                                            historyEntryTest.setValues(valuesTest);
//                                            historyListTest.add(historyEntryTest);
//                                            historyEntryTest = null;
//                                            valuesTest = null;
//                                        }
//                                        //xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
//                                    }
//                                } catch (NoSuchElementException e) {
//                                    // skip this exception
//                                }
//                            }
//                            if (isExist(data.getId(), values)){
//                                historyDataByGrowDayMap.put(entry.getKey(),values);
//                            }
//                        }
//                    }
//                    values = "";
//                }
//            }
////            ///////////////////////////////////////////////////////////////////////////////////////////////////
////            Map<Long, Integer> dataHistMap;
////            dataHistMap = new HashMap<Long, Integer>();
////            for(Map.Entry<Integer, String> entry : historyDataByGrowDayMap.entrySet()) {
////                if (!entry.getValue().equals("-1")) {
////                    StringTokenizer token = new StringTokenizer(entry.getValue(), " ");
////                    while (token.hasMoreElements() && token.countTokens() >= 4) {
////                        dataIdString = (String) token.nextElement();
////                        valueString = (String) token.nextElement();
////
////                        dataId = Long.parseLong(dataIdString);
////                        value = Integer.parseInt(valueString);
////                        dataHistMap.put(dataId, value);
////                    }
////                }
////            }
////            //////////////////////////////////////////////////////////////////////////////////////////////////
//
//
//
////            for(Map.Entry<Integer, String> entry : historyDataByGrowDayMap.entrySet()){
////                for (Data data : perAllDayHistoryList) {
//////                    if(isExist(data.getId(), entry.getValue())){
////                     if(entry.getValue().contains(data.getId().toString().toLowerCase()) || entry.getValue().contains(data.getType().toString().toLowerCase())){
////                        historyEntry = new HistoryEntry();
////                        historyEntry.setValues(getHistoryMap(historyDataByGrowDayMap, data.getId(), data.getFormat()));
////                        historyEntry.setId(data.getId());
////                        historyEntry.setTitle(data.getTitle());
////                        historyEntry.setFormat(data.getFormat());
////                        historyList.add(historyEntry);
////
////                        flag = true;
////                        if (historyList.size() == 0){
////                            historyList.add(historyEntry);
////                            flag = false;
////                            historyEntry = null;
////                        } else {
////                            for (int i = 0; i < historyList.size(); i++) {
////                                HistoryEntry hisE = historyList.get(i);
////                                if ((Long)hisE.getId() == data.getId() || (Long)hisE.getId() == data.getType()) {
////                                    flag = false;
////                                }
////                            }
////                            if (flag){
////                                historyList.add(historyEntry);
////                                historyEntry = null;
////                            }
////                        }
////                    }
////                }
////            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        if (counter == 0){
//            counter++;
//            for (HistoryEntry he : historyListTest){
//                history.add(he);
//            }
//            return history;
//        } else {
//            return history;
//        }
////        return historyList;
////            return historyListTest;
//    }///////////////!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!