package com.agrologic.app.web;

import com.agrologic.app.dao.DaoType;
import com.agrologic.app.dao.DbImplDecider;
import com.agrologic.app.dao.ProgramActionSetDao;
import com.agrologic.app.dao.ProgramDao;
import com.agrologic.app.model.Program;
import com.agrologic.app.model.ProgramActionSet;
import com.agrologic.app.util.DateLocal;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

public class SaveProgramActionSetServlet extends AbstractServlet {

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
                long translateLang;

                try {
                    translateLang = Long.parseLong(request.getParameter("translateLang"));
                } catch (NumberFormatException ex) {
                    translateLang = 1;    // default program
                }

                Long programId = Long.parseLong(request.getParameter("programId"));
                Long screenId = Long.parseLong(request.getParameter("screenId"));
                String showActionsetMap = request.getParameter("showActionsetMap");
                String posActionsetMap = request.getParameter("posActionsetMap");
                String[] showTablePairs = showActionsetMap.split(";");
                Map<Long, String> showTableMap = new HashMap<Long, String>();

                for (String s : showTablePairs) {
                    StringTokenizer st = new StringTokenizer(s, ",");
                    Long dataId = Long.parseLong(st.nextToken());
                    String show = st.nextToken();

                    showTableMap.put(dataId, show);
                }

                String[] posDataPairs = posActionsetMap.split(";");
                Map<Long, Integer> posDataMap = new HashMap<Long, Integer>();

                for (String s : posDataPairs) {
                    StringTokenizer st = new StringTokenizer(s, ",");
                    Long dataId = Long.parseLong(st.nextToken());
                    Integer pos = Integer.parseInt(st.nextToken());

                    posDataMap.put(dataId, pos);
                }

                try {
                    ProgramActionSetDao programActionSetDao = DbImplDecider.use(DaoType.MYSQL).getDao(ProgramActionSetDao.class);
                    programActionSetDao.saveChanges(programId, screenId, showTableMap, posDataMap);
                    Collection<ProgramActionSet> programActionSets = programActionSetDao.getAllOnScreen(programId);

                    request.setAttribute("programactionset", programActionSets);

                    ProgramDao programDao = DbImplDecider.use(DaoType.MYSQL).getDao(ProgramDao.class);
                    Program program = programDao.getById(programId);

                    program.setModifiedDate(DateLocal.currentDate());
                    programDao.update(program);
                    request.setAttribute("message", "Changes successfully saved !");
                    request.setAttribute("error", false);
                    request.getRequestDispatcher("./all-tables.html?programid=" + programId + "screenId=" + screenId +
                            "&translateLang=" + translateLang).forward(request, response);
                } catch (SQLException ex) {
                    logger.error("Error occurs while updating program !");
                    request.setAttribute("message", "Error occurs while saving changes !");
                    request.setAttribute("error", true);
                    request.getRequestDispatcher("./all-tables.html?programid=" + programId + "screenId=" + screenId +
                            "&translateLang=" + translateLang).forward(request, response);
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



