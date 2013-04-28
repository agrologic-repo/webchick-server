
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

import java.util.Iterator;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author JanL
 */
public class RCChangeValuePopupServlet extends HttpServlet {

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
        final Logger logger = Logger.getLogger(RCChangeValuePopupServlet.class);

        response.setContentType("text/html;charset=UTF-8");

        PrintWriter out = response.getWriter();

        try {
            long           controllerId  = Long.parseLong(request.getParameter("controllerId"));
            long           dataId        = Long.parseLong(request.getParameter("dataId"));
            ControllerDao controllerDao = new ControllerDaoImpl();
            ControllerDto  controller    = controllerDao.getById(controllerId);
            DataDao       dataDao       = new DataDaoImpl();
            DataDto        data          = dataDao.getById(dataId);

            request.getSession().setAttribute("controller", controller);
            request.getSession().setAttribute("data", data);

            String       url    = request.getRequestURL().toString();
            StringBuffer sb     = new StringBuffer();
            Map          params = (Map) request.getParameterMap();

            if (!params.isEmpty()) {
                Iterator it = params.keySet().iterator();

                while (it.hasNext()) {
                    String key = (String) it.next();

                    if (key.equals("lang")) {

                        // skip parameter lang to avoid double occurance
                        continue;
                    } else {
                        String[] values      = (String[]) params.get(key);
                        String   valueString = values[0];

                        sb.append("&").append(key).append("=").append(valueString);
                    }
                }
            }

            String paramString = sb.toString();

            // "<!DOCTYPE html PUBLIC '"'-//W3C//DTD XHTML 1.0 Strict//EN'"' '"'http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd'"'>"
            out.println("<!DOCTYPE html PUBLIC " + "-//W3C//DTD XHTML 1.0 Strict//EN"
                        + "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd" + ">");
            out.println("<html>");
            out.println("<head>");
            out.println("<script language='javascript' type='text/javascript'>");
            out.println("function changeValue(){");
            out.println("   window.history.back();");
            out.println(
                "   var newWin = window.open('rmctrl-edit-value.jsp?controllerId=1&dataId=0&cellinkId=1','mywindow','status=no,width=300,height=200,left=350,top=400,screenX=100,screenY=100'); // params and stuff");
            out.println("}");

            // out.println("window.onload = changeValue; //this should work but often doesn't. Lack of parens () is correct.");
            out.println("</script>");
            out.println("</head>");
            out.println("<body onload='changeValue();'>");

//          out.println("<body onload=window.open('rmctrl-edit-value.jsp?controllerId=1&dataId=1&cellinkId=1','mywindow','status=no,width=300,height=200,left=350,top=400,screenX=100,screenY=100')>");                out.println("<br>");
            out.println("<h2> Changing value ... </h2>");
            out.println("</body>");
            out.println("</html>");
        } catch (SQLException ex) {

            // error page
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



