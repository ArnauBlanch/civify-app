package com.civify.model;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class UserTest {
    private User mUser;

    @Before
    public void setUp() throws Exception {
        mUser = new User("validUsername", "validName", "validSurname",
                "valid@email.com", "validPassw0rd", "validPassw0rd");
    }

    @After
    public void tearDown() {
        mUser = null;
    }

    @Test
    public void testConstructor() throws Exception {
        assertThat("validUsername", is(mUser.getUsername()));
        assertThat("validName", is(mUser.getName()));
        assertThat("validSurname", is(mUser.getSurname()));
        assertThat("valid@email.com", is(mUser.getEmail()));
        assertThat("validPassw0rd", is(mUser.getPassword()));
        assertThat("validPassw0rd", is(mUser.getPasswordConfirmation()));
    }

    @Test
    public void testUsername() {
        mUser.setUsername("testUsername");
        assertThat("testUsername", is(mUser.getUsername()));
    }

    @Test
    public void testName() {
        mUser.setName("testName");
        assertThat("testName", is(mUser.getName()));
    }

    @Test
    public void testSurname() {
        mUser.setSurname("testSurname");
        assertThat("testSurname", is(mUser.getSurname()));
    }

    @Test
    public void testEmail() {
        mUser.setEmail("test@email.com");
        assertThat("test@email.com", is(mUser.getEmail()));
    }

    @Test
    public void testPassword() {
        mUser.setPassword("testPassw0rd");
        assertThat("testPassw0rd", is(mUser.getPassword()));
    }

    @Test
    public void testPasswordConfirmation() {
        mUser.setPasswordConfirmation("testPassw0rd");
        assertThat("testPassw0rd", is(mUser.getPasswordConfirmation()));
    }
}
