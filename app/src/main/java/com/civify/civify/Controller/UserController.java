package com.civify.civify.Controller;

import com.civify.civify.Model.User;
import com.civify.civify.Model.UserService;

public class UserController {
  @SuppressWarnings("CanBeFinal") private UserService mUserService;
  private User mCurrentUser;

  public static final int INVALID = 0;
  public static final int USED = 1;
  public static final int VALID_UNUSED = 2;


  public UserController(UserService userService) {
    mUserService = userService;
  }

  public void registerUser(String username, String name, String surname, String email, String password) throws Exception {
    mCurrentUser = new User(username, name, surname, email, password);
    mUserService.registerUser(username, name, surname, email, password);
  }

  public int checkValidUnusedEmail(String email) {
    if (!User.checkValidEmail(email))
      return INVALID;
    else if (!mUserService.checkUnusedEmail(email))
      return USED;
    else
      return VALID_UNUSED;
  }

  public int checkValidUnusedUsername(String username) {
    if (!User.checkValidUsername(username))
      return INVALID;
    else if (!mUserService.checkUnusedUsername(username))
      return USED;
    else
      return VALID_UNUSED;
  }

  public boolean checkValidPassword(String password) {
    return User.checkValidPassword(password);
  }

}
