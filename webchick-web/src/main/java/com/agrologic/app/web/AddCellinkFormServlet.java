package com.agrologic.app.web;

import com.agrologic.app.dao.CellinkDao;
import com.agrologic.app.dao.ControllerDao;
import com.agrologic.app.dao.UserDao;
import com.agrologic.app.dao.impl.CellinkDaoImpl;
import com.agrologic.app.dao.impl.ControllerDaoImpl;
import com.agrologic.app.dao.impl.UserDaoImpl;
import com.agrologic.app.model.CellinkDto;
import com.agrologic.app.model.ControllerDto;
import com.agrologic.app.model.UserDto;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class AddCellinkFormServlet extends HttpServlet {

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
        final Logger logger = Logger.getLogger(AddCellinkFormServlet.class);

        response.setContentType("text/html;charset=UTF-8");

        PrintWriter out = response.getWriter();

        if (!CheckUserInSession.isUserInSession(request)) {
            logger.error("Unauthorized access!");
            request.getRequestDispatcher("./login.jsp").forward(request, response);
        } else {
            final Long userId = Long.parseLong(request.getParameter("Nuserid"));
            final String cellinkName = request.getParameter("Ncellinkname");
            final String password = request.getParameter("Npassword");
            final String simNumber = request.getParameter("Nsim");
            final String Ntype = request.getParameter("Ntype");
            final CellinkDto cellink = new CellinkDto();

            cellink.setUserId(userId);
            cellink.setName(cellinkName);
            cellink.setPassword(password);
            cellink.setType(Ntype);
            cellink.setIp("");
            cellink.setPort((int) 0);
            cellink.setState((int) 0);
            cellink.setScreenId((long) 1);
            cellink.setSimNumber(simNumber);
            cellink.setActual(true);
            cellink.setTime(new Timestamp(System.currentTimeMillis()));

            try {
                UserDao userDao = new UserDaoImpl();
                CellinkDao cellinkDao = new CellinkDaoImpl();
                ControllerDao controllerDao = new ControllerDaoImpl();

                cellinkDao.insert(cellink);
                logger.info("Cellink " + cellink + " successfully added !");
                request.getSession().setAttribute("message", "Cellink successfully added !");
                request.getSession().setAttribute("error", false);

                Collection<UserDto> users = new ArrayList<UserDto>();
                String paramRole = request.getParameter("role");

                if ((paramRole == null) || "3".equals(paramRole)) {
                    users = userDao.getAll();
                    paramRole = "3";
                } else {
                    int role = Integer.parseInt(paramRole);

                    users = userDao.getAllByRole(role);
                }

                for (UserDto u : users) {
                    List<CellinkDto> cellinks = (List<CellinkDto>) cellinkDao.getAllUserCellinks(u.getId());

                    for (CellinkDto c : cellinks) {
                        List<ControllerDto> controllers = controllerDao.getAllByCellinkId(c.getId());

                        c.setControllers(controllers);
                    }

                    u.setCellinks(cellinks);
                }

                logger.info("retrieve all users ");
                request.getSession().setAttribute("users", users);
                request.getRequestDispatcher("./userinfo.html?userId=" + userId).forward(request, response);
            } catch (SQLException ex) {

                // error page
                logger.error("Error occurs while adding cellink !", ex);
                request.getSession().setAttribute("message", "Error occurs while adding cellink !");
                request.getSession().setAttribute("error", true);
                request.getRequestDispatcher("./userinfo.html?userId=" + userId).forward(request, response);
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



