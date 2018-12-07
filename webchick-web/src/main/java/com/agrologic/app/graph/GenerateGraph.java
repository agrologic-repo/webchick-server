package com.agrologic.app.graph;

import com.agrologic.app.dao.ControllerDao;
import com.agrologic.app.dao.DaoType;
import com.agrologic.app.dao.DataDao;
import com.agrologic.app.dao.DbImplDecider;
import com.agrologic.app.graph.daily.*;
import com.agrologic.app.graph.history.HistoryGraph;
import com.agrologic.app.management.PerGrowDayHistoryDataType;
import com.agrologic.app.model.Data;
import com.agrologic.app.model.history.DayParam;
import com.agrologic.app.model.history.FromDayToDay;
import com.agrologic.app.service.LocaleService;
import com.agrologic.app.service.history.FlockHistoryService;
import com.agrologic.app.service.history.HistoryContent;
import com.agrologic.app.service.history.HistoryContentCreator;
import com.agrologic.app.service.history.transaction.FlockHistoryServiceImpl;
import org.jfree.chart.ChartRenderingInfo;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.entity.StandardEntityCollection;
import org.jfree.chart.servlet.ServletUtilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpSession;
import java.io.PrintWriter;
import java.util.*;

public class GenerateGraph {
    public static final String NO_DATA_AVAILABLE = "No data available in database to create {} graph .";
    private static final Logger logger = LoggerFactory.getLogger(GenerateGraph.class);
    private static LocaleService localeService = new LocaleService();


    public static String generateFlockGraph(Long flockId, String fromDay, String toDay, PrintWriter pw, Locale locale){

        String filename = null;

        FromDayToDay growDayRangeParam = new FromDayToDay(fromDay, toDay);

        try {

            Long langId = localeService.getLanguageId(locale.getLanguage());

            FlockHistoryService flockHistoryService = new FlockHistoryServiceImpl();
            Map<Integer, String> historyByGrowDay = flockHistoryService.getFlockPerDayNotParsedReportsWithinRange(flockId, growDayRangeParam);
            HistoryContent historyContent = HistoryContentCreator.createPerDayHistoryContent(historyByGrowDay);

            Map<Integer, List<Data>> histMap = historyContent.getHistoryContentPerDay();

            List <Data> dataList = histMap.get(0);

            DataDao dataDao = DbImplDecider.use(DaoType.MYSQL).getDao(DataDao.class);

            List<Map<Integer, Data>> dataHistoryList = new ArrayList<Map<Integer, Data>>();

        } catch (Exception e) {
            logger.error(NO_DATA_AVAILABLE, "No data Graphs");
            filename = "public_error_500x300.png";
        }

        return filename;
    }

//    //***************************************************************************************************************************************************************************************
//    public static String generateChartFlockEggs(Long flockId, String fromDay, String toDay, HttpSession session, PrintWriter pw, Locale locale) {
//
//        String filenamehot;
//
//        FromDayToDay growDayRangeParam = new FromDayToDay(fromDay, toDay);
//        try {
//            Long langId = localeService.getLanguageId(locale.getLanguage());
//
//            FlockHistoryService flockHistoryService = new FlockHistoryServiceImpl();
//            Map<Integer, String> historyByGrowDay = flockHistoryService.getFlockPerDayNotParsedReportsWithinRange(flockId, growDayRangeParam);
//
//            DataDao dataDao = DbImplDecider.use(DaoType.MYSQL).getDao(DataDao.class);
//
//            Data data1 = dataDao.getById(PerGrowDayHistoryDataType.TOTAL_EGGS_DEV_1.getId(), langId);
//            Data data2 = dataDao.getById(PerGrowDayHistoryDataType.TOTAL_EGGS_DEV_2.getId(), langId);
//            Data data3 = dataDao.getById(PerGrowDayHistoryDataType.TOTAL_EGGS_DEV_3.getId(), langId);
//            Data data4 = dataDao.getById(PerGrowDayHistoryDataType.TOTAL_EGGS_DEV_4.getId(), langId);
//            Data data5 = dataDao.getById(PerGrowDayHistoryDataType.TOTAL_EGGS_DEV_5.getId(), langId);
//            Data data6 = dataDao.getById(PerGrowDayHistoryDataType.TOTAL_EGGS_DEV_6.getId(), langId);
//            Data data7 = dataDao.getById(PerGrowDayHistoryDataType.TOTAL_EGGS_DEV_7.getId(), langId);
//            Data data8 = dataDao.getById(PerGrowDayHistoryDataType.TOTAL_EGGS_DEV_8.getId(), langId);
//            Data data9 = dataDao.getById(PerGrowDayHistoryDataType.TOTAL_EGGS_DEV_9.getId(), langId);
//            Data data10 = dataDao.getById(PerGrowDayHistoryDataType.TOTAL_EGGS_DEV_10.getId(), langId);
//            Data data11 = dataDao.getById(PerGrowDayHistoryDataType.TOTAL_EGGS_DEV_11.getId(), langId);
//            Data data12 = dataDao.getById(PerGrowDayHistoryDataType.TOTAL_EGGS_DEV_12.getId(), langId);
//
////            List<Data> dataTotalEggsList = new ArrayList<Data>();
//
////            dataTotalEggsList.add(data1);
////            dataTotalEggsList.add(data2);
////            dataTotalEggsList.add(data3);
////            dataTotalEggsList.add(data4);
////            dataTotalEggsList.add(data5);
////            dataTotalEggsList.add(data6);
////            dataTotalEggsList.add(data7);
////            dataTotalEggsList.add(data8);
////            dataTotalEggsList.add(data9);
////            dataTotalEggsList.add(data10);
////            dataTotalEggsList.add(data11);
////            dataTotalEggsList.add(data12);
//
//            List<String> axisTitles = new ArrayList<String>();
//
//            axisTitles.add(data1.getLabel());
//            axisTitles.add(data2.getLabel());
//            axisTitles.add(data3.getLabel());
//            axisTitles.add(data4.getLabel());
//            axisTitles.add(data5.getLabel());
//            axisTitles.add(data6.getLabel());
//            axisTitles.add(data7.getLabel());
//            axisTitles.add(data8.getLabel());
//            axisTitles.add(data9.getLabel());
//            axisTitles.add(data10.getLabel());
//            axisTitles.add(data11.getLabel());
//            axisTitles.add(data12.getLabel());
//
//            HistoryContent historyContent = HistoryContentCreator.createPerDayHistoryContent(historyByGrowDay);
//
//            Map<Integer, List<Data>> historyContentMap = historyContent.getHistoryContentPerDay();
////            historyContentMap = historyContent.getHistoryContentPerDay();
//
//            List<Map<Integer, Data>> dataHistoryList = new ArrayList<Map<Integer, Data>>();
//
//            dataHistoryList.add(DataGraphCreator.createHistoryData(historyContentMap, data1));
//            dataHistoryList.add(DataGraphCreator.createHistoryData(historyContentMap, data2));
//            dataHistoryList.add(DataGraphCreator.createHistoryData(historyContentMap, data3));
//            dataHistoryList.add(DataGraphCreator.createHistoryData(historyContentMap, data4));
//            dataHistoryList.add(DataGraphCreator.createHistoryData(historyContentMap, data5));
//            dataHistoryList.add(DataGraphCreator.createHistoryData(historyContentMap, data6));
//            dataHistoryList.add(DataGraphCreator.createHistoryData(historyContentMap, data7));
//            dataHistoryList.add(DataGraphCreator.createHistoryData(historyContentMap, data8));
//            dataHistoryList.add(DataGraphCreator.createHistoryData(historyContentMap, data9));
//            dataHistoryList.add(DataGraphCreator.createHistoryData(historyContentMap, data10));
//            dataHistoryList.add(DataGraphCreator.createHistoryData(historyContentMap, data11));
//            dataHistoryList.add(DataGraphCreator.createHistoryData(historyContentMap, data12));
//
//            HashMap<String, String> dictionary = createDictionary(locale);
//
//            String title = "Daily Eggs";    // "Daily Mortality";
//            String xAxisLabel = dictionary.get("graph.heater.on.time.axis.grow.day");    // "Grow Day[Day]";
//            String yAxisLabel = "Eggs";    // "Birds";
//
//            HistoryGraph eggsGraph = new HistoryGraph();
//
//            eggsGraph.setDataHistoryList(dataHistoryList);
//
//            if (!growDayRangeParam.useRange()) {
//                growDayRangeParam.setFromDay(1);
//                growDayRangeParam.setToDay(dataHistoryList.get(0).size());
//            }
//
//            eggsGraph.createChart(title, growDayRangeParam.toString(), xAxisLabel, yAxisLabel);
//
//            // Write the chart image to the temporary directory
//            ChartRenderingInfo info = new ChartRenderingInfo(new StandardEntityCollection());
//
//            filenamehot = ServletUtilities.saveChartAsPNG(eggsGraph.getChart(), 800, 400, info, session);
//
//            // Write the image map to the PrintWriter
//            ChartUtilities.writeImageMap(pw, filenamehot, info, false);
//            session.setAttribute("filenamehot-flockid=" + flockId + "&fromday=" + fromDay + "&today=" + toDay, filenamehot);
//            pw.flush();
//
//        } catch (Exception e) {
//            logger.error(NO_DATA_AVAILABLE, "Eggs");
//            filenamehot = "public_error_500x300.png";
//        }
//
//        return filenamehot;
//    }
//    //************************************************************************************************************************************************************************************************

    /**
     * Creates temperature and humidity chart and return path to png file with map of each series .
     *
     * @param controllerId controller id
     * @param session      the session
     * @param pw           PrintWriter object
     * @param locale       the current locale
     * @return the path to png file with chart
     */


    public static String generateChartTempHum(Long controllerId, HttpSession session, PrintWriter pw, Locale locale) {//last 24 hour
        String filenameth;
        ControllerDao controllerDao;
        String values;
        DataDao dataDao;
        Data setClock;
        InsideOutsideHumidityGraph graph;
        ChartRenderingInfo info;
        try {
            controllerDao = DbImplDecider.use(DaoType.MYSQL).getDao(ControllerDao.class);
            values = controllerDao.getControllerGraph(controllerId);
            dataDao = DbImplDecider.use(DaoType.MYSQL).getDao(DataDao.class);
            setClock = dataDao.getSetClockByController(controllerId);
            if (values == null) {
                logger.error(NO_DATA_AVAILABLE, "Temperature and Humidity");
                throw new Exception(NO_DATA_AVAILABLE);
            } else {
                if (setClock.getValue() == null) {
                    graph = new InsideOutsideHumidityGraph(values, Long.valueOf("0"), locale);
                } else {
                    graph = new InsideOutsideHumidityGraph(values, setClock.getValueToUI(), locale);
                }
                // Write the chart image to the temporary directory
                info = new ChartRenderingInfo(new StandardEntityCollection());
                filenameth = ServletUtilities.saveChartAsPNG(graph.createChart(), 800, 400, info, session);
                // Write the image map to the PrintWriter
                ChartUtilities.writeImageMap(pw, filenameth, info, false);
                session.setAttribute("filenameth", filenameth);
                pw.flush();
            }
        } catch (Exception e) {
            logger.error(NO_DATA_AVAILABLE, "Temperature/Humidity");
            filenameth = "public_error_500x300.png";
        }
        return filenameth;
    }

    /**
     * Creates feed and water chart and return path to png file with map of each series .
     *
     * @param controllerId controller id
     * @param session      the session
     * @param pw           PrintWriter object
     * @param locale       the current locale
     * @return the path to png file with chart
     */
    public static String generateChartWaterFeedTemp(Long controllerId, HttpSession session, PrintWriter pw, Locale locale) {//last 24 hour
        String filenamewft;
        ControllerDao controllerDao;
        String values;
        DataDao dataDao;
        Data setClock;
        FeedWaterConsumpTempGraph graph;
        ChartRenderingInfo info;
        try {
            controllerDao = DbImplDecider.use(DaoType.MYSQL).getDao(ControllerDao.class);
            values = controllerDao.getControllerGraph(controllerId);
            dataDao = DbImplDecider.use(DaoType.MYSQL).getDao(DataDao.class);
            setClock = dataDao.getSetClockByController(controllerId);
            if (values == null) {
                logger.error(NO_DATA_AVAILABLE, "Feed/Water");
                throw new Exception(NO_DATA_AVAILABLE);
            } else {
                if (setClock.getValue() == null) {
                    graph = new FeedWaterConsumpTempGraph(values, Long.valueOf("0"), locale);
                } else {
                    graph = new FeedWaterConsumpTempGraph(values, setClock.getValueToUI(), locale);
                }
                // Write the chart image to the temporary directory
                info = new ChartRenderingInfo(new StandardEntityCollection());
                filenamewft = ServletUtilities.saveChartAsPNG(graph.createChart(), 800, 400, info, session);
                // Write the image map to the PrintWriter
                ChartUtilities.writeImageMap(pw, filenamewft, info, false);
                session.setAttribute("filenamewct", filenamewft);
                pw.flush();
            }
        } catch (Exception e) {
            logger.error(NO_DATA_AVAILABLE, "Feed/Water");
            filenamewft = "public_error_500x300.png";
        }
        return filenamewft;
    }

    public static String generateChartFeedWaterPerBird24(Long controllerId, HttpSession session, PrintWriter pw, Locale locale) {// last 24 hour
        String filenamewft;
        ControllerDao controllerDao;
        String values;
        DataDao dataDao;
        Data setClock;
        FeedWaterPerBirdGraph graph;
        ChartRenderingInfo info;
        try {
            controllerDao = DbImplDecider.use(DaoType.MYSQL).getDao(ControllerDao.class);
            values = controllerDao.getControllerGraph(controllerId);
            dataDao = DbImplDecider.use(DaoType.MYSQL).getDao(DataDao.class);
            setClock = dataDao.getSetClockByController(controllerId);
            if (values == null) {
                logger.error(NO_DATA_AVAILABLE, "Feed/Water per Bird");
                throw new Exception(NO_DATA_AVAILABLE);
            } else {
                if (setClock.getValue() == null) {
                    graph = new FeedWaterPerBirdGraph(values, Long.valueOf("0"), locale);
                } else {
                    graph = new FeedWaterPerBirdGraph(values, setClock.getValueToUI(), locale);
                }
                // Write the chart image to the temporary directory
                info = new ChartRenderingInfo(new StandardEntityCollection());
                filenamewft = ServletUtilities.saveChartAsPNG(graph.createChart(), 800, 400, info, session);
                // Write the image map to the PrintWriter
                ChartUtilities.writeImageMap(pw, filenamewft, info, false);
                session.setAttribute("filenamewft", filenamewft);
                pw.flush();
            }
        } catch (Exception e) {
            logger.error(NO_DATA_AVAILABLE, "Feed/Water per Bird");
            filenamewft = "public_error_500x300.png";
        }
        return filenamewft;
    }

    public static String generateChartFeed2Water2_24(Long controllerId, HttpSession session, PrintWriter pw, Locale locale) {// last 24 hour
        String filenamewft;
        ControllerDao controllerDao;
        String values;
        DataDao dataDao;
        Data setClock;
        Feed2Water2ConsGraph graph;
        ChartRenderingInfo info;
        try {
            controllerDao = DbImplDecider.use(DaoType.MYSQL).getDao(ControllerDao.class);
            values = controllerDao.getControllerGraph(controllerId);
            dataDao = DbImplDecider.use(DaoType.MYSQL).getDao(DataDao.class);
            setClock = dataDao.getSetClockByController(controllerId);
            if (values == null) {
                logger.error(NO_DATA_AVAILABLE, "Feed2/Water2");
                throw new Exception(NO_DATA_AVAILABLE);
            } else {
                if (setClock.getValue() == null) {
                    graph = new Feed2Water2ConsGraph(values, Long.valueOf("0"), locale);
                } else {
                    graph = new Feed2Water2ConsGraph(values, setClock.getValueToUI(), locale);
                }
                // Write the chart image to the temporary directory
                info = new ChartRenderingInfo(new StandardEntityCollection());
                filenamewft = ServletUtilities.saveChartAsPNG(graph.createChart(), 800, 400, info, session);
                // Write the image map to the PrintWriter
                ChartUtilities.writeImageMap(pw, filenamewft, info, false);
                session.setAttribute("filenamewft", filenamewft);
                pw.flush();
            }
        } catch (Exception e) {
            logger.error(NO_DATA_AVAILABLE, "Feed2/Water2");
            filenamewft = "public_error_500x300.png";
        }
        return filenamewft;
    }

