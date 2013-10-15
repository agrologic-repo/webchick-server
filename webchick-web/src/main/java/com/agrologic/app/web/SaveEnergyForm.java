package com.agrologic.app.web;

import com.agrologic.app.dao.*;
import com.agrologic.app.dao.mysql.impl.FuelDaoImpl;
import com.agrologic.app.dao.mysql.impl.GasDaoImpl;
import com.agrologic.app.model.Controller;
import com.agrologic.app.model.Flock;
import com.agrologic.app.model.Fuel;
import com.agrologic.app.model.Gas;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

public class SaveEnergyForm extends AbstractServlet {

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
            String beginGas = request.getParameter("beginGas");
            String beginCostGas = request.getParameter("beginCostGas");
            String totalGas = request.getParameter("totalGas");
            String beginFuel = request.getParameter("beginFuel");
            String beginCostFuel = request.getParameter("beginCostFuel");
            String totalFuel = request.getParameter("totalFuel");
            String endGas = request.getParameter("endGas");
            String endCostGas = request.getParameter("endCostGas");
            String totalCostGas = request.getParameter("totalGas");
            String endFuel = request.getParameter("endFuel");
            String endCostFuel = request.getParameter("endCostFuel");
            String totalCostFuel = request.getParameter("totalFuel");

            try {
                FlockDao flockDao = DbImplDecider.use(DaoType.MYSQL).getDao(FlockDao.class);
                Flock flock = flockDao.getById(flockId);

                flock.setGasBegin(Integer.parseInt(beginGas));
                flock.setGasEnd(Integer.parseInt(endGas));
                flock.setCostGas(Float.parseFloat(beginCostGas));
                flock.setCostGasEnd(Float.parseFloat(endCostGas));
                sumAddGas(flock);
                flock.setTotalGas(flock.calcTotalGasCost());
                flock.setFuelBegin(Integer.parseInt(beginFuel));
                flock.setFuelEnd(Integer.parseInt(endFuel));
                flock.setCostFuel(Float.parseFloat(beginCostFuel));
                flock.setCostFuelEnd(Float.parseFloat(endCostFuel));
                sumAddFuel(flock);
                flock.setTotalFuel(flock.calcTotalFuelCost());
                flockDao.update(flock);

                ControllerDao controllerDao = DbImplDecider.use(DaoType.MYSQL).getDao(ControllerDao.class);
                Collection<Controller> controllers = controllerDao.getAllByCellink(cellinkId);
                for (Controller controller : controllers) {
                    Collection<Flock> flocks = flockDao.getAllFlocksByController(controller.getId());
                    controller.setFlocks(flocks);
                }
                logger.info("retrieve user and user cellinks and all controllers of each cellink");
                request.setAttribute("controllers", controllers);
                request.getRequestDispatcher("./rmctrl-flock-management.jsp?celinkId=" + cellinkId + "&controllerId="
                        + controllerId + "&flockId=" + flockId).forward(request, response);
            } catch (SQLException ex) {
            }
        } finally {
            out.close();
        }
    }

    public void sumAddGas(Flock flock) throws SQLException {
        GasDao gazDao = DbImplDecider.use(DaoType.MYSQL).getDao(GasDaoImpl.class);
        List<Gas> gazList = gazDao.getAllByFlockId(flock.getFlockId());
        int gazAmount = 0;
        float gazTotalCost = 0;

        for (Gas g : gazList) {
            gazAmount += g.getAmount();
            gazTotalCost += g.getTotal();
        }

        flock.setGasAdd(gazAmount);
        flock.setTotalGas(gazTotalCost);
    }

    public void sumAddFuel(Flock flock) throws SQLException {
        FuelDao fuelDao = DbImplDecider.use(DaoType.MYSQL).getDao(FuelDaoImpl.class);
        List<Fuel> fuelList = fuelDao.getAllByFlockId(flock.getFlockId());
        int fuelAmount = 0;
        float fuelTotalCost = 0;

        for (Fuel g : fuelList) {
            fuelAmount += g.getAmount();
            fuelTotalCost += g.getTotal();
        }

        flock.setFuelAdd(fuelAmount);
        flock.setTotalFuel(fuelTotalCost);
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



