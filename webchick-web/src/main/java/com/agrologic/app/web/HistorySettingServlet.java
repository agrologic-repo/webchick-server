package com.agrologic.app.web;



/*
* To change this template, choose Tools | Templates
* and open the template in the editor.
 */

import com.agrologic.app.dao.DataDao;
import com.agrologic.app.dao.HistorySettingDao;
import com.agrologic.app.dao.impl.DataDaoImpl;
import com.agrologic.app.dao.impl.HistorySettingDaoImpl;
import com.agrologic.app.model.DataDto;
import com.agrologic.app.model.HistorySettingDto;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.List;

//~--- JDK imports ------------------------------------------------------------


public class HistorySettingServlet extends HttpServlet {

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
        final Logger logger = Logger.getLogger(HistorySettingServlet.class);

        response.setContentType("text/html;charset=UTF-8");

        PrintWriter out = response.getWriter();

        try {
            if (!CheckUserInSession.isUserInSession(request)) {
                logger.error("Unauthorized access!");
                request.getRequestDispatcher("./login.jsp").forward(request, response);
            } else {
                Long programId = Long.parseLong(request.getParameter("programId"));

                try {
                    HistorySettingDao historySettingDao = new HistorySettingDaoImpl();
                    List<HistorySettingDto> histSettingList = historySettingDao.getHistorySetting(programId);
                    DataDao dataDao = new DataDaoImpl();
                    List<DataDto> historyData = dataDao.getHistoryDataList();

                    request.getSession().setAttribute("historyData", historyData);
                    request.getSession().setAttribute("historySettingData", histSettingList);
                    logger.info("retrieve all history data");
                    request.getRequestDispatcher("./history-setting.jsp?programId=" + programId).forward(request,
                            response);
                } catch (SQLException ex) {
                    logger.trace("Fail save history setting", ex);
                }
            }
        } finally {
            out.close();
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