    public static String generateChartWaterSum_24(Long controllerId, HttpSession session, PrintWriter pw, Locale locale) {// last 24 hour
        String filenamewft;
        ControllerDao controllerDao;
        String values;
        DataDao dataDao;
        Data setClock;
        WaterSumConsGraph graph;
        ChartRenderingInfo info;
        try {
            controllerDao = DbImplDecider.use(DaoType.MYSQL).getDao(ControllerDao.class);
            values = controllerDao.getControllerGraph(controllerId);
            dataDao = DbImplDecider.use(DaoType.MYSQL).getDao(DataDao.class);
            setClock = dataDao.getSetClockByController(controllerId);
            if (values == null) {
                logger.error(NO_DATA_AVAILABLE, "Water Sum");
                throw new Exception(NO_DATA_AVAILABLE);
            } else {
                if (setClock.getValue() == null) {
                    graph = new WaterSumConsGraph(values, Long.valueOf("0"), locale);
                } else {
                    graph = new WaterSumConsGraph(values, setClock.getValueToUI(), locale);
                }
                // Write the chart image to the temporary directory
                info = new ChartRenderingInfo(new StandardEntityCollection());
                filenamewft = ServletUtilities.saveChartAsPNG(graph.createChart(), 800, 400, info, session);
                // Write the image map to the PrintWriter
                ChartUtilities.writeImageMap(pw, filenamewft, info, false);
                session.setAttribute("filenamewft", filenamewft);
                pw.flush();
            }
        } catch (Exception e) {
            logger.error(NO_DATA_AVAILABLE, "Water Sum");
            filenamewft = "public_error_500x300.png";
        }
        return filenamewft;
    }

    /**
     * Creates temperature and humidity chart and return path to png file with map of each series .
     *
     * @param flockId the flock id
     * @param session the session
     * @param pw      PrintWriter object
     * @param locale  the current locale
     * @return the path to png file with chart
     */
    public static String generateChartFlockTempHum(Long flockId, String growDay, HttpSession session, PrintWriter pw, Locale locale) {//D18 temp_in, D19 temp_out, D20 hum!
        DayParam growDayParam;
        Long langId;
        String filenameth;
        FlockHistoryService flockHistoryService;
        Collection<String> historyValueList;
        StringBuilder chainedHistoryValues;
        Long resetTime;
        PerHourReportGraph perHourReportGraph;
        ChartRenderingInfo info;
        int i;

        langId = 1L;
        i = 0;
        growDayParam = new DayParam(growDay);
        try {
            flockHistoryService = new FlockHistoryServiceImpl();
            historyValueList = flockHistoryService.getFlockPerHourReportsTitlesUsingGraphObjects(flockId, growDayParam.getGrowDay(), langId);
            chainedHistoryValues = new StringBuilder();
            for (String historyValues : historyValueList) {
//                chainedHistoryValues.append(historyValues);
                if(i == 0) {
                    i++;
                    chainedHistoryValues.append(historyValues);
                } else {
                    chainedHistoryValues.append(" ");
                    chainedHistoryValues.append(historyValues);
                }
            }
            i = 0;
            resetTime = flockHistoryService.getResetTime(flockId, growDayParam.getGrowDay());
            perHourReportGraph = new InsideOutsideHumidityGraph(chainedHistoryValues.toString(), resetTime, locale);

            // Write the chart image to the temporary directory
            info = new ChartRenderingInfo(new StandardEntityCollection());
            filenameth = ServletUtilities.saveChartAsPNG(perHourReportGraph.createChart(), 800, 400, info, session);

            // Write the image map to the PrintWriter
            ChartUtilities.writeImageMap(pw, filenameth, info, false);
            session.setAttribute("filenameth-flockid=" + flockId + "&growday=" + growDay, filenameth);
            pw.flush();
        } catch (Exception e) {
            logger.error(NO_DATA_AVAILABLE, "Temperature and Humidity");
            filenameth = "public_error_500x300.png";
        }
        return filenameth;
    }

    /**
     * Creates water and feed chart and return path to png file with map of each series .
     *
     * @param flockId the flock id
     * @param session the session
     * @param pw      PrintWriter object
     * @param locale  the current locale
     * @return the path to png file with chart
     */
    public static String generateChartFlockWaterFeedTemp(Long flockId, String growDay, HttpSession session, PrintWriter pw, Locale locale) {//D72 feed, D21 water! !!!
        DayParam growDayParam;
        String filenamewft;
        Long langId;
        FlockHistoryService flockHistoryService;
        Collection<String> historyValueList;
        StringBuilder chainedHistoryValues;
        Long resetTime;
        PerHourReportGraph perHourReportGraph;
        String historyValuesStr;
        int i;

        growDayParam = new DayParam(growDay);
        langId = 1L;
        i = 0;
        try {
            flockHistoryService = new FlockHistoryServiceImpl();
            historyValueList = flockHistoryService.getFlockPerHourReportsTitlesUsingGraphObjects(flockId, growDayParam.getGrowDay(), langId);
            chainedHistoryValues = new StringBuilder();
            for (String historyValues : historyValueList) {
//                chainedHistoryValues.append(historyValues);
                if(i == 0) {
                    i++;
                    chainedHistoryValues.append(historyValues);
                } else {
                    chainedHistoryValues.append(" ");
                    chainedHistoryValues.append(historyValues);
                }
            }
            i = 0;
            resetTime = flockHistoryService.getResetTime(flockId, growDayParam.getGrowDay());
            perHourReportGraph = new FeedWaterConsumpTempGraph(chainedHistoryValues.toString(), resetTime, locale);

            // Write the chart image to the temporary directory
            ChartRenderingInfo info = new ChartRenderingInfo(new StandardEntityCollection());
            filenamewft = ServletUtilities.saveChartAsPNG(perHourReportGraph.createChart(), 800, 400, info, session);

            // Write the image map to the PrintWriter
            ChartUtilities.writeImageMap(pw, filenamewft, info, false);
            session.setAttribute("filenamewct", filenamewft);
            pw.flush();
        } catch (Exception e) {
            logger.error(NO_DATA_AVAILABLE, "Feed/Water/Temp");
            filenamewft = "public_error_500x300.png";
        }
        return filenamewft;
    }

    public static String generateChartFlockWaterFeedTempSpecial(Long flockId, String growDay, HttpSession session, PrintWriter pw, Locale locale) {//D feed, D21 water!
        DayParam growDayParam;
        String filenamewft;
        Long langId;
        FlockHistoryService flockHistoryService;
        Collection<String> historyValueList;
        StringBuilder chainedHistoryValues;
        Long resetTime;
        PerHourReportGraph perHourReportGraph;
        String historyValuesStr;
        int i;

        growDayParam = new DayParam(growDay);
        langId = 1L;
        i = 0;
        try {
            flockHistoryService = new FlockHistoryServiceImpl();
            historyValueList = flockHistoryService.getFlockPerHourReportsTitlesUsingGraphObjectsSpecial(flockId, growDayParam.getGrowDay(), langId);
            chainedHistoryValues = new StringBuilder();
            for (String historyValues : historyValueList) {
//                chainedHistoryValues.append(historyValues);
                if(i == 0) {
                    i++;
                    chainedHistoryValues.append(historyValues);
                } else {
                    chainedHistoryValues.append(" ");
                    chainedHistoryValues.append(historyValues);
                }
            }
            i = 0;
            resetTime = flockHistoryService.getResetTime(flockId, growDayParam.getGrowDay());
            perHourReportGraph = new FeedWaterConsumpTempGraph(chainedHistoryValues.toString(), resetTime, locale);

            // Write the chart image to the temporary directory
            ChartRenderingInfo info = new ChartRenderingInfo(new StandardEntityCollection());
            filenamewft = ServletUtilities.saveChartAsPNG(perHourReportGraph.createChart(), 800, 400, info, session);

            // Write the image map to the PrintWriter
            ChartUtilities.writeImageMap(pw, filenamewft, info, false);
            session.setAttribute("filenamewct", filenamewft);
            pw.flush();
        } catch (Exception e) {
            logger.error(NO_DATA_AVAILABLE, "Feed/Water/Temp");
            filenamewft = "public_error_500x300.png";
        }
        return filenamewft;
    }

    public static String generateChartFlockFeed2Water2(Long flockId, String growDay, HttpSession session, PrintWriter pw, Locale locale) {// D73 feed2, D71 water2
        DayParam growDayParam;
        String filenamewft;
        Long langId;
        FlockHistoryService flockHistoryService;
        Collection<String> historyValueList;
        StringBuilder chainedHistoryValues;
        Long resetTime;
        PerHourReportGraph perHourReportGraph;
        int i;

        growDayParam = new DayParam(growDay);
        langId = 1L;
        i = 0;
        try {
            flockHistoryService = new FlockHistoryServiceImpl();
            historyValueList = flockHistoryService.getFlockPerHourReportsTitlesUsingGraphObjects(flockId, growDayParam.getGrowDay(), langId);
            chainedHistoryValues = new StringBuilder();
//            for (String historyValues : historyValueList) {
//                chainedHistoryValues.append(historyValues);
//            }
            for (String historyValues : historyValueList) {
//                chainedHistoryValues.append(historyValues);
                if(i == 0) {
                    i++;
                    chainedHistoryValues.append(historyValues);
                } else {
                    chainedHistoryValues.append(" ");
                    chainedHistoryValues.append(historyValues);
                }
            }
            i = 0;
            resetTime = flockHistoryService.getResetTime(flockId, growDayParam.getGrowDay());
            perHourReportGraph = new Feed2Water2ConsGraph(chainedHistoryValues.toString(), resetTime, locale);//

            // Write the chart image to the temporary directory
            ChartRenderingInfo info = new ChartRenderingInfo(new StandardEntityCollection());
            filenamewft = ServletUtilities.saveChartAsPNG(perHourReportGraph.createChart(), 800, 400, info, session);

            // Write the image map to the PrintWriter
            ChartUtilities.writeImageMap(pw, filenamewft, info, false);
            session.setAttribute("filenamewct", filenamewft);
            pw.flush();
        } catch (Exception e) {
            logger.error(NO_DATA_AVAILABLE, "Feed2/Water2");
            filenamewft = "public_error_500x300.png";
        }
        return filenamewft;
    }

    /**
     * Creates water and feed chart and return path to png file with map of each series .
     *
     * @param flockId the flock id
     * @param session the session
     * @param pw      PrintWriter object
     * @param locale  the current locale
     * @return the path to png file with chart
     */
    public static String generateChartFlockWaterFeed(Long flockId, String fromDay, String toDay, HttpSession session, PrintWriter pw, Locale locale) {
        String filenamefw;
        FromDayToDay growDayRangeParam;
        Long langId;
        HashMap<String, String> dictionary;
        String title;
        String y1AxisLabel;
        String y2AxisLabel;
        String xAxisLabel;
        FlockHistoryService flockHistoryService;
        Map<Integer, String> historyByGrowDay;
        List<Map<Integer, Data>> dataHistoryList;
        DataDao dataDao;
        Data data1;
        Data data2;
        List<String> axisTitles;
        HistoryGraph waterFeedGraph;
        ChartRenderingInfo info;

        growDayRangeParam = new FromDayToDay(fromDay, toDay);
        try {
            langId = localeService.getLanguageId(locale.getLanguage());

            dictionary = createDictionary(locale);
            title = dictionary.get("graph.feed.water.title");
            y1AxisLabel = dictionary.get("graph.feed.water.axis.feed");
            y2AxisLabel = dictionary.get("graph.feed.water.axis.water");
            xAxisLabel = dictionary.get("graph.feed.water.axis.grow.day");

            flockHistoryService = new FlockHistoryServiceImpl();
            historyByGrowDay = flockHistoryService.getFlockPerDayNotParsedReportsWithinRange(flockId, growDayRangeParam);

            dataHistoryList = new ArrayList<Map<Integer, Data>>();
            dataDao = DbImplDecider.use(DaoType.MYSQL).getDao(DataDao.class);
            data1 = dataDao.getById(PerGrowDayHistoryDataType.FEED_CONSUMPTION_ID.getId(), langId);

            axisTitles = new ArrayList<String>();
            axisTitles.add(data1.getLabel());
            dataHistoryList.add(DataGraphCreator.createHistoryDataByGrowDay(historyByGrowDay, data1));

            waterFeedGraph = new HistoryGraph();
            waterFeedGraph.setDataHistoryList(dataHistoryList);
            if (!growDayRangeParam.useRange()) {
                growDayRangeParam.setFromDay(1);
                growDayRangeParam.setToDay(dataHistoryList.get(0).size());
            }

            waterFeedGraph.createChart(title, growDayRangeParam.toString(), xAxisLabel, y1AxisLabel);

            data2 = dataDao.getById(PerGrowDayHistoryDataType.WATER_CONSUMPTION_ID.getId(), langId);
            axisTitles.add(data2.getLabel());
            dataHistoryList.clear();
            dataHistoryList.add(DataGraphCreator.createHistoryDataByGrowDay(historyByGrowDay, data2));
            waterFeedGraph.createAndAddSeriesCollection(dataHistoryList, y2AxisLabel);

            // Write the chart image to the temporary directory
            info = new ChartRenderingInfo(new StandardEntityCollection());
            filenamefw = ServletUtilities.saveChartAsPNG(waterFeedGraph.getChart(), 800, 400, info, session);

            // Write the image map to the PrintWriter
            ChartUtilities.writeImageMap(pw, filenamefw, info, false);
            session.setAttribute("filenamefw-flockid=" + flockId + "&fromday=" + fromDay + "&today=" + toDay, filenamefw);

            pw.flush();
        } catch (Exception e) {
            logger.error(NO_DATA_AVAILABLE, "Water/Feed");
            filenamefw = "public_error_500x300.png";
        }
        return filenamefw;
    }

    public static String generateChartFlockWaterFeedSpecial(Long flockId, String fromDay, String toDay, HttpSession session, PrintWriter pw, Locale locale) {
        String filenamefw;
        FromDayToDay growDayRangeParam;
        Long langId;
        HashMap<String, String> dictionary;
        String title;
        String y1AxisLabel;
        String y2AxisLabel;
        String xAxisLabel;
        FlockHistoryService flockHistoryService;
        Map<Integer, String> historyByGrowDay;
        List<Map<Integer, Data>> dataHistoryList;
        DataDao dataDao;
        Data data1;
        Data data2;
        List<String> axisTitles;
        HistoryGraph waterFeedGraph;
        ChartRenderingInfo info;

        growDayRangeParam = new FromDayToDay(fromDay, toDay);
        try {
            langId = localeService.getLanguageId(locale.getLanguage());

            dictionary = createDictionary(locale);
            title = dictionary.get("graph.feed.water.title");
            y1AxisLabel = dictionary.get("graph.feed.water.axis.feed");
            y2AxisLabel = dictionary.get("graph.feed.water.axis.water");
            xAxisLabel = dictionary.get("graph.feed.water.axis.grow.day");

            flockHistoryService = new FlockHistoryServiceImpl();
            historyByGrowDay = flockHistoryService.getFlockPerDayNotParsedReportsWithinRange(flockId, growDayRangeParam);

            dataHistoryList = new ArrayList<Map<Integer, Data>>();
            dataDao = DbImplDecider.use(DaoType.MYSQL).getDao(DataDao.class);
            data1 = dataDao.getById(PerGrowDayHistoryDataType.FEED_CONSUMPTION_SPECIAL.getId(), langId);

            axisTitles = new ArrayList<String>();
            axisTitles.add(data1.getLabel());
            dataHistoryList.add(DataGraphCreator.createHistoryDataByGrowDay(historyByGrowDay, data1));

            waterFeedGraph = new HistoryGraph();
            waterFeedGraph.setDataHistoryList(dataHistoryList);
            if (!growDayRangeParam.useRange()) {
                growDayRangeParam.setFromDay(1);
                growDayRangeParam.setToDay(dataHistoryList.get(0).size());
            }

            waterFeedGraph.createChart(title, growDayRangeParam.toString(), xAxisLabel, y1AxisLabel);

            data2 = dataDao.getById(PerGrowDayHistoryDataType.WATER_CONSUMPTION_ID.getId(), langId);
            axisTitles.add(data2.getLabel());
            dataHistoryList.clear();
            dataHistoryList.add(DataGraphCreator.createHistoryDataByGrowDay(historyByGrowDay, data2));
            waterFeedGraph.createAndAddSeriesCollection(dataHistoryList, y2AxisLabel);

            // Write the chart image to the temporary directory
            info = new ChartRenderingInfo(new StandardEntityCollection());
            filenamefw = ServletUtilities.saveChartAsPNG(waterFeedGraph.getChart(), 800, 400, info, session);

            // Write the image map to the PrintWriter
            ChartUtilities.writeImageMap(pw, filenamefw, info, false);
            session.setAttribute("filenamefw-flockid=" + flockId + "&fromday=" + fromDay + "&today=" + toDay, filenamefw);

            pw.flush();
        } catch (Exception e) {
            logger.error(NO_DATA_AVAILABLE, "Water/Feed");
            filenamefw = "public_error_500x300.png";
        }
        return filenamefw;
    }

