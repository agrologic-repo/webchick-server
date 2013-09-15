/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.agrologic.app.dao.mysql.impl;

import com.agrologic.app.model.User;
import com.agrologic.app.util.Base64;
import org.junit.Test;

import java.sql.SQLException;

import static org.unitils.reflectionassert.ReflectionAssert.assertReflectionEquals;

public class UserDaoImplTest extends AbstractDaoTest {

    @Test
    public void getCanFindUserAfterInsert() throws SQLException {
        User expected = user();
        userDao.insert(expected);

        User actual = userDao.validate("test", Base64.encode("test"));
        // set user id after adding to the database;
        expected.setId(actual.getId());
        // set password without encoding
        expected.setPassword("test");
        assertReflectionEquals(expected, actual);
    }

    @Override
    public void setUp() throws SQLException {

    }

    @Override
    public void tearDown() throws SQLException {

    }
}
