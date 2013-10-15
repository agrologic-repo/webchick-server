package com.agrologic.app.web;

import com.agrologic.app.excel.DataForExcelCreator;
import com.agrologic.app.excel.WriteToExcel;
import com.agrologic.app.model.DataFormat;
import com.agrologic.app.model.Flock;
import com.agrologic.app.service.FlockHistoryService;
import com.agrologic.app.utils.FileDownloadUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;


public class ExportToExcelHistory24 extends AbstractServlet {

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

        long flockId = Long.parseLong(request.getParameter("flockId"));
        GrowDayParam growDayParam = new GrowDayParam(request.getParameter("growDay"));

        try {
            FlockHistoryService flockHistoryService = new FlockHistoryService();
            List<String> historyValueList = flockHistoryService.getFlockHistory24Hour(flockId, growDayParam.getGrowDay());
            StringBuilder chainedHistoryValues = new StringBuilder();
            for (String historyValues : historyValueList) {
                chainedHistoryValues.append(historyValues);
            }
            Flock flock = flockHistoryService.getFlock(flockId);

            Long resetTime = flockHistoryService.getResetTime(flockId, growDayParam.getGrowDay());

            Map<Integer, String> history24ByHour = parseHistory24(resetTime, historyValueList.get(0));
            List<List<String>> allHistory24DataForExcel = new ArrayList<List<String>>();

            allHistory24DataForExcel.add((DataForExcelCreator.createDataList(history24ByHour.keySet())));
            history24ByHour = parseHistory24(resetTime, historyValueList.get(0));
            allHistory24DataForExcel.add(DataForExcelCreator.createDataHistory24List(history24ByHour,
                    DataFormat.DEC_1));
            history24ByHour = parseHistory24(resetTime, historyValueList.get(1));
            allHistory24DataForExcel.add(DataForExcelCreator.createDataHistory24List(history24ByHour,
                    DataFormat.DEC_1));
            history24ByHour = parseHistory24(resetTime, historyValueList.get(2));
            allHistory24DataForExcel.add(DataForExcelCreator.createDataHistory24List(history24ByHour,
                    DataFormat.HUMIDITY));
            history24ByHour = parseHistory24(resetTime, historyValueList.get(3));
            allHistory24DataForExcel.add(DataForExcelCreator.createDataHistory24List(history24ByHour,
                    DataFormat.DEC_4));
            history24ByHour = parseHistory24(resetTime, historyValueList.get(4));
            allHistory24DataForExcel.add(DataForExcelCreator.createDataHistory24List(history24ByHour,
                    DataFormat.DEC_4));

            List<String> perHourHistoryDataTitles = flockHistoryService.getPerHourHistoryDataTitles(growDayParam.getGrowDay());
            WriteToExcel excel = new WriteToExcel();
            excel.setTitleList(perHourHistoryDataTitles);
            excel.setCellDataList(allHistory24DataForExcel);
            excel.setOutputFile("C:/flock-" + flock.getFlockName());
            excel.write();
            FileDownloadUtil.doDownload(response, "C:/flock-" + flock.getFlockName(), "xls");
        } catch (Exception e) {
            logger.error("Unknown error. ", e);
        } finally {
            response.getOutputStream().close();
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

    private Map<Integer, String> parseHistory24(long resetTime, String values) {
        String[] valueList = values.split(" ");
        Map<Integer, String> valuesMap = new TreeMap<Integer, String>();
        int j = (int) resetTime / 100;
        for (int i = 0; i < 24; i++) {
            if (j == 24) {
                j = 0;
            }
            valuesMap.put(j++, valueList[i]);
        }
        return valuesMap;
    }
}



