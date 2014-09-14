package com.agrologic.app.web;

import com.agrologic.app.service.excel.ExcelService;
import com.agrologic.app.service.excel.ExportToExcelException;
import com.agrologic.app.utils.FileDownloadUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ExportToExcelPerDayReport extends AbstractServlet {
    private ExcelService excelService;

    public ExportToExcelPerDayReport() {
        super();
        excelService = new ExcelService();
    }

    /**
     * Processes requests for both HTTP
     * <code>GET</code> and
     * <code>POST</code> methods.
     *
     * @param request  servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException      if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        /**
         * Logger for this class and subclasses
         */
        try {
            if (!CheckUserInSession.isUserInSession(request)) {
                logger.error("Unauthorized access!");
                response.sendRedirect("./login.jsp");
            } else {
                long flockId = Long.parseLong(request.getParameter("flockId"));
                long langId = getInSessionLanguageId(request);
                String outputFile = excelService.writeHistoryPerDayToExcelFile(flockId, langId);
                FileDownloadUtil.doDownload(response, outputFile, "xls");
            }
        } catch (ExportToExcelException e) {
            logger.error("Failed to export to excel file. ", e);
            request.setAttribute("message", "Failed to export to excel file.");
            request.setAttribute("error", true);
        } catch (Exception e) {
            request.setAttribute("message", e.getMessage());
            request.setAttribute("error", true);
            logger.error(e.getMessage(), e);
        } finally {
            response.getOutputStream().close();
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">

    /**
     * Handles the HTTP
     * <code>GET</code> method.
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
     * Handles the HTTP
     * <code>POST</code> method.
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



