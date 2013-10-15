package com.agrologic.app.web;

import com.agrologic.app.dao.DaoType;
import com.agrologic.app.dao.DbImplDecider;
import com.agrologic.app.dao.UserDao;
import com.agrologic.app.model.UserRole;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.general.DefaultPieDataset;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.SQLException;


public class TotalUserPieChart extends AbstractServlet {


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
        OutputStream out = response.getOutputStream();

        try {
            if (!CheckUserInSession.isUserInSession(request)) {
                logger.error("Unauthorized access!");
                response.sendRedirect("./login.jsp");
            } else {
                try {
                    DefaultPieDataset dataset = new DefaultPieDataset();
                    JFreeChart jfc;
                    UserDao userDao = DbImplDecider.use(DaoType.MYSQL).getDao(UserDao.class);
                    final int admins = userDao.getAllByRole(UserRole.ADMIN.getValue()).size();
                    final int regular = userDao.getAllByRole(UserRole.USER.getValue()).size();

                    dataset = new DefaultPieDataset();
                    dataset.setValue("Administrator", new Integer(admins));
                    dataset.setValue("Regular", new Integer(regular));
                    jfc = ChartFactory.createPieChart3D("Total Users Summary", dataset, true, false, false);

                    PiePlot pp = (PiePlot) jfc.getPlot();

                    pp.setSectionOutlinesVisible(false);
                    pp.setLabelFont(new Font("SansSerif", Font.PLAIN, 12));
                    pp.setCircular(false);
                    pp.setIgnoreNullValues(false);
                    pp.setIgnoreZeroValues(true);
                    pp.setInteriorGap(0.05D);
                    pp.setSimpleLabels(true);

                    // disable caching
                    response.setHeader("Cache-Control", "no-cache");
                    response.setHeader("Pragma", "no-cache");
                    response.setDateHeader("Expires", 0);
                    response.setContentType("image/png");
                    ChartUtilities.writeChartAsPNG(out, jfc, 300, 200);
                    ChartUtilities.saveChartAsJPEG(new File("C:/chart.jpg"), jfc, 300, 200);
                    out.flush();
                } catch (SQLException ex) {
                    logger.error(ex.getMessage());
                }
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



