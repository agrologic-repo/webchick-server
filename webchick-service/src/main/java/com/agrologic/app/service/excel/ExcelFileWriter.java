package com.agrologic.app.service.excel;

import com.agrologic.app.model.Data;
import com.agrologic.app.service.history.HistoryContent;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.format.UnderlineStyle;
import jxl.write.*;

import java.io.File;
import java.io.IOException;
import java.util.*;

import com.agrologic.app.model.Flock;

import java.text.SimpleDateFormat; ////
import java.text.ParseException; ////

public class ExcelFileWriter {
    private String inputFile;
    private List<String> titles;
    private List<List<String>> cellDataList;

    public void setOutputFile(String inputFile) {
        this.inputFile = inputFile;
    }

    public void write(Flock flock, HistoryContent history_content) throws IOException, WriteException {
        File file;
        WorkbookSettings wbSettings;
        WritableWorkbook workbook;
        WritableSheet excelSheet;

        file = new File(inputFile);

        wbSettings = new WorkbookSettings();
        wbSettings.setEncoding("UTF-8");

        wbSettings.setLocale(new Locale("en", "EN"));
        workbook = Workbook.createWorkbook(file, wbSettings);
        workbook.createSheet("Report", 0);

        excelSheet = workbook.getSheet(0);

        createContent(excelSheet, flock, history_content);
        workbook.write();
        workbook.close();
    }

    public void writeH(Flock flock) throws IOException, WriteException {
        File file;
        WorkbookSettings wbSettings;
        WritableWorkbook workbook;
        WritableSheet excelSheet;

        file = new File(inputFile);

        wbSettings = new WorkbookSettings();
        wbSettings.setEncoding("UTF-8");

        wbSettings.setLocale(new Locale("en", "EN"));
        workbook = Workbook.createWorkbook(file, wbSettings);
        workbook.createSheet("Report", 0);

        excelSheet = workbook.getSheet(0);
        createTitlesH(excelSheet);
        createContentH(excelSheet, flock);
        workbook.write();
        workbook.close();
    }

    private void createTitlesH(WritableSheet sheet) throws WriteException {
        for (int col = 0; col < titles.size(); col++) {
            addTitle(sheet, col, 0, titles.get(col));
        }
    }

    private void createContent(WritableSheet sheet, Flock flock, HistoryContent history_content) throws WriteException {

        try {
            String stringDate;
            String startDayFlock = flock.getStartTime();
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yy");
            Calendar calendar = Calendar.getInstance();
//            calendar.setTime(formatter.parse(startDayFlock));

            int col = 0;
//            int row = 1;
//            int gr_day = 1;
            int col_width_l = 25;
            int col_width_v = 20;

            Map<Integer, List<Data>> historyContentPerDay = history_content.getHistoryContentPerDay();

//            for(List <Data> list : history_content.getHistoryContentPerDay().values()){// num of rows // row // col
            for (Map.Entry<Integer, List<Data>> entry : historyContentPerDay.entrySet()) {
                List <Data> list = entry.getValue();
                Integer growDay = entry.getKey();

                int row = 1;
//                int col = 0;

                calendar.setTime(formatter.parse(startDayFlock));
                calendar.add(Calendar.DAY_OF_MONTH, growDay - 1);
                stringDate = formatter.format(calendar.getTime());

                sheet.setColumnView(col, col_width_l);
                sheet.setColumnView(col + 1, col_width_v);

                addLabel(sheet, col, row - 1, stringDate);
                addLabel(sheet, col + 1, row - 1, "Grow day :" + String.valueOf(growDay));

                for (Data data : list){ // col // row
//                    if (row == 1){
                    if (col == 1){
                        String title = data.getLabel();
                        String value = data.getFormattedValue();
//                        addLabel(sheet, col, row - 1, title);
//                        addLabel(sheet, col, row, value);
                        addLabel(sheet, col - 1, row, title);
                        addLabel(sheet, col, row, value);
//                        col++;
                        row++;
                    } else {
                        String value = data.getFormattedValue();
                        String label = data.getLabel();
                        addLabel(sheet, col, row, label);
                        addLabel(sheet, col + 1, row, value);
//                        col++;
                            row++;
//                        }
                    }
                }
                col++;
                col++;
//                row++;
            }
        }
        catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private void createContentH(WritableSheet sheet, Flock flock) throws WriteException {
        List<String> listHistData;
        int countRows;
        String value;

        countRows = 0;
        for (int col = 0; col < cellDataList.size(); col++) {
            listHistData = cellDataList.get(col);
            countRows = listHistData.size();
            for (int row = 0; row < listHistData.size(); row++) {
                value = listHistData.get(row);
                addLabel(sheet, col, row + 1, value);
                }
            }
    }

    private void addTitle(WritableSheet sheet, int column, int row, String title) throws WriteException {
        WritableCellFormat wcf;
        Label label;
        wcf = getTitleFormat();
        label = new Label(column, row, title, wcf);
        sheet.addCell(label);
    }

    public WritableCellFormat getTitleFormat() throws WriteException {
        /* Format the Font */
        WritableFont wf;
        WritableCellFormat cf;
        wf = new WritableFont(WritableFont.ARIAL, 10, WritableFont.BOLD, true,UnderlineStyle.NO_UNDERLINE, Colour.WHITE);
        cf = new WritableCellFormat(wf);
        cf.setWrap(true);
        cf.setBackground(Colour.BLUE);
        cf.setBorder(Border.ALL, BorderLineStyle.THIN);
        return cf;
    }

    private void addLabel(WritableSheet sheet, int column, int row, String s) throws WriteException {
        WritableCellFormat wcf;
        wcf = getLabelFormat();
        Label label;
        label = new Label(column, row, s, wcf);
        sheet.addCell(label);
    }

    public WritableCellFormat getLabelFormat() throws WriteException {
        /* Format the Font */
        WritableFont wf;
        WritableCellFormat cf;
        wf = new WritableFont(WritableFont.COURIER, 10, WritableFont.BOLD);
        cf = new WritableCellFormat(wf);
        cf.setWrap(true);
        return cf;
    }

    public void setTitleList(List<String> titles) {
        this.titles = titles;
    }

    public List<String> getTitlesList() {
        return titles;
    }

    public void setCellDataList(List<List<String>> newList) {
        this.cellDataList = newList;
    }
}
