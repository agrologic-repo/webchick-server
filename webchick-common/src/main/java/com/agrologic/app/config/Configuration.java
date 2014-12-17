package com.agrologic.app.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Observable;
import java.util.prefs.Preferences;

public class Configuration extends Observable {
    private final Logger logger = LoggerFactory.getLogger(Configuration.class);
    private static final String PROTOCOL_FIELD = "protocol";
    public static final String LANGUAGE_FIELD = "language";
    public static final String ACCESS_FIELD = "access";
    public static final String COM_BAUD_FIELD = "com.baud";
    public static final String DATABASE_DRIVER_FIELD = "database.driver";
    public static final String DATABASE_PASSWORD_FIELD = "database.password";
    public static final String DATABASE_URL_FIELD = "database.url";
    public static final String DATABASE_USER_FIELD = "database.user";
    public static final String KEEP_ALIVE_TIMEOUT_FIELD = "keepalive.timeout";
    public static final String MAX_ERRORS_FIELD = "com.maxerr";
    public static final String DELAY_NEXT_FIELD = "com.next";
    public static final String DELAY_SOT_FIELD = "com.sot";
    public static final String DELAY_EOT_FIELD = "com.eot";
    public static final String RUN_AT_STARTUP_FIELD = "runatstartup";
    public static final String COMPORT_FIELD = "com.port";
    public static final String SERVER_IP_FIELD = "server.ip";
    public static final String SERVER_PORT_FIELD = "server.port";
    public static final String USER_ID_FIELD = "user.id";
    public static final String CELLINK_ID_FIELD = "cellink.id";
    public static final String VERSION_FIELD = "version";
    public static final String WEBCHICK_URI_FIELD = "webchick.uri";
    // default data values
    public static final String DEFAULT_PROTOCOL = "0";
    public static final String DEFAULT_LANGUAGE = "English";
    public static final String DEFAULT_ACCESS = "regular";
    public static final String DEFAULT_COM_BAUD = "2400";
    public static final String DEFAULT_COMPORT = "COM1";
    public static final String DEFAULT_CONFIG = "config.xml";
    public static final String DEFAULT_CONFIG_REG = "webchick-config";
    public static final String DEFAULT_DATABASE_DRIVER = "com.mysql.jdbc.Driver";
    public static final String DEFAULT_DATABASE_PASSWORD = "pass123$";
    public static final String DEFAULT_DATABASE_URL = "jdbc:mysql://localhost:3306/agrodb?autoReconnect=true";
    public static final String DEFAULT_DATABASE_USER = "root";
    public static final String DEFAULT_DELAY_SOT = "5000";
    public static final String DEFAULT_DELAY_EOT = "10000";
    public static final String DEFAULT_DELAY_NEXT = "5000";
    public static final Integer DEFAULT_KEEP_ALIVE_TIMEOUT = 3;
    public static final String DEFAULT_MAX_ERRORS = "3";
    public static final String DEFAULT_SERVER_IP = "192.168.1.1";
    public static final Integer DEFAULT_SERVER_PORT = 8080;
    public static final String DEFAULT_USER_ID = "1";
    public static final String DEFAULT_CELLINK_ID = "1";
    public static final String DEFAULT_WEBCHICK_URI = "http://localhost:8080/webchick/startpage.html";
    public static final Boolean DEFAULT_RUN_AT_STARTUP = Boolean.FALSE;
    public static final String DEFAULT_VERSION = "6.7.4";
    private String language;

    /**
     * access value to webchick
     */
    private String access;

    /**
     * baud
     */
    private String baud;

    /**
     * cellink id for loading database
     */
    private String cellinkId;

    /**
     * com port
     */
    private String comPort;

    /**
     * DB driver.
     */
    private String dbDriver;

    /**
     * DB password.
     */
    private String dbPassword;

    /**
     * DB url and schema .
     */
    private String dbUrl;

    /**
     * DB user name.
     */
    private String dbUser;

    /**
     * EOT.
     */
    private String eotDelay;

    /**
     * Server Location IP.
     */
    private String ip;

    /**
     * Keep alive interval.
     */
    private Integer keepalive;

    /**
     * Maximum errors.
     */
    private String maxErrors;

    /**
     * Next request delay.
     */
    private String nextDelay;

    /**
     * Server port.
     */
    private Integer port;

