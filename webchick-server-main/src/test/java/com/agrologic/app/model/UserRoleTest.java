/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.agrologic.app.model;

import com.agrologic.app.model.UserRole;
import org.junit.*;
import static org.junit.Assert.*;

/**
 *
 * @author Administrator
 */
public class UserRoleTest {

    public UserRoleTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test
    public void getUserRoleValue() {
        int role = 1;
        UserRole theRole = UserRole.get(role);
        assertEquals(UserRole.USER.getValue(), theRole.getValue());
    }

    @Test
    public void getUserRoleText() {
        int role = 2;
        UserRole theRole = UserRole.get(role);
        assertEquals("DISTRIBUTOR", theRole.getText());
    }

    @Test
    public void testUserRole() {
        int role = -9;
        UserRole theRole = UserRole.get(role);
        assertNotNull(theRole);
        assertEquals(UserRole.UNKNOWN, theRole);

    }
}
