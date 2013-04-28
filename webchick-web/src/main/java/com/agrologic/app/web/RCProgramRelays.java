
/*
* To change this template, choose Tools | Templates
* and open the template in the editor.
 */
package com.agrologic.app.web;



//~--- JDK imports ------------------------------------------------------------

import java.io.IOException;
import java.io.PrintWriter;

        import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Administrator
 */
public class RCProgramRelays extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        PrintWriter out = response.getWriter();

        try {

            /*
             *  TODO output your page here
             * out.println("<html>");
             * out.println("<head>");
             * out.println("<title>Servlet RCProgramRelays</title>");
             * out.println("</head>");
             * out.println("<body>");
             * out.println("<h1>Servlet RCProgramRelays at " + request.getContextPath () + "</h1>");
             * out.println("</body>");
             * out.println("</html>");
             */

//          Long controllerId = Long.parseLong(request.getParameter("controllerId"));
//          ControllerDao controllerDao = new ControllerDaoImpl();
//
//          ControllerDto controller = controllerDao.getById(controllerId);
//
//          List<ProgramRelayDto> programRelays = controller.getProgram().getProgramRelays();
//          List<ProgramRelayDto> relayList = getProgramRelaysByRelayType(programRelays, data.getId());
//          if (relayList.size() > 0) {
//              for (ProgramRelayDto relay : relayList) {
//                  if (relay.getRelayNumber() != 0) {
//                      <tr><td class="tableHeaders" nowrap>=relay.getText() </td>
//                          relay.init(data.getValue());
//
//
//
//
//                          if(relay.getText ()
//                              .startsWith("Fan")) {
//                                                           if (relay.isOn()) {
//                                  <td>  < img src = "img/fan-on.gif" > <  / td >
//
//
//                              } else {
//                                  <td>  < img src = "img/fan-off.gif" > <  / td >
//
//
//                              }
//                          }
//
//
//                          else if(relay.getText ()
//                              .startsWith("Light")) {
//                                                           if (relay.isOn()) {
//                                  <td>  < img src = "img/light-on.png" > <  / td >
//
//
//                              } else {
//                                  <td>  < img src = "img/light-off.png" > <  / td >
//
//
//                              }
//                          }
//
//
//
//                              else {
//                                                           if (relay.isOn()) {
//                                  <td
//
//                                  class
//
//                                  ="greenValues">=session.getAttribute("relay.on")</td>
//                                                          }   else {
//                                  <td
//
//                                  class
//
//
//                              ="redValues">=session.getAttribute("relay.off")</td>
//                                                          }
//                                                      }
//                                                  <  / tr >
//
//
//                          }
//                      }
//                  }
//
//
//              }  catch (SQLException ex) {
//          Logger.getLogger(RCProgramRelays.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            out.close();
        }
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



