package com.civify.model;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;

public class CivifyEmailCredentialsTest {
    private CivifyEmailCredentials mCivifyEmailCredentials;

    @Before
    public void setUp() throws Exception {
        mCivifyEmailCredentials = new CivifyEmailCredentials("email", "password");
    }

    @After
    public void tearDown() {
        mCivifyEmailCredentials = null;
    }

    @Test
    public void testConstructor() {
        assertEquals("email", mCivifyEmailCredentials.getEmail());
        assertEquals("password", mCivifyEmailCredentials.getPassword());
    }

    @Test
    public void testEmail() {
        mCivifyEmailCredentials.setEmail("email2");
        assertEquals("email2", mCivifyEmailCredentials.getEmail());
    }

    @Test
    public void testPassword() {
        mCivifyEmailCredentials.setPassword("password2");
        assertEquals("password2", mCivifyEmailCredentials.getPassword());
    }


}
