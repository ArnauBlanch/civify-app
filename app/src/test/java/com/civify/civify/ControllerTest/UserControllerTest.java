package com.civify.civify.ControllerTest;

import com.civify.civify.Controller.UserController;
import com.civify.civify.Model.User;
import com.civify.civify.Model.UserService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.verifyNew;
import static org.powermock.api.mockito.PowerMockito.whenNew;

@RunWith(PowerMockRunner.class)
@PrepareForTest({User.class, UserController.class})
public class UserControllerTest {
  private UserService mUserService;
  private UserController mUserController;

  @Before
  public void setUp() {
    mUserService = mock(UserService.class);
    mUserController = new UserController(mUserService);
  }

  @After
  public void tearDown() {
    mUserController = null;
  }

  @Test
  public void testRegisterUser() throws Exception {
    User mockUser = PowerMockito.mock(User.class);
    whenNew(User.class).withArguments("validUsername", "validName", "validSurname", "valid@email.com", "validPassw0rd").thenReturn(mockUser);
    mUserController.registerUser("validUsername", "validName", "validSurname", "valid@email.com", "validPassw0rd");

    verify(mUserService).registerUser("validUsername", "validName", "validSurname", "valid@email.com", "validPassw0rd");
    verifyNew(User.class).withArguments("validUsername", "validName", "validSurname", "valid@email.com", "validPassw0rd");
  }

  @Test
  public void testCheckValidUnusedUsername() {
    PowerMockito.mockStatic(User.class);
    when(User.checkValidUsername("ValidUnusedUser")).thenReturn(true);
    when(mUserService.checkUnusedUsername("ValidUnusedUser")).thenReturn(true);

    assertEquals(UserController.VALID_UNUSED, mUserController.checkValidUnusedUsername("ValidUnusedUser"));

    PowerMockito.verifyStatic();
    User.checkValidUsername("ValidUnusedUser");
    verify(mUserService).checkUnusedUsername("ValidUnusedUser");
  }

  @Test
  public void testCheckInvalidUsername() {
    PowerMockito.mockStatic(User.class);
    when(User.checkValidUsername("[]ValidUnusedUser")).thenReturn(false);

    assertEquals(UserController.INVALID, mUserController.checkValidUnusedUsername("[]ValidUnusedUser"));

    PowerMockito.verifyStatic();
    User.checkValidUsername("[]ValidUnusedUser");
  }

  @Test
  public void testCheckUsedUsername() {
    PowerMockito.mockStatic(User.class);
    when(User.checkValidUsername("ValidUsedUser")).thenReturn(true);
    when(mUserService.checkUnusedUsername("ValidUsedUser")).thenReturn(false);

    assertEquals(UserController.USED, mUserController.checkValidUnusedUsername("ValidUsedUser"));

    PowerMockito.verifyStatic();
    User.checkValidUsername("ValidUsedUser");
    verify(mUserService).checkUnusedUsername("ValidUsedUser");
  }

  @Test
  public void testCheckValidUnusedEmail() {
    PowerMockito.mockStatic(User.class);
    when(User.checkValidEmail("validunused@email.com")).thenReturn(true);
    when(mUserService.checkUnusedEmail("validunused@email.com")).thenReturn(true);

    assertEquals(UserController.VALID_UNUSED, mUserController.checkValidUnusedEmail("validunused@email.com"));

    PowerMockito.verifyStatic();
    User.checkValidEmail("validunused@email.com");
    verify(mUserService).checkUnusedEmail("validunused@email.com");
  }

  @Test
  public void testCheckInvalidEmail() {
    PowerMockito.mockStatic(User.class);
    when(User.checkValidEmail("invalidunusedemail.com")).thenReturn(false);

    assertEquals(UserController.INVALID, mUserController.checkValidUnusedEmail("invalidunusedemail.com"));

    PowerMockito.verifyStatic();
    User.checkValidEmail("invalidunusedemail.com");
  }

  @Test
  public void testCheckUsedEmail() {
    PowerMockito.mockStatic(User.class);
    when(User.checkValidEmail("validused@email.com")).thenReturn(true);
    when(mUserService.checkUnusedEmail("validused@email.com")).thenReturn(false);

    assertEquals(UserController.USED, mUserController.checkValidUnusedEmail("validused@email.com"));

    PowerMockito.verifyStatic();
    User.checkValidEmail("validused@email.com");
    verify(mUserService).checkUnusedEmail("validused@email.com");
  }

  @Test
  public void testCheckValidPassword() {
    PowerMockito.mockStatic(User.class);
    when(User.checkValidPassword("validPassw0rd")).thenReturn(true);

    assertEquals(true, mUserController.checkValidPassword("validPassw0rd"));

    PowerMockito.verifyStatic();
    User.checkValidPassword("validPassw0rd");
  }

}
