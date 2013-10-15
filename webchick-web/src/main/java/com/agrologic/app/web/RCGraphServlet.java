package com.agrologic.app.web;

import com.agrologic.app.dao.*;
import com.agrologic.app.model.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.List;

public class RCGraphServlet extends AbstractServlet {

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
        PrintWriter out = response.getWriter();

        try {
            long userId = Long.parseLong(request.getParameter("userId"));
            long cellinkId = Long.parseLong(request.getParameter("cellinkId"));
            long controllerId = Long.parseLong(request.getParameter("controllerId"));
            long screenId = Long.parseLong(request.getParameter("screenId"));
            String lang = (String) request.getAttribute("lang");

            if ((lang == null) || lang.equals("")) {
                lang = "en";
            }

            checkRealTimeSessionTimeout(request);
            try {
                CellinkDao cellinkDao = DbImplDecider.use(DaoType.MYSQL).getDao(CellinkDao.class);
                Cellink cellink = cellinkDao.getById(cellinkId);
                cellinkDao.update(cellink);

                ControllerDao controllerDao = DbImplDecider.use(DaoType.MYSQL).getDao(ControllerDao.class);
                Controller controller = controllerDao.getById(controllerId);
                request.setAttribute("controller", controller);


                ProgramDao programDao = DbImplDecider.use(DaoType.MYSQL).getDao(ProgramDao.class);
                Program program = programDao.getById(controller.getProgramId());
                controller.setProgram(program);

                LanguageDao languageDao = DbImplDecider.use(DaoType.MYSQL).getDao(LanguageDao.class);
                long langId = languageDao.getLanguageId(lang);

                ScreenDao screenDao = DbImplDecider.use(DaoType.MYSQL).getDao(ScreenDao.class);
                List<Screen> screens = (List<Screen>) screenDao.getAllScreensByProgramAndLang(program.getId(), langId,
                        false);
                program.setScreens(screens);

                final ProgramRelayDao programRelayDao = DbImplDecider.use(DaoType.MYSQL).getDao(ProgramRelayDao.class);
                List<ProgramRelay> programRelays = programRelayDao.getAllProgramRelays(program.getId(), langId);
                program.setProgramRelays(programRelays);

                DataDao dataDao = DbImplDecider.use(DaoType.MYSQL).getDao(DataDao.class);
                List<Data> dataRelays = (List<Data>) dataDao.getRelays();
                logger.info("retrieve program data relay!");
                request.setAttribute("dataRelays", dataRelays);

                request.getRequestDispatcher("./rmctrl-controller-graphs.jsp?userId" + userId + "&cellinkId="
                        + cellinkId + "&screenId=" + screenId + "&filename=").forward(request,
                        response);
            } catch (SQLException ex) {
                // error page
                logger.error("SQLException", ex);
            }
        } finally {
            out.close();
        }
    }

    /**
     * Check if real time session time out expired , if yes than newConnectionTimeout=10; This will affect to logout of
     * the system.
     *
     * @param request the request object
     */
    private void checkRealTimeSessionTimeout(HttpServletRequest request) {
        String doResetTimeout = request.getParameter("doResetTimeout");
        Integer newConnTimeout = 0;
        Long startConnTime = null;

        logger.info("refresh executed");

        // here we reset timeout
        HttpSession session = request.getSession();

        if ((doResetTimeout != null) && doResetTimeout.equals("true")) {
            newConnTimeout = Integer.valueOf(CellinkState.CONNECT_TIMEOUT);
            session.setAttribute("newConnectionTimeout", newConnTimeout);
            startConnTime = Long.valueOf(System.currentTimeMillis());
            session.setAttribute("startSessionTime", startConnTime);
            logger.info("Reset start session time");
        }

        // connection timeout counter
        newConnTimeout = (Integer) session.getAttribute("newConnectionTimeout");

        if (newConnTimeout == null) {
            newConnTimeout = Integer.valueOf(CellinkState.CONNECT_TIMEOUT);
            session.setAttribute("newConnectionTimeout", newConnTimeout);
            startConnTime = Long.valueOf(System.currentTimeMillis());
            session.setAttribute("startSessionTime", startConnTime);
            logger.info("newConnectionTimeout was null");
        } else {
            startConnTime = (Long) session.getAttribute("startSessionTime");

            Long currentTime = Long.valueOf(System.currentTimeMillis());

            if ((currentTime - startConnTime) < CellinkState.CONNECT_TIMEOUT) {
                if (newConnTimeout < 0) {
                    newConnTimeout = Integer.valueOf(1000);
                    logger.info("session timeout expired in 40 second");
                }

                session.setAttribute("newConnectionTimeout", newConnTimeout);
            } else {
                logger.info("session timeout expired in 45 second");
                session.setAttribute("newConnectionTimeout", 1000);
            }
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
}



