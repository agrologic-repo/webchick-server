package com.agrologic.app.web;

import com.agrologic.app.dao.*;
import com.agrologic.app.dao.impl.CellinkDaoImpl;
import com.agrologic.app.dao.impl.ControllerDaoImpl;
import com.agrologic.app.dao.impl.LanguageDaoImpl;
import com.agrologic.app.model.*;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

public class RCScreensServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

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
        final Logger logger = Logger.getLogger(RCScreensServlet.class);

        response.setContentType("text/html;charset=UTF-8");

        PrintWriter out = response.getWriter();

        try {
            long userId = Long.parseLong(request.getParameter("userId"));
            long cellinkId = Long.parseLong(request.getParameter("cellinkId"));
            long controllerId = Long.parseLong(request.getParameter("controllerId"));
            long screenId = Long.parseLong(request.getParameter("screenId"));
            String lang = (String) request.getSession().getAttribute("lang");

            if ((lang == null) || lang.equals("")) {
                lang = "en";
            }

            checkRealTimeSessionTimeout(request, logger);

            try {

//              IUserDao userDao = new UserDaoImpl();
//              UserDto user = userDao.getById(userId);
//              request.getSession().setAttribute("user", user);
                CellinkDao cellinkDao = new CellinkDaoImpl();
                CellinkDto cellink = cellinkDao.getById(cellinkId);

                // cellink.setTime(new Timestamp(System.currentTimeMillis()));
                cellinkDao.update(cellink);

                LanguageDao languageDao = new LanguageDaoImpl();
                long langId = languageDao.getLanguageId(lang);
                ControllerDao controllerDao = new ControllerDaoImpl();
                List<ControllerDto> controllers = controllerDao.getAllByCellinkId(cellinkId);
                ProgramDao programDao = DbImplDecider.use(DaoType.MYSQL).getDao(ProgramDao.class);
                final ProgramRelayDao programRelayDao = DbImplDecider.use(DaoType.MYSQL).getDao(ProgramRelayDao.class);

                ScreenDao screenDao = DbImplDecider.use(DaoType.MYSQL).getDao(ScreenDao.class);
                ;
                TableDao tableDao = DbImplDecider.use(DaoType.MYSQL).getDao(TableDao.class);
                DataDao dataDao = DbImplDecider.use(DaoType.MYSQL).getDao(DataDao.class);

                for (ControllerDto controller : controllers) {
                    if (controller.getId() == controllerId) {
                        Program program = programDao.getById(controller.getProgramId());
                        List<ProgramRelay> programRelays = programRelayDao.getAllProgramRelays(program.getId());

                        program.setProgramRelays(programRelays);

                        List<Screen> screens = (List<Screen>) screenDao.getAllScreensByProgramAndLang(program.getId(),
                                langId, false);

                        for (Screen screen : screens) {
                            if (screen.getId() == screenId) {
                                Collection<Table> tables = tableDao.getScreenTables(program.getId(), screen.getId(),
                                        langId, false);

                                for (Table table : tables) {
                                    Collection<Data> dataList = dataDao.getOnlineTableDataList(controller.getId(),
                                            program.getId(), screen.getId(), table.getId(), langId);
                                    table.setDataList(dataList);
                                }

                                screen.setTables(tables);
                                logger.info("retrieve screen tables and data");
                            }
                        }

                        program.setScreens(screens);
                        controller.setProgram(program);
                    } else {
                        continue;
                    }
                }

                List<Data> dataRelays = dataDao.getRelays();

                logger.info("retrieve program data relay!");
                request.getSession().setAttribute("dataRelays", dataRelays);
                request.getSession().setAttribute("controllers", controllers);
                request.getRequestDispatcher("./rmctrl-controller-screens.jsp?userId" + userId + "&cellinkId="
                        + cellinkId + "&screenId=" + screenId).forward(request, response);
            } catch (SQLException ex) {

                // error page
                logger.info("retrieve program data relay!", ex);
                request.getRequestDispatcher("./rmctrl-controller-screens.jsp?userId" + userId + "&cellinkId="
                        + cellinkId + "&screenId=" + screenId).forward(request, response);
            } catch (Exception ex) {

                // error page
                ex.printStackTrace();
                logger.info("retrieve program data relay!");
                request.getRequestDispatcher("./rmctrl-controller-screens.jsp?userId" + userId + "&cellinkId="
                        + cellinkId + "&screenId=" + screenId).forward(request, response);
            }
        } finally {
            out.close();
        }
    }

    /**
     * Check if real time session time out expired , if yes than
     * newConnectionTimeout=10; This will affect to logout of the system.
     *
     * @param request the request object
     */
    private void checkRealTimeSessionTimeout(HttpServletRequest request, Logger logger) {
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
            startConnTime = (Long) request.getSession().getAttribute("startSessionTime");

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



