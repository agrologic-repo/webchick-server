
/*
* To change this template, choose Tools | Templates
* and open the template in the editor.
 */
package com.agrologic.app.web;



import com.agrologic.app.dao.ActionSetDao;
import com.agrologic.app.dao.DataDao;
import com.agrologic.app.dao.ProgramDao;
import com.agrologic.app.dao.ScreenDao;
import com.agrologic.app.dao.TableDao;
import com.agrologic.app.dao.impl.ActionSetDaoImpl;
import com.agrologic.app.dao.impl.DataDaoImpl;
import com.agrologic.app.dao.impl.ProgramDaoImpl;
import com.agrologic.app.dao.impl.ScreenDaoImpl;
import com.agrologic.app.dao.impl.TableDaoImpl;
import com.agrologic.app.model.ActionSetDto;
import com.agrologic.app.model.DataDto;
import com.agrologic.app.model.ProgramDto;
import com.agrologic.app.model.ScreenDto;
import com.agrologic.app.model.TableDto;

import org.apache.log4j.Logger;

//~--- JDK imports ------------------------------------------------------------

import java.io.IOException;
import java.io.PrintWriter;

import java.sql.SQLException;

import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AddSelectedScreenFormServlet extends HttpServlet {

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
        final Logger logger = Logger.getLogger(AddProgramServlet.class);

        response.setContentType("text/html;charset=UTF-8");

        PrintWriter out = response.getWriter();

        try {
            if (!CheckUserInSession.isUserInSession(request)) {
                logger.error("Unauthorized access!");
                request.getRequestDispatcher("./login.jsp").forward(request, response);
            } else {
                Long programId         = Long.parseLong(request.getParameter("programId"));
                Long selectedProgramId = Long.parseLong(request.getParameter("selectedProgramId"));
                Long selectedScreenId  = Long.parseLong(request.getParameter("selectedScreenId"));

                try {
                    ProgramDao programDao    = new ProgramDaoImpl();
                    ProgramDto  program       = programDao.getById(selectedProgramId);
                    ScreenDao  screenDao     = new ScreenDaoImpl();
                    ScreenDto   screen        = screenDao.getById(program.getId(), selectedScreenId);
                    int         nextScreenPos = screenDao.getNextScreenPosByProgramId(programId);

                    screen.setProgramId(programId);
                    screen.setPosition(nextScreenPos);
                    screen.setDisplay("yes");
                    screenDao.insertExistScreen(screen);

                    if (screen.getTitle().equals("Action Set Buttons")) {
                        ActionSetDao      actionSetDao  = new ActionSetDaoImpl();
                        List<ActionSetDto> actionsetList = actionSetDao.getAll(program.getId());

                        actionSetDao.insertActionSetList(actionsetList, programId);
                    } else {
                        TableDao      tableDao     = new TableDaoImpl();
                        List<TableDto> screenTables = tableDao.getAllScreenTables(selectedProgramId, selectedScreenId,
                                                          null);
                        DataDao dataDao = new DataDaoImpl();

                        for (TableDto t : screenTables) {
                            t.setProgramId(programId);
                            tableDao.insertExsitTable(t);

                            List<DataDto> tableData = dataDao.getTableDataList(selectedProgramId, selectedScreenId,
                                                          t.getId(), null);

                            for (DataDto d : tableData) {
                                dataDao.insertDataToTable(programId, screen.getId(), t.getId(), d.getId(), "yes",
                                                          d.getPosition());
                            }
                        }
                    }

                    logger.info("New screen and screens data successfully added !");
                    request.getSession().setAttribute("error", false);
                    request.getRequestDispatcher("./all-screens.html?programId=" + programId).forward(request,
                                                 response);
                } catch (SQLException e) {

                    // error page
                    logger.error("Error occurs while adding screen !", e);
                    request.getSession().setAttribute("message", "Error occurs during adding table!");
                    request.getSession().setAttribute("error", true);
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



