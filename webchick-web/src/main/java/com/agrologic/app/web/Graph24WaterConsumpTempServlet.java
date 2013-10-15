package com.agrologic.app.web;

import com.agrologic.app.dao.ControllerDao;
import com.agrologic.app.dao.DaoType;
import com.agrologic.app.dao.DataDao;
import com.agrologic.app.dao.DbImplDecider;
import com.agrologic.app.graph.daily.Graph;
import com.agrologic.app.graph.daily.Graph24FWI;
import com.agrologic.app.graph.daily.GraphType;
import com.agrologic.app.model.Data;
import org.jfree.chart.ChartRenderingInfo;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.entity.StandardEntityCollection;
import org.jfree.chart.servlet.ServletUtilities;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class Graph24WaterConsumpTempServlet extends AbstractServlet {

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
        PrintWriter pw = new PrintWriter(response.getOutputStream());
        long controllerId = Long.parseLong(request.getParameter("controllerId"));

        try {
            ControllerDao controllerDao = DbImplDecider.use(DaoType.MYSQL).getDao(ControllerDao.class);
            String values = controllerDao.getControllerGraph(controllerId);
            DataDao dataDao = DbImplDecider.use(DaoType.MYSQL).getDao(DataDao.class);
            Data setClock = dataDao.getSetClockByController(controllerId);

            if (values == null) {
                throw new Exception("No data available for this type of graphs");
            } else {
                Graph graph = null;

                if (setClock.getValue() == null) {
                    graph = new Graph24FWI(GraphType.IN_OUT_TEMP_HUMID, values, setClock.getValue());
                } else {
                    graph = new Graph24FWI(GraphType.IN_OUT_TEMP_HUMID, values, setClock.getValue());
                }

                // Write the chart image to the temporary directory
                ChartRenderingInfo info = new ChartRenderingInfo(new StandardEntityCollection());
                String filename = ServletUtilities.saveChartAsPNG(graph.createChart(), 800, 600, info, request.getSession());

                // Write the image map to the PrintWriter
                ChartUtilities.writeImageMap(pw, filename, info, false);
                request.setAttribute("filenamewct", filename);
                pw.flush();
            }
        } catch (Exception e) {
            e.printStackTrace(System.out);
            ChartUtilities.writeImageMap(pw, "public_error_500x300.png", null, false);
            request.setAttribute("filenamewct", "public_error_500x300.png");
            pw.flush();
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



