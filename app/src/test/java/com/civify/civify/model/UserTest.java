package com.civify.civify.model;

import com.civify.civify.BuildConfig;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static org.junit.Assert.assertEquals;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
@PowerMockIgnore({"org.mockito.*", "org.robolectric.*", "android.*"})
@PrepareForTest(User.class)
public class UserTest {
    private User mUser;

    @Before
    public void setUp() throws Exception {
        mUser = new User("validUsername", "validName", "validSurname", "valid@email.com", "validPassw0rd");
    }

    @After
    public void tearDown() {
        mUser = null;
    }

    @Test
    public void testConstructor() throws Exception {
        mUser = new User("validUsername", "validName", "validSurname", "valid@email.com", "validPassw0rd");

        assertEquals("validUsername", mUser.getUsername());
        assertEquals("validName", mUser.getName());
        assertEquals("validSurname", mUser.getSurname());
        assertEquals("valid@email.com", mUser.getEmail());
        assertEquals("validPassw0rd", mUser.getPassword());
    }

    @Test
    public void testConstructorWithInvalidUsername() throws Exception {
        try {
            mUser = new User("Invalid[]Username", "validName", "validSurname", "valid@email.com",
                    "validPassw0rd");
        } catch (Exception e) {
            assertEquals("Invalid username", e.getMessage());
        }
    }

    @Test
    public void testConstructorWithInvalidEmail() throws Exception {
        try {
            mUser = new User("validUsername", "validName", "validSurname", "invalidemail.com", "validPassw0rd");
        } catch (Exception e) {
            assertEquals("Invalid e-mail format", e.getMessage());
        }
    }

    @Test
    public void testConstructorWithInvalidPassword() throws Exception {
        try {
            mUser = new User("validUsername", "validName", "validSurname", "valid@email.com", "invalidPassword");
        } catch (Exception e) {
            assertEquals("Invalid password", e.getMessage());
        }
    }
/*
  // Username
  @Test
  public void testValidUsername() {
    assertEquals("Username is valid", true, User.checkValidUsername("ValidUsername"));
  }

  @Test
  public void testInvalidUsername() {
    assertEquals("Username is invalid", false, User.checkValidUsername("_Invalid__Username"));
  }

  // Email
  @Test
  public void testValidEmail() {
    assertEquals("Email is valid", true, User.checkValidEmail("example@domain.com"));
  }

  @Test
  public void testInvalidEmail() {
    assertEquals("Email is invalid", false, User.checkValidEmail("exampledomain.com"));
  }

  // Password
  @Test
  public void testValidPassword() {
    assertEquals("Password is valid", true, User.checkValidPassword("klfsd&Asdf9"));
  }

  @Test
  public void testInvalidShortPassword() {
    assertEquals("Password is invalid (too short)", false, User.checkValidPassword("1234"));
  }

  @Test
  public void testInvalidLongPassword() {
    assertEquals("Password is invalid (too long)", false, User.checkValidPassword("kfsklas.423234jklkjlkjl3&4234234234234234sf4ljk234.jl342"));
  }

  @Test
  public void testInvalidNoUppercasePassword() {
    assertEquals("Password is invalid (at least 1 uppercase letter is required)",
        false, User.checkValidPassword("asdf123456"));
  }

  @Test
  public void testInvalidNoLowercasePassword() {
    assertEquals("Password is invalid (at least 1 lowercase letter is required)",
        false, User.checkValidPassword("ASDF123456"));
  }

  @Test
  public void testInvalidNoNumberPassword() {
    assertEquals("Password is invalid (at least 1 number is required)",
        false, User.checkValidPassword("asdfASDF"));
  }

  @Test
  public void testSetValidUsername() throws Exception {
    mUser.setUsername("ValidUsernameTest");
    assertEquals("ValidUsernameTest", mUser.getUsername());
  }

  @Test
  public void testSetInvalidUsername() throws Exception {
    try {
      mUser.setUsername("InvalidUsername[]");
    } catch (Exception e) {
      assertEquals("Invalid username", e.getMessage());
    }
  }

  @Test
  public void testSetValidEmail() throws Exception {
    mUser.setEmail("validtest@email.com");
    assertEquals("validtest@email.com", mUser.getEmail());
  }

  @Test
  public void testSetInvalidEmail() throws Exception {
    try {
      mUser.setEmail("invalidemail.com");
    } catch (Exception e) {
      assertEquals("Invalid e-mail format", e.getMessage());
    }
  }

  @Test
  public void testSetValidPassword() throws Exception {
    mUser.setPassword("ValidPassw0rdTEST");
    assertEquals("ValidPassw0rdTEST", mUser.getPassword());
  }

  @Test
  public void testSetInvalidPassword() throws Exception {
    try {
      mUser.setPassword("InvalidPassword");
    } catch (Exception e) {
      assertEquals("Invalid password", e.getMessage());
    }
  }

  @Test
  public void testSetName() {
    mUser.setName("TestName");
    assertEquals("TestName", mUser.getName());
  }

  @Test
  public void testSetSurname() {
    mUser.setSurname("TestSurname");
    assertEquals("TestSurname", mUser.getSurname());
  }*/
}
