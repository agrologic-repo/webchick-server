package com.agrologic.app.graph;

import com.agrologic.app.dao.*;
import com.agrologic.app.graph.daily.*;
import com.agrologic.app.graph.history.HistoryGraph;
import com.agrologic.app.model.Data;
import com.agrologic.app.model.HistoryHour;
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
import java.sql.SQLException;
import java.util.*;

public class GenerateGraph {

    public static final String NO_DATA_AVAILABLE = "No data available in database to create {} graph .";
    private static final Logger logger = LoggerFactory.getLogger(GenerateGraph.class);
    private static LocaleService localeService = new LocaleService();


    public static List<Data> getHistDataList(Long flock_id, Locale locale) {

        List<Data> list = null;

        try {

            FlockDao flockDao = DbImplDecider.use(DaoType.MYSQL).getDao(FlockDao.class);
            Integer max_gr = flockDao.get_max_grow_day(flock_id);

            String str = flockDao.get_history_of_grow_day(flock_id, max_gr);

            while((str == null || (str.trim().equals("-1"))) && max_gr > 0)
                str = flockDao.get_history_of_grow_day(flock_id, max_gr = max_gr - 1);

            Long lang_id = localeService.getLanguageId(locale.getLanguage());

            return HistoryContentCreator.history_str_to_list_without_value(str, lang_id);

        } catch (Exception e) {
            return null;
        }
    }
//    public static List<Data> getHistDataList(Long flock_id) {
//
//        List<Data> list = null;
//
//        try {
//
//            FlockDao flockDao = DbImplDecider.use(DaoType.MYSQL).getDao(FlockDao.class);
//            Integer max_gr = flockDao.get_max_grow_day(flock_id);
//
//            String str = flockDao.get_history_of_grow_day(flock_id, max_gr);
//
//            while(str == null && max_gr > 0)
//                str = flockDao.get_history_of_grow_day(flock_id, max_gr - 1);
//
//            return HistoryContentCreator.history_str_to_list_without_value(str);
//
//        } catch (Exception e) {
//            return null;
//        }
//    }

    public static String generateGraph(Long flockId, Long data_id, String fromDay, String toDay, HttpSession session, PrintWriter pw, Locale locale) {

        String filename = null;

        try {

            DataDao dataDao = DbImplDecider.use(DaoType.MYSQL).getDao(DataDao.class);
            Long lang_id = localeService.getLanguageId(locale.getLanguage());
            Data data = dataDao.getById(data_id, lang_id);
            filename = generate_graph_filename(flockId, data, fromDay, toDay, session, pw, locale);

        } catch (Exception e) {

            logger.error(NO_DATA_AVAILABLE, "No data available");
            filename = "public_error_500x300.png";

        }

        return filename;
    }

    public static Collection<HistoryHour> get_labels_and_format (Collection<HistoryHour> hour_history, Locale locale){
        try {
            DataDao dataDao = DbImplDecider.use(DaoType.MYSQL).getDao(DataDao.class);
            Long lang_id = localeService.getLanguageId(locale.getLanguage());
            for (HistoryHour hh : hour_history){
//                Data data = dataDao.get_data_by_d_num(hh.get_d_num());// with unicode label
                Data data = dataDao.get_data_by_d_num_with_unicode_l(hh.get_d_num(), lang_id);
//                hh.set_label(data.getLabel());
                hh.set_label(data.getUnicodeLabel());
                hh.set_format(data.getFormat());
            }
        } catch (Exception e){
            int test = 1 + 1;
        }

        return hour_history;
    }

    public static String generate_hour_graph(Long flockId, String d_num, String label, Integer format, Integer grow_day, HttpSession session, PrintWriter pw, Locale locale) throws SQLException {

        String file_name = null;

        FlockDao flock_dao = DbImplDecider.use(DaoType.MYSQL).getDao(FlockDao.class);
        DataDao dataDao = DbImplDecider.use(DaoType.MYSQL).getDao(DataDao.class);
        String values = flock_dao.getHistory24(flockId, grow_day, d_num);// get values from request

        try {

//            DayParam grow_day_param = new DayParam(grow_day.toString());

//            Integer reset_time = flock_dao.getResetTime(flockId, grow_day_param.getGrowDay());
            Data reset_time = dataDao.get_reset_time_by_flock_id(flockId);
            if (label == null){
                label = dataDao.get_data_by_d_num_with_unicode_l(d_num, localeService.getLanguageId(locale.getLanguage())).getUnicodeLabel();
            }
            HourHistoryGraph graph = new HourHistoryGraph(values, label + " (grow day " + grow_day + " )", format, reset_time.getValue(), locale);

            ChartRenderingInfo info = new ChartRenderingInfo(new StandardEntityCollection());
            file_name = ServletUtilities.saveChartAsPNG(graph.createChart(), 800, 400, info, session);

            ChartUtilities.writeImageMap(pw, file_name, info, false);
            session.setAttribute("filename", file_name);
            pw.flush();
        } catch (Exception e) {
            logger.error(NO_DATA_AVAILABLE, "No data availavle");
            file_name = "public_error_500x300.png";
        }
        return file_name;
    }

