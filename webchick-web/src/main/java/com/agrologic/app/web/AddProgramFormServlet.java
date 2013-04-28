
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
import com.agrologic.app.utils.DateLocal;

import org.apache.log4j.Logger;

//~--- JDK imports ------------------------------------------------------------

import java.io.IOException;
import java.io.PrintWriter;

import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author JanL
 */
public class AddProgramFormServlet extends HttpServlet {

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
        final Logger logger = Logger.getLogger(AddProgramFormServlet.class);

        response.setContentType("text/html;charset=UTF-8");

        PrintWriter out = response.getWriter();

        try {
            if (!CheckUserInSession.isUserInSession(request)) {
                logger.error("Unauthorized access!");
                request.getRequestDispatcher("./login.jsp").forward(request, response);
            } else {
                String name              = request.getParameter("Nname");
                Long   programId         = Long.parseLong(request.getParameter("Nprogramid"));
                Long   selectedProgramId = Long.parseLong(request.getParameter("Selectedprogramid"));

                try {
                    ProgramDto newProgram = new ProgramDto();

                    newProgram.setId(programId);
                    newProgram.setName(name);
                    newProgram.setCreatedDate(DateLocal.currentDate());
                    newProgram.setModifiedDate(DateLocal.currentDate());

                    // Here we insert new table to database
                    ProgramDao programDao = new ProgramDaoImpl();

                    programDao.insert(newProgram);
                    logger.info("Program " + newProgram + "successfully added !");
                    request.getSession().setAttribute("message", "program successfully added !");

                    // Here we execute query for inserting screens of new program
                    ScreenDao screenDao = new ScreenDaoImpl();

                    screenDao.insertDefaultScreens(newProgram.getId(), selectedProgramId);
                    logger.info("New screens successfully added !");

                    // Get screens of inserted program. Screens of new programs have
                    // same ID's , so we need same screens but new progam id in screen .
                    TableDao tableDao = new TableDaoImpl();

                    tableDao.insertDefaultTables(newProgram.getId(), selectedProgramId);

                    DataDao dataDao = new DataDaoImpl();

                    dataDao.insertDataList(newProgram.getId(), selectedProgramId);

                    // ----------------------------------------------------------
                    request.getSession().setAttribute("error", false);
                    request.getSession().setAttribute("message", "Program successfully added !");
                    request.getRequestDispatcher("./all-programs.html").forward(request, response);
                } catch (SQLException ex) {

                    // error page
                    logger.error("Error occurs while adding cellink !");
                    request.getSession().setAttribute("message", "Error occurs while adding program !");
                    request.getSession().setAttribute("error", true);
                    request.getRequestDispatcher("./all-programs.html").forward(request, response);
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



