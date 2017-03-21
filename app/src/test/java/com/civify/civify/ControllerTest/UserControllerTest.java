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

    assertEquals(true, mUserController.checkValidUnusedUsername("ValidUnusedUser"));

    PowerMockito.verifyStatic();
    User.checkValidUsername("ValidUnusedUser");
    verify(mUserService).checkUnusedUsername("ValidUnusedUser");
  }

  @Test
  public void testCheckValidUnusedEmail() {
    PowerMockito.mockStatic(User.class);
    when(User.checkValidEmail("validunused@email.com")).thenReturn(true);
    when(mUserService.checkUnusedEmail("validunused@email.com")).thenReturn(true);

    assertEquals(true, mUserController.checkValidUnusedEmail("validunused@email.com"));

    PowerMockito.verifyStatic();
    User.checkValidEmail("validunused@email.com");
    verify(mUserService).checkUnusedEmail("validunused@email.com");
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