    public static String generate_graph_filename(Long flockId, Data data, String fromDay, String toDay, HttpSession session, PrintWriter pw, Locale locale) {

        String filename = null;

        try {

            FromDayToDay growDayRangeParam = new FromDayToDay(fromDay, toDay);
            Map<Integer, String> historyByGrowDay = getFlockPerDayNotParsedReportsWithinRange(flockId, growDayRangeParam);
            HistoryContent historyContent = HistoryContentCreator.createPerDayHistoryContent(historyByGrowDay);

            List<Map<Integer, Data>> dataHistoryList = new ArrayList<Map<Integer, Data>>();
            Map<Integer, List<Data>> historyContentMap = historyContent.getHistoryContentPerDay();
            HashMap<String, String> dictionary = createDictionary(locale);

            dataHistoryList.add(DataGraphCreator.createHistoryData(historyContentMap, data));

            String title = data.getUnicodeLabel();
            String xAxisLabel = dictionary.get("graph.grow.day");
            String yAxisLabel = data.getUnicodeLabel();

            HistoryGraph dataGraph = new HistoryGraph();
            dataGraph.setDataHistoryList(dataHistoryList);

            if (!growDayRangeParam.useRange()) {
                growDayRangeParam.setFromDay(1);
                growDayRangeParam.setToDay(dataHistoryList.get(0).size());
            }

            dataGraph.createChart(title, growDayRangeParam.toString(), xAxisLabel, yAxisLabel);

            // Write the chart image to the temporary directory
            ChartRenderingInfo info = new ChartRenderingInfo(new StandardEntityCollection());

            filename = ServletUtilities.saveChartAsPNG(dataGraph.getChart(), 800, 400, info, session);

            // Write the image map to the PrintWriter
            ChartUtilities.writeImageMap(pw, filename, info, false);
            session.setAttribute("filename-flockid=" + flockId + "&fromday=" + fromDay + "&today=" + toDay, filename);
            pw.flush();

        } catch (Exception e) {
            logger.error(NO_DATA_AVAILABLE, "No data available");
            filename = "public_error_500x300.png";
        }

        return filename;
    }

    /**
     * Creates temperature and humidity chart and return path to png file with map of each series .
     *
     * @param controllerId controller id
     * @param session      the session
     * @param pw           PrintWriter object
     * @param locale       the current locale
     * @return the path to png file with chart
     */


    public static String generateChartTempHum(Long controllerId, HttpSession session, PrintWriter pw, Locale locale) {//screen
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
    public static String generateChartWaterFeedTemp(Long controllerId, HttpSession session, PrintWriter pw, Locale locale) {//screen
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

    public static String generateChartFeedWaterPerBird24(Long controllerId, HttpSession session, PrintWriter pw, Locale locale) {//screen
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

    public static String generateChartFeed2Water2_24(Long controllerId, HttpSession session, PrintWriter pw, Locale locale) {//screen
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

    public static String generateChartWaterSum_24(Long controllerId, HttpSession session, PrintWriter pw, Locale locale) {//screen
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

    public static Map<Integer, String> getFlockPerDayNotParsedReportsWithinRange(long flockId, FromDayToDay fromDayToDay) throws SQLException {
        Map<Integer, String> historyByGrowDay;
        FlockDao flockDao = DbImplDecider.use(DaoType.MYSQL).getDao(FlockDao.class);
        if (fromDayToDay.useRange()) {
            historyByGrowDay = flockDao.getFlockPerDayNotParsedReports(flockId, fromDayToDay.getFromDay(), fromDayToDay.getToDay());
        } else {
            historyByGrowDay = flockDao.getFlockPerDayNotParsedReports(flockId);
        }
        return historyByGrowDay;
    }
}


