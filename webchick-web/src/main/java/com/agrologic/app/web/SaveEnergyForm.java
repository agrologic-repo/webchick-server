
/*
* To change this template, choose Tools | Templates
* and open the template in the editor.
 */
package com.agrologic.app.web;


import com.agrologic.app.dao.ControllerDao;
import com.agrologic.app.dao.FlockDao;
import com.agrologic.app.dao.FuelDao;
import com.agrologic.app.dao.GasDao;
import com.agrologic.app.dao.impl.ControllerDaoImpl;
import com.agrologic.app.dao.impl.FlockDaoImpl;
import com.agrologic.app.dao.impl.FuelDaoImpl;
import com.agrologic.app.dao.impl.GasDaoImpl;
import com.agrologic.app.model.ControllerDto;
import com.agrologic.app.model.FlockDto;
import com.agrologic.app.model.FuelDto;
import com.agrologic.app.model.GasDto;
import org.apache.log4j.Logger;

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
public class SaveEnergyForm extends HttpServlet {

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
        final Logger logger = Logger.getLogger(SaveEnergyForm.class);

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
                FlockDao flockDao = new FlockDaoImpl();
                FlockDto flock = flockDao.getById(flockId);

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

                ControllerDao controllerDao = new ControllerDaoImpl();
                List<ControllerDto> controllers = controllerDao.getAllByCellinkId(cellinkId);
                for (ControllerDto controller : controllers) {
                    List<FlockDto> flocks = flockDao.getAllFlocksByController(controller.getId());
                    controller.setFlocks(flocks);
                }
                logger.info("retrieve user and user cellinks and all controllers of each cellink");
                request.getSession().setAttribute("controllers", controllers);
                request.getRequestDispatcher("./rmctrl-flock-management.jsp?celinkId=" + cellinkId + "&controllerId="
                        + controllerId + "&flockId=" + flockId).forward(request, response);
            } catch (SQLException ex) {
            }
        } finally {
            out.close();
        }
    }

    public void sumAddGas(FlockDto flock) throws SQLException {
        GasDao gazDao = new GasDaoImpl();
        List<GasDto> gazList = gazDao.getAllByFlockId(flock.getFlockId());
        int gazAmount = 0;
        float gazTotalCost = 0;

        for (GasDto g : gazList) {
            gazAmount += g.getAmount();
            gazTotalCost += g.getTotal();
        }

        flock.setGasAdd(gazAmount);
        flock.setTotalGas(gazTotalCost);
    }

    public void sumAddFuel(FlockDto flock) throws SQLException {
        FuelDao fuelDao = new FuelDaoImpl();
        List<FuelDto> fuelList = fuelDao.getAllByFlockId(flock.getFlockId());
        int fuelAmount = 0;
        float fuelTotalCost = 0;

        for (FuelDto g : fuelList) {
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



