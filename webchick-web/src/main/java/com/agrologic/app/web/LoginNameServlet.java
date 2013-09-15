package com.agrologic.app.web;

import com.agrologic.app.dao.DaoType;
import com.agrologic.app.dao.DbImplDecider;
import com.agrologic.app.dao.UserDao;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class LoginNameServlet extends HttpServlet {
//    Logger logger = LoggerFactory.getLogger(LoginNameServlet.class);

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
        final org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(LoginNameServlet.class);
        try {
            UserDao udi = DbImplDecider.use(DaoType.MYSQL).getDao(UserDao.class);
            ;
            String login = request.getParameter("loginName");
            boolean isOk = udi.loginEnabled(login);

            response.setContentType("text/xml");
            response.setHeader("Cache-Control", "no-cache");
            out.print("<message>");
            logger.debug("Checking login name {} " + login);
            if (isOk) {
                out.print("login valid");
                logger.debug("Login name {}  valid " + login);
            } else {
                out.print("login invalid, choose another name");
                logger.debug("Login name {}  invalid " + login);
            }

            out.println("</message>");
        } catch (Exception ex) {
            response.setContentType("text/xml");
            response.setHeader("Cache-Control", "no-cache");
            out = response.getWriter();
            out.print("<message>");
            out.print("exception");
            out.println("</message>");
            logger.debug("Exception occur ");
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