    /**
     * Creates water and feed chart and return path to png file with map of each series .
     *
     * @param flockId the flock id
     * @param session the session
     * @param pw      PrintWriter object
     * @param locale  the current locale
     * @return the path to png file with chart
     */
    public static String generateChartFlockAverageWeight(Long flockId, String fromDay, String toDay, HttpSession session, PrintWriter pw, Locale locale) {
        String filenameaw;
        FromDayToDay growDayRangeParam;
        Long langId;
        FlockHistoryService flockHistoryService;
        Map<Integer, String> historyByGrowDay;
        DataDao dataDao;
        List<Map<Integer, Data>> dataHistoryList;
        Data data1;
        Data data2;
        Data data3;
        Data data4;
        List<String> axisTitles;
        HistoryGraph averageWeightGraph;
        HashMap<String, String> dictionary;
        String title;
        String xAxisLabel;
        String yAxisLabel;
        ChartRenderingInfo info;

        growDayRangeParam = new FromDayToDay(fromDay, toDay);
        try {
            langId = localeService.getLanguageId(locale.getLanguage());

            flockHistoryService = new FlockHistoryServiceImpl();
            historyByGrowDay = flockHistoryService.getFlockPerDayNotParsedReportsWithinRange(flockId, growDayRangeParam);

            dataDao = DbImplDecider.use(DaoType.MYSQL).getDao(DataDao.class);
            dataHistoryList = new ArrayList<Map<Integer, Data>>();

            data1 = dataDao.getById(PerGrowDayHistoryDataType.AVERAGE_WEIGHT_1_ID.getId(), langId);
            dataHistoryList.add(DataGraphCreator.createHistoryDataByGrowDay(historyByGrowDay, data1));

            data2 = dataDao.getById(PerGrowDayHistoryDataType.AVERAGE_WEIGHT_2_ID.getId(), langId);
            dataHistoryList.add(DataGraphCreator.createHistoryDataByGrowDay(historyByGrowDay, data2));

            data3 = dataDao.getById(PerGrowDayHistoryDataType.AVERAGE_WEIGHT_3_ID.getId(), langId);
            dataHistoryList.add(DataGraphCreator.createHistoryDataByGrowDay(historyByGrowDay, data3));

            data4 = dataDao.getById(PerGrowDayHistoryDataType.AVERAGE_WEIGHT_4_ID.getId(), langId);
            dataHistoryList.add(DataGraphCreator.createHistoryDataByGrowDay(historyByGrowDay, data4));


            axisTitles = new ArrayList<String>();
            axisTitles.add(data1.getLabel());
            axisTitles.add(data2.getLabel());
            axisTitles.add(data3.getLabel());
            axisTitles.add(data4.getLabel());

            averageWeightGraph = new HistoryGraph();
            averageWeightGraph.setDataHistoryList(dataHistoryList);

            dictionary = createDictionary(locale);
            title = dictionary.get("graph.average.weight.title");    // "Daily Mortality";
            xAxisLabel = dictionary.get("graph.average.weight.axis.grow.day");    // "Grow Day[Day]";
            yAxisLabel = dictionary.get("graph.average.weight.axis.weight");    // "Birds";
            if (!growDayRangeParam.useRange()) {
                growDayRangeParam.setFromDay(1);
                growDayRangeParam.setToDay(dataHistoryList.get(0).size());
            }
            averageWeightGraph.createChart(title, growDayRangeParam.toString(), xAxisLabel, yAxisLabel);
            // Write the chart image to the temporary directory
            info = new ChartRenderingInfo(new StandardEntityCollection());
            filenameaw = ServletUtilities.saveChartAsPNG(averageWeightGraph.getChart(), 800, 400, info, session);

            // Write the image map to the PrintWriter
            ChartUtilities.writeImageMap(pw, filenameaw, info, false);
            session.setAttribute("filenameaw-flockid=" + flockId + "&fromday=" + fromDay + "&today=" + toDay, filenameaw);
            pw.flush();
        } catch (Exception e) {
            logger.error(NO_DATA_AVAILABLE, "Average Weight");
            filenameaw = "public_error_500x300.png";
        }
        return filenameaw;
    }

    /**
     * Creates heaters on time chart and return path to png file with map of each series .
     *
     * @param flockId the flock id
     * @param session the session
     * @param pw      PrintWriter object
     * @param locale  the current locale
     * @return the path to png file with chart
     */
    public static String generateChartFlockHeatOnTime(Long flockId, String fromDay, String toDay, HttpSession session, PrintWriter pw, Locale locale) {
        String filenamehot;
        FromDayToDay growDayRangeParam;
        Long langId;
        FlockHistoryService flockHistoryService;
        Map<Integer, String> historyByGrowDay;
        DataDao dataDao;
        Data data1;
        List<String> axisTitles;
        List<Map<Integer, Data>> dataHistoryList;
        Data data2;
        Data data3;
        Data data4;
        Data data5;
        Data data6;
        HashMap<String, String> dictionary;
        String title;
        String xAxisLabel;
        String yAxisLabel;
        HistoryGraph heatontimeGraph;
        ChartRenderingInfo info;

        growDayRangeParam = new FromDayToDay(fromDay, toDay);
        try {
            langId = localeService.getLanguageId(locale.getLanguage());

            flockHistoryService = new FlockHistoryServiceImpl();
            historyByGrowDay = flockHistoryService.getFlockPerDayNotParsedReportsWithinRange(flockId, growDayRangeParam);

            dataDao = DbImplDecider.use(DaoType.MYSQL).getDao(DataDao.class);
            data1 = dataDao.getById(PerGrowDayHistoryDataType.HEATER_1_TIME_ON.getId(), langId);

            axisTitles = new ArrayList<String>();
            axisTitles.add(data1.getLabel());

            dataHistoryList = new ArrayList<Map<Integer, Data>>();
            dataHistoryList.add(DataGraphCreator.createHistoryDataByGrowDay(historyByGrowDay, data1));

            data2 = dataDao.getById(PerGrowDayHistoryDataType.HEATER_2_TIME_ON.getId(), langId);

            axisTitles.add(data2.getLabel());
            dataHistoryList.add(DataGraphCreator.createHistoryDataByGrowDay(historyByGrowDay, data2));

            data3 = dataDao.getById(PerGrowDayHistoryDataType.HEATER_3_TIME_ON.getId(), langId);

            axisTitles.add(data3.getLabel());
            dataHistoryList.add(DataGraphCreator.createHistoryDataByGrowDay(historyByGrowDay, data3));

            data4 = dataDao.getById(PerGrowDayHistoryDataType.HEATER_4_TIME_ON.getId(), langId);

            axisTitles.add(data4.getLabel());
            dataHistoryList.add(DataGraphCreator.createHistoryDataByGrowDay(historyByGrowDay, data4));

            data5 = dataDao.getById(PerGrowDayHistoryDataType.HEATER_5_TIME_ON.getId(), langId);

            axisTitles.add(data5.getLabel());
            dataHistoryList.add(DataGraphCreator.createHistoryDataByGrowDay(historyByGrowDay, data5));

            data6 = dataDao.getById(PerGrowDayHistoryDataType.HEATER_6_TIME_ON.getId(), langId);

            axisTitles.add(data6.getLabel());
            dataHistoryList.add(DataGraphCreator.createHistoryDataByGrowDay(historyByGrowDay, data6));

            dictionary = createDictionary(locale);
            title = dictionary.get("graph.heater.on.time.title");    // "Daily Mortality";
            xAxisLabel = dictionary.get("graph.heater.on.time.axis.grow.day");    // "Grow Day[Day]";
            yAxisLabel = dictionary.get("graph.heater.on.time.axis.time");    // "Birds";

            heatontimeGraph = new HistoryGraph();
            heatontimeGraph.setDataHistoryList(dataHistoryList);
            if (!growDayRangeParam.useRange()) {
                growDayRangeParam.setFromDay(1);
                growDayRangeParam.setToDay(dataHistoryList.get(0).size());
            }
            heatontimeGraph.createChart(title, growDayRangeParam.toString(), xAxisLabel, yAxisLabel);
            // Write the chart image to the temporary directory
            info = new ChartRenderingInfo(new StandardEntityCollection());
            filenamehot = ServletUtilities.saveChartAsPNG(heatontimeGraph.getChart(), 800, 400, info, session);

            // Write the image map to the PrintWriter
            ChartUtilities.writeImageMap(pw, filenamehot, info, false);
            session.setAttribute("filenamehot-flockid=" + flockId + "&fromday=" + fromDay + "&today=" + toDay, filenamehot);
            pw.flush();
        } catch (Exception e) {
            logger.error(NO_DATA_AVAILABLE, "Heat On Time");
            filenamehot = "public_error_500x300.png";
        }
        return filenamehot;
    }

    /**
     * Creates flock mortality chart and return path to png file with map of each series .
     *
     * @param flockId the flock id
     * @param session the session
     * @param pw      PrintWriter object
     * @param locale  the current locale
     * @return the path to png file with chart
     */
    public static String generateChartFlockMortality(Long flockId, String fromDay, String toDay, HttpSession session, PrintWriter pw, Locale locale) {
        String filenamem;
        FromDayToDay growDayRangeParam;
        Long langId;
        FlockHistoryService flockHistoryService;
        Map<Integer, String> historyByGrowDay;
        List<Map<Integer, Data>> dataHistoryList;
        List<String> axisTitles;
        DataDao dataDao;
        Data data1;
        Data data2;
        Data data3;
        HashMap<String, String> dictionary;
        String title;
        String xAxisTitle;
        String yAxisTitle;
        HistoryGraph mortalityGraph;
        ChartRenderingInfo info;

        growDayRangeParam = new FromDayToDay(fromDay, toDay);
        try {
            langId = localeService.getLanguageId(locale.getLanguage());

            flockHistoryService = new FlockHistoryServiceImpl();
            historyByGrowDay = flockHistoryService.getFlockPerDayNotParsedReportsWithinRange(flockId, growDayRangeParam);

            dataHistoryList = new ArrayList<Map<Integer, Data>>();
            axisTitles = new ArrayList<String>();

            dataDao = DbImplDecider.use(DaoType.MYSQL).getDao(DataDao.class);
            data1 = dataDao.getById(PerGrowDayHistoryDataType.DAY_MORTALITY.getId(), langId);
            axisTitles.add(data1.getLabel());
            dataHistoryList.add(DataGraphCreator.createHistoryDataByGrowDay(historyByGrowDay, data1));

            data2 = dataDao.getById(PerGrowDayHistoryDataType.DAY_MORTALITY_MALE.getId(), langId);
            axisTitles.add(data2.getLabel());
            dataHistoryList.add(DataGraphCreator.createHistoryDataByGrowDay(historyByGrowDay, data2));

            data3 = dataDao.getById(PerGrowDayHistoryDataType.DAY_MORTALITY_FEMALE.getId(), langId);
            axisTitles.add(data3.getLabel());
            dataHistoryList.add(DataGraphCreator.createHistoryDataByGrowDay(historyByGrowDay, data3));

            dictionary = createDictionary(locale);
            title = dictionary.get("graph.mortality.title");    // "Daily Mortality";
            xAxisTitle = dictionary.get("graph.mortality.axis.grow.day");    // "Grow Day[Day]";
            yAxisTitle = dictionary.get("graph.mortality.axis.birds");    // "Birds";
            mortalityGraph = new HistoryGraph();
            mortalityGraph.setDataHistoryList(dataHistoryList);

            if (!growDayRangeParam.useRange()) {
                growDayRangeParam.setFromDay(1);
                growDayRangeParam.setToDay(dataHistoryList.get(0).size());
            }

            mortalityGraph.createChart(title, growDayRangeParam.toString(), xAxisTitle, yAxisTitle);

            // Write the chart image to the temporary directory
            info = new ChartRenderingInfo(new StandardEntityCollection());
            filenamem = ServletUtilities.saveChartAsPNG(mortalityGraph.getChart(), 800, 400, info, session);

            // Write the image map to the PrintWriter
            ChartUtilities.writeImageMap(pw, filenamem, info, false);
            session.setAttribute("filenamem-flockid=" + flockId + "&fromday=" + fromDay + "&today=" + toDay, filenamem);
            pw.flush();
        } catch (Exception e) {
            logger.error(NO_DATA_AVAILABLE, "Mortality");
            filenamem = "public_error_500x300.png";
        }
        return filenamem;
    }

    /**
     * Creates flock co2 chart and return path to png file with map of each series .
     *
     * @param flockId the flock id
     * @param session the session
     * @param pw      PrintWriter object
     * @param locale  the current locale
     * @return the path to png file with chart
     */
    public static String generateChartFlockCO2(Long flockId, String fromDay, String toDay, HttpSession session, PrintWriter pw, Locale locale) {
        String filenamem;
        FromDayToDay growDayRangeParam;
        Long langId;
        FlockHistoryService flockHistoryService;
        Map<Integer, String> historyByGrowDay;
        List<Map<Integer, Data>> dataHistoryList;
        List<String> axisTitles;
        DataDao dataDao;
        Data data1;
        Data data2;
        HashMap<String, String> dictionary;
        String title;
        String xAxisTitle;
        String yAxisTitle;
        HistoryGraph mortalityGraph;
        ChartRenderingInfo info;

        growDayRangeParam = new FromDayToDay(fromDay, toDay);
        try {
            langId = localeService.getLanguageId(locale.getLanguage());

            flockHistoryService = new FlockHistoryServiceImpl();
            historyByGrowDay = flockHistoryService.getFlockPerDayNotParsedReportsWithinRange(flockId, growDayRangeParam);

            dataHistoryList = new ArrayList<Map<Integer, Data>>();
            axisTitles = new ArrayList<String>();

            dataDao = DbImplDecider.use(DaoType.MYSQL).getDao(DataDao.class);
            data1 = dataDao.getById(PerGrowDayHistoryDataType.DAY_CO2_MAX.getId(), langId);
            axisTitles.add(data1.getLabel());
            dataHistoryList.add(DataGraphCreator.createHistoryDataByGrowDay(historyByGrowDay, data1));

            data2 = dataDao.getById(PerGrowDayHistoryDataType.DAY_CO2_MIN.getId(), langId);
            axisTitles.add(data2.getLabel());
            dataHistoryList.add(DataGraphCreator.createHistoryDataByGrowDay(historyByGrowDay, data2));

            dictionary = createDictionary(locale);
            title = dictionary.get("graph.co2.title");    // "Daily Mortality";
            xAxisTitle = dictionary.get("graph.co2.axis.grow.day");    // "Grow Day[Day]";
            yAxisTitle = dictionary.get("graph.co2.axis.co2");    // "Birds";
            mortalityGraph = new HistoryGraph();
            mortalityGraph.setDataHistoryList(dataHistoryList);

            if (!growDayRangeParam.useRange()) {
                growDayRangeParam.setFromDay(1);
                growDayRangeParam.setToDay(dataHistoryList.get(0).size());
            }

            mortalityGraph.createChart(title, growDayRangeParam.toString(), xAxisTitle, yAxisTitle);

            // Write the chart image to the temporary directory
            info = new ChartRenderingInfo(new StandardEntityCollection());
            filenamem = ServletUtilities.saveChartAsPNG(mortalityGraph.getChart(), 800, 400, info, session);

            // Write the image map to the PrintWriter
            ChartUtilities.writeImageMap(pw, filenamem, info, false);
            session.setAttribute("filenamem-flockid=" + flockId + "&fromday=" + fromDay + "&today=" + toDay, filenamem);
            pw.flush();
        } catch (Exception e) {
            logger.error(NO_DATA_AVAILABLE, "CO2");
            filenamem = "public_error_500x300.png";
        }
        return filenamem;
    }

