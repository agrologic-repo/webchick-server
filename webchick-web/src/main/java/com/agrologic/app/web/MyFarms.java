
/*
* To change this template, choose Tools | Templates
* and open the template in the editor.
 */
package com.agrologic.app.web;


import com.agrologic.app.dao.CellinkDao;
import com.agrologic.app.dao.ControllerDao;
import com.agrologic.app.dao.DaoType;
import com.agrologic.app.dao.DbImplDecider;
import com.agrologic.app.model.Cellink;
import com.agrologic.app.model.CellinkCriteria;
import com.agrologic.app.model.Controller;
import com.agrologic.app.model.User;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.Collection;

public class MyFarms extends HttpServlet {


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
        final Logger logger = Logger.getLogger(ListUserCellinksServlet.class);

        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

        if (!CheckUserInSession.isUserInSession(request)) {
            logger.error("Unauthorized access!");
            request.getRequestDispatcher("./login.jsp").forward(request, response);
        } else {
            User user = (User) request.getSession().getAttribute("user");
            String name = request.getParameter("name");
            String typeParam = request.getParameter("type");
            String stateParam = request.getParameter("state");
            String indexParam = request.getParameter("index");

            Integer state = null;
            try {
                state = Integer.parseInt(stateParam);
                if (state == -1) {
                    state = null;
                }
            } catch (Exception e) {
                state = null;
            }

            Integer index = null;
            try {
                index = Integer.parseInt(indexParam);
            } catch (Exception e) {
                index = null;
            }

            try {
                CellinkDao cellinkDao = DbImplDecider.use(DaoType.MYSQL).getDao(CellinkDao.class);
                ControllerDao controllerDao = DbImplDecider.use(DaoType.MYSQL).getDao(ControllerDao.class);
                CellinkCriteria criteria = new CellinkCriteria();
                criteria.setUserId(user.getId());
                criteria.setRole(user.getRole().getValue());
                criteria.setCompany(user.getCompany());
                criteria.setState(state);
                criteria.setName(name);
                criteria.setType(typeParam);
                criteria.setIndex(index);

                int count;
                Collection<Cellink> cellinks;
                switch (user.getRole()) {
                    default:
                    case USER:
                        logger.info("retrieve all cellinks that belongs to  " + user);
                        cellinks = cellinkDao.getAll(criteria);
                        for (Cellink cellink : cellinks) {
                            Collection<Controller> controllers = controllerDao.getAllByCellink(cellink.getId());
                            cellink.setControllers(controllers);
                        }
                        count = cellinkDao.count(criteria);
                        break;
                    case DISTRIBUTOR:
                        logger.info("retrieve all cellinks that belongs to distributor " + user);
                        cellinks = cellinkDao.getAll(criteria);
                        for (Cellink cellink : cellinks) {
                            Collection<Controller> controllers = controllerDao.getAllByCellink(cellink.getId());
                            cellink.setControllers(controllers);
                        }
                        count = cellinkDao.count(criteria);
                        break;

                    case ADMIN:
                        cellinks = cellinkDao.getAll(criteria);
                        for (Cellink cellink : cellinks) {
                            Collection<Controller> controllers = controllerDao.getAllByCellink(cellink.getId());
                            cellink.setControllers(controllers);
                        }
                        count = cellinkDao.count(criteria);
                        break;
                }
                request.getSession().setAttribute("cellinks", cellinks);
                setTableParameters(request, index, count);
                response.sendRedirect("./my-farms.jsp");
            } catch (SQLException ex) {
                // error page
                logger.error("Error during sql query running ", ex);
            } finally {
                out.close();
            }
        }
    }

    private void setTableParameters(HttpServletRequest request, Integer index, int count) {
        if (index == null) {
            index = 0;
        }

        if (index.equals(0)) {
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
            int from = index;
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



