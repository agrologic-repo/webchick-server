package com.agrologic.app.web;

import com.agrologic.app.dao.DaoType;
import com.agrologic.app.dao.DataDao;
import com.agrologic.app.dao.DbImplDecider;
import com.agrologic.app.excel.DataForExcelCreator;
import com.agrologic.app.excel.WriteToExcel;
import com.agrologic.app.management.PerGrowDayHistoryDataType;
import com.agrologic.app.model.Data;
import com.agrologic.app.model.Flock;
import com.agrologic.app.service.FlockHistoryService;
import com.agrologic.app.utils.FileDownloadUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ExportToExcelHistory extends AbstractServlet {
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
        response.setContentType("text/html;charset=UTF-8");

        /**
         * Logger for this class and subclasses
         */
        try {
            if (!CheckUserInSession.isUserInSession(request)) {
                logger.error("Unauthorized access!");
                response.sendRedirect("./login.jsp");
            } else {
                long flockId = Long.parseLong(request.getParameter("flockId"));
                FlockHistoryService flockHistoryService = new FlockHistoryService();
                Map<Integer, String> historyByGrowDay = flockHistoryService.getFlockHistory(flockId);
                Flock flock = flockHistoryService.getFlock(flockId);
                /**
                 * management column titles for excel
                 */
                List<String> columnTitles = new ArrayList<String>();
                /**
                 * management data for excel
                 */
                List<List<String>> historyData = createHistoryDataForExcel(columnTitles, historyByGrowDay);
                WriteToExcel excel = new WriteToExcel();
                excel.setTitleList(columnTitles);
                excel.setCellDataList(historyData);
                excel.setOutputFile("C:/flock-" + flock.getFlockName());
                excel.write();
                FileDownloadUtil.doDownload(response, "C:/flock-" + flock.getFlockName(), "xls");
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

        for (PerGrowDayHistoryDataType perGrowDayHistoryDataType : PerGrowDayHistoryDataType.values()) {
            try {
                Data data = dataDao.getById(perGrowDayHistoryDataType.getId(), langId);
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
                logger.error(ex.getMessage(), ex);
            }
        }
        return historyDataForExcel;
    }
}



