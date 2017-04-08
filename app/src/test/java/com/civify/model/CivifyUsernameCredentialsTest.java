package com.civify.model;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;

public class CivifyUsernameCredentialsTest {
    private CivifyUsernameCredentials mCivifyUsernameCredentials;

    @Before
    public void setUp() throws Exception {
        mCivifyUsernameCredentials = new CivifyUsernameCredentials("username", "password");
    }

    @After
    public void tearDown() {
        mCivifyUsernameCredentials = null;
    }

    @Test
    public void testConstructor() {
        assertEquals("username", mCivifyUsernameCredentials.getUsername());
        assertEquals("password", mCivifyUsernameCredentials.getPassword());
    }

    @Test
    public void testUsername() {
        mCivifyUsernameCredentials.setUsername("username2");
        assertEquals("username2", mCivifyUsernameCredentials.getUsername());
    }

    @Test
    public void testPassword() {
        mCivifyUsernameCredentials.setPassword("password2");
        assertEquals("password2", mCivifyUsernameCredentials.getPassword());
    }
}
