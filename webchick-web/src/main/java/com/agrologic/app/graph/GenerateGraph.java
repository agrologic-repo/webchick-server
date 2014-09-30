package com.agrologic.app.graph;

import com.agrologic.app.dao.ControllerDao;
import com.agrologic.app.dao.DaoType;
import com.agrologic.app.dao.DataDao;
import com.agrologic.app.dao.DbImplDecider;
import com.agrologic.app.graph.daily.FeedWaterConsumpTemp;
import com.agrologic.app.graph.daily.InsideOutsideHumidityGraph;
import com.agrologic.app.graph.daily.PerHourReportGraph;
import com.agrologic.app.graph.history.HistoryGraph;
import com.agrologic.app.management.PerGrowDayHistoryDataType;
import com.agrologic.app.model.Data;
import com.agrologic.app.model.history.DayParam;
import com.agrologic.app.model.history.FromDayToDay;
import com.agrologic.app.service.LocaleService;
import com.agrologic.app.service.history.FlockHistoryService;
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

    /**
     * Creates temperature and humidity chart and return path to png file with map of each series .
     *
     * @param controllerId controller id
     * @param session      the session
     * @param pw           PrintWriter object
     * @param locale       the current locale
     * @return the path to png file with chart
     */
    public static String generateChartTempHum(Long controllerId, HttpSession session, PrintWriter pw, Locale locale) {
        String filenameth;

        try {
            ControllerDao controllerDao = DbImplDecider.use(DaoType.MYSQL).getDao(ControllerDao.class);
            String values = controllerDao.getControllerGraph(controllerId);
            DataDao dataDao = DbImplDecider.use(DaoType.MYSQL).getDao(DataDao.class);
            Data setClock = dataDao.getSetClockByController(controllerId);

            if (values == null) {
                logger.error(NO_DATA_AVAILABLE, "Temperature nad Humidity");
                throw new Exception(NO_DATA_AVAILABLE);
            } else {
                InsideOutsideHumidityGraph graph;
                if (setClock.getValue() == null) {
                    graph = new InsideOutsideHumidityGraph(values, Long.valueOf("0"), locale);
                } else {
                    graph = new InsideOutsideHumidityGraph(values, setClock.getValueToUI(), locale);
                }

                // Write the chart image to the temporary directory
                ChartRenderingInfo info = new ChartRenderingInfo(new StandardEntityCollection());
                filenameth = ServletUtilities.saveChartAsPNG(graph.createChart(), 1024, 500, info, session);

                // Write the image map to the PrintWriter
                ChartUtilities.writeImageMap(pw, filenameth, info, false);
                session.setAttribute("filenameth", filenameth);
                pw.flush();
            }
        } catch (Exception e) {
            logger.error(NO_DATA_AVAILABLE, "Feed/Water");
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
    public static String generateChartWaterFeedTemp(Long controllerId, HttpSession session, PrintWriter pw,
                                                    Locale locale) {
        String filenamewft;

        try {
            ControllerDao controllerDao = DbImplDecider.use(DaoType.MYSQL).getDao(ControllerDao.class);
            String values = controllerDao.getControllerGraph(controllerId);
            DataDao dataDao = DbImplDecider.use(DaoType.MYSQL).getDao(DataDao.class);
            Data setClock = dataDao.getSetClockByController(controllerId);

            if (values == null) {
                logger.error(NO_DATA_AVAILABLE, "Feed/Water");
                throw new Exception(NO_DATA_AVAILABLE);
            } else {
                FeedWaterConsumpTemp graph;
                if (setClock.getValue() == null) {
                    graph = new FeedWaterConsumpTemp(values, Long.valueOf("0"), locale);
                } else {
                    graph = new FeedWaterConsumpTemp(values, setClock.getValueToUI(), locale);
                }

                // Write the chart image to the temporary directory
                ChartRenderingInfo info = new ChartRenderingInfo(new StandardEntityCollection());

                filenamewft = ServletUtilities.saveChartAsPNG(graph.createChart(), 1024, 500, info, session);

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

    /**
     * Creates temperature and humidity chart and return path to png file with map of each series .
     *
     * @param flockId the flock id
     * @param session the session
     * @param pw      PrintWriter object
     * @param locale  the current locale
     * @return the path to png file with chart
     */
    public static String generateChartFlockTempHum(Long flockId, String growDay, HttpSession session, PrintWriter pw,
                                                   Locale locale) {
        DayParam growDayParam = new DayParam(growDay);
        String filenameth;

        Long langId = 1L;
        try {
            FlockHistoryService flockHistoryService = new FlockHistoryServiceImpl();
            Collection<String> historyValueList = flockHistoryService.getFlockPerHourReportsTitlesUsingGraphObjects(flockId,
                    growDayParam.getGrowDay(), langId);
            StringBuilder chainedHistoryValues = new StringBuilder();
            for (String historyValues : historyValueList) {
                chainedHistoryValues.append(historyValues);
            }
            Long resetTime = flockHistoryService.getResetTime(flockId, growDayParam.getGrowDay());
            PerHourReportGraph perHourReportGraph = new InsideOutsideHumidityGraph(chainedHistoryValues.toString(), resetTime, locale);

            // Write the chart image to the temporary directory
            ChartRenderingInfo info = new ChartRenderingInfo(new StandardEntityCollection());
            filenameth = ServletUtilities.saveChartAsPNG(perHourReportGraph.createChart(), 1024, 500, info, session);

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
    public static String generateChartFlockWaterFeedTemp(Long flockId, String growDay,
                                                         HttpSession session, PrintWriter pw, Locale locale) {


        DayParam growDayParam = new DayParam(growDay);
        String filenamewft;

        Long langId = 1L;
        try {
            FlockHistoryService flockHistoryService = new FlockHistoryServiceImpl();
            Collection<String> historyValueList = flockHistoryService.getFlockPerHourReportsTitlesUsingGraphObjects(flockId,
                    growDayParam.getGrowDay(), langId);
            StringBuilder chainedHistoryValues = new StringBuilder();
            for (String historyValues : historyValueList) {
                chainedHistoryValues.append(historyValues);
            }
            Long resetTime = flockHistoryService.getResetTime(flockId, growDayParam.getGrowDay());
            PerHourReportGraph perHourReportGraph = new FeedWaterConsumpTemp(chainedHistoryValues.toString(), resetTime, locale);

            // Write the chart image to the temporary directory
            ChartRenderingInfo info = new ChartRenderingInfo(new StandardEntityCollection());
            filenamewft = ServletUtilities.saveChartAsPNG(perHourReportGraph.createChart(), 1024, 500, info, session);

            // Write the image map to the PrintWriter
            ChartUtilities.writeImageMap(pw, filenamewft, info, false);
            session.setAttribute("filenamewct", filenamewft);
            pw.flush();
        } catch (Exception e) {
            logger.error(NO_DATA_AVAILABLE, "Feed/Water");
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
    public static String generateChartFlockWaterFeed(Long flockId, String fromDay, String toDay,
                                                     HttpSession session, PrintWriter pw, Locale locale) {
        String filenamefw;
        FromDayToDay growDayRangeParam = new FromDayToDay(fromDay, toDay);
        try {
            Long langId = localeService.getLanguageId(locale.getLanguage());

            HashMap<String, String> dictionary = createDictionary(locale);
            String title = dictionary.get("graph.feed.water.title");
            String y1AxisLabel = dictionary.get("graph.feed.water.axis.feed");
            String y2AxisLabel = dictionary.get("graph.feed.water.axis.water");
            String xAxisLabel = dictionary.get("graph.feed.water.axis.grow.day");

            FlockHistoryService flockHistoryService = new FlockHistoryServiceImpl();
            Map<Integer, String> historyByGrowDay = flockHistoryService.getFlockPerDayNotParsedReportsWithinRange(flockId, growDayRangeParam);

            List<Map<Integer, Data>> dataHistoryList = new ArrayList<Map<Integer, Data>>();
            DataDao dataDao = DbImplDecider.use(DaoType.MYSQL).getDao(DataDao.class);
            Data data1 = dataDao.getById(PerGrowDayHistoryDataType.FEED_CONSUMPTION_ID.getId(), langId);

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

            Data data2 = dataDao.getById(PerGrowDayHistoryDataType.WATER_CONSUMPTION_ID.getId(), langId);
            axisTitles.add(data2.getLabel());
            dataHistoryList.clear();
            dataHistoryList.add(DataGraphCreator.createHistoryDataByGrowDay(historyByGrowDay, data2));
            waterFeedGraph.createAndAddSeriesCollection(dataHistoryList, y2AxisLabel);

            // Write the chart image to the temporary directory
            ChartRenderingInfo info = new ChartRenderingInfo(new StandardEntityCollection());
            filenamefw = ServletUtilities.saveChartAsPNG(waterFeedGraph.getChart(), 1024, 500, info, session);

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
    public static String generateChartFlockAverageWeight(Long flockId, String fromDay, String toDay,
                                                         HttpSession session, PrintWriter pw, Locale locale) {

        String filenameaw;
        FromDayToDay growDayRangeParam = new FromDayToDay(fromDay, toDay);
        try {
            Long langId = localeService.getLanguageId(locale.getLanguage());

            FlockHistoryService flockHistoryService = new FlockHistoryServiceImpl();
            Map<Integer, String> historyByGrowDay = flockHistoryService.getFlockPerDayNotParsedReportsWithinRange(flockId,
                    growDayRangeParam);

            DataDao dataDao = DbImplDecider.use(DaoType.MYSQL).getDao(DataDao.class);
            List<Map<Integer, Data>> dataHistoryList = new ArrayList<Map<Integer, Data>>();

            Data data1 = dataDao.getById(PerGrowDayHistoryDataType.AVERAGE_WEIGHT_1_ID.getId(), langId);
            dataHistoryList.add(DataGraphCreator.createHistoryDataByGrowDay(historyByGrowDay, data1));

            Data data2 = dataDao.getById(PerGrowDayHistoryDataType.AVERAGE_WEIGHT_2_ID.getId(), langId);
            dataHistoryList.add(DataGraphCreator.createHistoryDataByGrowDay(historyByGrowDay, data2));

            Data data3 = dataDao.getById(PerGrowDayHistoryDataType.AVERAGE_WEIGHT_3_ID.getId(), langId);
            dataHistoryList.add(DataGraphCreator.createHistoryDataByGrowDay(historyByGrowDay, data3));

            Data data4 = dataDao.getById(PerGrowDayHistoryDataType.AVERAGE_WEIGHT_4_ID.getId(), langId);
            dataHistoryList.add(DataGraphCreator.createHistoryDataByGrowDay(historyByGrowDay, data4));


            List<String> axisTitles = new ArrayList<String>();
            axisTitles.add(data1.getLabel());
            axisTitles.add(data2.getLabel());
            axisTitles.add(data3.getLabel());
            axisTitles.add(data4.getLabel());

            HistoryGraph averageWeightGraph = new HistoryGraph();
            averageWeightGraph.setDataHistoryList(dataHistoryList);

            HashMap<String, String> dictionary = createDictionary(locale);
            String title = dictionary.get("graph.average.weight.title");    // "Daily Mortality";
            String xAxisLabel = dictionary.get("graph.average.weight.axis.grow.day");    // "Grow Day[Day]";
            String yAxisLabel = dictionary.get("graph.average.weight.axis.weight");    // "Birds";
            if (!growDayRangeParam.useRange()) {
                growDayRangeParam.setFromDay(1);
                growDayRangeParam.setToDay(dataHistoryList.get(0).size());
            }
            averageWeightGraph.createChart(title, growDayRangeParam.toString(), xAxisLabel, yAxisLabel);
            // Write the chart image to the temporary directory
            ChartRenderingInfo info = new ChartRenderingInfo(new StandardEntityCollection());
            filenameaw = ServletUtilities.saveChartAsPNG(averageWeightGraph.getChart(), 1024, 500, info, session);

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
    public static String generateChartFlockHeatOnTime(Long flockId, String fromDay, String toDay, HttpSession session,
                                                      PrintWriter pw, Locale locale) {

        String filenamehot;
        FromDayToDay growDayRangeParam = new FromDayToDay(fromDay, toDay);
        try {
            Long langId = localeService.getLanguageId(locale.getLanguage());

            FlockHistoryService flockHistoryService = new FlockHistoryServiceImpl();
            Map<Integer, String> historyByGrowDay = flockHistoryService.getFlockPerDayNotParsedReportsWithinRange(flockId, growDayRangeParam);

            DataDao dataDao = DbImplDecider.use(DaoType.MYSQL).getDao(DataDao.class);
            Data data1 = dataDao.getById(PerGrowDayHistoryDataType.HEATER_1_TIME_ON.getId(), langId);

            List<String> axisTitles = new ArrayList<String>();
            axisTitles.add(data1.getLabel());

            List<Map<Integer, Data>> dataHistoryList = new ArrayList<Map<Integer, Data>>();
            dataHistoryList.add(DataGraphCreator.createHistoryDataByGrowDay(historyByGrowDay, data1));

            Data data2 = dataDao.getById(PerGrowDayHistoryDataType.HEATER_2_TIME_ON.getId(), langId);

            axisTitles.add(data2.getLabel());
            dataHistoryList.add(DataGraphCreator.createHistoryDataByGrowDay(historyByGrowDay, data2));

            Data data3 = dataDao.getById(PerGrowDayHistoryDataType.HEATER_3_TIME_ON.getId(), langId);

            axisTitles.add(data3.getLabel());
            dataHistoryList.add(DataGraphCreator.createHistoryDataByGrowDay(historyByGrowDay, data3));

            Data data4 = dataDao.getById(PerGrowDayHistoryDataType.HEATER_4_TIME_ON.getId(), langId);

            axisTitles.add(data4.getLabel());
            dataHistoryList.add(DataGraphCreator.createHistoryDataByGrowDay(historyByGrowDay, data4));

            Data data5 = dataDao.getById(PerGrowDayHistoryDataType.HEATER_5_TIME_ON.getId(), langId);

            axisTitles.add(data5.getLabel());
            dataHistoryList.add(DataGraphCreator.createHistoryDataByGrowDay(historyByGrowDay, data5));

            Data data6 = dataDao.getById(PerGrowDayHistoryDataType.HEATER_6_TIME_ON.getId(), langId);

            axisTitles.add(data6.getLabel());
            dataHistoryList.add(DataGraphCreator.createHistoryDataByGrowDay(historyByGrowDay, data6));

            HashMap<String, String> dictionary = createDictionary(locale);
            String title = dictionary.get("graph.heater.on.time.title");    // "Daily Mortality";
            String xAxisLabel = dictionary.get("graph.heater.on.time.axis.grow.day");    // "Grow Day[Day]";
            String yAxisLabel = dictionary.get("graph.heater.on.time.axis.time");    // "Birds";

            HistoryGraph heatontimeGraph = new HistoryGraph();
            heatontimeGraph.setDataHistoryList(dataHistoryList);
            if (!growDayRangeParam.useRange()) {
                growDayRangeParam.setFromDay(1);
                growDayRangeParam.setToDay(dataHistoryList.get(0).size());
            }
            heatontimeGraph.createChart(title, growDayRangeParam.toString(), xAxisLabel, yAxisLabel);
            // Write the chart image to the temporary directory
            ChartRenderingInfo info = new ChartRenderingInfo(new StandardEntityCollection());
            filenamehot = ServletUtilities.saveChartAsPNG(heatontimeGraph.getChart(), 1024, 500, info, session);

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
    public static String generateChartFlockMortality(Long flockId, String fromDay, String toDay, HttpSession session,
                                                     PrintWriter pw, Locale locale) {

        String filenamem;
        FromDayToDay growDayRangeParam = new FromDayToDay(fromDay, toDay);
        try {
            Long langId = localeService.getLanguageId(locale.getLanguage());

            FlockHistoryService flockHistoryService = new FlockHistoryServiceImpl();
            Map<Integer, String> historyByGrowDay = flockHistoryService.getFlockPerDayNotParsedReportsWithinRange(flockId, growDayRangeParam);

            List<Map<Integer, Data>> dataHistoryList = new ArrayList<Map<Integer, Data>>();
            List<String> axisTitles = new ArrayList<String>();

            DataDao dataDao = DbImplDecider.use(DaoType.MYSQL).getDao(DataDao.class);
            Data data1 = dataDao.getById(PerGrowDayHistoryDataType.DAY_MORTALITY.getId(), langId);
            axisTitles.add(data1.getLabel());
            dataHistoryList.add(DataGraphCreator.createHistoryDataByGrowDay(historyByGrowDay, data1));

            Data data2 = dataDao.getById(PerGrowDayHistoryDataType.DAY_MORTALITY_MALE.getId(), langId);
            axisTitles.add(data2.getLabel());
            dataHistoryList.add(DataGraphCreator.createHistoryDataByGrowDay(historyByGrowDay, data2));

            Data data3 = dataDao.getById(PerGrowDayHistoryDataType.DAY_MORTALITY_FEMALE.getId(), langId);
            axisTitles.add(data3.getLabel());
            dataHistoryList.add(DataGraphCreator.createHistoryDataByGrowDay(historyByGrowDay, data3));

            HashMap<String, String> dictionary = createDictionary(locale);
            String title = dictionary.get("graph.mortality.title");    // "Daily Mortality";
            String xAxisTitle = dictionary.get("graph.mortality.axis.grow.day");    // "Grow Day[Day]";
            String yAxisTitle = dictionary.get("graph.mortality.axis.birds");    // "Birds";
            HistoryGraph mortalityGraph = new HistoryGraph();
            mortalityGraph.setDataHistoryList(dataHistoryList);

            if (!growDayRangeParam.useRange()) {
                growDayRangeParam.setFromDay(1);
                growDayRangeParam.setToDay(dataHistoryList.get(0).size());
            }

            mortalityGraph.createChart(title, growDayRangeParam.toString(), xAxisTitle, yAxisTitle);

            // Write the chart image to the temporary directory
            ChartRenderingInfo info = new ChartRenderingInfo(new StandardEntityCollection());
            filenamem = ServletUtilities.saveChartAsPNG(mortalityGraph.getChart(), 1024, 500, info, session);

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
     * Creates minimum and maximum temperature and humidity chart and return path to png file with map of each series .
     *
     * @param flockId the flock id
     * @param session the session
     * @param pw      PrintWriter object
     * @param locale  the current locale
     * @return the path to png file with chart
     */
    public static String generateChartFlockMinMaxTemperatureHumidity(Long flockId, String fromDay, String toDay,
                                                                     HttpSession session, PrintWriter pw, Locale locale) {

        String filenamemmh;
        FromDayToDay growDayRangeParam = new FromDayToDay(fromDay, toDay);
        try {
            Long langId = localeService.getLanguageId(locale.getLanguage());

            FlockHistoryService flockHistoryService = new FlockHistoryServiceImpl();
            Map<Integer, String> historyByGrowDay = flockHistoryService.getFlockPerDayNotParsedReportsWithinRange(flockId, growDayRangeParam);

            DataDao dataDao = DbImplDecider.use(DaoType.MYSQL).getDao(DataDao.class);
            Data data = dataDao.getById(PerGrowDayHistoryDataType.MAX_TEMP_IN.getId(), langId);

            Map<Integer, Data> interestData = createDataSet(historyByGrowDay, data);
            data = dataDao.getById(PerGrowDayHistoryDataType.MIN_TEMP_IN.getId(), langId);

            Map<Integer, Data> interestData2 = createDataSet(historyByGrowDay, data);
            data = dataDao.getById(PerGrowDayHistoryDataType.MAX_TEMP_OUT.getId(), langId);

            Map<Integer, Data> interestData3 = createDataSet(historyByGrowDay, data);
            data = dataDao.getById(PerGrowDayHistoryDataType.MIN_TEMP_OUT.getId(), langId);

            Map<Integer, Data> interestData4 = createDataSet(historyByGrowDay, data);
            CombinedXYGraph combGraph = new CombinedXYGraph();

            HashMap<String, String> dictionary = createDictionary(locale);
            String title = dictionary.get("graph.minimum.maximum.humidity.title");    // "Daily Mortality";
            String xAxisTitle = dictionary.get("graph.minimum.maximum.humidity.axis.grow.day");    // "Grow Day[Day]";
            String y1AxisTitle = dictionary.get("graph.minimum.maximum.humidity.axis.temperature");    // "Birds";
            String y2AxisTitle = dictionary.get("graph.minimum.maximum.humidity.axis.humidity");    // "Birds";


            combGraph.createFirstNextPlot(title, xAxisTitle, y1AxisTitle, data, 0, interestData, interestData2);

            combGraph.createNextPlot(title, xAxisTitle, y1AxisTitle, data, 0, interestData3, interestData4);
            data = dataDao.getById(PerGrowDayHistoryDataType.MAX_HUMIDITY.getId(), langId);

            Map<Integer, Data> interestData5 = createDataSet(historyByGrowDay, data);
            data = dataDao.getById(PerGrowDayHistoryDataType.MIN_HUMIDITY.getId(), langId);

            Map<Integer, Data> interestData6 = createDataSet(historyByGrowDay, data);

            combGraph.createNextPlot("", xAxisTitle, y2AxisTitle, data, 1, interestData5, interestData6);
            combGraph.createChart(title, growDayRangeParam.toString(), xAxisTitle);
            // Write the chart image to the temporary directory
            ChartRenderingInfo info = new ChartRenderingInfo(new StandardEntityCollection());
            filenamemmh = ServletUtilities.saveChartAsPNG(combGraph.getChart(), 1024, 500, info, session);

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
        HashMap<String, String> dictionary = new HashMap<String, String>();
        ResourceBundle bundle = ResourceBundle.getBundle("labels", locale);
        for (Enumeration<String> e = bundle.getKeys(); e.hasMoreElements(); ) {
            String key = e.nextElement();
            if (key.startsWith("graph")) {
                String value = bundle.getString(key);
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
        Map<Integer, Data> dataSet = new HashMap<Integer, Data>();
        Iterator iter = historyByGrowDay.keySet().iterator();

        while (iter.hasNext()) {
            Integer key = (Integer) iter.next();
            String value = historyByGrowDay.get(key);
            StringTokenizer st = new StringTokenizer(value, " ");

            while (st.hasMoreElements()) {
                try {
                    String dataElem = (String) st.nextElement();
                    String valElem = (String) st.nextElement();
                    String dataType = data.getType().toString();

                    if (dataElem.equals(dataType) && (valElem.indexOf('-') == -1)) {
                        data.setValueFromUI(valElem);

                        Data cloned = (Data) data.clone();

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
}



