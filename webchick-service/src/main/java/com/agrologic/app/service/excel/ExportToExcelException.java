package com.agrologic.app.service.excel;

import com.agrologic.app.exception.GeneralException;

/**
 * Throws while exporting history data to excel file .
 *
 * @author Valery Manakhimov
 */
public class ExportToExcelException extends GeneralException {
    public ExportToExcelException(String message, Exception e) {
        super(message);
        setStackTrace(e.getStackTrace());
    }
}