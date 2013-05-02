package com.agrologic.app.dao;

import org.apache.commons.lang.Validate;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class DbImplDecider {
    public DbImplDecider(ApplicationContext daoContext) {
        this.daoContext = daoContext;
    }

    public <T> T getDao(Class<T> daoType) {
        return daoContext.getBean(daoType);
    }

    public static DbImplDecider use(DaoType daoType) {
        Validate.notNull(daoType, "Dao Type should not be null!");
        return daoType == DaoType.MYSQL ? mysqlInstance : derbyInstance;
    }

    private static DbImplDecider mysqlInstance = new DbImplDecider(new ClassPathXmlApplicationContext("/mysql-dao-context.xml"));
    private static DbImplDecider derbyInstance = new DbImplDecider(new ClassPathXmlApplicationContext("/derby-dao-context.xml"));
    private final ApplicationContext daoContext;
}
