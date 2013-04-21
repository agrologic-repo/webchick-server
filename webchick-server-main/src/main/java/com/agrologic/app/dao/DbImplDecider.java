package com.agrologic.app.dao;

import com.agrologic.app.dao.derby.DerbyDaoFactory;
import com.agrologic.app.dao.mysql.MySqlDaoFactory;

public class DbImplDecider {
    public static DaoFactory getDaoFactory(DaoType daoType) {
        switch (daoType) {
            case DERBY:
                return DerbyDaoFactory.instance();
            case MYSQL:
                return MySqlDaoFactory.instance();

            default:
                return null;
        }
    }
}
