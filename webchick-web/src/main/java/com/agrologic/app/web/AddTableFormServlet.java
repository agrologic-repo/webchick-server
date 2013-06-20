
/*
* To change this template, choose Tools | Templates
* and open the template in the editor.
 */
package com.agrologic.app.web;


import com.agrologic.app.dao.ProgramDao;
import com.agrologic.app.dao.TableDao;
import com.agrologic.app.dao.impl.ProgramDaoImpl;
import com.agrologic.app.dao.impl.TableDaoImpl;
import com.agrologic.app.model.ProgramDto;
import com.agrologic.app.model.TableDto;
import com.agrologic.app.utils.DateLocal;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

//~--- JDK imports ------------------------------------------------------------

/**
 * @author JanL
 */
public class AddTableFormServlet extends HttpServlet {

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
        final Logger logger = Logger.getLogger(AddProgramServlet.class);

        response.setContentType("text/html;charset=UTF-8");

        PrintWriter out = response.getWriter();

        try {
            if (!CheckUserInSession.isUserInSession(request)) {
                logger.error("Unauthorized access!");
                request.getRequestDispatcher("./login.jsp").forward(request, response);
            } else {
                Long programId = Long.parseLong(request.getParameter("programId"));
                Long screenId = Long.parseLong(request.getParameter("screenId"));
                String tableName = (String) request.getParameter("Ntablename");
                Integer position = Integer.parseInt(request.getParameter("Nposition"));

                try {
                    TableDto table = new TableDto();

                    table.setScreenId(screenId);
                    table.setProgramId(programId);
                    table.setTitle(tableName);
                    table.setDisplay("yes");
                    table.setPosition(position);

                    TableDao tableDao = new TableDaoImpl();

                    tableDao.insert(table);
                    logger.info("New table successfully added !");

                    ProgramDao programDao = new ProgramDaoImpl();
                    ProgramDto program = programDao.getById(programId);

                    program.setModifiedDate(DateLocal.currentDate());
                    programDao.update(program);
                    request.getSession().setAttribute("message", "table successfully added !");
                    request.getSession().setAttribute("error", false);
                    request.getRequestDispatcher("./all-tables.html?programId=" + programId + "&screenId="
                            + screenId).forward(request, response);
                } catch (SQLException ex) {

                    // error page
                    logger.error("Error occurs while adding table !");
                    request.getSession().setAttribute("message", "Error occurs while adding table!");
                    request.getSession().setAttribute("error", true);
                    request.getRequestDispatcher("./all-tables.html?programId=" + programId + "&screenId="
                            + screenId).forward(request, response);
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



