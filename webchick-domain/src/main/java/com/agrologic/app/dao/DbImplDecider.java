package com.agrologic.app.dao;

import org.apache.commons.lang.Validate;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * This decider create instance of deciders by given DaoType {DERBY or MySQL }  and return bean object
 */
public class DbImplDecider {
    public DbImplDecider(ApplicationContext daoContext) {
        this.daoContext = daoContext;
    }

    public <T> T getDao(Class<T> daoType) {
        return daoContext.getBean(daoType);
    }

    public static DbImplDecider use(DaoType daoType) {
        Validate.notNull(daoType, "Dao Type should not be null!");
        return daoType == DaoType.MYSQL ? MySqlHolder.mysqlInstance : DerbyHolder.derbyInstance;
    }

    private static class MySqlHolder {
        static DbImplDecider mysqlInstance = new DbImplDecider(new ClassPathXmlApplicationContext("/common-dao-context.xml", "/mysql-dao-context.xml"));
    }

    private static class DerbyHolder {
        static DbImplDecider derbyInstance = new DbImplDecider(new ClassPathXmlApplicationContext("/common-dao-context.xml", "/derby-dao-context.xml"));
    }
//    public static DbImplDecider use(DaoType daoType) {
//        Validate.notNull(daoType, "Dao Type should not be null!");
//        return daoType == DaoType.MYSQL ? mysqlInstance : derbyInstance;
//    }
//
//    private static DbImplDecider mysqlInstance = new DbImplDecider(new ClassPathXmlApplicationContext("/common-dao-context.xml", "/mysql-dao-context.xml"));
//    private static DbImplDecider derbyInstance = new DbImplDecider(new ClassPathXmlApplicationContext("/common-dao-context.xml", "/derby-dao-context.xml"));

    private final ApplicationContext daoContext;
}
