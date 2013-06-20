
/*
* To change this template, choose Tools | Templates
* and open the template in the editor.
 */
package com.agrologic.app.web;


import com.agrologic.app.dao.CellinkDao;
import com.agrologic.app.dao.ControllerDao;
import com.agrologic.app.dao.ProgramDao;
import com.agrologic.app.dao.UserDao;
import com.agrologic.app.dao.impl.CellinkDaoImpl;
import com.agrologic.app.dao.impl.ControllerDaoImpl;
import com.agrologic.app.dao.impl.ProgramDaoImpl;
import com.agrologic.app.dao.impl.UserDaoImpl;
import com.agrologic.app.model.CellinkDto;
import com.agrologic.app.model.ControllerDto;
import com.agrologic.app.model.ProgramDto;
import com.agrologic.app.model.UserDto;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

//~--- JDK imports ------------------------------------------------------------

/**
 * @author JanL
 */
public class ListCellinkControllersServlet extends HttpServlet {
    final static String EMPTY_CELLINKID = "0";

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
        final Logger logger = Logger.getLogger(ListCellinkControllersServlet.class);

        response.setContentType("text/html;charset=UTF-8");

        PrintWriter out = response.getWriter();

        if (!CheckUserInSession.isUserInSession(request)) {
            logger.error("Unauthorized access!");
            request.getRequestDispatcher("./login.jsp").forward(request, response);
        } else {
            Long userId = Long.parseLong(request.getParameter("userId"));
            Long cellinkId = Long.parseLong(request.getParameter("cellinkId"));

            try {
                UserDao userDao = new UserDaoImpl();
                CellinkDao cellinkDao = new CellinkDaoImpl();
                ControllerDao controllerDao = new ControllerDaoImpl();
                ProgramDao programDao = new ProgramDaoImpl();
                List<ProgramDto> programs = programDao.getAll();
                List<UserDto> users = new ArrayList<UserDto>();

                users = userDao.getAll();

                for (UserDto u : users) {
                    List<CellinkDto> cellinks = (List<CellinkDto>) cellinkDao.getAllUserCellinks(u.getId());

                    for (CellinkDto c : cellinks) {
                        List<ControllerDto> controllers = controllerDao.getAllByCellinkId(c.getId());

                        for (ControllerDto ctrl : controllers) {
                            ProgramDto program = programDao.getById(ctrl.getProgramId());

                            ctrl.setProgram(program);
                        }

                        c.setControllers(controllers);
                    }

                    u.setCellinks(cellinks);
                }

                List<String> controllernames = controllerDao.getControllerNames();

                logger.info("retrieve all users ");
                request.getSession().setAttribute("users", users);
                request.getSession().setAttribute("programs", programs);
                request.getSession().setAttribute("controllernames", controllernames);
                request.getRequestDispatcher("./cellinkinfo.jsp?userId=" + userId + "&cellinkId="
                        + cellinkId).forward(request, response);
            } catch (SQLException ex) {

                // error page
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



