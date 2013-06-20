
/*
* To change this template, choose Tools | Templates
* and open the template in the editor.
 */
package com.agrologic.app.web;


import com.agrologic.app.dao.FlockDao;
import com.agrologic.app.dao.impl.FlockDaoImpl;
import com.agrologic.app.excel.DataForExcelCreator;
import com.agrologic.app.excel.WriteToExcel;
import com.agrologic.app.model.DataFormat;
import com.agrologic.app.model.FlockDto;
import com.agrologic.app.utils.FileDownloadUtil;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

//~--- JDK imports ------------------------------------------------------------

/**
 * @author Administrator
 */
public class ExpHistory24ToExcel extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static String outfile;

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

        /** Logger for this class and subclasses */
        final Logger logger = Logger.getLogger(ExpHistory24ToExcel.class);

        response.setContentType("text/html;charset=UTF-8");

        long flockId = Long.parseLong(request.getParameter("flockId"));
        int growDay = 1;

        try {
            growDay = Integer.parseInt(request.getParameter("growDay"));
        } catch (Exception ex) {
            growDay = 1;
        }

        Locale locale = Locale.ENGLISH;

        try {
            FlockDao flockDao = new FlockDaoImpl();
            Long resetTime = new Long(flockDao.getResetTime(flockId, growDay));

            if (resetTime != null) {
                resetTime = DataFormat.convertToTimeFormat(resetTime);
            } else {
                resetTime = Long.valueOf("0");
            }

            String values1 = flockDao.getHistory24(flockId, growDay, "D18");
            String values2 = flockDao.getHistory24(flockId, growDay, "D19");
            String values3 = flockDao.getHistory24(flockId, growDay, "D20");
            String values4 = flockDao.getHistory24(flockId, growDay, "D21");
            String values5 = flockDao.getHistory24(flockId, growDay, "D72");

            values1 = getDefaultValuesIfEmpty(values1);
            values2 = getDefaultValuesIfEmpty(values2);
            values3 = getDefaultValuesIfEmpty(values3);
            values4 = getDefaultValuesIfEmpty(values4);
            values5 = getDefaultValuesIfEmpty(values5);

            String title1 = flockDao.getDNHistory24("D18");
            String title2 = flockDao.getDNHistory24("D19");
            String title3 = flockDao.getDNHistory24("D20");
            String title4 = flockDao.getDNHistory24("D21");
            String title5 = flockDao.getDNHistory24("D72");
            Map<Integer, String> history24ByHour = parseHistory24(resetTime, values1);
            List<List<String>> allHistory24DataForExcel = new ArrayList<List<String>>();

            allHistory24DataForExcel.add((DataForExcelCreator.createDataList(history24ByHour.keySet())));
            history24ByHour = parseHistory24(resetTime, values1);
            allHistory24DataForExcel.add(DataForExcelCreator.createDataHistory24List(history24ByHour,
                    DataFormat.DEC_1));
            history24ByHour = parseHistory24(resetTime, values2);
            allHistory24DataForExcel.add(DataForExcelCreator.createDataHistory24List(history24ByHour,
                    DataFormat.DEC_1));
            history24ByHour = parseHistory24(resetTime, values3);
            allHistory24DataForExcel.add(DataForExcelCreator.createDataHistory24List(history24ByHour,
                    DataFormat.HUMIDITY));
            history24ByHour = parseHistory24(resetTime, values4);
            allHistory24DataForExcel.add(DataForExcelCreator.createDataHistory24List(history24ByHour,
                    DataFormat.DEC_4));
            history24ByHour = parseHistory24(resetTime, values5);
            allHistory24DataForExcel.add(DataForExcelCreator.createDataHistory24List(history24ByHour,
                    DataFormat.DEC_4));

            List<String> tableTitles = new ArrayList<String>();

            tableTitles.add("Grow day " + growDay + "\nHour(24)");
            tableTitles.add(title1);
            tableTitles.add(title2);
            tableTitles.add(title3);
            tableTitles.add(title4);
            tableTitles.add(title5);

            FlockDto flock = flockDao.getById(flockId);

            outfile = "c:/flock-" + flock.getFlockName() + "-history.xls";

            WriteToExcel excel = new WriteToExcel();

            excel.setTitleList(tableTitles);
            excel.setCellDataList(allHistory24DataForExcel);
            excel.setOutputFile(outfile);
            excel.write();
            FileDownloadUtil.doDownload(response, outfile, "xls");
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

    public String getDefaultValuesIfEmpty(String values) {
        if (values.equals("-1 ") || values.equals("")) {
            values = "0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 ";
        }

        return values;
    }

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



