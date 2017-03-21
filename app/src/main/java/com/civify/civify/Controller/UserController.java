package com.civify.civify.Controller;

import com.civify.civify.Model.User;
import com.civify.civify.Model.UserService;

public class UserController {
  @SuppressWarnings("CanBeFinal") private UserService mUserService;
  private User mCurrentUser;


  public UserController(UserService userService) {
    mUserService = userService;
  }

  public void registerUser(String username, String name, String surname, String email, String password) throws Exception {
    mCurrentUser = new User(username, name, surname, email, password);
    mUserService.registerUser(username, name, surname, email, password);
  }

  public boolean checkValidUnusedEmail(String email) {
    return User.checkValidEmail(email)
        && mUserService.checkUnusedEmail(email);
  }

  public boolean checkValidUnusedUsername(String username) {
    return User.checkValidUsername(username)
        && mUserService.checkUnusedUsername(username);
  }

  public boolean checkValidPassword(String password) {
    return User.checkValidPassword(password);
  }

}
