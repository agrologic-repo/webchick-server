package com.agrologic.app.web;

import com.agrologic.app.dao.*;
import com.agrologic.app.dao.mysql.impl.FuelDaoImpl;
import com.agrologic.app.model.Controller;
import com.agrologic.app.model.Flock;
import com.agrologic.app.model.Fuel;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

public class AddFuelFormServlet extends AbstractServlet {


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
            String amount = request.getParameter("amount");
            String date = request.getParameter("startDate");
            String price = request.getParameter("price");
            String[] currency = request.getParameterValues("currency");
            String numberAccount = request.getParameter("numberAccount");
            String total = request.getParameter("total");

            try {
                FuelDao fuelDao = DbImplDecider.use(DaoType.MYSQL).getDao(FuelDaoImpl.class);
                Fuel fuel = new Fuel();
                fuel.setFlockId(flockId);
                fuel.setAmount(Integer.parseInt(amount));
                fuel.setDate(date);
                fuel.setPrice(Float.parseFloat(price));
                fuel.setNumberAccount(Integer.parseInt(numberAccount));
                fuel.setTotal(Float.parseFloat(total));
                fuelDao.insert(fuel);
                logger.info("Fuel added successfully to the datebase");

                FlockDao flockDao = DbImplDecider.use(DaoType.MYSQL).getDao(FlockDao.class);
                Flock flock = flockDao.getById(flockId);
                List<Fuel> fuelList = fuelDao.getAllByFlockId(flockId);
                int fuelAmount = 0;
                float fuelTotalCost = 0;

                for (Fuel g : fuelList) {
                    fuelAmount += g.getAmount();
                    fuelTotalCost += g.getTotal();
                }

                flock.setFuelAdd(fuelAmount);
                flock.setTotalFuel(fuelTotalCost);
                flockDao.update(flock);

                ControllerDao controllerDao = DbImplDecider.use(DaoType.MYSQL).getDao(ControllerDao.class);
                Collection<Controller> controllers = controllerDao.getAllByCellink(cellinkId);

                for (Controller controller : controllers) {
                    Collection<Flock> flocks = flockDao.getAllFlocksByController(controller.getId());
                    controller.setFlocks(flocks);
                }

                request.setAttribute("controllers", controllers);
                request.getRequestDispatcher("./rmctrl-add-fuel.jsp?celinkId=" + cellinkId + "&flockId="
                        + flockId).forward(request, response);
            } catch (SQLException ex) {
                ex.printStackTrace();
            } catch (Exception ex) {
                ex.printStackTrace();
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



