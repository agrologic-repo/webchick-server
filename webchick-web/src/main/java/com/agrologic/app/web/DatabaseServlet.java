
/*
* To change this template, choose Tools | Templates
* and open the template in the editor.
 */
package com.agrologic.app.web;



import com.agrologic.app.dao.impl.ConnectorDao;
import com.agrologic.app.utils.FileDownloadUtil;
import java.io.*;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.dbcp.BasicDataSource;
import org.apache.log4j.Logger;

/**
 *
 * @author Administrator
 */
public class DatabaseServlet extends HttpServlet {
    private final static String DATABASE_DRIVER   = "database.driver";
    private final static String DATABASE_PASSWORD = "database.password";
    private final static String DATABASE_URL      = "database.url";
    private final static String DATABASE_USER     = "database.user";
    private static final long   serialVersionUID  = 1L;
    private final Logger        logger            = Logger.getLogger(DatabaseServlet.class);
    private ConnectorDao        conDao;
    private Process             process;

    @Override
    public void init() throws ServletException {
        super.init();
        conDao = new ConnectorDao();
    }

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        try {
            String outfile = makeBackupDB();

            FileDownloadUtil.doDownload(response, outfile, "sql");
        } catch (Exception e) {
            logger.error("Unknown error. ", e);
        } finally {
            response.getOutputStream().close();
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">

    /**
     * Handles the HTTP <code>GET</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }    // </editor-fold>

    private String makeBackupDB() {
        BasicDataSource bds      = (BasicDataSource) conDao.getDataSource();
        String          userName = bds.getUsername();
        String          password = bds.getPassword();
        String          dbName   = bds.getUrl();
        int             dbIndex  = dbName.lastIndexOf("/");

        dbName = dbName.substring(dbIndex + 1);

        String mysqlPath = getMySQLPath() + "\\mysqldump";

        logger.info("Openning mysql from " + mysqlPath);

        File backupFile = new File("C:\\database.sql");

        try {
            logger.info("Creating database backup!");
            logger.info("Please wait ...");

            String[]   executeCmd = new String[] { mysqlPath, "-B", "-u" + userName, "-p" + password, dbName };
            FileWriter fileWriter = new FileWriter(backupFile);

            process = Runtime.getRuntime().exec(executeCmd);

            InputStreamReader inStreamReader = new InputStreamReader(process.getInputStream(), "utf-8");
            BufferedReader    bufferReader   = new BufferedReader(inStreamReader);
            String            line;

            while ((line = bufferReader.readLine()) != null) {
                fileWriter.write(line + "\n");
                Thread.sleep(5);
            }

            BufferedReader stdError = new BufferedReader(new InputStreamReader(process.getErrorStream()));

            // read any errors from the attempted command
            System.out.println("Here is the standard error of the command (if any):\n");

            while ((line = stdError.readLine()) != null) {
                System.out.println(line);
            }

            stdError.close();
            fileWriter.close();
            inStreamReader.close();
            bufferReader.close();
            logger.info("Backup file created successfully");
        } catch (InterruptedException ex) {
            logger.error("Failed create backup " + dbName, ex);
        } catch (IOException ex) {
            logger.error("Failed create backup " + dbName, ex);
        }

        return backupFile.getAbsolutePath();
    }

    private String getPath() {
        String        drive = System.getProperty("user.dir");
        String[]      pp    = drive.split("\\\\");
        StringBuilder sb    = new StringBuilder();

        for (int i = 0; i < 2; i++) {
            sb.append(pp[i]).append("\\");
        }

        return sb.toString();
    }

    private String getMySQLPath() {
        String mySqlPath = getPath();
        String version   = conDao.getMySQLVersion();

        mySqlPath = mySqlPath + "MySql\\MySql Server " + version + "\\bin";

        return mySqlPath;
    }
}



