package com.agrologic.app.graph;

import com.agrologic.app.dao.*;
import com.agrologic.app.graph.daily.FeedWaterConsumpTemp;
import com.agrologic.app.graph.daily.InsideOutsideHumidityGraph;
import com.agrologic.app.graph.daily.PerHourReportGraph;
import com.agrologic.app.graph.history.HistoryGraph;
import com.agrologic.app.management.PerGrowDayHistoryDataType;
import com.agrologic.app.model.Data;
import com.agrologic.app.model.history.DayParam;
import com.agrologic.app.model.history.FromDayToDay;
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
    public static final String FEED_KG_AXIST_TITLE = "Feed[KG]";
    public static final String FEED_AND_WATER_CONSUMPTION_TITLE = "Feed And Water Consumption";
    public static final String GROW__DAY_AXIS_TITLE = "Grow Day[Day]";
    public static final String WATER_LITER_AXIS_TITLE = "Water[Liter]";

    private static final Logger logger = LoggerFactory.getLogger(GenerateGraph.class);

    /**
     * @param controllerId
     * @param session
     * @param pw
     * @param locale
     * @return
     */
    public static String generateChartTempHum(Long controllerId, HttpSession session, PrintWriter pw,
                                              Locale locale) {
        String filenameth;

        try {
            ControllerDao controllerDao = DbImplDecider.use(DaoType.MYSQL).getDao(ControllerDao.class);
            String values = controllerDao.getControllerGraph(controllerId);
            DataDao dataDao = DbImplDecider.use(DaoType.MYSQL).getDao(DataDao.class);
            Data setClock = dataDao.getSetClockByController(controllerId);

            if (values == null) {
                logger.error("No data available in database to create Temperature\\Humidity graph .");
                throw new Exception("No data available in database to create Temperature\\Humidity graph .");
            } else {
                InsideOutsideHumidityGraph graph;
                if (setClock.getValue() == null) {
                    graph = new InsideOutsideHumidityGraph(values, Long.valueOf("0"), locale);
                } else {
                    graph = new InsideOutsideHumidityGraph(values, setClock.getValueToUI(), locale);
                }

                // Write the chart image to the temporary directory
                ChartRenderingInfo info = new ChartRenderingInfo(new StandardEntityCollection());
                filenameth = ServletUtilities.saveChartAsPNG(graph.createChart(), 800, 600, info, session);

                // Write the image map to the PrintWriter
                ChartUtilities.writeImageMap(pw, filenameth, info, false);
                session.setAttribute("filenameth", filenameth);
                pw.flush();
            }
        } catch (Exception e) {
            logger.error("No data available in database to create Temperature\\Humidity graph .", e);
            filenameth = "public_error_500x300.png";
        }
        return filenameth;
    }

    /**
     * @param flockId
     * @param growDay
     * @param session
     * @param pw
     * @param locale
     * @return
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
            filenameth = ServletUtilities.saveChartAsPNG(perHourReportGraph.createChart(), 800, 600, info, session);

            // Write the image map to the PrintWriter
            ChartUtilities.writeImageMap(pw, filenameth, info, false);
            session.setAttribute("filenameth-flockid=" + flockId + "&growday=" + growDay, filenameth);
            pw.flush();
        } catch (Exception e) {
            logger.error("No data available in database to create Temperature\\Humidity graph .", e);
            filenameth = "public_error_500x300.png";
        }
        return filenameth;
    }

    /**
     * @param controllerId
     * @param session
     * @param pw
     * @param locale
     * @return
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
                logger.error("No data available in database to create Water\\Feed\\Humidity graph .");
                throw new Exception("No data available in database to create Water\\Feed\\Humidity graph .");
            } else {
                FeedWaterConsumpTemp graph = null;
                if (setClock.getValue() == null) {
                    graph = new FeedWaterConsumpTemp(values, Long.valueOf("0"), locale);
                } else {
                    graph = new FeedWaterConsumpTemp(values, setClock.getValueToUI(), locale);
                }

                // Write the chart image to the temporary directory
                ChartRenderingInfo info = new ChartRenderingInfo(new StandardEntityCollection());

                filenamewft = ServletUtilities.saveChartAsPNG(graph.createChart(), 800, 600, info, session);

                // Write the image map to the PrintWriter
                ChartUtilities.writeImageMap(pw, filenamewft, info, false);
                session.setAttribute("filenamewct", filenamewft);
                pw.flush();
            }
        } catch (Exception e) {
            logger.error("No data available in database to create Water\\Feed\\Humidity graph .");
            filenamewft = "public_error_500x300.png";
        }
        return filenamewft;
    }

    /**
     * @param flockId
     * @param growDay
     * @param session
     * @param pw
     * @param locale
     * @return
     */
    public static String generateChartFlockWaterFeedTemp(Long flockId, String growDay, HttpSession session, PrintWriter pw,
                                                         Locale locale) {


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
            filenamewft = ServletUtilities.saveChartAsPNG(perHourReportGraph.createChart(), 800, 600, info, session);

            // Write the image map to the PrintWriter
            ChartUtilities.writeImageMap(pw, filenamewft, info, false);
            session.setAttribute("filenamewct", filenamewft);
            pw.flush();
        } catch (Exception e) {
            logger.error("No data available in database to create Water\\Feed\\Humidity graph .");
            filenamewft = "public_error_500x300.png";
        }
        return filenamewft;
    }

    public static String generateChartFlockWaterFeed(Long flockId, String fromDay, String toDay, HttpSession session,
                                                     PrintWriter pw, Locale locale) {

        String filenamefw;
        FromDayToDay growDayRangeParam = new FromDayToDay(fromDay, toDay);
        try {
            FlockHistoryService flockHistoryService = new FlockHistoryServiceImpl();
            Map<Integer, String> historyByGrowDay = flockHistoryService.getFlockPerDayNotParsedReportsWithinRange(flockId, growDayRangeParam);

            List<Map<Integer, Data>> dataHistoryList = new ArrayList<Map<Integer, Data>>();
            DataDao dataDao = DbImplDecider.use(DaoType.MYSQL).getDao(DataDao.class);
            Data data1 = dataDao.getById(PerGrowDayHistoryDataType.FEED_CONSUMPTION_ID.getId());

            List<String> axisTitles = new ArrayList<String>();
            axisTitles.add(data1.getLabel());
            dataHistoryList.add(DataGraphCreator.createHistoryDataByGrowDay(historyByGrowDay, data1));

            HistoryGraph waterFeedGraph = new HistoryGraph();
            waterFeedGraph.setDataHistoryList(dataHistoryList);
            waterFeedGraph.createChart(FEED_AND_WATER_CONSUMPTION_TITLE, GROW__DAY_AXIS_TITLE, FEED_KG_AXIST_TITLE);

            Data data2 = dataDao.getById(PerGrowDayHistoryDataType.WATER_CONSUMPTION_ID.getId());
            axisTitles.add(data2.getLabel());
            dataHistoryList.clear();
            dataHistoryList.add(DataGraphCreator.createHistoryDataByGrowDay(historyByGrowDay, data2));
            waterFeedGraph.createAndAddSeriesCollection(dataHistoryList, WATER_LITER_AXIS_TITLE);

            // Write the chart image to the temporary directory
            ChartRenderingInfo info = new ChartRenderingInfo(new StandardEntityCollection());
            filenamefw = ServletUtilities.saveChartAsPNG(waterFeedGraph.getChart(), 800, 600, info, session);

            // Write the image map to the PrintWriter
            ChartUtilities.writeImageMap(pw, filenamefw, info, false);
            session.setAttribute("filenamefw-flockid=" + flockId + "&fromday=" + fromDay + "&today=" + toDay, filenamefw);

            pw.flush();
        } catch (Exception e) {
            logger.error("No data available in database to create Water\\Feed graph .");
            filenamefw = "public_error_500x300.png";
        }
        return filenamefw;
    }


    public static String generateChartFlockAverageWeight(Long flockId, String fromDay, String toDay, HttpSession session,
                                                         PrintWriter pw, Locale locale) {

        String filenameaw;
        FromDayToDay growDayRangeParam = new FromDayToDay(fromDay, toDay);
        try {
            FlockHistoryService flockHistoryService = new FlockHistoryServiceImpl();
            Map<Integer, String> historyByGrowDay = flockHistoryService.getFlockPerDayNotParsedReportsWithinRange(flockId,
                    growDayRangeParam);

            DataDao dataDao = DbImplDecider.use(DaoType.MYSQL).getDao(DataDao.class);
            List<Map<Integer, Data>> dataHistoryList = new ArrayList<Map<Integer, Data>>();

            Data data1 = dataDao.getById(PerGrowDayHistoryDataType.AVERAGE_WEIGHT_1_ID.getId());
            dataHistoryList.add(DataGraphCreator.createHistoryDataByGrowDay(historyByGrowDay, data1));

            Data data2 = dataDao.getById(PerGrowDayHistoryDataType.AVERAGE_WEIGHT_2_ID.getId());
            dataHistoryList.add(DataGraphCreator.createHistoryDataByGrowDay(historyByGrowDay, data2));

            Data data3 = dataDao.getById(PerGrowDayHistoryDataType.AVERAGE_WEIGHT_3_ID.getId());
            dataHistoryList.add(DataGraphCreator.createHistoryDataByGrowDay(historyByGrowDay, data3));

            Data data4 = dataDao.getById(PerGrowDayHistoryDataType.AVERAGE_WEIGHT_4_ID.getId());
            dataHistoryList.add(DataGraphCreator.createHistoryDataByGrowDay(historyByGrowDay, data4));


            List<String> axisTitles = new ArrayList<String>();
            axisTitles.add(data1.getLabel());
            axisTitles.add(data2.getLabel());
            axisTitles.add(data3.getLabel());
            axisTitles.add(data4.getLabel());

            HistoryGraph averageWeightGraph = new HistoryGraph();
            averageWeightGraph.setDataHistoryList(dataHistoryList);

            String title = "Average Weight";
            String xAxisLabel = "Grow Day[Day]";
            String yAxisLabel = "Weight[KG]";

            averageWeightGraph.createChart(title, xAxisLabel, yAxisLabel);
            // Write the chart image to the temporary directory
            ChartRenderingInfo info = new ChartRenderingInfo(new StandardEntityCollection());
            filenameaw = ServletUtilities.saveChartAsPNG(averageWeightGraph.getChart(), 800, 600, info, session);

            // Write the image map to the PrintWriter
            ChartUtilities.writeImageMap(pw, filenameaw, info, false);
            session.setAttribute("filenameaw-flockid=" + flockId + "&fromday=" + fromDay + "&today=" + toDay, filenameaw);
            pw.flush();
        } catch (Exception e) {
            logger.error("No data available in database to create Water\\Feed graph .");
            filenameaw = "public_error_500x300.png";
        }
        return filenameaw;
    }

    public static String generateChartFlockHeatOnTime(Long flockId, String fromDay, String toDay, HttpSession session,
                                                      PrintWriter pw, Locale locale) {

        String filenamehot;
        FromDayToDay growDayRangeParam = new FromDayToDay(fromDay, toDay);
        try {
            FlockHistoryService flockHistoryService = new FlockHistoryServiceImpl();
            Map<Integer, String> historyByGrowDay = flockHistoryService.getFlockPerDayNotParsedReportsWithinRange(flockId, growDayRangeParam);

            DataDao dataDao = DbImplDecider.use(DaoType.MYSQL).getDao(DataDao.class);
            Data data1 = dataDao.getById(PerGrowDayHistoryDataType.HEATER_1_TIME_ON.getId());

            List<String> axisTitles = new ArrayList<String>();
            axisTitles.add(data1.getLabel());

            List<Map<Integer, Data>> dataHistoryList = new ArrayList<Map<Integer, Data>>();
            dataHistoryList.add(DataGraphCreator.createHistoryDataByGrowDay(historyByGrowDay, data1));

            Data data2 = dataDao.getById(PerGrowDayHistoryDataType.HEATER_2_TIME_ON.getId());

            axisTitles.add(data2.getLabel());
            dataHistoryList.add(DataGraphCreator.createHistoryDataByGrowDay(historyByGrowDay, data2));

            Data data3 = dataDao.getById(PerGrowDayHistoryDataType.HEATER_3_TIME_ON.getId());

            axisTitles.add(data3.getLabel());
            dataHistoryList.add(DataGraphCreator.createHistoryDataByGrowDay(historyByGrowDay, data3));

            Data data4 = dataDao.getById(PerGrowDayHistoryDataType.HEATER_4_TIME_ON.getId());

            axisTitles.add(data4.getLabel());
            dataHistoryList.add(DataGraphCreator.createHistoryDataByGrowDay(historyByGrowDay, data4));

            Data data5 = dataDao.getById(PerGrowDayHistoryDataType.HEATER_5_TIME_ON.getId());

            axisTitles.add(data5.getLabel());
            dataHistoryList.add(DataGraphCreator.createHistoryDataByGrowDay(historyByGrowDay, data5));

            Data data6 = dataDao.getById(PerGrowDayHistoryDataType.HEATER_6_TIME_ON.getId());

            axisTitles.add(data6.getLabel());
            dataHistoryList.add(DataGraphCreator.createHistoryDataByGrowDay(historyByGrowDay, data6));

            String title = "Heat On Time";
            String xAxisLabel = "Grow Day[Day]";
            String yAxisLabel = "Time[Minute]";
            HistoryGraph heatontimeGraph = new HistoryGraph();
            heatontimeGraph.setDataHistoryList(dataHistoryList);
            heatontimeGraph.createChart(title, xAxisLabel, yAxisLabel);
            // Write the chart image to the temporary directory
            ChartRenderingInfo info = new ChartRenderingInfo(new StandardEntityCollection());
            filenamehot = ServletUtilities.saveChartAsPNG(heatontimeGraph.getChart(), 800, 600, info, session);

            // Write the image map to the PrintWriter
            ChartUtilities.writeImageMap(pw, filenamehot, info, false);
            session.setAttribute("filenamehot-flockid=" + flockId + "&fromday=" + fromDay + "&today=" + toDay, filenamehot);
            pw.flush();
        } catch (Exception e) {
            logger.error("No data available in database to create Water\\Feed graph .");
            filenamehot = "public_error_500x300.png";
        }
        return filenamehot;
    }

    public static String generateChartFlockMortality(Long flockId, String fromDay, String toDay, HttpSession session,
                                                     PrintWriter pw, Locale locale) {

        String filenamem;
        FromDayToDay growDayRangeParam = new FromDayToDay(fromDay, toDay);
        try {
            FlockHistoryService flockHistoryService = new FlockHistoryServiceImpl();
            Map<Integer, String> historyByGrowDay = flockHistoryService.getFlockPerDayNotParsedReportsWithinRange(flockId, growDayRangeParam);

            List<Map<Integer, Data>> dataHistoryList = new ArrayList<Map<Integer, Data>>();
            List<String> axisTitles = new ArrayList<String>();
            Locale currLocale = locale;
            String lang = currLocale.toString().substring(0, 2);
            LanguageDao languageDao = DbImplDecider.use(DaoType.MYSQL).getDao(LanguageDao.class);
            long langId = languageDao.getLanguageId(lang);

            DataDao dataDao = DbImplDecider.use(DaoType.MYSQL).getDao(DataDao.class);
            Data data1 = dataDao.getById(PerGrowDayHistoryDataType.DAY_MORTALITY.getId());
            axisTitles.add(data1.getLabel());
            dataHistoryList.add(DataGraphCreator.createHistoryDataByGrowDay(historyByGrowDay, data1));

            Data data2 = dataDao.getById(PerGrowDayHistoryDataType.DAY_MORTALITY_MALE.getId());
            axisTitles.add(data2.getLabel());
            dataHistoryList.add(DataGraphCreator.createHistoryDataByGrowDay(historyByGrowDay, data2));

            Data data3 = dataDao.getById(PerGrowDayHistoryDataType.DAY_MORTALITY_FEMALE.getId());
            axisTitles.add(data3.getLabel());
            dataHistoryList.add(DataGraphCreator.createHistoryDataByGrowDay(historyByGrowDay, data3));

            HashMap<String, String> dictinary = createDictionary(currLocale);
            String title = dictinary.get("graph.mrt.title");    // "Daily Mortality";
            String xAxisTitle = dictinary.get("graph.mrt.axis.growday");    // "Grow Day[Day]";
            String yAxisTitle = dictinary.get("graph.mrt.axis.birds");    // "Birds";
            HistoryGraph mortalityGraph = new HistoryGraph();

            mortalityGraph.setDataHistoryList(dataHistoryList);
            mortalityGraph.createChart(title, xAxisTitle, yAxisTitle);
            // Write the chart image to the temporary directory
            ChartRenderingInfo info = new ChartRenderingInfo(new StandardEntityCollection());
            filenamem = ServletUtilities.saveChartAsPNG(mortalityGraph.getChart(), 800, 600, info, session);

            // Write the image map to the PrintWriter
            ChartUtilities.writeImageMap(pw, filenamem, info, false);
            session.setAttribute("filenamem-flockid=" + flockId + "&fromday=" + fromDay + "&today=" + toDay, filenamem);
            pw.flush();
        } catch (Exception e) {
            logger.error("No data available in database to create Water\\Feed graph .");
            filenamem = "public_error_500x300.png";
        }
        return filenamem;
    }


    public static String generateChartFlockMinMaxTemperatureHumidity(Long flockId, String fromDay, String toDay, HttpSession session,
                                                                     PrintWriter pw, Locale locale) {

        String filenamemmh;
        FromDayToDay growDayRangeParam = new FromDayToDay(fromDay, toDay);
        try {
            FlockHistoryService flockHistoryService = new FlockHistoryServiceImpl();
            Map<Integer, String> historyByGrowDay = flockHistoryService.getFlockPerDayNotParsedReportsWithinRange(flockId, growDayRangeParam);

            DataDao dataDao = DbImplDecider.use(DaoType.MYSQL).getDao(DataDao.class);
            Data data = dataDao.getById(PerGrowDayHistoryDataType.MAX_TEMP_IN.getId());

            Map<Integer, Data> interestData = createDataSet(historyByGrowDay, data);
            data = dataDao.getById(PerGrowDayHistoryDataType.MIN_TEMP_IN.getId());

            Map<Integer, Data> interestData2 = createDataSet(historyByGrowDay, data);
            data = dataDao.getById(PerGrowDayHistoryDataType.MAX_TEMP_OUT.getId());

            Map<Integer, Data> interestData3 = createDataSet(historyByGrowDay, data);
            data = dataDao.getById(PerGrowDayHistoryDataType.MIN_TEMP_OUT.getId());

            Map<Integer, Data> interestData4 = createDataSet(historyByGrowDay, data);
            CombinedXYGraph combGraph = new CombinedXYGraph();
            combGraph.createFirstNextPlot("Maximum and Minimum Inside Temperature", "Grow Day[Day]",
                    "Temperature[C]", data, 0, interestData,
                    interestData2);

            combGraph.createNextPlot("Maximum and Minimum Outside Temperature", "Grow Day[Day]",
                    "Temperature[C]", data, 0, interestData3, interestData4);
            data = dataDao.getById(PerGrowDayHistoryDataType.MAX_HUMIDITY.getId());

            Map<Integer, Data> interestData5 = createDataSet(historyByGrowDay, data);
            data = dataDao.getById(PerGrowDayHistoryDataType.MIN_HUMIDITY.getId());
            Map<Integer, Data> interestData6 = createDataSet(historyByGrowDay, data);

            combGraph.createNextPlot("Humidity", "Grow Day[Day]", "Humidity[%]", data, 1, interestData5,
                    interestData6);
            combGraph.createChart("In\\Out Min\\Max Temperature And Humidity Graph", growDayRangeParam.toString());
            // Write the chart image to the temporary directory
            ChartRenderingInfo info = new ChartRenderingInfo(new StandardEntityCollection());
            filenamemmh = ServletUtilities.saveChartAsPNG(combGraph.getChart(), 800, 600, info, session);

            // Write the image map to the PrintWriter
            ChartUtilities.writeImageMap(pw, filenamemmh, info, false);
            session.setAttribute("filenamemmh-flockid=" + flockId + "&fromday=" + fromDay + "&today=" + toDay, filenamemmh);
            pw.flush();
        } catch (Exception e) {
            logger.error("No data available in database to create Water\\Feed graph .");
            filenamemmh = "public_error_500x300.png";
        }
        return filenamemmh;
    }


    protected static HashMap<String, String> createDictionary(Locale locale) {
        HashMap<String, String> dictinary = new HashMap<String, String>();
        ResourceBundle bundle = ResourceBundle.getBundle("labels", locale);

        for (Enumeration<String> e = bundle.getKeys(); e.hasMoreElements(); ) {
            String key = e.nextElement();

            if (key.startsWith("graph")) {
                String value = bundle.getString(key);

                dictinary.put(key, value);
            }
        }

        return dictinary;
    }

    /**
     * @param historyByGrowDay
     * @param data
     * @return
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



