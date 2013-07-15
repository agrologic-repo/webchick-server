/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.agrologic.app.dao.mysql.impl;

import com.agrologic.app.dao.UserDao;
import com.agrologic.app.model.User;
import com.agrologic.app.model.UserRole;
import com.agrologic.app.util.Base64;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;

import static org.unitils.reflectionassert.ReflectionAssert.assertReflectionEquals;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"/common-dao-context.xml", "/mysql-dao-context.xml"})
@TransactionConfiguration
@Transactional
public class UserDaoImplTest {

    @Autowired
    private UserDao userDao;

    @Test
    public void getCanFindUserAfterInsert() throws SQLException {
        User user = new User();
        user.setLogin("test");
        String encpsswd = Base64.encode("test");
        user.setPassword(encpsswd);
        user.setFirstName("test");
        user.setLastName("test");
        user.setPhone("0000000000");
        user.setUserRole(UserRole.USER);
        user.setCompany("Agrologic");
        user.setEmail("test@agrologic.com");
        user.setState(1);
        user.setValidate(true);
        userDao.insert(user);

        User actual = userDao.validate("test", encpsswd);
        user.setId(actual.getId());
        user.setPassword("test");
        assertReflectionEquals(user, actual);
    }
}
