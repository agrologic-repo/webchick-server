
/*
* To change this template, choose Tools | Templates
* and open the template in the editor.
 */
package com.agrologic.app.utils;

//~--- JDK imports ------------------------------------------------------------

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Timestamp;

/**
 * @author JanL
 */
public class FileDownloadUtil {
    public static void doDownload(HttpServletResponse response, String outfile)
            throws FileNotFoundException, IOException {

        // export action
        String filename = outfile;
        File fileToDownload = new File(filename);
        FileInputStream fileInputStream = new FileInputStream(fileToDownload);
        Timestamp dateTimestamp = new Timestamp(System.currentTimeMillis());

        response.setContentType("application/octet-stream");
        response.setHeader("Content-disposition", "attachment; filename=" + outfile);

        int i;

        while ((i = fileInputStream.read()) != -1) {
            response.getOutputStream().write(i);
        }

        fileInputStream.close();
        response.getOutputStream().flush();
        fileInputStream.close();
    }

    public static void doDownload(HttpServletResponse response, String outfile, String fileExtension)
            throws FileNotFoundException, IOException {

        // export action
        String filename = outfile;
        File fileToDownload = new File(filename);
        FileInputStream fileInputStream = new FileInputStream(fileToDownload);
        Timestamp dateTimestamp = new Timestamp(System.currentTimeMillis());

        response.setContentType("application/octet-stream");
        response.setHeader("Content-disposition",
                "attachment; filename=" + outfile + dateTimestamp + "." + fileExtension);

        int i;

        while ((i = fileInputStream.read()) != -1) {
            response.getOutputStream().write(i);
        }

        fileInputStream.close();
        response.getOutputStream().flush();
        fileInputStream.close();
    }
}



