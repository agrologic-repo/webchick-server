package com.agrologic.app.service.table;

import com.agrologic.app.model.Data;
import com.agrologic.app.model.history.FromDayToDayParam;
import com.agrologic.app.service.history.FlockHistoryService;
import com.agrologic.app.service.history.HistoryContent;
import com.agrologic.app.service.history.HistoryContentCreator;
import com.agrologic.app.service.history.HistoryService;
import com.agrologic.app.service.history.transaction.FlockHistoryServiceImpl;
import com.agrologic.app.service.history.transaction.HistoryServiceImpl;
import org.apache.commons.lang.StringEscapeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * This service should have methods which give more abilities in manipulating with showing history data in html page .
 *
 * @author Valery Manakhimov
 */
public class HtmlTableService {
    private FlockHistoryService flockHistoryService;
    private HistoryService historyService;
    private Logger logger;

    public HtmlTableService() {
        historyService = new HistoryServiceImpl();
        flockHistoryService = new FlockHistoryServiceImpl();
        logger = LoggerFactory.getLogger(HtmlTableService.class);
    }

    /**
     * Create list of history values of specified flock
     *
     * @param flockId the flock id
     * @return listOfHistoryDataValueColumns the list of history values
     */
    private HistoryContent createHistoryPerDayTableContent(Long flockId, FromDayToDayParam fromDayToDayParam, Long langId)
            throws HistoryContentException {
        try {
            List<Data> defaultPerDayHistoryList = historyService.getPerDayHistoryData(langId);
            Map<Integer, String> flockPerDayHistoryNotParsedList
                    = flockHistoryService.getFlockHistoryWithinRange(flockId, fromDayToDayParam);
            return HistoryContentCreator.createPerDayHistoryContent(flockPerDayHistoryNotParsedList,
                    defaultPerDayHistoryList);
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw new HistoryContentException(e.getMessage(), e);
        }
    }

    /**
     * Create list of history values of specified flock
     *
     * @param flockId the flock id
     * @return HistoryContent the history
     */
    private HistoryContent createHistoryPerHourTableContent(Long flockId, Integer growDay, Long langId)
            throws HistoryContentException {
        try {
            Collection<Data> perHourHistoryList = flockHistoryService.getFlockPerHourHistoryData(flockId, growDay, langId);
            return HistoryContentCreator.createPerHourHistoryContent(growDay, perHourHistoryList);
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw new HistoryContentException(e.getMessage(), e);
        }
    }


    /**
     * Creates string with history data in html format .
     *
     * @param flockId           the flock id
     * @param fromDayToDayParam
     * @return the html table with history data
     * @throws HistoryContentException if failed to create history data content
     */
    public String toHtmlTableHistoryData(Long flockId, FromDayToDayParam fromDayToDayParam, Long langId)
            throws HistoryContentException {
        try {
            HistoryContent tableContent = createHistoryPerDayTableContent(flockId, fromDayToDayParam, langId);
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("<p><table class=table-list cellpadding='1' cellspacing='1' border='1'><tr>");

            for (Data data : tableContent.getTitlesForHtml()) {
                stringBuilder.append("<th style=\"font-size: small\">" +
                        StringEscapeUtils.unescapeHtml(data.getUnicodeLabel()) + "</th>");
            }
            stringBuilder.append("</tr>");

            for (Map.Entry<Integer, List<Data>> entry : tableContent.getHistoryContentPerDay().entrySet()) {
                stringBuilder.append("<tr>");
                for (Data data : entry.getValue()) {
                    stringBuilder.append("<td align=center>" + data.getFormattedValue() + "</td>");
                }
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

    /**
     * Creates string with history data in html format .
     *
     * @param flockId the flock id
     * @param growDay the grow day
     * @return the html table with history data
     * @throws HistoryContentException if failed to create history data content
     */

    public String toHtmlTableHistoryDataPerHour(Long flockId, Integer growDay, Long langId) throws HistoryContentException {
        try {
            HistoryContent tableContent = createHistoryPerHourTableContent(flockId, growDay, langId);
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("<p><table class=table-list cellpadding='1' cellspacing='1' border='1'><tr>");

            for (String title : tableContent.getTitlesPerHourForHtml()) {
                stringBuilder.append("<th style=\"font-size: small\">" + title + "</th>");
            }
            stringBuilder.append("</tr>");

            List<List<String>> historyContentPerHour = tableContent.getHistoryContentPerHour();
            for (List<String> list : historyContentPerHour) {
                stringBuilder.append("<tr>");
                for (String content : list) {
                    stringBuilder.append("<td align=center>" + content + "</td>");
                }
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
