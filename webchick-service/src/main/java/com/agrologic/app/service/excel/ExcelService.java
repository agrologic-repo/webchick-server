package com.agrologic.app.service.excel;

import com.agrologic.app.dao.DaoType;
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
import java.util.Collections;
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
        excelFileWriter = new ExcelFileWriter();
        historyService = new HistoryServiceImpl();
        flockHistoryService = new FlockHistoryServiceImpl();
        logger = LoggerFactory.getLogger(ExcelService.class);
    }

    public ExcelService(DaoType daoType) {
        historyService = new HistoryServiceImpl(daoType);
        excelFileWriter = new ExcelFileWriter();
        flockHistoryService = new FlockHistoryServiceImpl(daoType);
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
            Map<Integer, String> flockPerDayNotParsedReports = flockHistoryService.getFlockPerDayNotParsedReports(flockId);
            return HistoryContentCreator.createPerDayHistoryContent(flockPerDayNotParsedReports,
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
            Collection<Data> perHourHistoryList = flockHistoryService.getFlockPerHourReportsData(flockId, growDay,
                    langId);
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
    public String writeHistoryPerHourToExcelFile(Long flockId, Integer growDay, Long langId)
            throws ExportToExcelException {
        try {
            HistoryContent historyContent = createPerHourHistoryTableContent(flockId, growDay, langId);
            Flock flock = flockHistoryService.getFlock(flockId);
            String outputFile = flock.getFlockName();
            excelFileWriter.setTitleList(historyContent.getTitlesForExcelPerHour());
            excelFileWriter.setCellDataList(historyContent.getExcelDataColumnsPerHour());
            excelFileWriter.setOutputFile(outputFile);
            excelFileWriter.write();
            return outputFile;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new ExportToExcelException(e.getMessage(), e);
        }
    }

    /**
     * Write history data per hour of specified flock and grow day to excel file .
     *
     * @param flockId the specified flock id
     * @param growDay the specified grow day
     * @return true if data was successful saved
     * @throws ExportToExcelException if failed to write to excel file
     */
    public Boolean writeHistoryPerHourToExcelFile(String filename, Long flockId, Integer growDay, Long langId)
            throws ExportToExcelException {
        try {
            HistoryContent historyContent = createPerHourHistoryTableContent(flockId, growDay, langId);
            excelFileWriter.setTitleList(historyContent.getTitlesForExcelPerHour());
            excelFileWriter.setCellDataList(historyContent.getExcelDataColumnsPerHour());
            excelFileWriter.setOutputFile(filename);
            excelFileWriter.write();
            return true;
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
            Flock flock = flockHistoryService.getFlock(flockId);
            String outputFile = flock.getFlockName();
            excelFileWriter.setTitleList(historyContent.getTitlesForExcel());
            excelFileWriter.setCellDataList(historyContent.getExcelDataColumns());
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
     * @return true if data was successful saved
     * @throws ExportToExcelException if failed to write to excel file
     */
    public Boolean writeHistoryPerDayToExcelFile(String filename, Long flockId, Long langId)
            throws ExportToExcelException {
        try {
            HistoryContent historyContent = createPerDayHistoryTableContent(flockId, langId);
            excelFileWriter.setTitleList(historyContent.getTitlesForExcel());
            excelFileWriter.setCellDataList(historyContent.getExcelDataColumns());
            excelFileWriter.setOutputFile(filename);
            excelFileWriter.write();
            return true;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new ExportToExcelException(e.getMessage(), e);
        }
    }
}
