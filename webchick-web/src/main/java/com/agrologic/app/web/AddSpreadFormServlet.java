
package com.agrologic.app.web;


import com.agrologic.app.dao.DaoType;
import com.agrologic.app.dao.DbImplDecider;
import com.agrologic.app.dao.FlockDao;
import com.agrologic.app.dao.SpreadDao;
import com.agrologic.app.model.Flock;
import com.agrologic.app.model.Spread;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.List;

public class AddSpreadFormServlet extends AbstractServlet {


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
            String amount = request.getParameter("amount");
            String date = request.getParameter("startDate");
            String price = request.getParameter("price");
            String numberAccount = request.getParameter("numberAccount");
            String total = request.getParameter("total");

            try {
                SpreadDao spreadDao = DbImplDecider.use(DaoType.MYSQL).getDao(SpreadDao.class);
                Spread spread = new Spread();

                spread.setFlockId(flockId);
                spread.setAmount(Integer.parseInt(amount));
                spread.setDate(date);
                spread.setPrice(Float.parseFloat(price));
                spread.setNumberAccount(Integer.parseInt(numberAccount));
                spread.setTotal(Float.parseFloat(total));
                spreadDao.insert(spread);

                FlockDao flockDao = DbImplDecider.use(DaoType.MYSQL).getDao(FlockDao.class);
                Flock flock = flockDao.getById(flockId);
                List<Spread> spreadList = spreadDao.getAllByFlockId(flockId);
                int addSpreadAmount = 0;
                float addSpreadSum = 0;

                for (Spread s : spreadList) {
                    addSpreadAmount += s.getAmount();
                    addSpreadSum += s.getTotal();
                }

                flock.setSpreadAdd(addSpreadAmount);
                flock.setTotalSpread(addSpreadSum);
                flockDao.updateFlockDetail(flock);
                logger.info("Spread added successfully to the datebase");
                request.getRequestDispatcher("./rmctrl-add-spread.jsp?celinkId=" + cellinkId + "&controllerId="
                        + controllerId + "&flockId=" + flockId).forward(request, response);
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