    /**
     * Creates minimum and maximum temperature and humidity chart and return path to png file with map of each series .
     *
     * @param flockId the flock id
     * @param session the session
     * @param pw      PrintWriter object
     * @param locale  the current locale
     * @return the path to png file with chart
     */
    public static String generateChartFlockMinMaxTemperatureHumidity(Long flockId, String fromDay, String toDay, HttpSession session, PrintWriter pw, Locale locale) {
        String filenamemmh;
        FromDayToDay growDayRangeParam;
        Long langId;
        FlockHistoryService flockHistoryService;
        Map<Integer, String> historyByGrowDay;
        DataDao dataDao;
        Data data;
        Map<Integer, Data> interestData;
        Map<Integer, Data> interestData2;
        Map<Integer, Data> interestData3;
        Map<Integer, Data> interestData4;
        CombinedXYGraph combGraph;
        HashMap<String, String> dictionary;
        String title;
        String xAxisTitle;
        String y1AxisTitle;
        String y2AxisTitle;
        Map<Integer, Data> interestData5;
        Map<Integer, Data> interestData6;
        ChartRenderingInfo info;
        growDayRangeParam = new FromDayToDay(fromDay, toDay);
        try {
            langId = localeService.getLanguageId(locale.getLanguage());
            flockHistoryService = new FlockHistoryServiceImpl();
            historyByGrowDay = flockHistoryService.getFlockPerDayNotParsedReportsWithinRange(flockId, growDayRangeParam);

            dataDao = DbImplDecider.use(DaoType.MYSQL).getDao(DataDao.class);
            data = dataDao.getById(PerGrowDayHistoryDataType.MAX_TEMP_IN.getId(), langId);

            interestData = createDataSet(historyByGrowDay, data);
            data = dataDao.getById(PerGrowDayHistoryDataType.MIN_TEMP_IN.getId(), langId);

            interestData2 = createDataSet(historyByGrowDay, data);
            data = dataDao.getById(PerGrowDayHistoryDataType.MAX_TEMP_OUT.getId(), langId);

            interestData3 = createDataSet(historyByGrowDay, data);
            data = dataDao.getById(PerGrowDayHistoryDataType.MIN_TEMP_OUT.getId(), langId);

            interestData4 = createDataSet(historyByGrowDay, data);
            combGraph = new CombinedXYGraph();

            dictionary = createDictionary(locale);
            title = dictionary.get("graph.minimum.maximum.humidity.title");
            xAxisTitle = dictionary.get("graph.minimum.maximum.humidity.axis.grow.day");
            y1AxisTitle = dictionary.get("graph.minimum.maximum.humidity.axis.temperature");
            y2AxisTitle = dictionary.get("graph.minimum.maximum.humidity.axis.humidity");

            combGraph.createFirstNextPlot(title, xAxisTitle, y1AxisTitle, data, 0, interestData, interestData2);

            combGraph.createNextPlot(title, xAxisTitle, y1AxisTitle, data, 0, interestData3, interestData4);
            data = dataDao.getById(PerGrowDayHistoryDataType.MAX_HUMIDITY.getId(), langId);

            interestData5 = createDataSet(historyByGrowDay, data);
            data = dataDao.getById(PerGrowDayHistoryDataType.MIN_HUMIDITY.getId(), langId);

            interestData6 = createDataSet(historyByGrowDay, data);

            combGraph.createNextPlot("", xAxisTitle, y2AxisTitle, data, 1, interestData5, interestData6);
            combGraph.createChart(title, growDayRangeParam.toString(), xAxisTitle);
            // Write the chart image to the temporary directory
            info = new ChartRenderingInfo(new StandardEntityCollection());
            filenamemmh = ServletUtilities.saveChartAsPNG(combGraph.getChart(), 800, 400, info, session);

            // Write the image map to the PrintWriter
            ChartUtilities.writeImageMap(pw, filenamemmh, info, false);
            session.setAttribute("filenamemmh-flockid=" + flockId + "&fromday=" + fromDay + "&today=" + toDay, filenamemmh);
            pw.flush();
        } catch (Exception e) {
            logger.error(NO_DATA_AVAILABLE, "Minimum/Maximum and Humidity");
            filenamemmh = "public_error_500x300.png";
        }
        return filenamemmh;
    }

    /**
     * Creates dictionary map for graphs information according to locale .
     *
     * @param locale the current locale
     * @return the dictionary
     */
    protected static HashMap<String, String> createDictionary(Locale locale) {
        HashMap<String, String> dictionary;
        ResourceBundle bundle;
        String key;
        String value;
        dictionary = new HashMap<String, String>();
        bundle = ResourceBundle.getBundle("labels", locale);
        for (Enumeration<String> e = bundle.getKeys(); e.hasMoreElements(); ) {
            key = e.nextElement();
            if (key.startsWith("graph")) {
                value = bundle.getString(key);
                dictionary.put(key, value);
            }
        }
        return dictionary;
    }

