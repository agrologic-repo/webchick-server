package com.agrologic.app.service.table;

import com.agrologic.app.model.Data;
import com.agrologic.app.model.Flock;
import com.agrologic.app.model.history.FromDayToDay;
import com.agrologic.app.service.history.FlockHistoryService;
import com.agrologic.app.service.history.HistoryContent;
import com.agrologic.app.service.history.HistoryContentCreator;
import com.agrologic.app.service.history.HistoryService;
import com.agrologic.app.service.history.transaction.FlockHistoryServiceImpl;
import com.agrologic.app.service.history.transaction.HistoryServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;

public class HtmlTableService {
    private FlockHistoryService flockHistoryService;
    private HistoryService historyService;
    private Logger logger;

    public HtmlTableService() {
        historyService = new HistoryServiceImpl();
        flockHistoryService = new FlockHistoryServiceImpl();
        logger = LoggerFactory.getLogger(HtmlTableService.class);
    }

    private HistoryContent createHistoryPerDayTableContent(Long flockId, FromDayToDay fromDayToDay, Long langId) throws HistoryContentException {
        try {
            List<Data> defaultPerDayHistoryList = historyService.getPerDayHistoryData(langId);
            // if grow day data not the first in array so find it and swap with first
            if (!defaultPerDayHistoryList.get(0).getId().equals(800L)) {
                int growDayIndex = -1;
                int counter = 0;
                for (Data data : defaultPerDayHistoryList) {
                    if (data.getId().equals(800L)) {
                        growDayIndex = counter;
                        break;
                    }
                    counter++;
                }
                Collections.swap(defaultPerDayHistoryList, 0, growDayIndex);
            }

            Map<Integer, String> flockPerDayHistoryNotParsedList = flockHistoryService.getFlockPerDayNotParsedReportsWithinRange(flockId, fromDayToDay);
            return HistoryContentCreator.createPerDayHistoryContent(flockPerDayHistoryNotParsedList, defaultPerDayHistoryList);

        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw new HistoryContentException(e.getMessage(), e);
        }
    }

    private HistoryContent createHistoryPerHourTableContent(Long flockId, Integer growDay, Long langId) throws HistoryContentException {
        try {

            Collection<Data> perHourHistoryList = flockHistoryService.getFlockPerHourReportsData(flockId, growDay, langId);
            return HistoryContentCreator.createPerHourHistoryContent(growDay, perHourHistoryList);

        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw new HistoryContentException(e.getMessage(), e);
        }
    }


