
/*
* To change this template, choose Tools | Templates
* and open the template in the editor.
 */
package com.agrologic.app.web;


import com.agrologic.app.dao.DaoType;
import com.agrologic.app.dao.DataDao;
import com.agrologic.app.dao.DbImplDecider;
import com.agrologic.app.dao.FlockDao;
import com.agrologic.app.dao.impl.FlockDaoImpl;
import com.agrologic.app.excel.DataForExcelCreator;
import com.agrologic.app.excel.WriteToExcel;
import com.agrologic.app.model.Data;
import com.agrologic.app.utils.FileDownloadUtil;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

//~--- JDK imports ------------------------------------------------------------

public class ExpToExcelMinMaxHum extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private String outfile = "c:/minmanhum.xls";
    final Logger logger = Logger.getLogger(ExpToExcelMinMaxHum.class);

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     *
     * @param request  servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException      if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        /** Logger for this class and subclasses */
        try {
            if (!CheckUserInSession.isUserInSession(request)) {
                logger.error("Unauthorized access!");
                request.getRequestDispatcher("./login.jsp").forward(request, response);
            } else {
                long flockId = Long.parseLong(request.getParameter("flockId"));
                FlockDao flockDao = new FlockDaoImpl();
                Map<Integer, String> historyByGrowDay = flockDao.getAllHistoryByFlock(flockId);
                List<List<String>> allHistDataForExcel = new ArrayList<List<String>>();
                DataDao dataDao = DbImplDecider.use(DaoType.MYSQL).getDao(DataDao.class);
                Data data0 = dataDao.getById(Long.valueOf(800), Long.valueOf(1));

                allHistDataForExcel.add(DataForExcelCreator.createDataList(historyByGrowDay.keySet()));

                Data data1 = dataDao.getById(Long.valueOf(3002), Long.valueOf(1));

                allHistDataForExcel.add(DataForExcelCreator.createDataHistoryList(historyByGrowDay, data1));

                Data data2 = dataDao.getById(Long.valueOf(3003), Long.valueOf(1));

                allHistDataForExcel.add(DataForExcelCreator.createDataHistoryList(historyByGrowDay, data2));

                Data data3 = dataDao.getById(Long.valueOf(3004), Long.valueOf(1));

                allHistDataForExcel.add(DataForExcelCreator.createDataHistoryList(historyByGrowDay, data3));

                Data data4 = dataDao.getById(Long.valueOf(3005), Long.valueOf(1));

                allHistDataForExcel.add(DataForExcelCreator.createDataHistoryList(historyByGrowDay, data4));

                Data data5 = dataDao.getById(Long.valueOf(3006), Long.valueOf(1));

                allHistDataForExcel.add(DataForExcelCreator.createDataHistoryList(historyByGrowDay, data5));

                Data data6 = dataDao.getById(Long.valueOf(3007), Long.valueOf(1));

                allHistDataForExcel.add(DataForExcelCreator.createDataHistoryList(historyByGrowDay, data6));

                List<String> tableTitles = new ArrayList<String>();

                tableTitles.add(data0.getLabel());
                tableTitles.add(data1.getLabel());
                tableTitles.add(data2.getLabel());
                tableTitles.add(data3.getLabel());
                tableTitles.add(data4.getLabel());
                tableTitles.add(data5.getLabel());
                tableTitles.add(data6.getLabel());

                WriteToExcel excel = new WriteToExcel();

                excel.setTitleList(tableTitles);
                excel.setCellDataList(allHistDataForExcel);
                excel.setOutputFile(outfile);
                excel.write();
                FileDownloadUtil.doDownload(response, outfile, "xls");
            }
        } catch (Exception e) {
            logger.error("Unknown error. ", e);
        } finally {
            response.getWriter().close();
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">

    /**
     * Handles the HTTP <code>GET</code> method.
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
     * Handles the HTTP <code>POST</code> method.
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
}



