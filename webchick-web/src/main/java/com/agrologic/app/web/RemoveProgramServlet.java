
/*
* To change this template, choose Tools | Templates
* and open the template in the editor.
 */
package com.agrologic.app.web;


import com.agrologic.app.dao.DataDao;
import com.agrologic.app.dao.ProgramDao;
import com.agrologic.app.dao.ScreenDao;
import com.agrologic.app.dao.TableDao;
import com.agrologic.app.dao.impl.DataDaoImpl;
import com.agrologic.app.dao.impl.ProgramDaoImpl;
import com.agrologic.app.dao.impl.ScreenDaoImpl;
import com.agrologic.app.dao.impl.TableDaoImpl;
import com.agrologic.app.model.ProgramDto;
import com.agrologic.app.model.ScreenDto;
import com.agrologic.app.model.TableDto;
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

/**
 * @author JanL
 */
public class RemoveProgramServlet extends HttpServlet {

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
        final Logger logger = Logger.getLogger(RemoveProgramServlet.class);

        response.setContentType("text/html;charset=UTF-8");

        PrintWriter out = response.getWriter();

        try {
            if (!CheckUserInSession.isUserInSession(request)) {
                logger.error("Unauthorized access!");
                request.getRequestDispatcher("./login.jsp").forward(request, response);
            } else {
                Long programId = Long.parseLong(request.getParameter("programId"));

                if (CheckDefaultProgram.isDefaultProgram(programId)) {
                    logger.error("Can't remove default program!");
                    request.getSession().setAttribute("message", "Can't remove default program!");
                    request.getSession().setAttribute("error", true);
                    request.getRequestDispatcher("./all-programs.html").forward(request, response);
                } else {
                    try {
                        ProgramDao programDao = new ProgramDaoImpl();
                        ScreenDao screenDao = new ScreenDaoImpl();
                        TableDao tableDao = new TableDaoImpl();
                        DataDao dataDao = new DataDaoImpl();
                        ProgramDto program = programDao.getById(programId);
                        List<ScreenDto> screens = screenDao.getAllByProgramId(program.getId());

                        for (ScreenDto s : screens) {
                            List<TableDto> tables = tableDao.getAllScreenTables(programId, s.getId(), null);

                            for (TableDto t : tables) {
                                dataDao.removeDataFromTable(programId, s.getId(), t.getId());
                            }

                            screenDao.remove(programId, s.getId());
                        }

                        programDao.remove(program.getId());
                        logger.info("Program " + program + "successfully removed !");
                        request.getSession().setAttribute("message", "Program successfully  removed !");
                        request.getSession().setAttribute("error", false);
                        request.getRequestDispatcher("./all-programs.html").forward(request, response);
                    } catch (SQLException ex) {

                        // error page
                        logger.error("Error occurs while removing program !");
                        request.getSession().setAttribute("message", "Error occurs while removing program !");
                        request.getSession().setAttribute("error", true);
                        request.getRequestDispatcher("./all-programs.html").forward(request, response);
                    }
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