    /**
     * the communication protocol {0-2400 ASCII, 1-9600 ASCII, 2-2400 BINARY, 3-9600 BINARY}
     */
    private String protocol;

    /**
     * Run on start windows flag.
     */
    private Boolean runAtStartup;

    /**
     *
     */
    private Preferences settingPreferences;

    /**
     * SOT.
     */
    private String sotDelay;

    /**
     * user id for loading database
     */
    private String userId;

    /**
     * version
     */
    private String version;

    /**
     * local host uri for webchick in browser version
     */
    private String webchickURI;

    /**
     * Constructor.
     */
    public Configuration() {
        super();
        loadConfiguration();

        // Preferences
        loadPreferences();

        // init fields from Preferences
        initPreferences();

        //
        saveToXmlFile();
    }

    public void loadConfiguration() {
        InputStream is;
        try {
            is = new BufferedInputStream(new FileInputStream(DEFAULT_CONFIG));
            Preferences.importPreferences(is);
            settingPreferences = Preferences.userRoot();
        } catch (Exception e) {
            logger.error("Could not read configuration file", e);
        }
    }

    public void loadPreferences() {
        settingPreferences = Preferences.userRoot().node(DEFAULT_CONFIG_REG);
    }

    private void initPreferences() {
        protocol = settingPreferences.get(PROTOCOL_FIELD, DEFAULT_PROTOCOL);
        Protocol type = Protocol.get(Integer.valueOf(protocol));
        baud = type.getBaud();
        language = settingPreferences.get(LANGUAGE_FIELD, DEFAULT_LANGUAGE);
        ip = settingPreferences.get(SERVER_IP_FIELD, DEFAULT_SERVER_IP);
        port = settingPreferences.getInt(SERVER_PORT_FIELD, DEFAULT_SERVER_PORT);
        keepalive = settingPreferences.getInt(KEEP_ALIVE_TIMEOUT_FIELD, DEFAULT_KEEP_ALIVE_TIMEOUT);
        dbDriver = settingPreferences.get(DATABASE_DRIVER_FIELD, DEFAULT_DATABASE_DRIVER);
        dbUrl = settingPreferences.get(DATABASE_URL_FIELD, DEFAULT_DATABASE_URL);
        dbUser = settingPreferences.get(DATABASE_USER_FIELD, DEFAULT_DATABASE_USER);
        dbPassword = settingPreferences.get(DATABASE_PASSWORD_FIELD, DEFAULT_DATABASE_PASSWORD);
        sotDelay = settingPreferences.get(DELAY_SOT_FIELD, DEFAULT_DELAY_SOT);
        eotDelay = settingPreferences.get(DELAY_EOT_FIELD, DEFAULT_DELAY_EOT);
        nextDelay = settingPreferences.get(DELAY_NEXT_FIELD, DEFAULT_DELAY_NEXT);
        maxErrors = settingPreferences.get(MAX_ERRORS_FIELD, DEFAULT_MAX_ERRORS);
        runAtStartup = settingPreferences.getBoolean(RUN_AT_STARTUP_FIELD, DEFAULT_RUN_AT_STARTUP);
        comPort = settingPreferences.get(COMPORT_FIELD, DEFAULT_COMPORT);
        version = settingPreferences.get(VERSION_FIELD, DEFAULT_VERSION);
        webchickURI = settingPreferences.get(WEBCHICK_URI_FIELD, DEFAULT_WEBCHICK_URI);
        access = settingPreferences.get(ACCESS_FIELD, DEFAULT_ACCESS);
        userId = settingPreferences.get(USER_ID_FIELD, DEFAULT_USER_ID);
        cellinkId = settingPreferences.get(CELLINK_ID_FIELD, DEFAULT_CELLINK_ID);
    }

    public void saveToXmlFile() {
        try {
            settingPreferences.exportNode(new FileOutputStream(DEFAULT_CONFIG));
        } catch (Exception e) {
            e.printStackTrace();   //TODO :  Add message to user
        }
    }

    public void setLanguage(String l) {
        this.language = l;
    }

    public void setNetworkConfig(String ip, Integer port) {
        setIp(ip);
        setPort(port);
        setChanged();
        notifyObservers();
    }

    public void setMaxError(String maxerr) {
        setMaxErrors(maxerr);
        setChanged();
        notifyObservers();
    }

