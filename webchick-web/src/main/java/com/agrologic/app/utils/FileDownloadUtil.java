package com.agrologic.app.utils;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * File download util provide static method that used to download file to the root directory .
 */
public class FileDownloadUtil {

    /**
     * Download file with specified name to root directory and send to response object.
     *
     * @param response      the response object
     * @param outfile       the name of output file
     * @param fileExtension the extension of file
     * @throws IOException if failed to read\write the file
     */
    public static void doDownload(HttpServletResponse response, String outfile, String fileExtension)throws IOException {
        // export action
        String filename;
        File fileToDownload;
        FileInputStream fileInputStream;
        filename = outfile;
        fileToDownload = new File(filename);
        fileInputStream = new FileInputStream(fileToDownload);

        response.setContentType("application/octet-stream");
        response.setHeader("Content-disposition", "attachment; filename=" + outfile + "." + fileExtension);

        int i;

        while ((i = fileInputStream.read()) != -1) {
            response.getOutputStream().write(i);
        }

        fileInputStream.close();
        response.getOutputStream().flush();
        fileInputStream.close();
    }
}



