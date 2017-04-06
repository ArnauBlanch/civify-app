package com.civify.civify.adapter;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;

public class LoginErrorTest {
    private LoginError mLoginError;

    @Before
    public void setUp() throws Exception {
        mLoginError =  new LoginError(LoginError.ErrorType.INVALID_CREDENTIALS,
                LoginAdapterImpl.INVALID_CREDENTIALS_MESSAGE);
    }

    @After
    public void tearDown() {
        mLoginError = null;
    }

    @Test
    public void testConstructor() {
        assertEquals(LoginError.ErrorType.INVALID_CREDENTIALS, mLoginError.getType());
        assertEquals(LoginAdapterImpl.INVALID_CREDENTIALS_MESSAGE, mLoginError.getMessage());
    }

    @Test
    public void validType() {
        mLoginError.setType(LoginError.ErrorType.NOT_LOGGED_IN);
        assertEquals(LoginError.ErrorType.NOT_LOGGED_IN, mLoginError.getType());
    }

    @Test
    public void validMessage() {
        mLoginError.setMessage("A message");
        assertEquals("A message", mLoginError.getMessage());
    }
}
