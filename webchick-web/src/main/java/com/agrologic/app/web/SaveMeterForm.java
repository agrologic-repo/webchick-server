package com.agrologic.app.web;

import com.agrologic.app.dao.ControllerDao;
import com.agrologic.app.dao.DaoType;
import com.agrologic.app.dao.DbImplDecider;
import com.agrologic.app.dao.FlockDao;
import com.agrologic.app.model.Controller;
import com.agrologic.app.model.Flock;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;

public class SaveMeterForm extends AbstractServlet {

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
            Long controllerId = Long.parseLong(request.getParameter("controllerId"));
            Long flockId = Long.parseLong(request.getParameter("flockId"));
            String startElectMeter = request.getParameter("startElectMeter");
            String endElectMeter = request.getParameter("endElectMeter");
            String priceElectMeter = request.getParameter("priceElectMeter");
            String startWaterMeter = request.getParameter("startWaterMeter");
            String endWaterMeter = request.getParameter("endWaterMeter");
            String priceWaterMeter = request.getParameter("priceWaterMeter");

            try {
                FlockDao flockDao = DbImplDecider.use(DaoType.MYSQL).getDao(FlockDao.class);
                Flock flock = flockDao.getById(flockId);
                flock.setElectBegin(Integer.parseInt(startElectMeter));
                flock.setElectEnd(Integer.parseInt(endElectMeter));
                flock.setCostElect(Float.parseFloat(priceElectMeter));
                flock.setQuantityElect(Integer.parseInt(endElectMeter) - Integer.parseInt(startElectMeter));
                flock.setTotalElect(flock.calcTotalElectCost());
                flock.setWaterBegin(Integer.parseInt(startWaterMeter));
                flock.setWaterEnd(Integer.parseInt(endWaterMeter));
                flock.setCostWater(Float.parseFloat(priceWaterMeter));
                flock.setQuantityWater(Integer.parseInt(endWaterMeter) - Integer.parseInt(startWaterMeter));
                flock.setTotalWater(flock.calcTotalWaterCost());
                flockDao.updateFlockDetail(flock);

                ControllerDao controllerDao = DbImplDecider.use(DaoType.MYSQL).getDao(ControllerDao.class);
                Collection<Controller> controllers = controllerDao.getAllByCellink(cellinkId);
                for (Controller controller : controllers) {
                    Collection<Flock> flocks = flockDao.getAllFlocksByController(controller.getId());
                    controller.setFlocks(flocks);
                }
                logger.info("retrieve user and user cellinks and all controllers of each cellink");
                request.setAttribute("controllers", controllers);
                request.getRequestDispatcher("./flock-manager.html?celinkId=" + cellinkId + "&controllerId="
                        + controllerId + "&flockId=" + flockId).forward(request, response);
            } catch (Exception ex) {
                request.getRequestDispatcher("./flock-manager.html?celinkId=" + cellinkId + "&controllerId="
                        + controllerId + "&flockId=" + flockId).forward(request, response);
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



