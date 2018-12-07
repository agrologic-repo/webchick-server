package com.agrologic.app.service.excel;

import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.format.UnderlineStyle;
import jxl.write.*;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Locale;

import com.agrologic.app.model.Flock;

import java.text.SimpleDateFormat; ////
import java.util.Date; ////
import java.text.ParseException; ////
import java.util.Calendar; ////

/**
 * Created by Valery on 12/30/13.
 */
public class ExcelFileWriter {
    private String inputFile;
    private List<String> titles;
    private List<List<String>> cellDataList;

    public void setOutputFile(String inputFile) {
        this.inputFile = inputFile;
    }

//    public void write() throws IOException, WriteException {
    public void write(Flock flock) throws IOException, WriteException { ////
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
        createTitles(excelSheet);
//        createContent(excelSheet);
        createContent(excelSheet, flock); ////
        workbook.write();
        workbook.close();
    }

    public void writeH(Flock flock) throws IOException, WriteException { ////
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
//        createContent(excelSheet);
        createContentH(excelSheet, flock); ////
        workbook.write();
        workbook.close();
    }

    private void createTitles(WritableSheet sheet) throws WriteException {
        // Write a titles
        String date = "Date";////
        for (int col = 0; col < titles.size(); col++) { ////
            addTitle(sheet, 0, 0, date); ////
            addTitle(sheet, col+1, 0, titles.get(col)); ////
        }
    }

    private void createTitlesH(WritableSheet sheet) throws WriteException {
        for (int col = 0; col < titles.size(); col++) {
            addTitle(sheet, col, 0, titles.get(col));
        }
    }

//    private void createContent(WritableSheet sheet) throws WriteException {
      private void createContent(WritableSheet sheet, Flock flock) throws WriteException {
        // Write cell content
          try {
              String startDayFlock = flock.getStartTime();
              SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yy");
              Date date = formatter.parse(startDayFlock);
              String stringStartDate = formatter.format(date);
              String stringDate;
              Calendar calendar = Calendar.getInstance();
              calendar.setTime(date);
              addLabel(sheet, 0, 1, stringStartDate);
              int countRows = 0;
              for (int col = 0; col < cellDataList.size(); col++) {
                  List<String> listHistData = cellDataList.get(col);
                  countRows = listHistData.size();
                  for (int row = 0; row < listHistData.size(); row++) {
                      String value = listHistData.get(row);
                      addLabel(sheet, col + 1, row + 1, value);
                  }
              }
              for(int i = 2; i < countRows + 1; i++){
                  calendar.add(Calendar.DATE, 1);
                  date = calendar.getTime();
                  stringDate = formatter.format(date);
                  addLabel(sheet, 0, i, stringDate);
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
