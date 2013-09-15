
/*
* To change this template, choose Tools | Templates
* and open the template in the editor.
 */
package com.agrologic.app.web;


//~--- JDK imports ------------------------------------------------------------

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class SaveFlockFormServlet extends HttpServlet {

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

//      /** Logger for this class and subclasses */
//      final Logger logger = Logger.getLogger(SaveFlockFormServlet.class);
//      logger.error("Unauthorized access!");
//      response.setContentType("text/html;charset=UTF-8");
//      PrintWriter out = response.getWriter();
//      try {
//          if (!CheckUserInSession.isUserInSession(request)) {
//              logger.error("Unauthorized access!");
//              request.getRequestDispatcher("./login.jsp").forward(request, response);
//          } else {
//              Long userId = Long.parseLong(request.getParameter("userId"));
//              Long cellinkId = Long.parseLong(request.getParameter("cellinkId"));
//              Long flockId = Long.parseLong(request.getParameter("flockId"));
//
//              int quantityMale = Integer.parseInt(request.getParameter("NquantMale"));
//              float malePrice = Float.parseFloat(request.getParameter("NmalePrice"));
//              int quantityFemale = Integer.parseInt(request.getParameter("NquantFemale"));
//              float femalePrice = Float.parseFloat(request.getParameter("NfemalePrice"));
//              int totChicks = quantityMale+quantityFemale;
//              float totChicksPrice = (malePrice*quantityMale)+(femalePrice*quantityFemale);
//
//              int waterBegin = Integer.parseInt(request.getParameter("NwaterStart"));
//              int waterEnd = Integer.parseInt(request.getParameter("NwaterBegin"));
//              float waterPrice = Float.parseFloat(request.getParameter("NwaterPrice"));
//              int totWater = waterBegin-waterEnd;
//              float totWaterPrice = totWater*waterPrice;
//
//              int electBegin = Integer.parseInt(request.getParameter("NelectBegin"));
//              int electEnd = Integer.parseInt(request.getParameter("NelectEnd"));
//              float electPrice = Float.parseFloat(request.getParameter("NelectPrice"));
//              int totElect = electBegin - electEnd;
//              float totElectPrice = totElect * electPrice;
//
//
//              try {
//                  FlockDao flockDao = DbImplDecider.use(DaoType.MYSQL).getDao(FlockDao.class);
//                   Flock flock = flockDao.getById(flockId);
//                  flock.setQuantityMale(quantityMale);
//                  flock.setQuantityFemale(quantityFemale);
//                  flock.setPriceChickFemale(malePrice);
//                  flock.setPriceChickFemale(femalePrice);
//                  flock.setTotalChicks(totChicks);
//
//                  flock.setWaterBegin(waterBegin);
//                  flock.setWaterEnd(waterEnd);
//                  flock.setPriceWater(waterPrice);
//                  flock.setTotalWater(totWater);
//
//
//                  flock.setElectBegin(electBegin);
//                  flock.setElectEnd(electEnd);
//                  flock.setPriceElect(electPrice);
//                  flock.setTotalElect(totElect);
//
//                  flockDao.update(flock);
//                  logger.info("Flock "+ flock +"successfully updated !");
//                  request.getSession().setAttribute("message", "Flock successfully updated !");
//                  request.getSession().setAttribute("error", false);
//                  request.getRequestDispatcher("./rmctrl-edit-flock.jsp?userId"+userId + "&cellinkId"+cellinkId).forward(request, response);
//              } catch (SQLException ex) {
//                  logger.error("Error occurs while updating flock !");
//                  request.getSession().setAttribute("message", "Error during while updating flock !");
//                  request.getSession().setAttribute("error", true);
//                  request.getRequestDispatcher("./rmctrl-edit-flock.jsp?userId"+userId + "&cellinkId"+cellinkId).forward(request, response);
//              }
//          }
//      } finally {
//          out.close();
//      }
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



