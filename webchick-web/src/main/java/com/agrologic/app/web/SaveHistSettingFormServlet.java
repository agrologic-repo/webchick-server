package com.agrologic.app.web;

import com.agrologic.app.dao.DaoType;
import com.agrologic.app.dao.DataDao;
import com.agrologic.app.dao.DbImplDecider;
import com.agrologic.app.dao.HistorySettingDao;
import com.agrologic.app.dao.mysql.impl.HistorySettingDaoImpl;
import com.agrologic.app.model.Data;
import com.agrologic.app.model.HistorySetting;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;


public class SaveHistSettingFormServlet extends AbstractServlet {

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
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

        try {
            if (!CheckUserInSession.isUserInSession(request)) {
                logger.error("Unauthorized access!");
                response.sendRedirect("./login.jsp");
            } else {
                Long programId = Long.parseLong(request.getParameter("programId"));
                String historySettingMapStr = request.getParameter("historySettingMap");
                List<HistorySetting> historySettingList = new ArrayList<HistorySetting>();
                String[] historySettingPairs = historySettingMapStr.split(";");

                for (String s : historySettingPairs) {
                    StringTokenizer st = new StringTokenizer(s, ",");
                    Long dataId = Long.parseLong(st.nextToken());
                    String checked = st.nextToken();
                    HistorySetting hs = new HistorySetting();

                    hs.setProgramId(programId);
                    hs.setDataId(dataId);
                    hs.setChecked(checked);
                    historySettingList.add(hs);
                }

                try {
                    HistorySettingDao historySettingDao = DbImplDecider.use(DaoType.MYSQL).getDao(HistorySettingDaoImpl.class);

                    historySettingDao.saveHistorySetting(historySettingList);
                    historySettingList = historySettingDao.getHistorySetting(programId);

                    DataDao dataDao = DbImplDecider.use(DaoType.MYSQL).getDao(DataDao.class);
                    List<Data> historyData = (List<Data>) dataDao.getHistoryDataList();

                    request.setAttribute("historyData", historyData);
                    request.setAttribute("historySettingData", historySettingList);
                    logger.info("retrieve all management data");
                    request.getRequestDispatcher("./management-setting.jsp?programId=" + programId).forward(request,
                            response);
                } catch (SQLException ex) {
                    logger.trace("Fail save management setting", ex);
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



