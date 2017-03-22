package com.civify.civify.Model;

import android.support.annotation.NonNull;
import android.util.Log;

public class UserService implements Service {
  public void registerUser(@NonNull String username, @NonNull String name,
      @NonNull String surname, @NonNull String email, @NonNull String password) throws Exception {
  }

  public boolean checkUnusedEmail(String email) {
    return true;
  }

  public boolean checkUnusedUsername(String username) {
    return true;
  }
}
