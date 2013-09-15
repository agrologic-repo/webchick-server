
/*
* To change this template, choose Tools | Templates
* and open the template in the editor.
 */
package com.agrologic.app.utils;


import jxl.CellView;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.format.UnderlineStyle;
import jxl.write.*;
import jxl.write.Number;
import jxl.write.biff.RowsExceededException;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Date;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

//~--- JDK imports ------------------------------------------------------------

public class WriteExcelUtil {
    private String inputFile;
    private WritableCellFormat times;
    private WritableCellFormat timesBoldUnderline;

    public void setOutputFile(String inputFile) {
        this.inputFile = inputFile;
    }

    public void write() throws IOException, WriteException {
        try {
            File file = new File(inputFile);
            WorkbookSettings wbSettings = new WorkbookSettings();

            wbSettings.setLocale(new Locale("en", "EN"));

            WritableWorkbook workbook = Workbook.createWorkbook(file, wbSettings);

            workbook.createSheet("Report", 0);

            WritableSheet excelSheet = workbook.getSheet(0);

            // WritableSheet s1 = workbook.createSheet("Report", 0);
            URL resource = getClass().getResource("16-Temp-Out.png");
            File imgFile = new File(resource.toURI());

            createLabel(excelSheet);
            createContent(excelSheet);

//          writeDataSheet(excelSheet);
//          writeImageSheet(excelSheet,imgFile);
            workbook.write();
            workbook.close();
        } catch (URISyntaxException ex) {
            Logger.getLogger(WriteExcelUtil.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static void writeImageSheet(WritableSheet s, File imgFile) throws WriteException {

        /* Creates Label and writes image to one cell of sheet */
        Label l = new Label(0, 0, "Image");

        s.addCell(l);

        WritableImage wi = new WritableImage(0, 3, 1, 1, imgFile /* new File("16-Temp-Out.png") */);

        s.addImage(wi);

        /* Creates Label and writes hyperlink to one cell of sheet */
        l = new Label(0, 15, "HYPERLINK");
        s.addCell(l);

        Formula f = new Formula(1, 15,
                "HYPERLINK(\"http://www.andykhan.com/jexcelapi\", " + "\"JExcelApi Home Page\")");

        s.addCell(f);
    }

    private static void writeDataSheet(WritableSheet s) throws WriteException {

        /* Format the Font */
        WritableFont wf = new WritableFont(WritableFont.ARIAL, 10, WritableFont.BOLD);
        WritableCellFormat cf = new WritableCellFormat(wf);

        cf.setWrap(true);

        /* Creates Label and writes date to one cell of sheet */
        Label l = new Label(0, 0, "Date", cf);

        s.addCell(l);

        WritableCellFormat cf1 = new WritableCellFormat(DateFormats.FORMAT9);
        DateTime dt = new DateTime(0, 1, new Date(), cf1, DateTime.GMT);

        s.addCell(dt);

        /* Creates Label and writes float number to one cell of sheet */
        l = new Label(2, 0, "Float", cf);
        s.addCell(l);

        WritableCellFormat cf2 = new WritableCellFormat(NumberFormats.FLOAT);
        Number n = new Number(2, 1, 3.1415926535, cf2);

        s.addCell(n);
        n = new Number(2, 2, -3.1415926535, cf2);
        s.addCell(n);

        /*
         *  Creates Label and writes float number upto 3
         * decimal to one cell of sheet
         */
        l = new Label(3, 0, "3dps", cf);
        s.addCell(l);

        NumberFormat dp3 = new NumberFormat("#.###");
        WritableCellFormat dp3cell = new WritableCellFormat(dp3);

        n = new Number(3, 1, 3.1415926535, dp3cell);
        s.addCell(n);

        /* Creates Label and adds 2 cells of sheet */
        l = new Label(4, 0, "Add 2 cells", cf);
        s.addCell(l);
        n = new Number(4, 1, 10);
        s.addCell(n);
        n = new Number(4, 2, 16);
        s.addCell(n);

        Formula f = new Formula(4, 3, "E2+E3");

        s.addCell(f);

        /* Creates Label and multipies value of one cell of sheet by 2 */
        l = new Label(5, 0, "Multipy by 2", cf);
        s.addCell(l);
        n = new Number(5, 1, 10);
        s.addCell(n);
        f = new Formula(5, 2, "F2 * 3");
        s.addCell(f);

        /* Creates Label and divide value of one cell of sheet by 2.5 */
        l = new Label(6, 0, "Divide", cf);
        s.addCell(l);
        n = new Number(6, 1, 12);
        s.addCell(n);
        f = new Formula(6, 2, "F2/2.5");
        s.addCell(f);
    }

    private void createLabel(WritableSheet sheet) throws WriteException {

        // Lets create a times font
        WritableFont times10pt = new WritableFont(WritableFont.TIMES, 10);

        // Define the cell format
        times = new WritableCellFormat(times10pt);

        // Lets automatically wrap the cells
        times.setWrap(true);

        // Create create a bold font with unterlines
        WritableFont times10ptBoldUnderline = new WritableFont(WritableFont.TIMES, 10, WritableFont.BOLD, false,
                UnderlineStyle.SINGLE);

        timesBoldUnderline = new WritableCellFormat(times10ptBoldUnderline);

        // Lets automatically wrap the cells
        timesBoldUnderline.setWrap(true);

        CellView cv = new CellView();

        cv.setFormat(times);
        cv.setFormat(timesBoldUnderline);
        cv.setAutosize(true);

        // Write a few headers
        addCaption(sheet, 0, 0, "Header 1");
        addCaption(sheet, 1, 0, "This is another header");
    }

    private void createContent(WritableSheet sheet) throws WriteException, RowsExceededException {

        // Write a few number
        for (int i = 1; i < 10; i++) {

            // First column
            addNumber(sheet, 0, i, i + 10);

            // Second column
            addNumber(sheet, 1, i, i * i);
        }

        // Lets calculate the sum of it
        StringBuffer buf = new StringBuffer();

        buf.append("SUM(A2:A10)");

        Formula f = new Formula(0, 10, buf.toString());

        sheet.addCell(f);
        buf = new StringBuffer();
        buf.append("SUM(B2:B10)");
        f = new Formula(1, 10, buf.toString());
        sheet.addCell(f);

        // Now a bit of text
        for (int i = 12; i < 20; i++) {

            // First column
            addLabel(sheet, 0, i, "Boring text " + i);

            // Second column
            addLabel(sheet, 1, i, "Another text");
        }
    }

    private void addCaption(WritableSheet sheet, int column, int row, String s)
            throws RowsExceededException, WriteException {
        Label label;

        label = new Label(column, row, s, timesBoldUnderline);
        sheet.addCell(label);
    }

    private void addNumber(WritableSheet sheet, int column, int row, Integer integer)
            throws WriteException, RowsExceededException {
        Number number;

        number = new Number(column, row, integer, times);
        sheet.addCell(number);
    }

    private void addLabel(WritableSheet sheet, int column, int row, String s)
            throws WriteException, RowsExceededException {
        Label label;

        label = new Label(column, row, s, times);
        sheet.addCell(label);
    }

    public static void main(String[] args) throws WriteException, IOException {
        WriteExcelUtil test = new WriteExcelUtil();

        test.setOutputFile("c:/temp/lars.xls");
        test.write();
        System.out.println("Please check the result file under c:/temp/lars.xls ");
    }
}



