
/*
* To change this template, choose Tools | Templates
* and open the template in the editor.
 */
package com.agrologic.app.web;


import com.agrologic.app.dao.ControllerDao;
import com.agrologic.app.dao.FlockDao;
import com.agrologic.app.dao.impl.ControllerDaoImpl;
import com.agrologic.app.dao.impl.FlockDaoImpl;
import com.agrologic.app.model.ControllerDto;
import com.agrologic.app.model.FlockDto;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

//~--- JDK imports ------------------------------------------------------------

public class AddFlockFormServlet extends HttpServlet {
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
        final Logger logger = Logger.getLogger(AddFlockFormServlet.class);

        response.setContentType("text/html;charset=UTF-8");

        PrintWriter out = response.getWriter();

        if (!CheckUserInSession.isUserInSession(request)) {
            logger.error("Unauthorized access!");
            request.getRequestDispatcher("./login.jsp").forward(request, response);
        } else {
            Long userId = Long.parseLong(request.getParameter("userId"));
            Long cellinkId = Long.parseLong(request.getParameter("cellinkId"));
            Long controllerId = Long.parseLong(request.getParameter("house_Filter"));
            String flockName = request.getParameter("flockName");
            String status = request.getParameter("status_Filter");
            String startDate = request.getParameter("startDate");
            String endDate = request.getParameter("endDate");
            FlockDto flock = new FlockDto();

            flock.setFlockName(flockName);
            flock.setControllerId(controllerId);
            flock.setStatus(status);
            flock.setStartDate(startDate);
            flock.setEndDate(endDate);

            try {
                ControllerDao controllerDao = new ControllerDaoImpl();
                ControllerDto controller = controllerDao.getById(controllerId);

                flock.setProgramId(controller.getProgramId());

                FlockDao flockDao = new FlockDaoImpl();

                flockDao.insert(flock);
                logger.info("Flock " + flock + " successfully added !");
                request.getSession().setAttribute("message", "Flock successfully added !");
                request.getSession().setAttribute("error", false);
                request.getRequestDispatcher("./flocks.html?userId=" + userId + "&cellinkId="
                        + cellinkId).forward(request, response);
            } catch (SQLException ex) {
                logger.error("Error occurs during adding flock !");
                request.getSession().setAttribute("message", "Error occurs during adding flock !");
                request.getSession().setAttribute("error", true);
                request.getRequestDispatcher("./flocks.html?userId=" + userId + "&cellinkId="
                        + cellinkId).forward(request, response);
            } finally {
                out.close();
            }
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



