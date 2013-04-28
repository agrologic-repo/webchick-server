
/*
* To change this template, choose Tools | Templates
* and open the template in the editor.
 */
package com.agrologic.app.web;



import com.agrologic.app.dao.CellinkDao;
import com.agrologic.app.dao.ControllerDao;
import com.agrologic.app.dao.DataDao;
import com.agrologic.app.dao.LanguageDao;
import com.agrologic.app.dao.ProgSysStateDao;
import com.agrologic.app.dao.ProgramAlarmDao;
import com.agrologic.app.dao.ProgramDao;
import com.agrologic.app.dao.ProgramRelayDao;
import com.agrologic.app.dao.ScreenDao;
import com.agrologic.app.dao.TableDao;
import com.agrologic.app.dao.impl.CellinkDaoImpl;
import com.agrologic.app.dao.impl.ControllerDaoImpl;
import com.agrologic.app.dao.impl.DataDaoImpl;
import com.agrologic.app.dao.impl.LanguageDaoImpl;
import com.agrologic.app.dao.impl.ProgramAlarmDaoImpl;
import com.agrologic.app.dao.impl.ProgramDaoImpl;
import com.agrologic.app.dao.impl.ProgramRelayDaoImpl;
import com.agrologic.app.dao.impl.ProgramSystemStateDaoImpl;
import com.agrologic.app.dao.impl.ScreenDaoImpl;
import com.agrologic.app.dao.impl.TableDaoImpl;
import com.agrologic.app.model.CellinkDto;
import com.agrologic.app.model.ControllerDto;
import com.agrologic.app.model.DataDto;
import com.agrologic.app.model.ProgramDto;
import com.agrologic.app.model.ScreenDto;
import com.agrologic.app.model.TableDto;

import org.apache.log4j.Logger;

//~--- JDK imports ------------------------------------------------------------

import java.io.IOException;
import java.io.PrintWriter;

import java.sql.SQLException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author JanL
 */
public class RCMainScreenServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        /** Logger for this class and subclasses */
        final Logger logger = Logger.getLogger(RCMainScreenServlet.class);

        response.setContentType("text/html;charset=UTF-8");

        PrintWriter out = response.getWriter();

        try {
            long   userId    = Long.parseLong(request.getParameter("userId"));
            long   cellinkId = Long.parseLong(request.getParameter("cellinkId"));
            long   screenId  = Long.parseLong(request.getParameter("screenId"));
            String lang      = (String) request.getSession().getAttribute("lang");

            if ((lang == null) || lang.equals("")) {
                lang = "en";
            }

            checkRealTimeSessionTimeout(request, logger);

            try {
                CellinkDao cellinkDao = new CellinkDaoImpl();
                CellinkDto  cellink    = cellinkDao.getById(cellinkId);

                if (cellink.getState() == CellinkState.STATE_ONLINE) {
                    cellink.setState(CellinkState.STATE_START);
                    cellinkDao.update(cellink);
                }

                // cellink.setTime(new Timestamp(System.currentTimeMillis()));
                cellinkDao.update(cellink);
                logger.info("Connect to cellink " + cellink);
                request.getSession().setAttribute("cellink", cellink);

                LanguageDao languageDao = new LanguageDaoImpl();
                long         langId      = languageDao.getLanguageId(lang);    // get language id

                // get all controllers connected to cellink
                ControllerDao         controllerDao         = new ControllerDaoImpl();
                List<ControllerDto>    controllers           = controllerDao.getAllByCellinkId(new Long(cellinkId));
                final DataDao         dataDao               = new DataDaoImpl();
                final TableDao        tableDao              = new TableDaoImpl();
                final ScreenDao       screenDao             = new ScreenDaoImpl();
                final ProgramDao      programDao            = new ProgramDaoImpl();
                final ProgramRelayDao programRelayDao       = new ProgramRelayDaoImpl();
                final ProgramAlarmDao programAlarmDao       = new ProgramAlarmDaoImpl();
                final ProgSysStateDao programSystemStateDao = new ProgramSystemStateDaoImpl();
                Map<Long, Long>        nextScrIdsByCntrl     = new HashMap<Long, Long>();

                for (ControllerDto controller : controllers) {

                    // get asigned to controller program
                    ProgramDto program = programDao.getById(controller.getProgramId());

                    controller.setProgram(program);

                    Long nsid = screenDao.getSecondScreenAfterMain(program.getId());

                    nextScrIdsByCntrl.put(controller.getId(), nsid);

                    DataDto setClock = dataDao.getSetClockByController(controller.getId());

                    controller.setSetClock(setClock);

                    // get program relays
                    program.setProgramRelays(programRelayDao.getAllProgramRelays(program.getId(), langId));

                    // get program alarms
                    program.setProgramAlarms(programAlarmDao.getAllProgramAlarms(program.getId(), langId));

                    // get program system states
                    program.setProgramSystemStates(programSystemStateDao.getAllProgramSystemStates(program.getId(),
                            langId));

                    // get program current screen
                    ScreenDto screen = screenDao.getById(program.getId(), screenId, langId);

                    // get screen tables
                    List<TableDto> tables = tableDao.getScreenTables(program.getId(), screen.getId(), langId, false);

                    for (TableDto table : tables) {
                        table.setDataList(dataDao.getOnlineTableDataList(program.getId(), controller.getId(),
                                screen.getId(), table.getId(), langId));

                        // table.setDataList(dataDao.getOnlineTableDataList(program.getId(), controller.getId(), table.getId(), langId));
                    }

                    screen.setTables(tables);
                    program.addScreen(screen);
                }

                request.getSession().setAttribute("controllers", controllers);
                logger.info("retreive controllers!");
                request.getSession().setAttribute("dataRelays", dataDao.getRelays());
                logger.info("retreive program data relay!");
                request.getSession().setAttribute("nextScrIdsByCntrl", nextScrIdsByCntrl);
                request.getRequestDispatcher("./rmctrl-main-screen.jsp?lang=" + lang + "&userId" + userId
                                             + "&screenId=" + screenId).forward(request, response);
            } catch (SQLException ex) {

                // error page
            }
        } finally {
            out.close();
        }
    }

    /**
     * Check if real time session time out expired ,
     * if yes than newConnectionTimeout=10;
     * This will affect to logout of the system.
     *
     * @param request the request object
     */
    private void checkRealTimeSessionTimeout(HttpServletRequest request, Logger logger) {
        String  doResetTimeout = request.getParameter("doResetTimeout");
        Integer newConnTimeout = 0;
        Long    startConnTime  = null;

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
     * Handles the HTTP <code>GET</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }    // </editor-fold>
}



