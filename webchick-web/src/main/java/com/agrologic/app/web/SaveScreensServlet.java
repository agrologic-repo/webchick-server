
/*
* To change this template, choose Tools | Templates
* and open the template in the editor.
 */
package com.agrologic.app.web;


import com.agrologic.app.dao.DaoType;
import com.agrologic.app.dao.DbImplDecider;
import com.agrologic.app.dao.ProgramDao;
import com.agrologic.app.dao.ScreenDao;
import com.agrologic.app.model.Program;
import com.agrologic.app.utils.DateLocal;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

//~--- JDK imports ------------------------------------------------------------

/**
 * @author JanL
 */
public class SaveScreensServlet extends HttpServlet {

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
        final Logger logger = Logger.getLogger(SaveScreensServlet.class);

        response.setContentType("text/html;charset=UTF-8");

        PrintWriter out = response.getWriter();

        try {
            if (!CheckUserInSession.isUserInSession(request)) {
                logger.error("Unauthorized access!");
                request.getRequestDispatcher("./login.jsp").forward(request, response);
            } else {
                Long programId = Long.parseLong(request.getParameter("programId"));
                String showScreenMapStr = request.getParameter("showScreenMap");
                String posScreenMapStr = request.getParameter("posScreenMap");
                String[] showScreenPairs = showScreenMapStr.split(";");
                Map<Long, String> showScreenMap = new HashMap<Long, String>();

                for (String s : showScreenPairs) {
                    StringTokenizer st = new StringTokenizer(s, ",");
                    Long tableId = Long.parseLong(st.nextToken());
                    String show = st.nextToken();
                    showScreenMap.put(tableId, show);
                }

                String[] posScreenPairs = posScreenMapStr.split(";");
                Map<Long, Integer> posScreenMap = new HashMap<Long, Integer>();

                for (String s : posScreenPairs) {
                    StringTokenizer st = new StringTokenizer(s, ",");
                    Long tableId = Long.parseLong(st.nextToken());
                    Integer pos = Integer.parseInt(st.nextToken());

                    posScreenMap.put(tableId, pos);
                }

                try {
                    ScreenDao screenDao = DbImplDecider.use(DaoType.MYSQL).getDao(ScreenDao.class);
                    ;

                    screenDao.saveChanges(showScreenMap, posScreenMap, programId);

                    ProgramDao programDao = DbImplDecider.use(DaoType.MYSQL).getDao(ProgramDao.class);
                    Program program = programDao.getById(programId);

                    program.setModifiedDate(DateLocal.currentDate());
                    programDao.update(program);
                    request.getSession().setAttribute("message", "Screens successfully saved !");
                    request.getSession().setAttribute("error", Boolean.FALSE);
                    request.getRequestDispatcher("./all-screens.html?programId=" + programId).forward(request,
                            response);
                } catch (SQLException ex) {
                    logger.error("Error occurs while updating screen!");
                    request.getSession().setAttribute("message", "Error occurs while updating screen !");
                    request.getSession().setAttribute("error", Boolean.TRUE);
                    request.getRequestDispatcher("./all-screens.html?programId=" + programId).forward(request,
                            response);
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



