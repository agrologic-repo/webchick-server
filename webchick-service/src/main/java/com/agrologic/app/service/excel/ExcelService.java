package com.agrologic.app.service.excel;

import com.agrologic.app.model.Data;
import com.agrologic.app.model.Flock;
import com.agrologic.app.service.history.FlockHistoryService;
import com.agrologic.app.service.history.HistoryContent;
import com.agrologic.app.service.history.HistoryContentCreator;
import com.agrologic.app.service.history.HistoryService;
import com.agrologic.app.service.history.transaction.FlockHistoryServiceImpl;
import com.agrologic.app.service.history.transaction.HistoryServiceImpl;
import com.agrologic.app.service.table.HistoryContentException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * This service should have methods which give more abilities in manipulating with exporting history data
 * into excel file .
 *
 * @author Valery Manakhimov
 */
public class ExcelService {
    private HistoryService historyService;
    private ExcelFileWriter excelFileWriter;
    private FlockHistoryService flockHistoryService;
    private Logger logger;


    public ExcelService() {
        historyService = new HistoryServiceImpl();
        excelFileWriter = new ExcelFileWriter();
        flockHistoryService = new FlockHistoryServiceImpl();
        logger = LoggerFactory.getLogger(ExcelService.class);
    }

    /**
     * Create list of history values of specified flock
     *
     * @param flockId the flock id
     * @return HistoryContent the content of history data
     */
    private HistoryContent createPerDayHistoryTableContent(Long flockId, Long langId) throws HistoryContentException {
        try {
            List<Data> defaultPerDayHistoryList = historyService.getPerDayHistoryData(langId);
            Map<Integer, String> flockPerDayHistoryNotParsedList = flockHistoryService.getFlockHistory(flockId);
            return HistoryContentCreator.createPerDayHistoryContent(flockPerDayHistoryNotParsedList,
                    defaultPerDayHistoryList);
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw new HistoryContentException(e.getMessage(), e);
        }
    }

    /**
     * Create list of history values per hour of specified flock and grow day
     *
     * @param flockId the flock id
     * @param growDay the grow day
     * @return HistoryContent the content with history data
     * @throws HistoryContentException if failed to create history content
     */
    private HistoryContent createPerHourHistoryTableContent(Long flockId, Integer growDay, Long langId)
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
     * Write history data per hour of specified flock and grow day to excel file .
     *
     * @param flockId the specified flock id
     * @param growDay the specified grow day
     * @return outputFile the name of outputFile
     * @throws ExportToExcelException if failed to write to excel file
     */
    public String writeHistoryPerHourToExcelFile(Long flockId, Integer growDay, Long langId) throws ExportToExcelException {
        try {
            HistoryContent historyContent = createPerHourHistoryTableContent(flockId, growDay, langId);
            List<String> columnTitles = historyContent.getTitlesForExcelPerHour();
            List<List<String>> historyDataColumns = historyContent.getExcelDataColumnsPerHour();
            FlockHistoryService flockHistoryService = new FlockHistoryServiceImpl();
            Flock flock = flockHistoryService.getFlock(flockId);
            String outputFile = flock.getFlockName();
            excelFileWriter.setTitleList(columnTitles);
            excelFileWriter.setCellDataList(historyDataColumns);
            excelFileWriter.setOutputFile(outputFile);
            excelFileWriter.write();
            return outputFile;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new ExportToExcelException(e.getMessage(), e);
        }
    }

    /**
     * Write history data  per day of specified flock to excel file .
     *
     * @param flockId the specified flock id
     * @return outputFile the name of outputFile
     * @throws ExportToExcelException if failed to write to excel file
     */
    public String writeHistoryPerDayToExcelFile(Long flockId, Long langId) throws ExportToExcelException {
        try {
            HistoryContent historyContent = createPerDayHistoryTableContent(flockId, langId);
            List<String> columnTitles = historyContent.getTitlesForExcel();
            List<List<String>> historyDataColumns = historyContent.getExcelDataColumns();

            FlockHistoryService flockHistoryService = new FlockHistoryServiceImpl();
            Flock flock = flockHistoryService.getFlock(flockId);
            String outputFile = flock.getFlockName();
            excelFileWriter.setTitleList(columnTitles);
            excelFileWriter.setCellDataList(historyDataColumns);
            excelFileWriter.setOutputFile(outputFile);
            excelFileWriter.write();
            return outputFile;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new ExportToExcelException(e.getMessage(), e);
        }
    }
}
