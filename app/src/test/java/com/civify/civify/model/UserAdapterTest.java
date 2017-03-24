package com.civify.civify.model;

import com.civify.civify.adapter.UserAdapter;
import com.civify.civify.service.UserService;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.verifyNew;
import static org.powermock.api.mockito.PowerMockito.whenNew;

@RunWith(PowerMockRunner.class)
@PrepareForTest({User.class, UserAdapter.class})
public class UserAdapterTest {
    private UserService mUserService;
    private UserAdapter mUserAdapter;

    @Before
    public void setUp() {
        mUserService = mock(UserService.class);
        mUserAdapter = new UserAdapter();
    }

    @After
    public void tearDown() {
        mUserAdapter = null;
    }

    @Test
    public void testRegisterUser() throws Exception {
        User mockUser = PowerMockito.mock(User.class);
        whenNew(User.class).withArguments("validUsername", "validName", "validSurname", "valid@email.com", "validPassw0rd").thenReturn(mockUser);
        mUserAdapter.registerUser(new User("validUsername", "validName", "validSurname", "valid@email.com", "validPassw0rd"));

        verify(mUserService).registerUser(new User("validUsername", "validName", "validSurname", "valid@email.com", "validPassw0rd"));
        verifyNew(User.class).withArguments("validUsername", "validName", "validSurname", "valid@email.com", "validPassw0rd");
    }
/*
  @Test
  public void testCheckValidUnusedUsername() {
    PowerMockito.mockStatic(User.class);
    when(User.checkValidUsername("ValidUnusedUser")).thenReturn(true);
    when(mUserService.checkUnusedUsername("ValidUnusedUser")).thenReturn(true);

    assertEquals(UserAdapter.VALID_UNUSED, mUserAdapter.checkValidUnusedUsername("ValidUnusedUser"));

    PowerMockito.verifyStatic();
    User.checkValidUsername("ValidUnusedUser");
    verify(mUserService).checkUnusedUsername("ValidUnusedUser");
  }

  @Test
  public void testCheckInvalidUsername() {
    PowerMockito.mockStatic(User.class);
    when(User.checkValidUsername("[]ValidUnusedUser")).thenReturn(false);

    assertEquals(UserAdapter.INVALID, mUserAdapter.checkValidUnusedUsername("[]ValidUnusedUser"));

    PowerMockito.verifyStatic();
    User.checkValidUsername("[]ValidUnusedUser");
  }

  @Test
  public void testCheckUsedUsername() {
    PowerMockito.mockStatic(User.class);
    when(User.checkValidUsername("ValidUsedUser")).thenReturn(true);
    when(mUserService.checkUnusedUsername("ValidUsedUser")).thenReturn(false);

    assertEquals(UserAdapter.USED, mUserAdapter.checkValidUnusedUsername("ValidUsedUser"));

    PowerMockito.verifyStatic();
    User.checkValidUsername("ValidUsedUser");
    verify(mUserService).checkUnusedUsername("ValidUsedUser");
  }

  @Test
  public void testCheckValidUnusedEmail() {
    PowerMockito.mockStatic(User.class);
    when(User.checkValidEmail("validunused@email.com")).thenReturn(true);
    when(mUserService.checkUnusedEmail("validunused@email.com")).thenReturn(true);

    assertEquals(UserAdapter.VALID_UNUSED, mUserAdapter.checkValidUnusedEmail("validunused@email.com"));

    PowerMockito.verifyStatic();
    User.checkValidEmail("validunused@email.com");
    verify(mUserService).checkUnusedEmail("validunused@email.com");
  }

  @Test
  public void testCheckInvalidEmail() {
    PowerMockito.mockStatic(User.class);
    when(User.checkValidEmail("invalidunusedemail.com")).thenReturn(false);

    assertEquals(UserAdapter.INVALID, mUserAdapter.checkValidUnusedEmail("invalidunusedemail.com"));

    PowerMockito.verifyStatic();
    User.checkValidEmail("invalidunusedemail.com");
  }

  @Test
  public void testCheckUsedEmail() {
    PowerMockito.mockStatic(User.class);
    when(User.checkValidEmail("validused@email.com")).thenReturn(true);
    when(mUserService.checkUnusedEmail("validused@email.com")).thenReturn(false);

    assertEquals(UserAdapter.USED, mUserAdapter.checkValidUnusedEmail("validused@email.com"));

    PowerMockito.verifyStatic();
    User.checkValidEmail("validused@email.com");
    verify(mUserService).checkUnusedEmail("validused@email.com");
  }

  @Test
  public void testCheckValidPassword() {
    PowerMockito.mockStatic(User.class);
    when(User.checkValidPassword("validPassw0rd")).thenReturn(true);

    assertEquals(true, mUserAdapter.checkValidPassword("validPassw0rd"));

    PowerMockito.verifyStatic();
    User.checkValidPassword("validPassw0rd");
  }*/

}
