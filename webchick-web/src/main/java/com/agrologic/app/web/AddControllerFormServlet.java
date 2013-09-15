
/*
* To change this template, choose Tools | Templates
* and open the template in the editor.
 */
package com.agrologic.app.web;


import com.agrologic.app.dao.ControllerDao;
import com.agrologic.app.dao.DaoType;
import com.agrologic.app.dao.DbImplDecider;
import com.agrologic.app.model.Controller;
import com.agrologic.app.model.User;
import com.agrologic.app.model.UserRole;
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
public class AddControllerFormServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

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
        final Logger logger = Logger.getLogger(AddControllerFormServlet.class);

        response.setContentType("text/html;charset=UTF-8");

        PrintWriter out = response.getWriter();

        if (!CheckUserInSession.isUserInSession(request)) {
            logger.error("Unauthorized access!");
            request.getRequestDispatcher("./login.jsp").forward(request, response);
        }

        User user = (User) request.getSession().getAttribute("user");
        String forwardLink = "";

        if (user.getRole() == UserRole.ADMIN) {
            forwardLink = "./cellinkinfo.html";
        } else {
            forwardLink = "./cellink-setting.html";
        }

        Long userId = Long.parseLong(request.getParameter("userId"));
        Long cellinkId = Long.parseLong(request.getParameter("cellinkId"));
        String title = request.getParameter("Ntitle");
        String netName = request.getParameter("Nnetname");
        Long programId = Long.parseLong(request.getParameter("Nprogramid"));
        String newControllNameList = request.getParameter("Ncontrollernamelist");
        String newControllName = request.getParameter("Ncontrollername");
        String newControllNameCheckBox = request.getParameter("newControllerName");
        String active = request.getParameter("Nactive");
        Controller controller = new Controller();

        controller.setId(null);
        controller.setNetName(netName);
        controller.setTitle(title);
        controller.setCellinkId(cellinkId);
        controller.setProgramId(programId);

        if (newControllNameCheckBox != null) {
            if ("ON".equals(newControllNameCheckBox.toUpperCase())) {
                controller.setName(newControllName);
            } else {
                controller.setName(newControllNameList);
            }
        } else {
            controller.setName(newControllNameList);
        }

        if ((active != null) && "ON".equals(active.toUpperCase())) {
            controller.setActive(true);
        } else {
            controller.setActive(false);
        }

        try {
            ControllerDao controllerDao = DbImplDecider.use(DaoType.MYSQL).getDao(ControllerDao.class);
            controllerDao.insert(controller);
            logger.info("Cellink " + controller + " successfully added !");
            request.getSession().setAttribute("message", "Controller successfully added !");
            request.getSession().setAttribute("error", false);
            request.getRequestDispatcher(forwardLink + "?userId" + userId + "&cellinkId" + cellinkId).forward(request,
                    response);
        } catch (SQLException ex) {

            // error page
            logger.error("Error occurs while adding cellink !");
            request.getSession().setAttribute("message", "Error occurs while adding controller !");
            request.getSession().setAttribute("error", true);
            request.getRequestDispatcher(forwardLink + "?userId" + userId + "&cellinkId" + cellinkId).forward(request,
                    response);
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



