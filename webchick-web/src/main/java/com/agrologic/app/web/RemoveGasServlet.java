package com.agrologic.app.web;

import com.agrologic.app.dao.*;
import com.agrologic.app.model.Controller;
import com.agrologic.app.model.Flock;
import com.agrologic.app.model.Gas;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;


public class RemoveGasServlet extends AbstractServlet {

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

        try {
            Long cellinkId = Long.parseLong(request.getParameter("cellinkId"));
            Long flockId = Long.parseLong(request.getParameter("flockId"));
            Long gazId = Long.parseLong(request.getParameter("gazId"));

            try {
                GasDao gazDao = DbImplDecider.use(DaoType.MYSQL).getDao(GasDao.class);
                Gas gaz = gazDao.getById(gazId);

                if (gaz == null) {
                    logger.info("Gas " + gazId + " can't be removed");
                    request.getRequestDispatcher("./rmctrl-add-gas.jsp?celinkId=" + cellinkId + "&flockId="
                            + flockId).forward(request, response);
                } else {
                    gazDao.remove(gaz.getId());
                    logger.info("Gas removed successfully from the datebase");

                    FlockDao flockDao = DbImplDecider.use(DaoType.MYSQL).getDao(FlockDao.class);
                    Flock flock = flockDao.getById(flockId);
                    List<Gas> gazList = gazDao.getAllByFlockId(flockId);
                    int gazAmount = 0;
                    float gazTotalCost = 0;

                    for (Gas g : gazList) {
                        gazAmount += g.getAmount();
                        gazTotalCost += g.getTotal();
                    }

                    flock.setGasAdd(gazAmount);
                    flock.setTotalGas(gazTotalCost);
                    flockDao.update(flock);

                    ControllerDao controllerDao = DbImplDecider.use(DaoType.MYSQL).getDao(ControllerDao.class);
                    Collection<Controller> controllers = controllerDao.getAllByCellink(cellinkId);

                    for (Controller controller : controllers) {
                        Collection<Flock> flocks = flockDao.getAllFlocksByController(controller.getId());
                        controller.setFlocks(flocks);
                    }

                    request.setAttribute("controllers", controllers);
                    request.getRequestDispatcher("./rmctrl-add-gas.jsp?celinkId=" + cellinkId + "&flockId="
                            + flockId).forward(request, response);
                }
            } catch (SQLException ex) {
                logger.info("Error occurs durring removing gaz");
                request.getRequestDispatcher("./rmctrl-add-gas.jsp?celinkId=" + cellinkId + "&flockId="
                        + flockId).forward(request, response);
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



