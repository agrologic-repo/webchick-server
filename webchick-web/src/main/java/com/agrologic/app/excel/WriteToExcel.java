

package com.agrologic.app.excel;


import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.format.Colour;
import jxl.format.UnderlineStyle;
import jxl.write.*;
import jxl.write.biff.RowsExceededException;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Locale;


/**
 * Title: WriteToExcel.java <br>
 * Description: <br>
 * Copyright:   Copyright  2010 <br>
 * Company:     Agro Logic Ltd. <br>
 *
 * @author Valery Manakhimov <br>
 * @version 0.1.1 <br>
 */
public class WriteToExcel {
    private String inputFile;
    private List<String> titles;
    private List<List<String>> cellDataList;

    public void setOutputFile(String inputFile) {
        this.inputFile = inputFile;
    }

    public void write() throws IOException, WriteException {
        File file = new File(inputFile);
        WorkbookSettings wbSettings = new WorkbookSettings();
        wbSettings.setLocale(new Locale("en", "EN"));

        WritableWorkbook workbook = Workbook.createWorkbook(file, wbSettings);
        workbook.createSheet("Report", 0);
        WritableSheet excelSheet = workbook.getSheet(0);
        createTitles(excelSheet);
        createContent(excelSheet);
        workbook.write();
        workbook.close();
    }

    private void createTitles(WritableSheet sheet) throws WriteException, RowsExceededException {

        // Write a titles
        for (int col = 0; col < titles.size(); col++) {
            addTitle(sheet, col, 0, titles.get(col));
        }
    }

    private void createContent(WritableSheet sheet) throws WriteException, RowsExceededException {

        // Write cell content
        for (int col = 0; col < cellDataList.size(); col++) {
            List<String> listHistData = cellDataList.get(col);

            for (int row = 0; row < listHistData.size(); row++) {
                String value = listHistData.get(row);

                addLabel(sheet, col, row + 1, value);
            }
        }
    }

    private void addTitle(WritableSheet sheet, int column, int row, String title)
            throws WriteException, RowsExceededException {
        WritableCellFormat wcf = getTitleFormat();
        Label label;

        label = new Label(column, row, title, wcf);
        sheet.addCell(label);
    }

    public WritableCellFormat getTitleFormat() throws WriteException {

        /* Format the Font */
        WritableFont wf = new WritableFont(WritableFont.ARIAL, 10, WritableFont.BOLD, true,
                UnderlineStyle.NO_UNDERLINE, Colour.BLACK);
        WritableCellFormat cf = new WritableCellFormat(wf);

        cf.setBackground(Colour.ICE_BLUE);
        cf.setWrap(true);
        cf.setBorder(Border.ALL, BorderLineStyle.DASHED);

        return cf;
    }

    private void addLabel(WritableSheet sheet, int column, int row, String s)
            throws WriteException, RowsExceededException {
        WritableCellFormat wcf = getLabelFormat();
        Label label;

        label = new Label(column, row, s, wcf);
        sheet.addCell(label);
    }

    public WritableCellFormat getLabelFormat() throws WriteException {

        /* Format the Font */
        WritableFont wf = new WritableFont(WritableFont.COURIER, 10, WritableFont.BOLD);
        WritableCellFormat cf = new WritableCellFormat(wf);

        cf.setWrap(true);

        return cf;
    }

    public void setTitleList(List<String> titles) {
        this.titles = titles;
    }

    public void setCellDataList(List<List<String>> newList) {
        this.cellDataList = newList;
    }

//  private static void writeDataSheet(WritableSheet writableSheet) throws WriteException {
//      /* Format the Font */
//      WritableFont wf = new WritableFont(WritableFont.ARIAL,
//        10, WritableFont.BOLD);
//      WritableCellFormat cf = new WritableCellFormat(wf);
//      cf.setWrap(true);
//
//      /* Creates Label and writes date to one cell of sheet*/
//      Label l = new Label(0,0,"Date",cf);
//      writableSheet.addCell(l);
//      WritableCellFormat cf1 = new WritableCellFormat(DateFormats.FORMAT9);
//
//      DateTime dt = new DateTime(0,1,new Date(), cf1, DateTime.GMT);
//
//      writableSheet.addCell(dt);
//
//      /* Creates Label and writes float number to one cell of sheet*/
//      l = new Label(2,0,"Float", cf);
//      writableSheet.addCell(l);
//      WritableCellFormat cf2 = new WritableCellFormat(NumberFormats.FLOAT);
//      Number n = new Number(2,1,3.1415926535,cf2);
//      writableSheet.addCell(n);
//
//      n = new Number(2,2,-3.1415926535, cf2);
//      writableSheet.addCell(n);
//
//      /* Creates Label and writes float number upto 3
//         decimal to one cell of sheet */
//      l = new Label(3,0,"3dps",cf);
//      writableSheet.addCell(l);
//      NumberFormat dp3 = new NumberFormat("#.###");
//      WritableCellFormat dp3cell = new WritableCellFormat(dp3);
//
//      n = new Number(3,1,3.1415926535,dp3cell);
//      writableSheet.addCell(n);
//
//      /* Creates Label and adds 2 cells of sheet*/
//      l = new Label(4, 0, "Add 2 cells",cf);
//      writableSheet.addCell(l);
//
//      n = new Number(4,1,10);
//      writableSheet.addCell(n);
//
//      n = new Number(4,2,16);
//      writableSheet.addCell(n);
//
//      Formula f = new Formula(4,3, "E2+E3");
//      writableSheet.addCell(f);
//
//      /* Creates Label and multipies value of one cell of sheet by 2*/
//      l = new Label(5,0, "Multipy by 2",cf);
//      writableSheet.addCell(l);
//
//      n = new Number(5,1,10);
//      writableSheet.addCell(n);
//
//      f = new Formula(5,2, "F2 * 3");
//      writableSheet.addCell(f);
//
//      /* Creates Label and divide value of one cell of sheet by 2.5 */
//      l = new Label(6,0, "Divide",cf);
//      writableSheet.addCell(l);
//
//      n = new Number(6,1, 12);
//      writableSheet.addCell(n);
//
//      f = new Formula(6,2, "F2/2.5");
//      writableSheet.addCell(f);
//  }
//  private static void writeImageSheet(WritableSheet s,File imgFile) throws WriteException {
//      /* Creates Label and writes image to one cell of sheet*/
//      Label l = new Label(0, 0, "Image");
//      s.addCell(l);
//
//      WritableImage wi = new WritableImage(0, 3, 1, 1, imgFile/*new File("16-Temp-Out.png")*/);
//      s.addImage(wi);
//
//      /* Creates Label and writes hyperlink to one cell of sheet*/
//      l = new Label(0,15, "HYPERLINK");
//      s.addCell(l);
//      Formula f = new Formula(1, 15,
//        "HYPERLINK(\"http://www.andykhan.com/jexcelapi\", "+
//        "\"JExcelApi Home Page\")");
//      s.addCell(f);
//  }
//  private void addCaption(WritableSheet sheet, int column, int row, String s)
//                  throws RowsExceededException, WriteException {
//          Label label;
//          label = new Label(column, row, s, timesBoldUnderline);
//          sheet.addCell(label);
//  }
//
//  private void addNumber(WritableSheet sheet, int column, int row,
//                  Integer integer) throws WriteException, RowsExceededException {
//          Number number;
//          number = new Number(column, row, integer, times);
//          sheet.addCell(number);
//  }
}



