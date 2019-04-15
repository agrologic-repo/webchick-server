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

public class ExcelService {
    private ExcelFileWriter excelFileWriter;
    private FlockHistoryService flockHistoryService;
    private Logger logger;

    public ExcelService() {
        excelFileWriter = new ExcelFileWriter();
        flockHistoryService = new FlockHistoryServiceImpl();
        logger = LoggerFactory.getLogger(ExcelService.class);
    }

    public ExcelService(DaoType daoType) {
        excelFileWriter = new ExcelFileWriter();
        flockHistoryService = new FlockHistoryServiceImpl(daoType);
        logger = LoggerFactory.getLogger(ExcelService.class);
    }

    private HistoryContent createPerDayHistoryTableContent(Long flockId, Long langId) throws HistoryContentException {
        try {

            Map<Integer, String> flockPerDayNotParsed = flockHistoryService.getFlockPerDayNotParsedReports(flockId);
            return HistoryContentCreator.createPerDayHistoryContent(flockPerDayNotParsed);

        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw new HistoryContentException(e.getMessage(), e);
        }
    }

    private HistoryContent createPerHourHistoryTableContent(Long flockId, Integer growDay, Long langId)throws HistoryContentException {
        try {

            Collection<Data> perHourHistoryList = flockHistoryService.getFlockPerHourReportsData(flockId, growDay, langId);
            return HistoryContentCreator.createPerHourHistoryContent(growDay, perHourHistoryList);

        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw new HistoryContentException(e.getMessage(), e);
        }
    }

    public String writeHistoryPerHourToExcelFile(Long flockId, Integer growDay, Long langId)throws ExportToExcelException {
        HistoryContent historyContent;
        Flock flock;
        String outputFile;
        try {
            historyContent = createPerHourHistoryTableContent(flockId, growDay, langId);
            flock = flockHistoryService.getFlock(flockId);
            outputFile = flock.getFlockName();
            excelFileWriter.setTitleList(historyContent.getTitlesForExcelPerHour());
            excelFileWriter.setCellDataList(historyContent.getExcelDataColumnsPerHour());

            excelFileWriter.setOutputFile(outputFile);
            excelFileWriter.writeH(flock);
            return outputFile;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new ExportToExcelException(e.getMessage(), e);
        }
    }

    public Boolean writeHistoryPerHourToExcelFile(String filename, Long flockId, Integer growDay, Long langId) throws ExportToExcelException {
        try {

            HistoryContent historyContent = createPerHourHistoryTableContent(flockId, growDay, langId);
            Flock flock = flockHistoryService.getFlock(flockId);
            excelFileWriter.setTitleList(historyContent.getTitlesForExcelPerHour());
            excelFileWriter.setCellDataList(historyContent.getExcelDataColumnsPerHour());
            excelFileWriter.setOutputFile(filename);
            excelFileWriter.writeH(flock);
            return true;

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new ExportToExcelException(e.getMessage(), e);
        }
    }

    public String writeHistoryPerDayToExcelFile(Long flockId, Long langId) throws ExportToExcelException {
        try {
            HistoryContent historyContent = createPerDayHistoryTableContent(flockId, langId);
            Flock flock = flockHistoryService.getFlock(flockId);
            String outputFile = flock.getFlockName();
            excelFileWriter.setTitleList(historyContent.getTitlesForExcel());
            excelFileWriter.setCellDataList(historyContent.getExcelDataColumns(excelFileWriter.getTitlesList().size()));
            excelFileWriter.setOutputFile(outputFile);
            excelFileWriter.write(flock, historyContent);
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
    public Boolean writeHistoryPerDayToExcelFile(String filename, Long flockId, Long langId) throws ExportToExcelException {
        try {
            HistoryContent historyContent = createPerDayHistoryTableContent(flockId, langId);
            Flock flock = flockHistoryService.getFlock(flockId); ////
            excelFileWriter.setTitleList(historyContent.getTitlesForExcel());
            excelFileWriter.setOutputFile(filename);
            excelFileWriter.write(flock, historyContent);
            return true;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new ExportToExcelException(e.getMessage(), e);
        }
    }
}
