package com.agrologic.app.web;

import com.agrologic.app.service.excel.ExcelService;
import com.agrologic.app.service.excel.ExportToExcelException;
import com.agrologic.app.utils.FileDownloadUtil;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class HistoryGraphAction extends AbstractServlet{

    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

        try {
            if (!CheckUserInSession.isUserInSession(request)) {
                logger.error("Unauthorized access!");
                response.sendRedirect("./login.jsp");
            } else {

                Long cellinkId = Long.parseLong(request.getParameter("cellinkId"));
                String flockId = request.getParameter("flockId");
                String growDay = request.getParameter("growDay");

                String btnClicked = request.getParameter("btn");

                if (btnClicked != null) {
                    switch (btnClicked) {
                        case "apply":
                            request.getRequestDispatcher("./rmctrl-flock-graph-daily-hour.jsp?cellinkId=" + cellinkId).forward(request, response);
                            break;

                        case "show_table":

                            break;

                        case "export_to_excel":
                            try {
                                Long langId = getInSessionLanguageId(request);
                                ExcelService excelService = new ExcelService();
                                String outputFile = excelService.writeHistoryPerHourToExcelFile(Long.parseLong(flockId), Integer.parseInt(growDay), langId);
                                FileDownloadUtil.doDownload(response, outputFile, "xls");
                            } catch (ExportToExcelException e) {
                                e.printStackTrace();
                            }

                            break;
                    }
                } else {
                    request.getRequestDispatcher("./rmctrl-flock-graph-daily-hour.jsp?cellinkId=" + cellinkId + "&growDay=" + growDay).forward(request, response);
                }
            }
        } finally {
            out.close();
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }    // </editor-fold>

}