    /**
     * Helper method to create data set for minimum maximum and humidity graph .
     *
     * @param historyByGrowDay the data map by grow day to create data set for graph
     * @param data             the data object that encapsulate data id and title for data set
     * @return dataSet the created data set map
     */
    private static Map<Integer, Data> createDataSet(final Map<Integer, String> historyByGrowDay, final Data data) {
        Map<Integer, Data> dataSet;
        Iterator iter;
        Integer key;
        String value;
        StringTokenizer st;
        String dataElem;
        String valElem;
        String dataType;
        Data cloned;

        dataSet = new HashMap<Integer, Data>();
        iter = historyByGrowDay.keySet().iterator();

        while (iter.hasNext()) {
            key = (Integer) iter.next();
            value = historyByGrowDay.get(key);
            st = new StringTokenizer(value, " ");

            while (st.hasMoreElements()) {
                try {
                    dataElem = (String) st.nextElement();
                    valElem = (String) st.nextElement();
                    dataType = data.getType().toString();
                    if (dataElem.equals(dataType) && (valElem.indexOf('-') == -1)) {
                        data.setValueFromUI(valElem);
                        cloned = (Data) data.clone();
                        dataSet.put(key, cloned);
                        break;
                    }
                } catch (Exception e) {
                    break;
                }
            }
        }
        return dataSet;
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////
    /**
     * Creates water and feed chart and return path to png file with map of each series .
     *
     * @param flockId the flock id
     * @param session the session
     * @param pw      PrintWriter object
     * @param locale  the current locale
     * @return the path to png file with chart
     */
    //***************************************************************************************************************************************************************************************
    public static String generateChartFlockEggs(Long flockId, String fromDay, String toDay, HttpSession session, PrintWriter pw, Locale locale) {

        String filenamehot;

        FromDayToDay growDayRangeParam = new FromDayToDay(fromDay, toDay);
        try {
            Long langId = localeService.getLanguageId(locale.getLanguage());

            FlockHistoryService flockHistoryService = new FlockHistoryServiceImpl();
            Map<Integer, String> historyByGrowDay = flockHistoryService.getFlockPerDayNotParsedReportsWithinRange(flockId, growDayRangeParam);

            DataDao dataDao = DbImplDecider.use(DaoType.MYSQL).getDao(DataDao.class);

            Data data1 = dataDao.getById(PerGrowDayHistoryDataType.TOTAL_EGGS_DEV_1.getId(), langId);
            Data data2 = dataDao.getById(PerGrowDayHistoryDataType.TOTAL_EGGS_DEV_2.getId(), langId);
            Data data3 = dataDao.getById(PerGrowDayHistoryDataType.TOTAL_EGGS_DEV_3.getId(), langId);
            Data data4 = dataDao.getById(PerGrowDayHistoryDataType.TOTAL_EGGS_DEV_4.getId(), langId);
            Data data5 = dataDao.getById(PerGrowDayHistoryDataType.TOTAL_EGGS_DEV_5.getId(), langId);
            Data data6 = dataDao.getById(PerGrowDayHistoryDataType.TOTAL_EGGS_DEV_6.getId(), langId);
            Data data7 = dataDao.getById(PerGrowDayHistoryDataType.TOTAL_EGGS_DEV_7.getId(), langId);
            Data data8 = dataDao.getById(PerGrowDayHistoryDataType.TOTAL_EGGS_DEV_8.getId(), langId);
            Data data9 = dataDao.getById(PerGrowDayHistoryDataType.TOTAL_EGGS_DEV_9.getId(), langId);
            Data data10 = dataDao.getById(PerGrowDayHistoryDataType.TOTAL_EGGS_DEV_10.getId(), langId);
            Data data11 = dataDao.getById(PerGrowDayHistoryDataType.TOTAL_EGGS_DEV_11.getId(), langId);
            Data data12 = dataDao.getById(PerGrowDayHistoryDataType.TOTAL_EGGS_DEV_12.getId(), langId);

//            List<Data> dataTotalEggsList = new ArrayList<Data>();

//            dataTotalEggsList.add(data1);
//            dataTotalEggsList.add(data2);
//            dataTotalEggsList.add(data3);
//            dataTotalEggsList.add(data4);
//            dataTotalEggsList.add(data5);
//            dataTotalEggsList.add(data6);
//            dataTotalEggsList.add(data7);
//            dataTotalEggsList.add(data8);
//            dataTotalEggsList.add(data9);
//            dataTotalEggsList.add(data10);
//            dataTotalEggsList.add(data11);
//            dataTotalEggsList.add(data12);

            List<String> axisTitles = new ArrayList<String>();

            axisTitles.add(data1.getLabel());
            axisTitles.add(data2.getLabel());
            axisTitles.add(data3.getLabel());
            axisTitles.add(data4.getLabel());
            axisTitles.add(data5.getLabel());
            axisTitles.add(data6.getLabel());
            axisTitles.add(data7.getLabel());
            axisTitles.add(data8.getLabel());
            axisTitles.add(data9.getLabel());
            axisTitles.add(data10.getLabel());
            axisTitles.add(data11.getLabel());
            axisTitles.add(data12.getLabel());

            HistoryContent historyContent = HistoryContentCreator.createPerDayHistoryContent(historyByGrowDay);

            Map<Integer, List<Data>> historyContentMap = historyContent.getHistoryContentPerDay();
//            historyContentMap = historyContent.getHistoryContentPerDay();

            List<Map<Integer, Data>> dataHistoryList = new ArrayList<Map<Integer, Data>>();

            dataHistoryList.add(DataGraphCreator.createHistoryData(historyContentMap, data1));
            dataHistoryList.add(DataGraphCreator.createHistoryData(historyContentMap, data2));
            dataHistoryList.add(DataGraphCreator.createHistoryData(historyContentMap, data3));
            dataHistoryList.add(DataGraphCreator.createHistoryData(historyContentMap, data4));
            dataHistoryList.add(DataGraphCreator.createHistoryData(historyContentMap, data5));
            dataHistoryList.add(DataGraphCreator.createHistoryData(historyContentMap, data6));
            dataHistoryList.add(DataGraphCreator.createHistoryData(historyContentMap, data7));
            dataHistoryList.add(DataGraphCreator.createHistoryData(historyContentMap, data8));
            dataHistoryList.add(DataGraphCreator.createHistoryData(historyContentMap, data9));
            dataHistoryList.add(DataGraphCreator.createHistoryData(historyContentMap, data10));
            dataHistoryList.add(DataGraphCreator.createHistoryData(historyContentMap, data11));
            dataHistoryList.add(DataGraphCreator.createHistoryData(historyContentMap, data12));

            HashMap<String, String> dictionary = createDictionary(locale);

            String title = "Daily Eggs";    // "Daily Mortality";
            String xAxisLabel = dictionary.get("graph.heater.on.time.axis.grow.day");    // "Grow Day[Day]";
            String yAxisLabel = "Eggs";    // "Birds";

            HistoryGraph eggsGraph = new HistoryGraph();

            eggsGraph.setDataHistoryList(dataHistoryList);

            if (!growDayRangeParam.useRange()) {
                growDayRangeParam.setFromDay(1);
                growDayRangeParam.setToDay(dataHistoryList.get(0).size());
            }

            eggsGraph.createChart(title, growDayRangeParam.toString(), xAxisLabel, yAxisLabel);

            // Write the chart image to the temporary directory
            ChartRenderingInfo info = new ChartRenderingInfo(new StandardEntityCollection());

            filenamehot = ServletUtilities.saveChartAsPNG(eggsGraph.getChart(), 800, 400, info, session);

            // Write the image map to the PrintWriter
            ChartUtilities.writeImageMap(pw, filenamehot, info, false);
            session.setAttribute("filenamehot-flockid=" + flockId + "&fromday=" + fromDay + "&today=" + toDay, filenamehot);
            pw.flush();

        } catch (Exception e) {
            logger.error(NO_DATA_AVAILABLE, "Eggs");
            filenamehot = "public_error_500x300.png";
        }

        return filenamehot;
    }
    //************************************************************************************************************************************************************************************************

//    //***************************************************************************************************************************************************************************************
//    public static String generateChartFlockEggs(Long flockId, String fromDay, String toDay, HttpSession session, PrintWriter pw, Locale locale) {
//        String filenamehot;
//        FromDayToDay growDayRangeParam;
//        Long langId;
//        FlockHistoryService flockHistoryService;
//        Map<Integer, String> historyByGrowDay;
//        DataDao dataDao;
//        Data data1, data2, data3, data4, data5, data6, data7, data8, data9, data10, data11, data12;
//        List<Data> dataTotalEggsList;
//        List<String> axisTitles;
//        HistoryContent historyContent;
//        Map<Integer, List<Data>> historyContentMap;
//        List<Map<Integer, Data>> dataHistoryList;
//        HashMap<String, String> dictionary;
//        String title;
//        String xAxisLabel;
//        String yAxisLabel;
//        HistoryGraph eggsGraph;
//        ChartRenderingInfo info;
//
//        growDayRangeParam = new FromDayToDay(fromDay, toDay);
//        try {
//            langId = localeService.getLanguageId(locale.getLanguage());
//
//            flockHistoryService = new FlockHistoryServiceImpl();
//            historyByGrowDay = flockHistoryService.getFlockPerDayNotParsedReportsWithinRange(flockId, growDayRangeParam);
//
//            dataDao = DbImplDecider.use(DaoType.MYSQL).getDao(DataDao.class);
//
//            data1 = dataDao.getById(PerGrowDayHistoryDataType.TOTAL_EGGS_DEV_1.getId(), langId);
//            data2 = dataDao.getById(PerGrowDayHistoryDataType.TOTAL_EGGS_DEV_2.getId(), langId);
//            data3 = dataDao.getById(PerGrowDayHistoryDataType.TOTAL_EGGS_DEV_3.getId(), langId);
//            data4 = dataDao.getById(PerGrowDayHistoryDataType.TOTAL_EGGS_DEV_4.getId(), langId);
//            data5 = dataDao.getById(PerGrowDayHistoryDataType.TOTAL_EGGS_DEV_5.getId(), langId);
//            data6 = dataDao.getById(PerGrowDayHistoryDataType.TOTAL_EGGS_DEV_6.getId(), langId);
//            data7 = dataDao.getById(PerGrowDayHistoryDataType.TOTAL_EGGS_DEV_7.getId(), langId);
//            data8 = dataDao.getById(PerGrowDayHistoryDataType.TOTAL_EGGS_DEV_8.getId(), langId);
//            data9 = dataDao.getById(PerGrowDayHistoryDataType.TOTAL_EGGS_DEV_9.getId(), langId);
//            data10 = dataDao.getById(PerGrowDayHistoryDataType.TOTAL_EGGS_DEV_10.getId(), langId);
//            data11 = dataDao.getById(PerGrowDayHistoryDataType.TOTAL_EGGS_DEV_11.getId(), langId);
//            data12 = dataDao.getById(PerGrowDayHistoryDataType.TOTAL_EGGS_DEV_12.getId(), langId);
//
//            dataTotalEggsList = new ArrayList<Data>();
//
//            dataTotalEggsList.add(data1);
//            dataTotalEggsList.add(data2);
//            dataTotalEggsList.add(data3);
//            dataTotalEggsList.add(data4);
//            dataTotalEggsList.add(data5);
//            dataTotalEggsList.add(data6);
//            dataTotalEggsList.add(data7);
//            dataTotalEggsList.add(data8);
//            dataTotalEggsList.add(data9);
//            dataTotalEggsList.add(data10);
//            dataTotalEggsList.add(data11);
//            dataTotalEggsList.add(data12);
//
//            axisTitles = new ArrayList<String>();
//
//            axisTitles.add(data1.getLabel());
//            axisTitles.add(data2.getLabel());
//            axisTitles.add(data3.getLabel());
//            axisTitles.add(data4.getLabel());
//            axisTitles.add(data5.getLabel());
//            axisTitles.add(data6.getLabel());
//            axisTitles.add(data7.getLabel());
//            axisTitles.add(data8.getLabel());
//            axisTitles.add(data9.getLabel());
//            axisTitles.add(data10.getLabel());
//            axisTitles.add(data11.getLabel());
//            axisTitles.add(data12.getLabel());
//
//            historyContent = HistoryContentCreator.createPerDayHistoryContent(historyByGrowDay, dataTotalEggsList);
//            historyContentMap = new TreeMap<Integer, List<Data>>();
//            historyContentMap = historyContent.getHistoryContentPerDay();
//
//            dataHistoryList = new ArrayList<Map<Integer, Data>>();
//
//            dataHistoryList.add(DataGraphCreator.createHistoryData(historyContentMap, data1));
//            dataHistoryList.add(DataGraphCreator.createHistoryData(historyContentMap, data2));
//            dataHistoryList.add(DataGraphCreator.createHistoryData(historyContentMap, data3));
//            dataHistoryList.add(DataGraphCreator.createHistoryData(historyContentMap, data4));
//            dataHistoryList.add(DataGraphCreator.createHistoryData(historyContentMap, data5));
//            dataHistoryList.add(DataGraphCreator.createHistoryData(historyContentMap, data6));
//            dataHistoryList.add(DataGraphCreator.createHistoryData(historyContentMap, data7));
//            dataHistoryList.add(DataGraphCreator.createHistoryData(historyContentMap, data8));
//            dataHistoryList.add(DataGraphCreator.createHistoryData(historyContentMap, data9));
//            dataHistoryList.add(DataGraphCreator.createHistoryData(historyContentMap, data10));
//            dataHistoryList.add(DataGraphCreator.createHistoryData(historyContentMap, data11));
//            dataHistoryList.add(DataGraphCreator.createHistoryData(historyContentMap, data12));
//
//            dictionary = createDictionary(locale);
//
//            title = "Daily Eggs";    // "Daily Mortality";
//            xAxisLabel = dictionary.get("graph.heater.on.time.axis.grow.day");    // "Grow Day[Day]";
//            yAxisLabel = "Eggs";    // "Birds";
//
//            eggsGraph = new HistoryGraph();
//            eggsGraph.setDataHistoryList(dataHistoryList);
//            if (!growDayRangeParam.useRange()) {
//                growDayRangeParam.setFromDay(1);
//                growDayRangeParam.setToDay(dataHistoryList.get(0).size());
//            }
//            eggsGraph.createChart(title, growDayRangeParam.toString(), xAxisLabel, yAxisLabel);
//            // Write the chart image to the temporary directory
//            info = new ChartRenderingInfo(new StandardEntityCollection());
//            filenamehot = ServletUtilities.saveChartAsPNG(eggsGraph.getChart(), 800, 400, info, session);
//
//            // Write the image map to the PrintWriter
//            ChartUtilities.writeImageMap(pw, filenamehot, info, false);
//            session.setAttribute("filenamehot-flockid=" + flockId + "&fromday=" + fromDay + "&today=" + toDay, filenamehot);
//            pw.flush();
//        } catch (Exception e) {
//            logger.error(NO_DATA_AVAILABLE, "Eggs");
//            filenamehot = "public_error_500x300.png";
//        }
//        return filenamehot;
//    }
//    //************************************************************************************************************************************************************************************************


    //////////////////////////////////////////////////////////////////////////////////////////////////////////
    /**
     * Creates water and feed chart and return path to png file with map of each series .
     *
     * @param flockId the flock id
     * @param session the session
     * @param pw      PrintWriter object
     * @param locale  the current locale
     * @return the path to png file with chart
     */
    public static String generateChartFlockEggsTotal(Long flockId, String fromDay, String toDay, HttpSession session, PrintWriter pw, Locale locale) {
        String filenamehot;
        FromDayToDay growDayRangeParam = new FromDayToDay(fromDay, toDay);
        try {
            Long langId = localeService.getLanguageId(locale.getLanguage());

            FlockHistoryService flockHistoryService = new FlockHistoryServiceImpl();
            Map<Integer, String> historyByGrowDay = flockHistoryService.getFlockPerDayNotParsedReportsWithinRange(flockId, growDayRangeParam);

            DataDao dataDao = DbImplDecider.use(DaoType.MYSQL).getDao(DataDao.class);

            Data data1 = dataDao.getById(PerGrowDayHistoryDataType.CONTROLLER_TOTAL_EGG_COUNT.getId(), langId);
            List<Data> dataTotalEggsList = new ArrayList<Data>();

            dataTotalEggsList.add(data1);
            List<String> axisTitles = new ArrayList<String>();

            axisTitles.add(data1.getLabel());

//            HistoryContent historyContent = HistoryContentCreator.createPerDayHistoryContent(historyByGrowDay, dataTotalEggsList);
            HistoryContent historyContent = HistoryContentCreator.createPerDayHistoryContent(historyByGrowDay);
            Map<Integer, List<Data>> historyContentMap = new TreeMap<Integer, List<Data>>();
            historyContentMap = historyContent.getHistoryContentPerDay();

            List<Map<Integer, Data>> dataHistoryList = new ArrayList<Map<Integer, Data>>();

            dataHistoryList.add(DataGraphCreator.createHistoryData(historyContentMap, data1));

            HashMap<String, String> dictionary = createDictionary(locale);

            String title = "Eggs Total";    // "Daily Mortality";
            String xAxisLabel = dictionary.get("graph.heater.on.time.axis.grow.day");    // "Grow Day[Day]";
            String yAxisLabel = "Eggs Total";    // "Birds";

            HistoryGraph eggsTotalGraph = new HistoryGraph();
            eggsTotalGraph.setDataHistoryList(dataHistoryList);
            if (!growDayRangeParam.useRange()) {
                growDayRangeParam.setFromDay(1);
                growDayRangeParam.setToDay(dataHistoryList.get(0).size());
            }
            eggsTotalGraph.createChart(title, growDayRangeParam.toString(), xAxisLabel, yAxisLabel);
            // Write the chart image to the temporary directory
            ChartRenderingInfo info = new ChartRenderingInfo(new StandardEntityCollection());
            filenamehot = ServletUtilities.saveChartAsPNG(eggsTotalGraph.getChart(), 800, 400, info, session);

            // Write the image map to the PrintWriter
            ChartUtilities.writeImageMap(pw, filenamehot, info, false);
            session.setAttribute("filenamehot-flockid=" + flockId + "&fromday=" + fromDay + "&today=" + toDay, filenamehot);
            pw.flush();
        } catch (Exception e) {
            logger.error(NO_DATA_AVAILABLE, "Eggs Total");
            filenamehot = "public_error_500x300.png";
        }
        return filenamehot;
    }

    public static String generateChartFlockEggsCounterFirst(Long flockId, String fromDay, String toDay, HttpSession session, PrintWriter pw, Locale locale) {
        String filenamehot;
        FromDayToDay growDayRangeParam = new FromDayToDay(fromDay, toDay);
        try {
            Long langId = localeService.getLanguageId(locale.getLanguage());

            FlockHistoryService flockHistoryService = new FlockHistoryServiceImpl();
            Map<Integer, String> historyByGrowDay = flockHistoryService.getFlockPerDayNotParsedReportsWithinRange(flockId, growDayRangeParam);

            DataDao dataDao = DbImplDecider.use(DaoType.MYSQL).getDao(DataDao.class);

            Data data1 = dataDao.getById(PerGrowDayHistoryDataType.TOTAL_EGGS_DEV_1.getId(), langId);
            List<Data> dataTotalEggsList = new ArrayList<Data>();

            dataTotalEggsList.add(data1);
            List<String> axisTitles = new ArrayList<String>();

            axisTitles.add(data1.getLabel());

//            HistoryContent historyContent = HistoryContentCreator.createPerDayHistoryContent(historyByGrowDay, dataTotalEggsList);
            HistoryContent historyContent = HistoryContentCreator.createPerDayHistoryContent(historyByGrowDay);
            Map<Integer, List<Data>> historyContentMap = new TreeMap<Integer, List<Data>>();
            historyContentMap = historyContent.getHistoryContentPerDay();

            List<Map<Integer, Data>> dataHistoryList = new ArrayList<Map<Integer, Data>>();

            dataHistoryList.add(DataGraphCreator.createHistoryData(historyContentMap, data1));

            HashMap<String, String> dictionary = createDictionary(locale);

            String title = "Eggs Counter 1";    // "Daily Mortality";
//            String xAxisLabel = dictionary.get("graph.heater.on.time.axis.grow.day");    // "Grow Day[Day]";
            String xAxisLabel = "Grow Day[Day]";;    // "Grow Day[Day]";

            String yAxisLabel = "Eggs Counter 1";    // "Birds";

            HistoryGraph eggsCounter1Graph = new HistoryGraph();
            eggsCounter1Graph.setDataHistoryList(dataHistoryList);
            if (!growDayRangeParam.useRange()) {
                growDayRangeParam.setFromDay(1);
                growDayRangeParam.setToDay(dataHistoryList.get(0).size());
            }
            eggsCounter1Graph.createChart(title, growDayRangeParam.toString(), xAxisLabel, yAxisLabel);
            // Write the chart image to the temporary directory
            ChartRenderingInfo info = new ChartRenderingInfo(new StandardEntityCollection());
            filenamehot = ServletUtilities.saveChartAsPNG(eggsCounter1Graph.getChart(), 800, 400, info, session);

            // Write the image map to the PrintWriter
            ChartUtilities.writeImageMap(pw, filenamehot, info, false);
            session.setAttribute("filenamehot-flockid=" + flockId + "&fromday=" + fromDay + "&today=" + toDay, filenamehot);
            pw.flush();
        } catch (Exception e) {
            logger.error(NO_DATA_AVAILABLE, "Eggs Counter 1");
            filenamehot = "public_error_500x300.png";
        }
        return filenamehot;
    }

    public static String generateChartFlockEggsCounterSecond(Long flockId, String fromDay, String toDay, HttpSession session, PrintWriter pw, Locale locale) {
        String filenamehot;
        FromDayToDay growDayRangeParam = new FromDayToDay(fromDay, toDay);
        try {
            Long langId = localeService.getLanguageId(locale.getLanguage());

            FlockHistoryService flockHistoryService = new FlockHistoryServiceImpl();
            Map<Integer, String> historyByGrowDay = flockHistoryService.getFlockPerDayNotParsedReportsWithinRange(flockId, growDayRangeParam);

            DataDao dataDao = DbImplDecider.use(DaoType.MYSQL).getDao(DataDao.class);

            Data data1 = dataDao.getById(PerGrowDayHistoryDataType.TOTAL_EGGS_DEV_2.getId(), langId);
            List<Data> dataTotalEggsList = new ArrayList<Data>();

            dataTotalEggsList.add(data1);
            List<String> axisTitles = new ArrayList<String>();

            axisTitles.add(data1.getLabel());

//            HistoryContent historyContent = HistoryContentCreator.createPerDayHistoryContent(historyByGrowDay, dataTotalEggsList);
            HistoryContent historyContent = HistoryContentCreator.createPerDayHistoryContent(historyByGrowDay);
            Map<Integer, List<Data>> historyContentMap = new TreeMap<Integer, List<Data>>();
            historyContentMap = historyContent.getHistoryContentPerDay();

            List<Map<Integer, Data>> dataHistoryList = new ArrayList<Map<Integer, Data>>();

            dataHistoryList.add(DataGraphCreator.createHistoryData(historyContentMap, data1));

            HashMap<String, String> dictionary = createDictionary(locale);

            String title = "Eggs Counter 2";    // "Daily Mortality";
            String xAxisLabel = dictionary.get("graph.heater.on.time.axis.grow.day");    // "Grow Day[Day]";
            String yAxisLabel = "Eggs Counter 2";    // "Birds";

            HistoryGraph eggsCounter2Graph = new HistoryGraph();
            eggsCounter2Graph.setDataHistoryList(dataHistoryList);
            if (!growDayRangeParam.useRange()) {
                growDayRangeParam.setFromDay(1);
                growDayRangeParam.setToDay(dataHistoryList.get(0).size());
            }
            eggsCounter2Graph.createChart(title, growDayRangeParam.toString(), xAxisLabel, yAxisLabel);
            // Write the chart image to the temporary directory
            ChartRenderingInfo info = new ChartRenderingInfo(new StandardEntityCollection());
            filenamehot = ServletUtilities.saveChartAsPNG(eggsCounter2Graph.getChart(), 800, 400, info, session);

            // Write the image map to the PrintWriter
            ChartUtilities.writeImageMap(pw, filenamehot, info, false);
            session.setAttribute("filenamehot-flockid=" + flockId + "&fromday=" + fromDay + "&today=" + toDay, filenamehot);
            pw.flush();
        } catch (Exception e) {
            logger.error(NO_DATA_AVAILABLE, "Eggs Counter 1");
            filenamehot = "public_error_500x300.png";
        }
        return filenamehot;
    }

    public static String generateChartFlockEggsCounterThird(Long flockId, String fromDay, String toDay, HttpSession session, PrintWriter pw, Locale locale) {
        String filenamehot;
        FromDayToDay growDayRangeParam = new FromDayToDay(fromDay, toDay);
        try {
            Long langId = localeService.getLanguageId(locale.getLanguage());

            FlockHistoryService flockHistoryService = new FlockHistoryServiceImpl();
            Map<Integer, String> historyByGrowDay = flockHistoryService.getFlockPerDayNotParsedReportsWithinRange(flockId, growDayRangeParam);

            DataDao dataDao = DbImplDecider.use(DaoType.MYSQL).getDao(DataDao.class);

            Data data1 = dataDao.getById(PerGrowDayHistoryDataType.TOTAL_EGGS_DEV_3.getId(), langId);
            List<Data> dataTotalEggsList = new ArrayList<Data>();

            dataTotalEggsList.add(data1);
            List<String> axisTitles = new ArrayList<String>();

            axisTitles.add(data1.getLabel());

//            HistoryContent historyContent = HistoryContentCreator.createPerDayHistoryContent(historyByGrowDay, dataTotalEggsList);
            HistoryContent historyContent = HistoryContentCreator.createPerDayHistoryContent(historyByGrowDay);
            Map<Integer, List<Data>> historyContentMap = new TreeMap<Integer, List<Data>>();
            historyContentMap = historyContent.getHistoryContentPerDay();

            List<Map<Integer, Data>> dataHistoryList = new ArrayList<Map<Integer, Data>>();

            dataHistoryList.add(DataGraphCreator.createHistoryData(historyContentMap, data1));

            HashMap<String, String> dictionary = createDictionary(locale);

            String title = "Eggs Counter 3";    // "Daily Mortality";
            String xAxisLabel = dictionary.get("graph.heater.on.time.axis.grow.day");    // "Grow Day[Day]";
            String yAxisLabel = "Eggs Counter 3";    // "Birds";

            HistoryGraph eggsCounter3Graph = new HistoryGraph();
            eggsCounter3Graph.setDataHistoryList(dataHistoryList);
            if (!growDayRangeParam.useRange()) {
                growDayRangeParam.setFromDay(1);
                growDayRangeParam.setToDay(dataHistoryList.get(0).size());
            }
            eggsCounter3Graph.createChart(title, growDayRangeParam.toString(), xAxisLabel, yAxisLabel);
            // Write the chart image to the temporary directory
            ChartRenderingInfo info = new ChartRenderingInfo(new StandardEntityCollection());
            filenamehot = ServletUtilities.saveChartAsPNG(eggsCounter3Graph.getChart(), 800, 400, info, session);

            // Write the image map to the PrintWriter
            ChartUtilities.writeImageMap(pw, filenamehot, info, false);
            session.setAttribute("filenamehot-flockid=" + flockId + "&fromday=" + fromDay + "&today=" + toDay, filenamehot);
            pw.flush();
        } catch (Exception e) {
            logger.error(NO_DATA_AVAILABLE, "Eggs Counter 3");
            filenamehot = "public_error_500x300.png";
        }
        return filenamehot;
    }

    public static String generateChartFlockEggsCounterFourth(Long flockId, String fromDay, String toDay, HttpSession session, PrintWriter pw, Locale locale) {
        String filenamehot;
        FromDayToDay growDayRangeParam = new FromDayToDay(fromDay, toDay);
        try {
            Long langId = localeService.getLanguageId(locale.getLanguage());

            FlockHistoryService flockHistoryService = new FlockHistoryServiceImpl();
            Map<Integer, String> historyByGrowDay = flockHistoryService.getFlockPerDayNotParsedReportsWithinRange(flockId, growDayRangeParam);

            DataDao dataDao = DbImplDecider.use(DaoType.MYSQL).getDao(DataDao.class);

            Data data1 = dataDao.getById(PerGrowDayHistoryDataType.TOTAL_EGGS_DEV_4.getId(), langId);
            List<Data> dataTotalEggsList = new ArrayList<Data>();

            dataTotalEggsList.add(data1);
            List<String> axisTitles = new ArrayList<String>();

            axisTitles.add(data1.getLabel());

//            HistoryContent historyContent = HistoryContentCreator.createPerDayHistoryContent(historyByGrowDay, dataTotalEggsList);
            HistoryContent historyContent = HistoryContentCreator.createPerDayHistoryContent(historyByGrowDay);
            Map<Integer, List<Data>> historyContentMap = new TreeMap<Integer, List<Data>>();
            historyContentMap = historyContent.getHistoryContentPerDay();

            List<Map<Integer, Data>> dataHistoryList = new ArrayList<Map<Integer, Data>>();

            dataHistoryList.add(DataGraphCreator.createHistoryData(historyContentMap, data1));

            HashMap<String, String> dictionary = createDictionary(locale);

            String title = "Eggs Counter 4";    // "Daily Mortality";
            String xAxisLabel = dictionary.get("graph.heater.on.time.axis.grow.day");    // "Grow Day[Day]";
            String yAxisLabel = "Eggs Counter 4";    // "Birds";

            HistoryGraph eggsCounter4Graph = new HistoryGraph();
            eggsCounter4Graph.setDataHistoryList(dataHistoryList);
            if (!growDayRangeParam.useRange()) {
                growDayRangeParam.setFromDay(1);
                growDayRangeParam.setToDay(dataHistoryList.get(0).size());
            }
            eggsCounter4Graph.createChart(title, growDayRangeParam.toString(), xAxisLabel, yAxisLabel);
            // Write the chart image to the temporary directory
            ChartRenderingInfo info = new ChartRenderingInfo(new StandardEntityCollection());
            filenamehot = ServletUtilities.saveChartAsPNG(eggsCounter4Graph.getChart(), 800, 400, info, session);

            // Write the image map to the PrintWriter
            ChartUtilities.writeImageMap(pw, filenamehot, info, false);
            session.setAttribute("filenamehot-flockid=" + flockId + "&fromday=" + fromDay + "&today=" + toDay, filenamehot);
            pw.flush();
        } catch (Exception e) {
            logger.error(NO_DATA_AVAILABLE, "Eggs Counter 4");
            filenamehot = "public_error_500x300.png";
        }
        return filenamehot;
    }

    public static String generateChartFlockEggsCounterFifth(Long flockId, String fromDay, String toDay, HttpSession session, PrintWriter pw, Locale locale) {
        String filenamehot;
        FromDayToDay growDayRangeParam = new FromDayToDay(fromDay, toDay);
        try {
            Long langId = localeService.getLanguageId(locale.getLanguage());

            FlockHistoryService flockHistoryService = new FlockHistoryServiceImpl();
            Map<Integer, String> historyByGrowDay = flockHistoryService.getFlockPerDayNotParsedReportsWithinRange(flockId, growDayRangeParam);

            DataDao dataDao = DbImplDecider.use(DaoType.MYSQL).getDao(DataDao.class);

            Data data1 = dataDao.getById(PerGrowDayHistoryDataType.TOTAL_EGGS_DEV_5.getId(), langId);
            List<Data> dataTotalEggsList = new ArrayList<Data>();

            dataTotalEggsList.add(data1);
            List<String> axisTitles = new ArrayList<String>();

            axisTitles.add(data1.getLabel());

//            HistoryContent historyContent = HistoryContentCreator.createPerDayHistoryContent(historyByGrowDay, dataTotalEggsList);
            HistoryContent historyContent = HistoryContentCreator.createPerDayHistoryContent(historyByGrowDay);
            Map<Integer, List<Data>> historyContentMap = new TreeMap<Integer, List<Data>>();
            historyContentMap = historyContent.getHistoryContentPerDay();

            List<Map<Integer, Data>> dataHistoryList = new ArrayList<Map<Integer, Data>>();

            dataHistoryList.add(DataGraphCreator.createHistoryData(historyContentMap, data1));

//            HashMap<String, String> dictionary = createDictionary(locale);

            String title = "Eggs Counter 5";
            String xAxisLabel = "Grow Day[Day]";
            String yAxisLabel = "Eggs Counter 5";

            HistoryGraph eggsCounter5Graph = new HistoryGraph();
            eggsCounter5Graph.setDataHistoryList(dataHistoryList);
            if (!growDayRangeParam.useRange()) {
                growDayRangeParam.setFromDay(1);
                growDayRangeParam.setToDay(dataHistoryList.get(0).size());
            }
            eggsCounter5Graph.createChart(title, growDayRangeParam.toString(), xAxisLabel, yAxisLabel);
            // Write the chart image to the temporary directory
            ChartRenderingInfo info = new ChartRenderingInfo(new StandardEntityCollection());
            filenamehot = ServletUtilities.saveChartAsPNG(eggsCounter5Graph.getChart(), 800, 400, info, session);

            // Write the image map to the PrintWriter
            ChartUtilities.writeImageMap(pw, filenamehot, info, false);
            session.setAttribute("filenamehot-flockid=" + flockId + "&fromday=" + fromDay + "&today=" + toDay, filenamehot);
            pw.flush();
        } catch (Exception e) {
            logger.error(NO_DATA_AVAILABLE, "Eggs Counter 5");
            filenamehot = "public_error_500x300.png";
        }
        return filenamehot;
    }

    public static String generateChartFlockEggsCounterSixth(Long flockId, String fromDay, String toDay, HttpSession session, PrintWriter pw, Locale locale) {
        String filenamehot;
        FromDayToDay growDayRangeParam = new FromDayToDay(fromDay, toDay);
        try {
            Long langId = localeService.getLanguageId(locale.getLanguage());

            FlockHistoryService flockHistoryService = new FlockHistoryServiceImpl();
            Map<Integer, String> historyByGrowDay = flockHistoryService.getFlockPerDayNotParsedReportsWithinRange(flockId, growDayRangeParam);

            DataDao dataDao = DbImplDecider.use(DaoType.MYSQL).getDao(DataDao.class);

            Data data1 = dataDao.getById(PerGrowDayHistoryDataType.TOTAL_EGGS_DEV_6.getId(), langId);
            List<Data> dataTotalEggsList = new ArrayList<Data>();

            dataTotalEggsList.add(data1);
            List<String> axisTitles = new ArrayList<String>();

            axisTitles.add(data1.getLabel());

//            HistoryContent historyContent = HistoryContentCreator.createPerDayHistoryContent(historyByGrowDay, dataTotalEggsList);
            HistoryContent historyContent = HistoryContentCreator.createPerDayHistoryContent(historyByGrowDay);
            Map<Integer, List<Data>> historyContentMap = new TreeMap<Integer, List<Data>>();
            historyContentMap = historyContent.getHistoryContentPerDay();

            List<Map<Integer, Data>> dataHistoryList = new ArrayList<Map<Integer, Data>>();

            dataHistoryList.add(DataGraphCreator.createHistoryData(historyContentMap, data1));

            HashMap<String, String> dictionary = createDictionary(locale);

            String title = "Eggs Counter 6";    // "Daily Mortality";
            String xAxisLabel = dictionary.get("graph.heater.on.time.axis.grow.day");    // "Grow Day[Day]";
            String yAxisLabel = "Eggs Counter 6";    // "Birds";

            HistoryGraph eggsCounter6Graph = new HistoryGraph();
            eggsCounter6Graph.setDataHistoryList(dataHistoryList);
            if (!growDayRangeParam.useRange()) {
                growDayRangeParam.setFromDay(1);
                growDayRangeParam.setToDay(dataHistoryList.get(0).size());
            }
            eggsCounter6Graph.createChart(title, growDayRangeParam.toString(), xAxisLabel, yAxisLabel);
            // Write the chart image to the temporary directory
            ChartRenderingInfo info = new ChartRenderingInfo(new StandardEntityCollection());
            filenamehot = ServletUtilities.saveChartAsPNG(eggsCounter6Graph.getChart(), 800, 400, info, session);

            // Write the image map to the PrintWriter
            ChartUtilities.writeImageMap(pw, filenamehot, info, false);
            session.setAttribute("filenamehot-flockid=" + flockId + "&fromday=" + fromDay + "&today=" + toDay, filenamehot);
            pw.flush();
        } catch (Exception e) {
            logger.error(NO_DATA_AVAILABLE, "Eggs Counter 6");
            filenamehot = "public_error_500x300.png";
        }
        return filenamehot;
    }

    public static String generateChartFlockEggsCounterSeventh(Long flockId, String fromDay, String toDay, HttpSession session, PrintWriter pw, Locale locale) {
        String filenamehot;
        FromDayToDay growDayRangeParam = new FromDayToDay(fromDay, toDay);
        try {
            Long langId = localeService.getLanguageId(locale.getLanguage());

            FlockHistoryService flockHistoryService = new FlockHistoryServiceImpl();
            Map<Integer, String> historyByGrowDay = flockHistoryService.getFlockPerDayNotParsedReportsWithinRange(flockId, growDayRangeParam);

            DataDao dataDao = DbImplDecider.use(DaoType.MYSQL).getDao(DataDao.class);

            Data data1 = dataDao.getById(PerGrowDayHistoryDataType.TOTAL_EGGS_DEV_7.getId(), langId);
            List<Data> dataTotalEggsList = new ArrayList<Data>();

            dataTotalEggsList.add(data1);
            List<String> axisTitles = new ArrayList<String>();

            axisTitles.add(data1.getLabel());

//            HistoryContent historyContent = HistoryContentCreator.createPerDayHistoryContent(historyByGrowDay, dataTotalEggsList);
            HistoryContent historyContent = HistoryContentCreator.createPerDayHistoryContent(historyByGrowDay);
            Map<Integer, List<Data>> historyContentMap = new TreeMap<Integer, List<Data>>();
            historyContentMap = historyContent.getHistoryContentPerDay();

            List<Map<Integer, Data>> dataHistoryList = new ArrayList<Map<Integer, Data>>();

            dataHistoryList.add(DataGraphCreator.createHistoryData(historyContentMap, data1));

            HashMap<String, String> dictionary = createDictionary(locale);

            String title = "Eggs Counter 7";
            String xAxisLabel = "Grow Day[Day]";
            String yAxisLabel = "Eggs Counter 7";

            HistoryGraph eggsCounter7Graph = new HistoryGraph();
            eggsCounter7Graph.setDataHistoryList(dataHistoryList);
            if (!growDayRangeParam.useRange()) {
                growDayRangeParam.setFromDay(1);
                growDayRangeParam.setToDay(dataHistoryList.get(0).size());
            }
            eggsCounter7Graph.createChart(title, growDayRangeParam.toString(), xAxisLabel, yAxisLabel);
            // Write the chart image to the temporary directory
            ChartRenderingInfo info = new ChartRenderingInfo(new StandardEntityCollection());
            filenamehot = ServletUtilities.saveChartAsPNG(eggsCounter7Graph.getChart(), 800, 400, info, session);

            // Write the image map to the PrintWriter
            ChartUtilities.writeImageMap(pw, filenamehot, info, false);
            session.setAttribute("filenamehot-flockid=" + flockId + "&fromday=" + fromDay + "&today=" + toDay, filenamehot);
            pw.flush();
        } catch (Exception e) {
            logger.error(NO_DATA_AVAILABLE, "Eggs Counter 7");
            filenamehot = "public_error_500x300.png";
        }
        return filenamehot;
    }

    public static String generateChartFlockEggsCounterEighth(Long flockId, String fromDay, String toDay, HttpSession session, PrintWriter pw, Locale locale) {
        String filenamehot;
        FromDayToDay growDayRangeParam = new FromDayToDay(fromDay, toDay);
        try {
            Long langId = localeService.getLanguageId(locale.getLanguage());

            FlockHistoryService flockHistoryService = new FlockHistoryServiceImpl();
            Map<Integer, String> historyByGrowDay = flockHistoryService.getFlockPerDayNotParsedReportsWithinRange(flockId, growDayRangeParam);

            DataDao dataDao = DbImplDecider.use(DaoType.MYSQL).getDao(DataDao.class);

            Data data1 = dataDao.getById(PerGrowDayHistoryDataType.TOTAL_EGGS_DEV_8.getId(), langId);
            List<Data> dataTotalEggsList = new ArrayList<Data>();

            dataTotalEggsList.add(data1);
            List<String> axisTitles = new ArrayList<String>();

            axisTitles.add(data1.getLabel());

//            HistoryContent historyContent = HistoryContentCreator.createPerDayHistoryContent(historyByGrowDay, dataTotalEggsList);
            HistoryContent historyContent = HistoryContentCreator.createPerDayHistoryContent(historyByGrowDay);
            Map<Integer, List<Data>> historyContentMap = new TreeMap<Integer, List<Data>>();
            historyContentMap = historyContent.getHistoryContentPerDay();

            List<Map<Integer, Data>> dataHistoryList = new ArrayList<Map<Integer, Data>>();

            dataHistoryList.add(DataGraphCreator.createHistoryData(historyContentMap, data1));

            HashMap<String, String> dictionary = createDictionary(locale);

            String title = "Eggs Counter 8";
            String xAxisLabel = "Grow Day[Day]";
            String yAxisLabel = "Eggs Counter 8";

            HistoryGraph eggsCounter8Graph = new HistoryGraph();
            eggsCounter8Graph.setDataHistoryList(dataHistoryList);
            if (!growDayRangeParam.useRange()) {
                growDayRangeParam.setFromDay(1);
                growDayRangeParam.setToDay(dataHistoryList.get(0).size());
            }
            eggsCounter8Graph.createChart(title, growDayRangeParam.toString(), xAxisLabel, yAxisLabel);
            // Write the chart image to the temporary directory
            ChartRenderingInfo info = new ChartRenderingInfo(new StandardEntityCollection());
            filenamehot = ServletUtilities.saveChartAsPNG(eggsCounter8Graph.getChart(), 800, 400, info, session);

            // Write the image map to the PrintWriter
            ChartUtilities.writeImageMap(pw, filenamehot, info, false);
            session.setAttribute("filenamehot-flockid=" + flockId + "&fromday=" + fromDay + "&today=" + toDay, filenamehot);
            pw.flush();
        } catch (Exception e) {
            logger.error(NO_DATA_AVAILABLE, "Eggs Counter 7");
            filenamehot = "public_error_500x300.png";
        }
        return filenamehot;
    }

    public static String generateChartFlockEggsCounterNinth(Long flockId, String fromDay, String toDay, HttpSession session, PrintWriter pw, Locale locale) {
        String filenamehot;
        FromDayToDay growDayRangeParam = new FromDayToDay(fromDay, toDay);
        try {
            Long langId = localeService.getLanguageId(locale.getLanguage());

            FlockHistoryService flockHistoryService = new FlockHistoryServiceImpl();
            Map<Integer, String> historyByGrowDay = flockHistoryService.getFlockPerDayNotParsedReportsWithinRange(flockId, growDayRangeParam);

            DataDao dataDao = DbImplDecider.use(DaoType.MYSQL).getDao(DataDao.class);

            Data data1 = dataDao.getById(PerGrowDayHistoryDataType.TOTAL_EGGS_DEV_9.getId(), langId);
            List<Data> dataTotalEggsList = new ArrayList<Data>();

            dataTotalEggsList.add(data1);
            List<String> axisTitles = new ArrayList<String>();

            axisTitles.add(data1.getLabel());

//            HistoryContent historyContent = HistoryContentCreator.createPerDayHistoryContent(historyByGrowDay, dataTotalEggsList);
            HistoryContent historyContent = HistoryContentCreator.createPerDayHistoryContent(historyByGrowDay);
            Map<Integer, List<Data>> historyContentMap = new TreeMap<Integer, List<Data>>();
            historyContentMap = historyContent.getHistoryContentPerDay();

            List<Map<Integer, Data>> dataHistoryList = new ArrayList<Map<Integer, Data>>();

            dataHistoryList.add(DataGraphCreator.createHistoryData(historyContentMap, data1));

            HashMap<String, String> dictionary = createDictionary(locale);

            String title = "Eggs Counter 9";    // "Daily Mortality";
//            String xAxisLabel = dictionary.get("graph.heater.on.time.axis.grow.day");    // "Grow Day[Day]";
            String xAxisLabel = "Grow Day[Day]";;    // "Grow Day[Day]";

            String yAxisLabel = "Eggs Counter 9";    // "Birds";

            HistoryGraph eggsCounter9Graph = new HistoryGraph();
            eggsCounter9Graph.setDataHistoryList(dataHistoryList);
            if (!growDayRangeParam.useRange()) {
                growDayRangeParam.setFromDay(1);
                growDayRangeParam.setToDay(dataHistoryList.get(0).size());
            }
            eggsCounter9Graph.createChart(title, growDayRangeParam.toString(), xAxisLabel, yAxisLabel);
            // Write the chart image to the temporary directory
            ChartRenderingInfo info = new ChartRenderingInfo(new StandardEntityCollection());
            filenamehot = ServletUtilities.saveChartAsPNG(eggsCounter9Graph.getChart(), 800, 400, info, session);

            // Write the image map to the PrintWriter
            ChartUtilities.writeImageMap(pw, filenamehot, info, false);
            session.setAttribute("filenamehot-flockid=" + flockId + "&fromday=" + fromDay + "&today=" + toDay, filenamehot);
            pw.flush();
        } catch (Exception e) {
            logger.error(NO_DATA_AVAILABLE, "Eggs Counter 1");
            filenamehot = "public_error_500x300.png";
        }
        return filenamehot;
    }

    public static String generateChartFlockEggsCounterTenth(Long flockId, String fromDay, String toDay, HttpSession session, PrintWriter pw, Locale locale) {
        String filenamehot;
        FromDayToDay growDayRangeParam = new FromDayToDay(fromDay, toDay);
        try {
            Long langId = localeService.getLanguageId(locale.getLanguage());

            FlockHistoryService flockHistoryService = new FlockHistoryServiceImpl();
            Map<Integer, String> historyByGrowDay = flockHistoryService.getFlockPerDayNotParsedReportsWithinRange(flockId, growDayRangeParam);

            DataDao dataDao = DbImplDecider.use(DaoType.MYSQL).getDao(DataDao.class);

            Data data1 = dataDao.getById(PerGrowDayHistoryDataType.TOTAL_EGGS_DEV_10.getId(), langId);
            List<Data> dataTotalEggsList = new ArrayList<Data>();

            dataTotalEggsList.add(data1);
            List<String> axisTitles = new ArrayList<String>();

            axisTitles.add(data1.getLabel());

//            HistoryContent historyContent = HistoryContentCreator.createPerDayHistoryContent(historyByGrowDay, dataTotalEggsList);
            HistoryContent historyContent = HistoryContentCreator.createPerDayHistoryContent(historyByGrowDay);
            Map<Integer, List<Data>> historyContentMap = new TreeMap<Integer, List<Data>>();
            historyContentMap = historyContent.getHistoryContentPerDay();

            List<Map<Integer, Data>> dataHistoryList = new ArrayList<Map<Integer, Data>>();

            dataHistoryList.add(DataGraphCreator.createHistoryData(historyContentMap, data1));

//            HashMap<String, String> dictionary = createDictionary(locale);

            String title = "Eggs Counter 10";    // "Daily Mortality";
//            String xAxisLabel = dictionary.get("graph.heater.on.time.axis.grow.day");    // "Grow Day[Day]";
            String xAxisLabel = "Grow Day[Day]";;    // "Grow Day[Day]";

            String yAxisLabel = "Eggs Counter 10";    // "Birds";

            HistoryGraph eggsCounter10Graph = new HistoryGraph();
            eggsCounter10Graph.setDataHistoryList(dataHistoryList);
            if (!growDayRangeParam.useRange()) {
                growDayRangeParam.setFromDay(1);
                growDayRangeParam.setToDay(dataHistoryList.get(0).size());
            }
            eggsCounter10Graph.createChart(title, growDayRangeParam.toString(), xAxisLabel, yAxisLabel);
            // Write the chart image to the temporary directory
            ChartRenderingInfo info = new ChartRenderingInfo(new StandardEntityCollection());
            filenamehot = ServletUtilities.saveChartAsPNG(eggsCounter10Graph.getChart(), 800, 400, info, session);

            // Write the image map to the PrintWriter
            ChartUtilities.writeImageMap(pw, filenamehot, info, false);
            session.setAttribute("filenamehot-flockid=" + flockId + "&fromday=" + fromDay + "&today=" + toDay, filenamehot);
            pw.flush();
        } catch (Exception e) {
            logger.error(NO_DATA_AVAILABLE, "Eggs Counter 10");
            filenamehot = "public_error_500x300.png";
        }
        return filenamehot;
    }

    public static String generateChartFlockEggsCounterEleventh(Long flockId, String fromDay, String toDay, HttpSession session, PrintWriter pw, Locale locale) {
        String filenamehot;
        FromDayToDay growDayRangeParam = new FromDayToDay(fromDay, toDay);
        try {
            Long langId = localeService.getLanguageId(locale.getLanguage());

            FlockHistoryService flockHistoryService = new FlockHistoryServiceImpl();
            Map<Integer, String> historyByGrowDay = flockHistoryService.getFlockPerDayNotParsedReportsWithinRange(flockId, growDayRangeParam);

            DataDao dataDao = DbImplDecider.use(DaoType.MYSQL).getDao(DataDao.class);

            Data data1 = dataDao.getById(PerGrowDayHistoryDataType.TOTAL_EGGS_DEV_11.getId(), langId);
            List<Data> dataTotalEggsList = new ArrayList<Data>();

            dataTotalEggsList.add(data1);
            List<String> axisTitles = new ArrayList<String>();

            axisTitles.add(data1.getLabel());

//            HistoryContent historyContent = HistoryContentCreator.createPerDayHistoryContent(historyByGrowDay, dataTotalEggsList);
            HistoryContent historyContent = HistoryContentCreator.createPerDayHistoryContent(historyByGrowDay);
            Map<Integer, List<Data>> historyContentMap = new TreeMap<Integer, List<Data>>();
            historyContentMap = historyContent.getHistoryContentPerDay();

            List<Map<Integer, Data>> dataHistoryList = new ArrayList<Map<Integer, Data>>();

            dataHistoryList.add(DataGraphCreator.createHistoryData(historyContentMap, data1));

//            HashMap<String, String> dictionary = createDictionary(locale);

            String title = "Eggs Counter 11";    // "Daily Mortality";
//            String xAxisLabel = dictionary.get("graph.heater.on.time.axis.grow.day");    // "Grow Day[Day]";
            String xAxisLabel = "Grow Day[Day]";;    // "Grow Day[Day]";

            String yAxisLabel = "Eggs Counter 11";    // "Birds";

            HistoryGraph eggsCounter11Graph = new HistoryGraph();
            eggsCounter11Graph.setDataHistoryList(dataHistoryList);
            if (!growDayRangeParam.useRange()) {
                growDayRangeParam.setFromDay(1);
                growDayRangeParam.setToDay(dataHistoryList.get(0).size());
            }
            eggsCounter11Graph.createChart(title, growDayRangeParam.toString(), xAxisLabel, yAxisLabel);
            // Write the chart image to the temporary directory
            ChartRenderingInfo info = new ChartRenderingInfo(new StandardEntityCollection());
            filenamehot = ServletUtilities.saveChartAsPNG(eggsCounter11Graph.getChart(), 800, 400, info, session);

            // Write the image map to the PrintWriter
            ChartUtilities.writeImageMap(pw, filenamehot, info, false);
            session.setAttribute("filenamehot-flockid=" + flockId + "&fromday=" + fromDay + "&today=" + toDay, filenamehot);
            pw.flush();
        } catch (Exception e) {
            logger.error(NO_DATA_AVAILABLE, "Eggs Counter 11");
            filenamehot = "public_error_500x300.png";
        }
        return filenamehot;
    }

    public static String generateChartFlockEggsCounterTwelfth(Long flockId, String fromDay, String toDay, HttpSession session, PrintWriter pw, Locale locale) {
        String filenamehot;
        FromDayToDay growDayRangeParam = new FromDayToDay(fromDay, toDay);
        try {
            Long langId = localeService.getLanguageId(locale.getLanguage());

            FlockHistoryService flockHistoryService = new FlockHistoryServiceImpl();
            Map<Integer, String> historyByGrowDay = flockHistoryService.getFlockPerDayNotParsedReportsWithinRange(flockId, growDayRangeParam);

            DataDao dataDao = DbImplDecider.use(DaoType.MYSQL).getDao(DataDao.class);

            Data data1 = dataDao.getById(PerGrowDayHistoryDataType.TOTAL_EGGS_DEV_12.getId(), langId);
            List<Data> dataTotalEggsList = new ArrayList<Data>();

            dataTotalEggsList.add(data1);
            List<String> axisTitles = new ArrayList<String>();

            axisTitles.add(data1.getLabel());

//            HistoryContent historyContent = HistoryContentCreator.createPerDayHistoryContent(historyByGrowDay, dataTotalEggsList);
            HistoryContent historyContent = HistoryContentCreator.createPerDayHistoryContent(historyByGrowDay);
            Map<Integer, List<Data>> historyContentMap = new TreeMap<Integer, List<Data>>();
            historyContentMap = historyContent.getHistoryContentPerDay();

            List<Map<Integer, Data>> dataHistoryList = new ArrayList<Map<Integer, Data>>();

            dataHistoryList.add(DataGraphCreator.createHistoryData(historyContentMap, data1));

//            HashMap<String, String> dictionary = createDictionary(locale);

            String title = "Eggs Counter 12";    // "Daily Mortality";
//            String xAxisLabel = dictionary.get("graph.heater.on.time.axis.grow.day");    // "Grow Day[Day]";
            String xAxisLabel = "Grow Day[Day]";;    // "Grow Day[Day]";

            String yAxisLabel = "Eggs Counter 12";    // "Birds";

            HistoryGraph eggsCounter12Graph = new HistoryGraph();
            eggsCounter12Graph.setDataHistoryList(dataHistoryList);
            if (!growDayRangeParam.useRange()) {
                growDayRangeParam.setFromDay(1);
                growDayRangeParam.setToDay(dataHistoryList.get(0).size());
            }
            eggsCounter12Graph.createChart(title, growDayRangeParam.toString(), xAxisLabel, yAxisLabel);
            // Write the chart image to the temporary directory
            ChartRenderingInfo info = new ChartRenderingInfo(new StandardEntityCollection());
            filenamehot = ServletUtilities.saveChartAsPNG(eggsCounter12Graph.getChart(), 800, 400, info, session);

            // Write the image map to the PrintWriter
            ChartUtilities.writeImageMap(pw, filenamehot, info, false);
            session.setAttribute("filenamehot-flockid=" + flockId + "&fromday=" + fromDay + "&today=" + toDay, filenamehot);
            pw.flush();
        } catch (Exception e) {
            logger.error(NO_DATA_AVAILABLE, "Eggs Counter 12");
            filenamehot = "public_error_500x300.png";
        }
        return filenamehot;
    }

    public static String generateChartFlockFeedAndWaterPerBird(Long flockId, String fromDay, String toDay, HttpSession session, PrintWriter pw, Locale locale) {
        String filenamefw;
        FromDayToDay growDayRangeParam = new FromDayToDay(fromDay, toDay);
        try {
            Long langId = localeService.getLanguageId(locale.getLanguage());

            HashMap<String, String> dictionary = createDictionary(locale);
            String title = dictionary.get("graph.feed.water.per.bird.title");
            String y1AxisLabel = dictionary.get("graph.feed.water.axis.feed.per.bird");
            String y2AxisLabel = dictionary.get("graph.feed.water.axis.water.per.bird");
            String xAxisLabel = dictionary.get("graph.feed.water.axis.grow.day");

            FlockHistoryService flockHistoryService = new FlockHistoryServiceImpl();
            Map<Integer, String> historyByGrowDay = flockHistoryService.getFlockPerDayNotParsedReportsWithinRange(flockId, growDayRangeParam);

            List<Map<Integer, Data>> dataHistoryList = new ArrayList<Map<Integer, Data>>();
            DataDao dataDao = DbImplDecider.use(DaoType.MYSQL).getDao(DataDao.class);
            Data data1 = dataDao.getById(PerGrowDayHistoryDataType.FEED_PER_BIRD.getId(), langId);

            List<String> axisTitles = new ArrayList<String>();
            axisTitles.add(data1.getLabel());
            dataHistoryList.add(DataGraphCreator.createHistoryDataByGrowDay(historyByGrowDay, data1));

            HistoryGraph waterFeedGraph = new HistoryGraph();
            waterFeedGraph.setDataHistoryList(dataHistoryList);
            if (!growDayRangeParam.useRange()) {
                growDayRangeParam.setFromDay(1);
                growDayRangeParam.setToDay(dataHistoryList.get(0).size());
            }

            waterFeedGraph.createChart(title, growDayRangeParam.toString(), xAxisLabel, y1AxisLabel);

            Data data2 = dataDao.getById(PerGrowDayHistoryDataType.WATER_PER_BIRD.getId(), langId);
            axisTitles.add(data2.getLabel());
            dataHistoryList.clear();
            dataHistoryList.add(DataGraphCreator.createHistoryDataByGrowDay(historyByGrowDay, data2));
            waterFeedGraph.createAndAddSeriesCollection(dataHistoryList, y2AxisLabel);

            // Write the chart image to the temporary directory
            ChartRenderingInfo info = new ChartRenderingInfo(new StandardEntityCollection());
            filenamefw = ServletUtilities.saveChartAsPNG(waterFeedGraph.getChart(), 800, 400, info, session);

            // Write the image map to the PrintWriter
            ChartUtilities.writeImageMap(pw, filenamefw, info, false);
            session.setAttribute("filenamefw-flockid=" + flockId + "&fromday=" + fromDay + "&today=" + toDay, filenamefw);

            pw.flush();
        } catch (Exception e) {
            logger.error(NO_DATA_AVAILABLE, "Water/Feed per bird");
            filenamefw = "public_error_500x300.png";
        }
        return filenamefw;
    }

    public static String generateChartFlockRequiredTemperature(Long flockId, String fromDay, String toDay, HttpSession session, PrintWriter pw, Locale locale) {
        String filenamehot;
        FromDayToDay growDayRangeParam;
        Long langId;
        FlockHistoryService flockHistoryService;
        Map<Integer, String> historyByGrowDay;
        DataDao dataDao;
        Data data1;
        List<Data> dataReqTempList;////
        List<String> axisTitles;
        HistoryContent historyContent;
        Map<Integer, List<Data>> historyContentMap;
        List<Map<Integer, Data>> dataHistoryList;
        HashMap<String, String> dictionary;
        String title;////
        String xAxisLabel;////
        String yAxisLabel;////
        HistoryGraph reqTempGraph;////
        ChartRenderingInfo info;
        try {
            growDayRangeParam = new FromDayToDay(fromDay, toDay);
            langId = localeService.getLanguageId(locale.getLanguage());
            flockHistoryService = new FlockHistoryServiceImpl();
            historyByGrowDay = flockHistoryService.getFlockPerDayNotParsedReportsWithinRange(flockId, growDayRangeParam);
            dataDao = DbImplDecider.use(DaoType.MYSQL).getDao(DataDao.class);
            data1 = dataDao.getById(PerGrowDayHistoryDataType.REQUIRED_TEMP.getId(), langId);
            dataReqTempList = new ArrayList<Data>();
            dataReqTempList.add(data1);
            axisTitles = new ArrayList<String>();
            axisTitles.add(data1.getLabel());
//            historyContent = HistoryContentCreator.createPerDayHistoryContent(historyByGrowDay, dataReqTempList);
            historyContent = HistoryContentCreator.createPerDayHistoryContent(historyByGrowDay);
            historyContentMap = new TreeMap<Integer, List<Data>>();
            historyContentMap = historyContent.getHistoryContentPerDay();
            dataHistoryList = new ArrayList<Map<Integer, Data>>();
            dataHistoryList.add(DataGraphCreator.createHistoryData(historyContentMap, data1));
            dictionary = createDictionary(locale);
            title = dictionary.get("graph.required.temperature");// "Required temperature";////
            xAxisLabel = dictionary.get("graph.average.weight.axis.grow.day");// "Grow Day[Day]";////
            yAxisLabel = dictionary.get("graph.minimum.maximum.humidity.axis.temperature");// "Temperature [C]";////
            reqTempGraph = new HistoryGraph();
            reqTempGraph.setDataHistoryList(dataHistoryList);
            if (!growDayRangeParam.useRange()) {
                growDayRangeParam.setFromDay(1);
                growDayRangeParam.setToDay(dataHistoryList.get(0).size());
            }
            reqTempGraph.createChart(title, growDayRangeParam.toString(), xAxisLabel, yAxisLabel);
            // Write the chart image to the temporary directory
            info = new ChartRenderingInfo(new StandardEntityCollection());
            filenamehot = ServletUtilities.saveChartAsPNG(reqTempGraph.getChart(), 800, 400, info, session);

            // Write the image map to the PrintWriter
            ChartUtilities.writeImageMap(pw, filenamehot, info, false);
            session.setAttribute("filenamehot-flockid=" + flockId + "&fromday=" + fromDay + "&today=" + toDay, filenamehot);
            pw.flush();
        } catch (Exception e) {
            logger.error(NO_DATA_AVAILABLE, "Required temperature");
            filenamehot = "public_error_500x300.png";
        }
        return filenamehot;
    }

    public static String generateChartDailySiloFill(Long flockId, String fromDay, String toDay, HttpSession session, PrintWriter pw, Locale locale) {
        String filenamefw;
        FromDayToDay growDayRangeParam;
        Long langId;
        HashMap<String, String> dictionary;
        String title;////
        String y1AxisLabel;////
        String y2AxisLabel;////
        String xAxisLabel;////
        FlockHistoryService flockHistoryService;
        Map<Integer, String> historyByGrowDay;
        List<Map<Integer, Data>> dataHistoryList;
        DataDao dataDao;
        Data data1;
        List<String> axisTitles;
        HistoryGraph dailySiloFillGraph;
        Data data2;
        try {
            growDayRangeParam = new FromDayToDay(fromDay, toDay);
            langId = localeService.getLanguageId(locale.getLanguage());
            dictionary = createDictionary(locale);
            title = dictionary.get("graph.daily.silo.fill");////"Daily Silo Fill"
            y1AxisLabel = dictionary.get("graph.daily.silo.fill1");////"Daily silo fill 1"
            y2AxisLabel = dictionary.get("graph.daily.silo.fill2");////"Daily silo fill 2"
            xAxisLabel = dictionary.get("graph.grow.day");////Grow Day[Day]
            flockHistoryService = new FlockHistoryServiceImpl();
            historyByGrowDay = flockHistoryService.getFlockPerDayNotParsedReportsWithinRange(flockId, growDayRangeParam);
            dataHistoryList = new ArrayList<Map<Integer, Data>>();
            dataDao = DbImplDecider.use(DaoType.MYSQL).getDao(DataDao.class);
            data1 = dataDao.getById(PerGrowDayHistoryDataType.DAILY_SILO_FILL_1.getId(), langId);
            axisTitles = new ArrayList<String>();
            axisTitles.add(data1.getLabel());
            dataHistoryList.add(DataGraphCreator.createHistoryDataByGrowDay(historyByGrowDay, data1));
            dailySiloFillGraph = new HistoryGraph();
            dailySiloFillGraph.setDataHistoryList(dataHistoryList);
            if (!growDayRangeParam.useRange()) {
                growDayRangeParam.setFromDay(1);
                growDayRangeParam.setToDay(dataHistoryList.get(0).size());
            }
            dailySiloFillGraph.createChart(title, growDayRangeParam.toString(), xAxisLabel, y1AxisLabel);
            data2 = dataDao.getById(PerGrowDayHistoryDataType.DAILY_SILO_FILL_2.getId(), langId);
            axisTitles.add(data2.getLabel());
            dataHistoryList.clear();
            dataHistoryList.add(DataGraphCreator.createHistoryDataByGrowDay(historyByGrowDay, data2));
            dailySiloFillGraph.createAndAddSeriesCollection(dataHistoryList, y2AxisLabel);
            // Write the chart image to the temporary directory
            ChartRenderingInfo info = new ChartRenderingInfo(new StandardEntityCollection());
            filenamefw = ServletUtilities.saveChartAsPNG(dailySiloFillGraph.getChart(), 800, 400, info, session);
            // Write the image map to the PrintWriter
            ChartUtilities.writeImageMap(pw, filenamefw, info, false);
            session.setAttribute("filenamefw-flockid=" + flockId + "&fromday=" + fromDay + "&today=" + toDay, filenamefw);
            pw.flush();
        } catch (Exception e) {
            logger.error(NO_DATA_AVAILABLE, "Daily silo fill");
            filenamefw = "public_error_500x300.png";
        }
        return filenamefw;
    }

    public static String generateChartStandartDeviation(Long flockId, String fromDay, String toDay, HttpSession session, PrintWriter pw, Locale locale) {
        String filenamehot;
        FromDayToDay growDayRangeParam;
        Long langId;
        FlockHistoryService flockHistoryService;
        Map<Integer, String> historyByGrowDay;
        DataDao dataDao;
        Data data1;
        Data data2;
        Data data3;
        Data data4;
        List<Data> dataStandardDeviationList;
        List<String> axisTitles;
        HistoryContent historyContent;
        Map<Integer, List<Data>> historyContentMap;
        List<Map<Integer, Data>> dataHistoryList;
        growDayRangeParam = new FromDayToDay(fromDay, toDay);
        try {
            langId = localeService.getLanguageId(locale.getLanguage());
            flockHistoryService = new FlockHistoryServiceImpl();
            historyByGrowDay = flockHistoryService.getFlockPerDayNotParsedReportsWithinRange(flockId, growDayRangeParam);
            dataDao = DbImplDecider.use(DaoType.MYSQL).getDao(DataDao.class);
            data1 = dataDao.getById(PerGrowDayHistoryDataType.STD_DEV_1.getId(), langId);
            data2 = dataDao.getById(PerGrowDayHistoryDataType.STD_DEV_2.getId(), langId);
            data3 = dataDao.getById(PerGrowDayHistoryDataType.STD_DEV_3.getId(), langId);
            data4 = dataDao.getById(PerGrowDayHistoryDataType.STD_DEV_4.getId(), langId);
            dataStandardDeviationList = new ArrayList<Data>();
            dataStandardDeviationList.add(data1);
            dataStandardDeviationList.add(data2);
            dataStandardDeviationList.add(data3);
            dataStandardDeviationList.add(data4);
            axisTitles = new ArrayList<String>();
            axisTitles.add(data1.getLabel());
            axisTitles.add(data2.getLabel());
            axisTitles.add(data3.getLabel());
            axisTitles.add(data4.getLabel());
//            historyContent = HistoryContentCreator.createPerDayHistoryContent(historyByGrowDay, dataStandardDeviationList);
            historyContent = HistoryContentCreator.createPerDayHistoryContent(historyByGrowDay);
            historyContentMap = new TreeMap<Integer, List<Data>>();
            historyContentMap = historyContent.getHistoryContentPerDay();
            dataHistoryList = new ArrayList<Map<Integer, Data>>();
            dataHistoryList.add(DataGraphCreator.createHistoryData(historyContentMap, data1));
            dataHistoryList.add(DataGraphCreator.createHistoryData(historyContentMap, data2));
            dataHistoryList.add(DataGraphCreator.createHistoryData(historyContentMap, data3));
            dataHistoryList.add(DataGraphCreator.createHistoryData(historyContentMap, data4));
            HashMap<String, String> dictionary = createDictionary(locale);
            String title = dictionary.get("graph.standard.deviation");
            String xAxisLabel = dictionary.get("graph.grow.day");
            String yAxisLabel = dictionary.get("graph.standard.deviation.plate");
            HistoryGraph eggsGraph = new HistoryGraph();
            eggsGraph.setDataHistoryList(dataHistoryList);
            if (!growDayRangeParam.useRange()) {
                growDayRangeParam.setFromDay(1);
                growDayRangeParam.setToDay(dataHistoryList.get(0).size());
            }
            eggsGraph.createChart(title, growDayRangeParam.toString(), xAxisLabel, yAxisLabel);
            // Write the chart image to the temporary directory
            ChartRenderingInfo info = new ChartRenderingInfo(new StandardEntityCollection());
            filenamehot = ServletUtilities.saveChartAsPNG(eggsGraph.getChart(), 800, 400, info, session);
            // Write the image map to the PrintWriter
            ChartUtilities.writeImageMap(pw, filenamehot, info, false);
            session.setAttribute("filenamehot-flockid=" + flockId + "&fromday=" + fromDay + "&today=" + toDay, filenamehot);
            pw.flush();
        } catch (Exception e) {
            logger.error(NO_DATA_AVAILABLE, "Standard Deviation");
            filenamehot = "public_error_500x300.png";
        }
        return filenamehot;
    }

    public static String generateChartCV(Long flockId, String fromDay, String toDay, HttpSession session, PrintWriter pw, Locale locale) {
        String filenamehot;
        FromDayToDay growDayRangeParam;
        Long langId;
        FlockHistoryService flockHistoryService;
        Map<Integer, String> historyByGrowDay;
        DataDao dataDao;
        Data data1;
        Data data2;
        Data data3;
        Data data4;
        List<Data> dataCVList;
        List<String> axisTitles;
        HistoryContent historyContent;
        Map<Integer, List<Data>> historyContentMap;
        List<Map<Integer, Data>> dataHistoryList;
        growDayRangeParam = new FromDayToDay(fromDay, toDay);
        try {
            langId = localeService.getLanguageId(locale.getLanguage());
            flockHistoryService = new FlockHistoryServiceImpl();
            historyByGrowDay = flockHistoryService.getFlockPerDayNotParsedReportsWithinRange(flockId, growDayRangeParam);
            dataDao = DbImplDecider.use(DaoType.MYSQL).getDao(DataDao.class);
            data1 = dataDao.getById(PerGrowDayHistoryDataType.CV_PLATE_1.getId(), langId);
            data2 = dataDao.getById(PerGrowDayHistoryDataType.CV_PLATE_2.getId(), langId);
            data3 = dataDao.getById(PerGrowDayHistoryDataType.CV_PLATE_3.getId(), langId);
            data4 = dataDao.getById(PerGrowDayHistoryDataType.CV_PLATE_4.getId(), langId);
            dataCVList = new ArrayList<Data>();
            dataCVList.add(data1);
            dataCVList.add(data2);
            dataCVList.add(data3);
            dataCVList.add(data4);
            axisTitles = new ArrayList<String>();
            axisTitles.add(data1.getLabel());
            axisTitles.add(data2.getLabel());
            axisTitles.add(data3.getLabel());
            axisTitles.add(data4.getLabel());
//            historyContent = HistoryContentCreator.createPerDayHistoryContent(historyByGrowDay, dataCVList);
            historyContent = HistoryContentCreator.createPerDayHistoryContent(historyByGrowDay);
            historyContentMap = new TreeMap<Integer, List<Data>>();
            historyContentMap = historyContent.getHistoryContentPerDay();
            dataHistoryList = new ArrayList<Map<Integer, Data>>();
            dataHistoryList.add(DataGraphCreator.createHistoryData(historyContentMap, data1));
            dataHistoryList.add(DataGraphCreator.createHistoryData(historyContentMap, data2));
            dataHistoryList.add(DataGraphCreator.createHistoryData(historyContentMap, data3));
            dataHistoryList.add(DataGraphCreator.createHistoryData(historyContentMap, data4));
            HashMap<String, String> dictionary = createDictionary(locale);
            String title = dictionary.get("graph.cv");
            String xAxisLabel = dictionary.get("graph.grow.day");
            String yAxisLabel = dictionary.get("graph.cv.plate");
            HistoryGraph eggsGraph = new HistoryGraph();
            eggsGraph.setDataHistoryList(dataHistoryList);
            if (!growDayRangeParam.useRange()) {
                growDayRangeParam.setFromDay(1);
                growDayRangeParam.setToDay(dataHistoryList.get(0).size());
            }
            eggsGraph.createChart(title, growDayRangeParam.toString(), xAxisLabel, yAxisLabel);
            // Write the chart image to the temporary directory
            ChartRenderingInfo info = new ChartRenderingInfo(new StandardEntityCollection());
            filenamehot = ServletUtilities.saveChartAsPNG(eggsGraph.getChart(), 800, 400, info, session);
            // Write the image map to the PrintWriter
            ChartUtilities.writeImageMap(pw, filenamehot, info, false);
            session.setAttribute("filenamehot-flockid=" + flockId + "&fromday=" + fromDay + "&today=" + toDay, filenamehot);
            pw.flush();
        } catch (Exception e) {
            logger.error(NO_DATA_AVAILABLE, "CV");
            filenamehot = "public_error_500x300.png";
        }
        return filenamehot;
    }
}



//    public static String generateChartWaterFeedTempSpecial(Long controllerId, HttpSession session, PrintWriter pw, Locale locale) {//last 24 hour
//        String filenamewft="";
////        String filenamewft;
////        ControllerDao controllerDao;
////        String values;
////        DataDao dataDao;
////        Data setClock;
////        FeedWaterConsumpTempGraph graph;
////        ChartRenderingInfo info;
////        try {
////            controllerDao = DbImplDecider.use(DaoType.MYSQL).getDao(ControllerDao.class);
////            values = controllerDao.getControllerGraph(controllerId);
////            dataDao = DbImplDecider.use(DaoType.MYSQL).getDao(DataDao.class);
////            setClock = dataDao.getSetClockByController(controllerId);
////            if (values == null) {
////                logger.error(NO_DATA_AVAILABLE, "Feed/Water");
////                throw new Exception(NO_DATA_AVAILABLE);
////            } else {
////                if (setClock.getValue() == null) {
////                    graph = new FeedWaterConsumpTempGraph(values, Long.valueOf("0"), locale);
////                } else {
////                    graph = new FeedWaterConsumpTempGraph(values, setClock.getValueToUI(), locale);
////                }
////                // Write the chart image to the temporary directory
////                info = new ChartRenderingInfo(new StandardEntityCollection());
////                filenamewft = ServletUtilities.saveChartAsPNG(graph.createChart(), 800, 400, info, session);
////                // Write the image map to the PrintWriter
////                ChartUtilities.writeImageMap(pw, filenamewft, info, false);
////                session.setAttribute("filenamewct", filenamewft);
////                pw.flush();
////            }
////        } catch (Exception e) {
////            logger.error(NO_DATA_AVAILABLE, "Feed/Water");
////            filenamewft = "public_error_500x300.png";
////        }
//        return filenamewft;
//    }
//
//    public static String generateChartFeedWaterPerBird24Special(Long controllerId, HttpSession session, PrintWriter pw, Locale locale) {// last 24 hour
//        String filenamewft="";
////        String filenamewft;
////        ControllerDao controllerDao;
////        String values;
////        DataDao dataDao;
////        Data setClock;
////        FeedWaterPerBirdGraph graph;
////        ChartRenderingInfo info;
////        try {
////            controllerDao = DbImplDecider.use(DaoType.MYSQL).getDao(ControllerDao.class);
////            values = controllerDao.getControllerGraph(controllerId);
////            dataDao = DbImplDecider.use(DaoType.MYSQL).getDao(DataDao.class);
////            setClock = dataDao.getSetClockByController(controllerId);
////            if (values == null) {
////                logger.error(NO_DATA_AVAILABLE, "Feed/Water per Bird");
////                throw new Exception(NO_DATA_AVAILABLE);
////            } else {
////                if (setClock.getValue() == null) {
////                    graph = new FeedWaterPerBirdGraph(values, Long.valueOf("0"), locale);
////                } else {
////                    graph = new FeedWaterPerBirdGraph(values, setClock.getValueToUI(), locale);
////                }
////                // Write the chart image to the temporary directory
////                info = new ChartRenderingInfo(new StandardEntityCollection());
////                filenamewft = ServletUtilities.saveChartAsPNG(graph.createChart(), 800, 400, info, session);
////                // Write the image map to the PrintWriter
////                ChartUtilities.writeImageMap(pw, filenamewft, info, false);
////                session.setAttribute("filenamewft", filenamewft);
////                pw.flush();
////            }
////        } catch (Exception e) {
////            logger.error(NO_DATA_AVAILABLE, "Feed/Water per Bird");
////            filenamewft = "public_error_500x300.png";
////        }
//        return filenamewft;
//    }
//
//    public static String generateChartFeed2Water2_24Special(Long controllerId, HttpSession session, PrintWriter pw, Locale locale) {// last 24 hour
//        String filenamewft="";
////        String filenamewft;
////        ControllerDao controllerDao;
////        String values;
////        DataDao dataDao;
////        Data setClock;
////        Feed2Water2ConsGraph graph;
////        ChartRenderingInfo info;
////        try {
////            controllerDao = DbImplDecider.use(DaoType.MYSQL).getDao(ControllerDao.class);
////            values = controllerDao.getControllerGraph(controllerId);
////            dataDao = DbImplDecider.use(DaoType.MYSQL).getDao(DataDao.class);
////            setClock = dataDao.getSetClockByController(controllerId);
////            if (values == null) {
////                logger.error(NO_DATA_AVAILABLE, "Feed2/Water2");
////                throw new Exception(NO_DATA_AVAILABLE);
////            } else {
////                if (setClock.getValue() == null) {
////                    graph = new Feed2Water2ConsGraph(values, Long.valueOf("0"), locale);
////                } else {
////                    graph = new Feed2Water2ConsGraph(values, setClock.getValueToUI(), locale);
////                }
////                // Write the chart image to the temporary directory
////                info = new ChartRenderingInfo(new StandardEntityCollection());
////                filenamewft = ServletUtilities.saveChartAsPNG(graph.createChart(), 800, 400, info, session);
////                // Write the image map to the PrintWriter
////                ChartUtilities.writeImageMap(pw, filenamewft, info, false);
////                session.setAttribute("filenamewft", filenamewft);
////                pw.flush();
////            }
////        } catch (Exception e) {
////            logger.error(NO_DATA_AVAILABLE, "Feed2/Water2");
////            filenamewft = "public_error_500x300.png";
////        }
//        return filenamewft;
//    }
//
//    public static String generateChartWaterSum_24Special(Long controllerId, HttpSession session, PrintWriter pw, Locale locale) {// last 24 hour
//        String filenamewft="";
////        String filenamewft;
////        ControllerDao controllerDao;
////        String values;
////        DataDao dataDao;
////        Data setClock;
////        WaterSumConsGraph graph;
////        ChartRenderingInfo info;
////        try {
////            controllerDao = DbImplDecider.use(DaoType.MYSQL).getDao(ControllerDao.class);
////            values = controllerDao.getControllerGraph(controllerId);
////            dataDao = DbImplDecider.use(DaoType.MYSQL).getDao(DataDao.class);
////            setClock = dataDao.getSetClockByController(controllerId);
////            if (values == null) {
////                logger.error(NO_DATA_AVAILABLE, "Water Sum");
////                throw new Exception(NO_DATA_AVAILABLE);
////            } else {
////                if (setClock.getValue() == null) {
////                    graph = new WaterSumConsGraph(values, Long.valueOf("0"), locale);
////                } else {
////                    graph = new WaterSumConsGraph(values, setClock.getValueToUI(), locale);
////                }
////                // Write the chart image to the temporary directory
////                info = new ChartRenderingInfo(new StandardEntityCollection());
////                filenamewft = ServletUtilities.saveChartAsPNG(graph.createChart(), 800, 400, info, session);
////                // Write the image map to the PrintWriter
////                ChartUtilities.writeImageMap(pw, filenamewft, info, false);
////                session.setAttribute("filenamewft", filenamewft);
////                pw.flush();
////            }
////        } catch (Exception e) {
////            logger.error(NO_DATA_AVAILABLE, "Water Sum");
////            filenamewft = "public_error_500x300.png";
////        }
//        return filenamewft;
//    }