    /**
     * Creates html table string with per day reports .
     *
     * @param flockId      the flock id
     * @param fromDayToDay the range
     * @return the html table with history data
     * @throws HistoryContentException if failed to create history data content
     */
    //////////////////////////////////***************************************************************************************
    public String toHtmlTablePerDayReports(Long flockId, FromDayToDay fromDayToDay, Long langId)throws HistoryContentException {
        try {
            HistoryContent tableContent = createHistoryPerDayTableContent(flockId, fromDayToDay, langId);
            StringBuilder stringBuilder = new StringBuilder();
//            stringBuilder.append("<p><table class=table-list cellpadding='1' cellspacing='1' border='1'><tr>");
            stringBuilder.append("<table class=table-list cellpadding='1' cellspacing='1' border='1'>");
//            List<Data> test = tableContent.getTitlesForHtml();

//            String dateTitle = "Date"; ////
//            stringBuilder.append("<th style=\"font-size: small\">" + StringEscapeUtils.unescapeHtml(dateTitle) + "</th>"); ////test
            Flock flock = flockHistoryService.getFlock(flockId); ////
            String startDayFlock = flock.getStartTime(); ////
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yy"); ////
//            Date date = formatter.parse(startDayFlock); ////
//            String stringStartDate = formatter.format(date);////
            String stringDate; ////
            Calendar calendar = Calendar.getInstance(); ////
//            calendar.setTime(date); ////
            int counter = 0; ////
            int count = 0; ////

//            for (Data data : tableContent.getTitlesForHtml()) {
//                stringBuilder.append("<th style=\"font-size: small\">" +
//                        StringEscapeUtils.unescapeHtml(data.getUnicodeLabel()) + "</th>");
//            }
//            stringBuilder.append("</tr>");
            for (Map.Entry<Integer, List<Data>> entry : tableContent.getHistoryContentPerDay().entrySet()) {
//            for (Map.Entry<Long, List<Data>> entry : tableContent.getHistoryContentPerDay().entrySet()) {
                stringBuilder.append("<tr>");

                if(fromDayToDay.useRange() == false) {
                    if (counter == 0) {
//                        Date date = formatter.parse(startDayFlock);
                        calendar.setTime(formatter.parse(startDayFlock));
                        calendar.add(Calendar.DAY_OF_MONTH, entry.getKey() - 1);
                        String stringStartDate = formatter.format(calendar.getTime());
                        stringBuilder.append("<td align=center><h2>" + stringStartDate + "</h2></td>");
                        stringBuilder.append("<td align=center><h2>" + entry.getKey() + "</h2></td>");
                    }
                    if (counter > 0) {
//                        calendar.add(Calendar.DATE, 1);
                        calendar.setTime(formatter.parse(startDayFlock));
                        calendar.add(Calendar.DAY_OF_MONTH, entry.getKey() - 1);
//                        date = calendar.getTime();
                        stringDate = formatter.format(calendar.getTime());
                        stringBuilder.append("<td align=center><h2>" + stringDate + "</h2></td>");
                        stringBuilder.append("<td align=center><h2>" + entry.getKey() + "</h2></td>");
                    }
                } else {
                    if(count == 0){
                        int from = fromDayToDay.getFromDay();
                        from = from - 1;
                        calendar.setTime(formatter.parse(startDayFlock));
                        calendar.add(Calendar.DAY_OF_MONTH, from);
//                        date = calendar.getTime();
                        stringDate = formatter.format(calendar.getTime());
                        stringBuilder.append("<td align=center><h2>" + stringDate + "</h2></td>");
                        stringBuilder.append("<td align=center><h2>" + entry.getKey() + "</h2></td>");
                    }
                    if (count > 0) {
                        calendar.setTime(formatter.parse(startDayFlock));
                        calendar.add(Calendar.DAY_OF_MONTH, 1);
//                        date = calendar.getTime();
                        stringDate = formatter.format(calendar.getTime());
                        stringBuilder.append("<td align=center><h2>" + stringDate + "</h2></td>");
                        stringBuilder.append("<td align=center><h2>" + entry.getKey() + "</h2></td>");
                    }
                }

                for (Data data : entry.getValue()) {
//                    stringBuilder.append("<td align=center>" + data.getFormattedValue() + "</td>");
//                    stringBuilder.append("<td align=center style=\"background-color: #D5EFFF;>" + "<td><h3>" + data.getLabel() + "</h3></td> <td><h3>"+ data.getFormattedValue() + "</h3></td>" + "</td>");
//                    stringBuilder.append("<td>" + "<h3 style=\"font-weight:bold;\">" + data.getLabel() + "</h3> <h3><font size=\"4\">"+ data.getFormattedValue() + "</font></h3>" + "</td>");
                    stringBuilder.append("<td>" + "<h3>" + data.getLabel() + "</h3> <h3><font size=\"4\">"+ data.getFormattedValue() + "</font></h3>" + "</td>");
                }
                stringBuilder.append("</tr>");

                counter++;
                count++;
            }
            stringBuilder.append("</table>");
            return stringBuilder.toString();
        } catch (HistoryContentException e) {
            logger.error("Failed to create history data values columns . ", e);
            throw new HistoryContentException("Failed to create history data values columns .", e);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new HistoryContentException(e.getMessage(), e);
        }
    }

//    public String toHtmlTablePerDayReports(Long flockId, FromDayToDay fromDayToDay, Long langId)throws HistoryContentException {
//        try {
//            HistoryContent tableContent = createHistoryPerDayTableContent(flockId, fromDayToDay, langId);
//            StringBuilder stringBuilder = new StringBuilder();
//            stringBuilder.append("<p><table class=table-list cellpadding='1' cellspacing='1' border='1'><tr>");
//            List<Data> test = tableContent.getTitlesForHtml();
//
//            String dateTitle = "Date"; ////
//            stringBuilder.append("<th style=\"font-size: small\">" + StringEscapeUtils.unescapeHtml(dateTitle) + "</th>"); ////test
//            Flock flock = flockHistoryService.getFlock(flockId); ////
//            String startDayFlock = flock.getStartTime(); ////
//            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yy"); ////
//            Date date = formatter.parse(startDayFlock); ////
//            String stringStartDate = formatter.format(date);////
//            String stringDate; ////
//            Calendar calendar = Calendar.getInstance(); ////
//            calendar.setTime(date); ////
//            int counter = 0; ////
//            int count = 0; ////
//
//            for (Data data : tableContent.getTitlesForHtml()) {
//                stringBuilder.append("<th style=\"font-size: small\">" +
//                        StringEscapeUtils.unescapeHtml(data.getUnicodeLabel()) + "</th>");
//            }
//            stringBuilder.append("</tr>");
//            for (Map.Entry<Integer, List<Data>> entry : tableContent.getHistoryContentPerDay().entrySet()) {
////            for (Map.Entry<Long, List<Data>> entry : tableContent.getHistoryContentPerDay().entrySet()) {
//                stringBuilder.append("<tr>");
//
//                if(fromDayToDay.useRange() == false) { ////
//                    if (counter == 0) { ////
//                        stringBuilder.append("<td align=center>" + stringStartDate + "</td>"); ////
//                    } /////
//                    if (counter > 0) { ////
//                        calendar.add(Calendar.DATE, 1); ////
//                        date = calendar.getTime(); ////
//                        stringDate = formatter.format(date); ////
//                        stringBuilder.append("<td align=center>" + stringDate + "</td>"); ////
//                    } ////
//                } ////
//                else{ ////
//                    if(count == 0){////
//                        int from = fromDayToDay.getFromDay();////
//                        from = from - 1;/////
//                        calendar.add(Calendar.DATE, from);/////
//                        date = calendar.getTime();////
//                        stringDate = formatter.format(date); ////
//                        stringBuilder.append("<td align=center>" + stringDate + "</td>"); ////
//                    }////
//                    if (count > 0) { ////
//                        calendar.add(Calendar.DATE, 1); ////
//                        date = calendar.getTime(); ////
//                        stringDate = formatter.format(date); ////
//                        stringBuilder.append("<td align=center>" + stringDate + "</td>"); ////
//                    } ////
//                }////
//
//                for (Data data : entry.getValue()) {
//                    stringBuilder.append("<td align=center>" + data.getFormattedValue() + "</td>");
////                    stringBuilder.append("<td align=center>"+ data.getLabel() + "" + data.getFormattedValue() + "</td>");
//                }
//                stringBuilder.append("</tr>");
//
//                counter++; ////test
//                count++; ////test
//            }
//            stringBuilder.append("</table>");
//            return stringBuilder.toString();
//        } catch (HistoryContentException e) {
//            logger.error("Failed to create history data values columns . ", e);
//            throw new HistoryContentException("Failed to create history data values columns .", e);
//        } catch (Exception e) {
//            logger.error(e.getMessage(), e);
//            throw new HistoryContentException(e.getMessage(), e);
//        }
//    }

