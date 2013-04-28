
/*
* To change this template, choose Tools | Templates
* and open the template in the editor.
 */
package com.agrologic.app.web;



import com.agrologic.app.dao.ControllerDao;
import com.agrologic.app.dao.DataDao;
import com.agrologic.app.dao.impl.ControllerDaoImpl;
import com.agrologic.app.dao.impl.DataDaoImpl;
import com.agrologic.app.model.ControllerDto;
import com.agrologic.app.model.DataDto;

import org.apache.log4j.Logger;

//~--- JDK imports ------------------------------------------------------------

import java.io.IOException;
import java.io.PrintWriter;

import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Administrator
 */
public class RCChangeValue extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        /** Logger for this class and subclasses */
        final Logger logger = Logger.getLogger(RCDataValueChangeServlet.class);

        response.setContentType("text/html;charset=UTF-8");

        PrintWriter out       = response.getWriter();
        long        userId    = Long.parseLong(request.getParameter("userId"));
        long        cellinkId = Long.parseLong(request.getParameter("cellinkId"));

        try {
            String info = request.getPathInfo();

            logger.debug(info);

            long controllerId = Long.parseLong(request.getParameter("controllerId"));
            long screenId     = 1;

            try {
                screenId = Long.parseLong(request.getParameter("screenId"));
            } catch (Exception e) {
                screenId = 1;
            }

            long   dataId = Long.parseLong(request.getParameter("dataId"));
            String svalue = request.getParameter("Nvalue");
            Long   value  = null;

            if ((svalue != null) &&!svalue.equals("")) {
                svalue = clearDots(svalue);
                value  = Long.parseLong(svalue);

                ControllerDao controllerDao = new ControllerDaoImpl();
                ControllerDto  controller    = controllerDao.getById(controllerId);
                DataDao       dataDao       = new DataDaoImpl();
                DataDto        data          = dataDao.getById(dataId);

                data.setValueToChange(value);
                controllerDao.sendNewDataValueToController(controller.getId(), data.getId(), data.getValue());
                controllerDao.saveNewDataValueOnController(controller.getId(), data.getId(), data.getValue());
                logger.info("Data successfully changed :" + data);

                if (screenId == 1) {
                    response.sendRedirect("./rmctrl-main-screen-ajax.jsp?userId=" + userId + "&cellinkId=" + cellinkId);
                } else {
                    response.sendRedirect("./rmctrl-controller-screens-ajax.jsp?userId=" + userId + "&cellinkId="
                                          + cellinkId + "&controllerId=" + controllerId + "&screenId=" + screenId
                                          + "&doResetTimeout=true");
                }
            }
        } catch (SQLException ex) {

            // error page
            logger.info("Error occurs while changing data", ex);
            response.sendRedirect("./rmctrl-main-screen-ajax.jsp?userId=" + userId + "&cellinkId=" + cellinkId);
        } finally {
            out.close();
        }
    }

    public String clearDots(String s) {
        String str = s.replace(".", "");

        str = str.replace(":", "");
        str = str.replace("/", "");

        return str;
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">

    /**
     * Handles the HTTP <code>GET</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }    // </editor-fold>
}



