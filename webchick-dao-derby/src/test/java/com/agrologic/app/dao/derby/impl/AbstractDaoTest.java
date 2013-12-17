package com.agrologic.app.dao.derby.impl;

import com.agrologic.app.dao.*;
import com.agrologic.app.model.*;
import com.agrologic.app.util.Base64;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.sql.Timestamp;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"/common-dao-context.xml", "/derby-dao-context.xml"})
@TransactionConfiguration
@Transactional
@Ignore
public abstract class AbstractDaoTest {

    @Autowired
    protected UserDao userDao;

    @Autowired
    protected CellinkDao cellinkDao;

    @Autowired
    protected ControllerDao controllerDao;

    @Autowired
    protected FlockDao flockDao;

    @Autowired
    protected AlarmDao alarmDao;

    @Autowired
    protected ProgramAlarmDao programAlarmDao;

    @Autowired
    protected LanguageDao languageDao;

    @Before
    public abstract void setUp() throws SQLException;

    @After
    public abstract void tearDown() throws SQLException;

    public final static User user() {
        User user = new User();
        user.setId(1L);
        user.setLogin("test");
        String encpsswd = Base64.encode("test");
        user.setPassword(encpsswd);
        user.setFirstName("test");
        user.setLastName("test");
        user.setPhone("0123456789");
        user.setUserRole(UserRole.USER);
        user.setCompany("Agrologic");
        user.setEmail("test@agrologic.com");
        user.setState(1);
        user.setValidate(true);
        return user;
    }

    public final static Cellink cellink() {
        Cellink cellink = new Cellink();
        cellink.setId(1L);
        cellink.setUserId(1L);
        cellink.setName("test");
        cellink.setPassword("test");
        cellink.setIp("1.1.1.1");
        cellink.setPort(55555);
        cellink.setTime(new Timestamp(System.currentTimeMillis()));
        cellink.setState(0);
        cellink.setScreenId(1L);
        cellink.setSimNumber("0501234567");
        cellink.setActual(false);
        cellink.setType("PC");
        cellink.setVersion("2.0W");
        return cellink;
    }

    public final static Controller controller() {
        Controller controller = new Controller();
        controller.setId(1L);
        controller.setCellinkId(1L);
        controller.setName("Image II");
        controller.setTitle("House 1");
        controller.setNetName("T901");
        controller.setProgramId(1L);
        controller.setArea(100);
        controller.setActive(true);
        controller.setHouseType("Broiler");
        return controller;

    }

    public final static Flock flock() {
        Flock flock = new Flock();
        flock.setFlockId(1L);
        flock.setControllerId(1L);
        flock.setFlockName("flock");
        flock.setStatus("open");
        flock.setStartDate("21/06/1977");
        flock.setEndDate("21/06/1977");
        return flock;
    }

    public final static Alarm alarm() {
        Alarm alarm = new Alarm();
        alarm.setId(1000L);
        alarm.setText("test");
        alarm.setUnicodeText("test");
        alarm.setLangId(1L);
        return alarm;
    }

    public final static Language language() {
        Language language = new Language();
        language.setId(1L);
        language.setLanguage("Eglish");
        language.setShortLang("en");
        return language;
    }
}
