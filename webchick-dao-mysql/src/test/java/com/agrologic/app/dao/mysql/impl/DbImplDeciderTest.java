package com.agrologic.app.dao.mysql.impl;

import com.agrologic.app.dao.DaoType;
import com.agrologic.app.dao.DbImplDecider;
import junit.framework.Assert;
import org.junit.Test;

public class DbImplDeciderTest {
    @Test
    public void testGetDao() throws Exception {
        DbImplDecider decider = DbImplDecider.use(DaoType.MYSQL);
        Assert.assertEquals(DbImplDecider.class, decider.getClass());
    }
}
