package com.agrologic.app.graph;

import com.agrologic.app.dao.ControllerDao;
import com.agrologic.app.dao.DaoType;
import com.agrologic.app.dao.DataDao;
import com.agrologic.app.dao.DbImplDecider;
import com.agrologic.app.graph.daily.Graph24FWI;
import com.agrologic.app.graph.daily.Graph24IOH;
import com.agrologic.app.graph.daily.GraphType;
import com.agrologic.app.model.Data;
import org.jfree.chart.ChartRenderingInfo;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.entity.StandardEntityCollection;
import org.jfree.chart.servlet.ServletUtilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpSession;
import java.io.PrintWriter;
import java.util.Locale;

public class GenerateGraph {
     private static final Logger logger = LoggerFactory.getLogger(GenerateGraph.class);

    public static String generateChartTempHum(Long controllerId, HttpSession session, PrintWriter pw, Locale locale) {
        String filenameth = null;

        try {
            ControllerDao controllerDao = DbImplDecider.use(DaoType.MYSQL).getDao(ControllerDao.class);
            String values = controllerDao.getControllerGraph(controllerId);
            DataDao dataDao = DbImplDecider.use(DaoType.MYSQL).getDao(DataDao.class);
            Data setClock = dataDao.getSetClockByController(controllerId);

            if (values == null) {
                logger.error("No data available in database to create Temperature\\Humidity graph .");
                throw new Exception("No data available in database to create Temperature\\Humidity graph .");
            } else {
                Graph24IOH graph = null;
                if (setClock.getValue() == null) {
                    graph = new Graph24IOH(GraphType.IN_OUT_TEMP_HUMID, values, Long.valueOf("0"), locale);
                } else {
                    graph = new Graph24IOH(GraphType.IN_OUT_TEMP_HUMID, values, setClock.getValueToUI(), locale);
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

    public static String generateChartWaterFeedTemp(Long controllerId, HttpSession session, PrintWriter pw,
                                                    Locale locale) {
        String filenamewft = null;

        try {
            ControllerDao controllerDao = DbImplDecider.use(DaoType.MYSQL).getDao(ControllerDao.class);
            String values = controllerDao.getControllerGraph(controllerId);
            DataDao dataDao = DbImplDecider.use(DaoType.MYSQL).getDao(DataDao.class);
            Data setClock = dataDao.getSetClockByController(controllerId);

            if (values == null) {
                logger.error("No data available in database to create Water\\Feed\\Humidity graph .");
                throw new Exception("No data available in database to create Water\\Feed\\Humidity graph .");
            } else {
                Graph24FWI graph = null;
                if (setClock.getValue() == null) {
                    graph = new Graph24FWI(GraphType.IN_OUT_TEMP_HUMID, values, Long.valueOf("0"), locale);
                } else {
                    graph = new Graph24FWI(GraphType.IN_OUT_TEMP_HUMID, values, setClock.getValue(), locale);
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
}



