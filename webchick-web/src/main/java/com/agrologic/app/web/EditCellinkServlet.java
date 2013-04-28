
/*
* To change this template, choose Tools | Templates
* and open the template in the editor.
 */
package com.agrologic.app.web;



import com.agrologic.app.dao.CellinkDao;
import com.agrologic.app.dao.UserDao;
import com.agrologic.app.dao.impl.CellinkDaoImpl;
import com.agrologic.app.dao.impl.UserDaoImpl;
import com.agrologic.app.model.CellinkDto;
import com.agrologic.app.model.UserDto;

import org.apache.log4j.Logger;

//~--- JDK imports ------------------------------------------------------------

import java.io.IOException;
import java.io.PrintWriter;

import java.sql.SQLException;

import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author JanL
 */
public class EditCellinkServlet extends HttpServlet {

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
        final Logger logger = Logger.getLogger(ListUserServlet.class);

        response.setContentType("text/html;charset=UTF-8");

        PrintWriter out = response.getWriter();

        try {
            if (!CheckUserInSession.isUserInSession(request)) {
                logger.error("Unauthorized access!");
                request.getRequestDispatcher("./login.jsp").forward(request, response);
            } else {
                Long userId    = Long.parseLong(request.getParameter("userId"));
                Long cellinkId = Long.parseLong(request.getParameter("cellinkId"));

                try {
                    UserDao userDao  = new UserDaoImpl();
                    UserDto  editUser = userDao.getById(userId);

                    editUser.setCellinks(new ArrayList<CellinkDto>());

                    CellinkDao cellinkDao = new CellinkDaoImpl();
                    CellinkDto  c          = (CellinkDto) cellinkDao.getById(cellinkId);

                    editUser.addCellink(c);
                    logger.info("retreive user and user cellinks to edit");
                    request.getSession().setAttribute("edituser", editUser);
                    request.getRequestDispatcher("./edit-cellink.jsp?userId=" + userId + "&celinkId="
                                                 + cellinkId).forward(request, response);
                } catch (SQLException ex) {

                    // error page
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



