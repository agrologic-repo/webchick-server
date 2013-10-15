package com.agrologic.app.utils;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Timestamp;

public class FileDownloadUtil {
    public static void doDownload(HttpServletResponse response, String outfile)
            throws IOException {

        // export action
        String filename = outfile;
        File fileToDownload = new File(filename);
        FileInputStream fileInputStream = new FileInputStream(fileToDownload);

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
            throws IOException {

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