    /**
     * Creates html table string with per hour reports .
     *
     * @param flockId the flock id
     * @param growDay the grow day
     * @return the html table with history data
     * @throws HistoryContentException if failed to create history data content
     */

    public String toHtmlTablePerHourReports(Long flockId, Integer growDay, Long langId) throws HistoryContentException {
        int i;
        i = 0;
        int con;
        HistoryContent tableContent;
        StringBuilder stringBuilder;
        List<List<String>> historyContentPerHour;
        try {
            tableContent = createHistoryPerHourTableContent(flockId, growDay, langId);
            stringBuilder = new StringBuilder();
//            stringBuilder.append("<p><table class=table-list cellpadding='1' cellspacing='1' border='1'><tr>");
            stringBuilder.append("<table class=table-list cellpadding='1' cellspacing='1' border='1'><tr>");
            for (String title : tableContent.getTitlesPerHourForHtml()) {
                if (title.equals("Date"))
                    continue;
                stringBuilder.append("<th style=\"font-size: small\">" + title + "</th>");
            }
            stringBuilder.append("</tr>");
            historyContentPerHour = tableContent.getHistoryContentPerHour();
            for (List<String> list : historyContentPerHour) {
                stringBuilder.append("<tr>");
                for (String content : list) {
                    if(i == 0){
                        i++;
                        con = Integer.parseInt(content);
                        con++;
                        content = String.valueOf(con);
                    }
                    stringBuilder.append("<td align=center>" + content + "</td>");/////////////////////
                }
                i = 0;
                stringBuilder.append("</tr>");
            }
            stringBuilder.append("</table>");
            return stringBuilder.toString();
        } catch (HistoryContentException e) {
            logger.error("Failed to create history data values columns . ", e);
            throw new HistoryContentException("Failed to create history data values columns .", e);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new HistoryContentException(e.getMessage(), e);
        }
    }
}
