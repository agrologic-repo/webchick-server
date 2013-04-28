
/*
* To change this template, choose Tools | Templates
* and open the template in the editor.
 */
package com.agrologic.app.web;



import com.agrologic.app.dao.UserDao;
import com.agrologic.app.dao.impl.UserDaoImpl;
import com.agrologic.app.model.UserDto;

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
public class AddUserFormServlet extends HttpServlet {

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
        final Logger logger = Logger.getLogger(AddUserFormServlet.class);

        response.setContentType("text/html;charset=UTF-8");

        PrintWriter out = response.getWriter();

        if (!CheckUserInSession.isUserInSession(request)) {
            logger.error("Unauthorized access!");
            request.getRequestDispatcher("./login.jsp").forward(request, response);
        }

        String  login              = request.getParameter("Nusername");
        String  password           = request.getParameter("Npassword");
        String  firstName          = request.getParameter("Nfname");
        String  lastName           = request.getParameter("Nlname");
        String  phone              = request.getParameter("Nphone");
        String  email              = request.getParameter("Nemail");
        String  role               = request.getParameter("Nrole");
        String  newCompanyList     = request.getParameter("Ncompanylist");
        String  newCompany         = request.getParameter("Ncompany");
        String  newCompanyCheckBox = request.getParameter("newCompany");
        UserDto user               = new UserDto();

        user.setLogin(login);

        String encpsswd = com.agrologic.app.utils.Base64.encode(password);

        user.setPassword(encpsswd);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setPhone(phone);
        user.setEmail(email);
        user.setRole(getUserRole(role));

        if ("ON".equals(newCompanyCheckBox)) {
            user.setCompany(newCompany);
        } else {
            user.setCompany(newCompanyList);
        }

        try {
            UserDao userDao = new UserDaoImpl();

            userDao.insert(user);
            logger.info("user " + user + " successfully added !");
            request.getSession().setAttribute("message", "User successfully added !");
            request.getSession().setAttribute("error", false);
            request.getRequestDispatcher("./all-users.html").forward(request, response);
        } catch (SQLException ex) {

            // error page
            logger.error("Error occurs while adding user.");
            request.getSession().setAttribute("message", "Error occurs while adding user.");
            request.getSession().setAttribute("error", true);
            request.getRequestDispatcher("./all-users.html").forward(request, response);
        } finally {
            out.close();
        }
    }

    private Integer getUserRole(String srole) {
        Integer role;

        try {
            role = Integer.parseInt(srole);
        } catch (NumberFormatException ex) {
            role = 0;
        }

        return role;
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



