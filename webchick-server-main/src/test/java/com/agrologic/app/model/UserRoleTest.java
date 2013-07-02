/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.agrologic.app.model;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class UserRoleTest {

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
