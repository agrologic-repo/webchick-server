
/*
* To change this template, choose Tools | Templates
* and open the template in the editor.
 */
package com.agrologic.app.web;


import com.agrologic.app.dao.CellinkDao;
import com.agrologic.app.dao.ControllerDao;
import com.agrologic.app.dao.impl.CellinkDaoImpl;
import com.agrologic.app.dao.impl.ControllerDaoImpl;
import com.agrologic.app.model.CellinkDto;
import com.agrologic.app.model.ControllerDto;
import com.agrologic.app.model.UserDto;
import org.apache.log4j.Logger;

import javax.servlet.ServletContext;
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
public class OverviewServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private ServletContext ctx;

    /**
     * Processes requests for both HTTP
     * <code>GET</code> and
     * <code>POST</code> methods.
     *
     * @param request  servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException      if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        /**
         * Logger for this class and subclasses
         */
        final Logger logger = Logger.getLogger(ListUserCellinksServlet.class);

        response.setContentType("text/html;charset=UTF-8");

        PrintWriter out = response.getWriter();

        if (!CheckUserInSession.isUserInSession(request)) {
            logger.error("Unauthorized access!");
            request.getRequestDispatcher("./login.jsp").forward(request, response);
        } else {
            UserDto user = (UserDto) request.getSession().getAttribute("user");
            String nameParam = request.getParameter("name");
            if (nameParam == null) {
                nameParam = "";
            }
            String stateParam = request.getParameter("state");
            if (stateParam == null) {
                stateParam = "";
            }

            String typeParam = request.getParameter("type");
            if (typeParam == null) {
                typeParam = "";
            }

            Integer state = null;

            try {
                state = Integer.parseInt(stateParam);
            } catch (Exception e) {
                state = -1;
            }

            String index = request.getParameter("index");

            if (index == null) {
                index = "0";
            }

            try {
                CellinkDao cellinkDao = new CellinkDaoImpl();
                ControllerDao controllerDao = new ControllerDaoImpl();
                int count = 0;

                switch (user.getRole()) {
                    default:
                        logger.info("retrieve all cellinks that belongs to  " + user);

                        List<CellinkDto> cellinks = cellinkDao.getAll(user.getId(), state, typeParam, nameParam);

                        for (CellinkDto cellink : cellinks) {
                            List<ControllerDto> controllers = controllerDao.getAllByCellinkId(cellink.getId());

                            cellink.setControllers(controllers);
                        }

                        request.getSession().setAttribute("cellinks", cellinks);
                        count = cellinkDao.count(user.getId(), user.getRole(), null, state, typeParam, nameParam);
                        setTableParameters(request, index, count);

                        break;

                    case UserRole.REGULAR:
                        logger.info("retrieve all cellinks that belongs to  " + user);
                        cellinks = cellinkDao.getAll(user.getId(), state, typeParam, nameParam);
                        for (CellinkDto cellink : cellinks) {
                            List<ControllerDto> controllers = controllerDao.getAllByCellinkId(cellink.getId());
                            cellink.setControllers(controllers);
                        }
                        request.getSession().setAttribute("cellinks", cellinks);
                        count = cellinkDao.count(user.getId(), user.getRole(), null, state, typeParam, nameParam);
                        setTableParameters(request, index, count);
                        break;
                    case UserRole.ADVANCED:
                        cellinks = cellinkDao.getAll(user.getRole(), user.getCompany(), state, typeParam, nameParam, index);
                        for (CellinkDto cellink : cellinks) {
                            List<ControllerDto> controllers = controllerDao.getAllByCellinkId(cellink.getId());
                            cellink.setControllers(controllers);
                        }
                        request.getSession().setAttribute("cellinks", cellinks);
                        count = cellinkDao.count(user.getId(), user.getRole(), user.getCompany(), state, typeParam, nameParam);
                        setTableParameters(request, index, count);

                        break;

                    case UserRole.ADMINISTRATOR:
                        try {
                            cellinks = cellinkDao.getAll(user.getRole(), user.getCompany(), state, typeParam, nameParam,
                                    index);

                            for (CellinkDto cellink : cellinks) {
                                List<ControllerDto> controllers = controllerDao.getAllByCellinkId(cellink.getId());

                                cellink.setControllers(controllers);
                            }

                            request.getSession().setAttribute("cellinks", cellinks);
                        } catch (NumberFormatException ex) {
                        }

                        count = cellinkDao.count(user.getId(), user.getRole(), null, state, typeParam, nameParam);
                        setTableParameters(request, index, count);

                        break;
                }

                request.getRequestDispatcher("./overview.jsp").forward(request, response);
            } catch (SQLException ex) {

                // error page
                logger.error("Error durring sql query running ", ex);
            } finally {
                out.close();
            }
        }
    }

    private void setTableParameters(HttpServletRequest request, String index, int count) {
        if (index.equals("0")) {
            int from = 0;
            int to = ((count - from) > 25
                    ? 25
                    : (count - from));
            int of = count;

            if (count > 0) {
                from += 1;
            }

            request.getSession().setAttribute("from", from);
            request.getSession().setAttribute("to", to);
            request.getSession().setAttribute("of", of);
        } else {
            int from = Integer.parseInt(index);
            int to = from + ((count - from) > 25
                    ? 25
                    : (count - from));
            int of = count;

            request.getSession().setAttribute("from", from + 1);
            request.getSession().setAttribute("to", to);
            request.getSession().setAttribute("of", of);
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">

    /**
     * Handles the HTTP
     * <code>GET</code> method.
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
     * Handles the HTTP
     * <code>POST</code> method.
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



