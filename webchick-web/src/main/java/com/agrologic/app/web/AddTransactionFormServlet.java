package com.agrologic.app.web;

import com.agrologic.app.dao.*;
import com.agrologic.app.dao.mysql.impl.TransactionDaoImpl;
import com.agrologic.app.model.Controller;
import com.agrologic.app.model.Flock;
import com.agrologic.app.model.Transaction;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

public class AddTransactionFormServlet extends AbstractServlet {


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
            String name = request.getParameter("name");
            String expenses = request.getParameter("expenses");
            String revenues = request.getParameter("revenues");

            try {
                TransactionDao transactDao = DbImplDecider.use(DaoType.MYSQL).getDao(TransactionDaoImpl.class);
                Transaction transaction = new Transaction();
                transaction.setFlockId(flockId);
                transaction.setName(name);
                transaction.setExpenses(Float.parseFloat(expenses));
                transaction.setRevenues(Float.parseFloat(revenues));
                transactDao.insert(transaction);

                FlockDao flockDao = DbImplDecider.use(DaoType.MYSQL).getDao(FlockDao.class);
                Flock flock = flockDao.getById(flockId);
                List<Transaction> transactList = transactDao.getAllByFlockId(flockId);
                float exp = 0;
                float rev = 0;

                for (Transaction t : transactList) {
                    exp += t.getExpenses();
                    rev += t.getRevenues();
                }

                flock.setExpenses(exp);
                flock.setRevenues(rev);
                flockDao.update(flock);
                logger.info("Feed added successfully to the database");

                ControllerDao controllerDao = DbImplDecider.use(DaoType.MYSQL).getDao(ControllerDao.class);
                Collection<Controller> controllers = controllerDao.getAllByCellink(cellinkId);

                for (Controller controller : controllers) {
                    Collection<Flock> flocks = flockDao.getAllFlocksByController(controller.getId());
                    controller.setFlocks(flocks);
                }

                request.setAttribute("controllers", controllers);
                logger.info("Transaction added successfully to the datebase");
                request.getRequestDispatcher("./rmctrl-add-transaction.jsp?celinkId=" + cellinkId + "&controllerId="
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



