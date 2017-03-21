package com.civify.civify.Model;

import android.support.annotation.NonNull;
import android.util.Patterns;

public class User {

  private String mUsername;
  private String mName;
  private String mSurname;
  private String mEmail;
  private String mPassword;

  public User(@NonNull String username, @NonNull String name,
      @NonNull String surname, @NonNull String email, @NonNull String password) throws Exception {
    setUsername(username);
    this.mName = name;
    this.mSurname = surname;
    setEmail(email);
    setPassword(password);
  }

  public String getUsername() {
    return mUsername;
  }

  public void setUsername(@NonNull String username) throws Exception {
    if (!checkValidUsername(username))
      throw new Exception("Invalid username");
    mUsername = username;
  }

  public String getName() {
    return mName;
  }

  public void setName(@NonNull String name) {
    mName = name;
  }

  public String getSurname() {
    return mSurname;
  }

  public void setSurname(@NonNull String surname) {
    mSurname = surname;
  }

  public String getEmail() {
    return mEmail;
  }

  @SuppressWarnings("WeakerAccess")
  public void setEmail(@NonNull String email) throws Exception {
    if (!checkValidEmail(email))
      throw new Exception("Invalid e-mail format");
    mEmail = email;
  }

  public String getPassword() {
    return mPassword;
  }

  public void setPassword(@NonNull String password) throws Exception {
    if (!checkValidPassword(password))
      throw new Exception("Invalid password");
    this.mPassword = password;
  }

  public static boolean checkValidEmail(@NonNull String email) {
    return Patterns.EMAIL_ADDRESS.matcher(email).matches();
  }

  public static boolean checkValidUsername(@NonNull String username) {
    return username.matches("^(?=.{8,20}$)(?![_.])(?!.*[_.]{2})[a-zA-Z0-9._]+(?<![_.])$");
  }

  public static boolean checkValidPassword(String password) {
    // Between 8 and 40 characters long, at least one digit, one lowercase character and
    // one uppercase character (valid special characters: @ & # $ %)
    return password.matches("^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])[a-zA-Z0-9@&#$%]{8,40}$");
  }
}
