package com.agrologic.app.web;

import com.agrologic.app.config.Configuration;
import com.agrologic.app.dao.DaoType;
import com.agrologic.app.dao.DbImplDecider;
import com.agrologic.app.dao.VersionDao;
import com.agrologic.app.utils.FileDownloadUtil;
import org.apache.commons.lang.SystemUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;


public class DupDatabaseServlet extends AbstractServlet {
    private Process process;

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     *
     * @param request  servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException      if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");


        try {
            String outfile = makeBackupDB();
            FileDownloadUtil.doDownload(response, outfile, "sql");
        } catch (Exception e) {
            logger.error("Unknown error during downloading dump file. ", e);
        } finally {
            response.getOutputStream().close();
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">

    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request  servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException      if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request  servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException      if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }    // </editor-fold>

    private String makeBackupDB() {
        Configuration CONFIG = new Configuration();
        String userName = CONFIG.getDbUser();
        String password = CONFIG.getDbPassword();
        String dbName = CONFIG.getDbUrl();
        int dbIndex = dbName.lastIndexOf("/");
        int askChar = dbName.lastIndexOf("?");
        dbName = dbName.substring(dbIndex + 1, askChar);

        String mysqlPath = System.getenv("MYSQL_HOME");
        File backupFile;
        if (mysqlPath == null || mysqlPath.equals("")) {
            mysqlPath = getMySQLPath() + "\\mysqldump";
            logger.info("Openning mysql from " + mysqlPath);
            backupFile = new File("C:\\database.sql");
        } else {
            mysqlPath = System.getenv("MYSQL_HOME") + "\\bin\\mysqldump";
            logger.info("Opening mysql from " + mysqlPath);
            String tomcatTempDir = SystemUtils.getJavaIoTmpDir().getAbsolutePath();
            backupFile = new File(tomcatTempDir + File.separator + "database.sql");
        }

        try {
            logger.info("Creating database dump!");
            logger.info("Please wait ...");

            String[] executeCmd = new String[]{mysqlPath, "-B", "-u" + userName, "-p" + password, dbName};
            FileWriter fileWriter = new FileWriter(backupFile);

            process = Runtime.getRuntime().exec(executeCmd);

            InputStreamReader inStreamReader = new InputStreamReader(process.getInputStream(), "utf-8");
            BufferedReader bufferReader = new BufferedReader(inStreamReader);
            String line;

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
            logger.error("Failed create dump sql file " + dbName, ex);
        } catch (IOException ex) {
            logger.error("Failed create dump sql file " + dbName, ex);
        } catch (Exception ex) {
            logger.error("Unknown error during creating dump sql file " + dbName, ex);
        }

        return backupFile.getAbsolutePath();
    }

    private String getPath() {
        String drive = System.getProperty("user.dir");
        String[] pp = drive.split("\\\\");
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < 2; i++) {
            sb.append(pp[i]).append("\\");
        }

        return sb.toString();
    }

    private String getMySQLPath() {
        String mySqlPath = getPath();
        VersionDao versionDao = DbImplDecider.use(DaoType.MYSQL).getDao(VersionDao.class);
        String version = versionDao.getVersion();
        mySqlPath = mySqlPath + "MySql\\MySql Server " + version + "\\bin";
        return mySqlPath;
    }
}