    public void setDBConfig(String dbDriver, String dbUrl, String dbUser, String dbPassword) {
        setDbDriver(dbDriver);
        setDbUrl(dbUrl);
        setDbUser(dbUser);
        setDbPassword(dbPassword);
        setChanged();
        notifyObservers();
    }

    public void reInitPreferences() {
        initPreferences();
        setChanged();
        notifyObservers();
    }

    public String getLanguage() {
        return language;
    }

    public String getDbDriver() {
        return dbDriver;
    }

    public void setDbDriver(String dbDriver) {
        this.dbDriver = dbDriver;
    }

    public String getDbPassword() {
        return dbPassword;
    }

    public void setDbPassword(String dbPassword) {
        this.dbPassword = dbPassword;
    }

    public String getDbUrl() {
        return dbUrl;
    }

    public void setDbUrl(String dbUrl) {
        this.dbUrl = dbUrl;
    }

    public String getDbUser() {
        return dbUser;
    }

    public void setDbUser(String dbUser) {
        this.dbUser = dbUser;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Integer getKeepalive() {
        return keepalive;
    }

    public void setKeepalive(Integer keepalive) {
        this.keepalive = keepalive;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getEotDelay() {
        return eotDelay;
    }

    public void setEotDelay(String eotDelay) {
        this.eotDelay = eotDelay;
    }

    public String getNextDelay() {
        return nextDelay;
    }

    public void setNextDelay(String nextDelay) {
        this.nextDelay = nextDelay;
    }

    public String getSotDelay() {
        return sotDelay;
    }

    public void setSotDelay(String sotDelay) {
        this.sotDelay = sotDelay;
    }

    public String getMaxErrors() {
        return maxErrors;
    }

    public void setMaxErrors(String maxErrors) {
        this.maxErrors = maxErrors;
    }

    public Boolean runOnWindowsStart() {
        return runAtStartup;
    }

    public void setStartup(Boolean startup) {
        this.runAtStartup = startup;
    }

    public String getVersion() {
        return " Version " + version;
    }

    public String getAccess() {
        return access;
    }

    public void setAccess(String access) {
        this.access = access;
    }

    public String getBaud() {
        return baud;
    }

    public void setBaud(String baud) {
        this.baud = baud;
    }

    public String getCellinkId() {
        return cellinkId;
    }

    public void setCellinkId(String cellinkId) {
        this.cellinkId = cellinkId;
    }

    public String getComPort() {
        return comPort;
    }

    public void setComPort(String comPort) {
        this.comPort = comPort;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getWebchickURI() {
        return webchickURI;
    }

    public void setWebchickURI(String webchickURI) {
        this.webchickURI = webchickURI;
    }

    public void saveUpdatePreferences() {
        settingPreferences.put(PROTOCOL_FIELD, getProtocol());
        settingPreferences.put(LANGUAGE_FIELD, getLanguage());
        settingPreferences.put(SERVER_IP_FIELD, getIp());
        settingPreferences.putInt(SERVER_PORT_FIELD, getPort());
        settingPreferences.putInt(KEEP_ALIVE_TIMEOUT_FIELD, getKeepalive());
        settingPreferences.put(COMPORT_FIELD, getComPort());
        settingPreferences.put(DATABASE_DRIVER_FIELD, getDbDriver());
        settingPreferences.put(DATABASE_URL_FIELD, getDbUrl());
        settingPreferences.put(DATABASE_USER_FIELD, getDbUser());
        settingPreferences.put(DATABASE_PASSWORD_FIELD, getDbPassword());
        settingPreferences.put(COM_BAUD_FIELD, getBaud());
        settingPreferences.put(DELAY_SOT_FIELD, getSotDelay());
        settingPreferences.put(DELAY_EOT_FIELD, getEotDelay());
        settingPreferences.put(DELAY_NEXT_FIELD, getNextDelay());
        settingPreferences.put(MAX_ERRORS_FIELD, getMaxErrors());
        settingPreferences.putBoolean(RUN_AT_STARTUP_FIELD, runOnWindowsStart());
        settingPreferences.put(ACCESS_FIELD, getAccess());
        settingPreferences.put(WEBCHICK_URI_FIELD, getWebchickURI());
        settingPreferences.put(CELLINK_ID_FIELD, getCellinkId());
        settingPreferences.put(USER_ID_FIELD, getUserId());
        saveToXmlFile();
        initPreferences();
    }


    @Override
    public String toString() {
        return "IP = " + ip;
    }
}


