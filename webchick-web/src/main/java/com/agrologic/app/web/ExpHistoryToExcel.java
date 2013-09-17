
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.agrologic.app.web;


import com.agrologic.app.dao.DaoType;
import com.agrologic.app.dao.DataDao;
import com.agrologic.app.dao.DbImplDecider;
import com.agrologic.app.dao.FlockDao;
import com.agrologic.app.excel.DataForExcelCreator;
import com.agrologic.app.excel.WriteToExcel;
import com.agrologic.app.model.Data;
import com.agrologic.app.model.Flock;
import com.agrologic.app.utils.FileDownloadUtil;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

//~--- JDK imports ------------------------------------------------------------

/**
 * @author Administrator
 */
public class ExpHistoryToExcel extends HttpServlet {

    private static final List<Long> historyDataIdList = new ArrayList<Long>();

    private static final Logger logger = Logger.getLogger(ExpHistoryToExcel.class);
    private static String outfile;

    static {
        historyDataIdList.add(Long.valueOf(800));
        historyDataIdList.add(Long.valueOf(1301));
        historyDataIdList.add(Long.valueOf(1302));
        historyDataIdList.add(Long.valueOf(2933));
        historyDataIdList.add(Long.valueOf(2934));
        historyDataIdList.add(Long.valueOf(2935));
        historyDataIdList.add(Long.valueOf(2936));
        historyDataIdList.add(Long.valueOf(1303));
        historyDataIdList.add(Long.valueOf(1304));
        historyDataIdList.add(Long.valueOf(1305));
        historyDataIdList.add(Long.valueOf(1306));
        historyDataIdList.add(Long.valueOf(1307));
        historyDataIdList.add(Long.valueOf(1308));
        historyDataIdList.add(Long.valueOf(3002));
        historyDataIdList.add(Long.valueOf(3003));
        historyDataIdList.add(Long.valueOf(3004));
        historyDataIdList.add(Long.valueOf(3005));
        historyDataIdList.add(Long.valueOf(3006));
        historyDataIdList.add(Long.valueOf(3007));
        historyDataIdList.add(Long.valueOf(3017));
        historyDataIdList.add(Long.valueOf(3033));
        historyDataIdList.add(Long.valueOf(3034));
    }

    /**
     * Processes requests for both HTTP
     * <code>GET</code> and
     * <code>POST</code> methods.
     *
     * @param request  servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException      if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        /**
         * Logger for this class and subclasses
         */
        response.setContentType("text/html;charset=UTF-8");

        try {
            if (!CheckUserInSession.isUserInSession(request)) {
                logger.error("Unauthorized access!");
                response.sendRedirect("./login.jsp");
            } else {
                long flockId = Long.parseLong(request.getParameter("flockId"));
                FlockDao flockDao = DbImplDecider.use(DaoType.MYSQL).getDao(FlockDao.class);

                /**
                 * history by grow day map
                 */
                Map<Integer, String> historyByGrowDay = flockDao.getAllHistoryByFlock(flockId);
                String startTime = flockDao.getById(flockId).getStartTime();

                /**
                 * history column titles for excel
                 */
                List<String> columnTitles = new ArrayList<String>();

                /**
                 * history data for excel
                 */
                List<List<String>> historyData = new ArrayList<List<String>>();

                historyData = createHistoryDataForExcel(columnTitles, historyByGrowDay);

                WriteToExcel excel = new WriteToExcel();

                excel.setTitleList(columnTitles);
                excel.setCellDataList(historyData);

                Flock flock = flockDao.getById(flockId);
                outfile = "C:/flock-" + flock.getFlockName() + "-history.xls";
                excel.setOutputFile(outfile);
                excel.write();
                FileDownloadUtil.doDownload(response, outfile, "xls");
            }
        } catch (Exception e) {
            logger.error("Unknown error. ", e);
        } finally {
            response.getOutputStream().close();
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">

    /**
     * Handles the HTTP
     * <code>GET</code> method.
     *
     * @param request  servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException      if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP
     * <code>POST</code> method.
     *
     * @param request  servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException      if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }    // </editor-fold>

    private List<List<String>> createHistoryDataForExcel(List<String> columnTitles,
                                                         Map<Integer, String> historyByGrowDay)
            throws UnsupportedOperationException {
        long langId = 1;
        List<String> tempList = new ArrayList<String>();
        List<List<String>> historyDataForExcel = new ArrayList<List<String>>();
        DataDao dataDao = DbImplDecider.use(DaoType.MYSQL).getDao(DataDao.class);

        for (Long dataId : historyDataIdList) {
            try {
                Data data = dataDao.getById(dataId, langId);
                if (data.getId() == 800) {
                    columnTitles.add(data.getLabel());
                    historyDataForExcel.add(DataForExcelCreator.createDataList(historyByGrowDay.keySet()));
                } else {
                    tempList = DataForExcelCreator.createDataHistoryList(historyByGrowDay, data);
                }
                if (!tempList.isEmpty()) {
                    columnTitles.add(data.getLabel());
                    historyDataForExcel.add(tempList);
                }
            } catch (SQLException ex) {
                logger.error(ex);
            }
        }
        return historyDataForExcel;
    }
}



