package com.civify.civify.adapter;

import android.support.annotation.NonNull;
import android.util.Patterns;

import com.civify.civify.model.User;
import com.civify.civify.service.IUserService;
import com.civify.civify.service.UserService;

public class UserAdapter {
    public static final int INVALID = 0;
    public static final int USED = 1;
    public static final int VALID_UNUSED = 2;

    private final UserService mUserService;

    public UserAdapter() {
        mUserService = new IUserService();
    }


    public void registerUser(User user) {
        mUserService.registerUser(user);
    }

    public int checkValidUnusedEmail(String email) {
        if (!checkValidEmail(email))
            return INVALID;
        else if (checkUnusedEmail(email))
            return USED;
        else
            return VALID_UNUSED;
    }

    public int checkValidUnusedUsername(String username) {
        if (!checkValidUsername(username))
            return INVALID;
        else if (checkUnusedUsername(username))
            return USED;
        else
            return VALID_UNUSED;
    }

    private boolean checkUnusedUsername(String username) {
        return mUserService.checkUnusedUsername(username);
    }

    private boolean checkUnusedEmail(String email) {
        return mUserService.checkUnusedEmail(email);
    }

    private boolean checkValidEmail(@NonNull String email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private boolean checkValidUsername(@NonNull String username) {
        return username.matches("^(?=.{8,20}$)(?![_.])(?!.*[_.]{2})[a-zA-Z0-9._]+(?<![_.])$");
    }

    public boolean checkValidPassword(String password) {
        // Between 8 and 40 characters long, at least one digit, one lowercase character and
        // one uppercase character (valid special characters: @ & # $ %)
        return password.matches("^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])[a-zA-Z0-9@&#$%]{8,40}$");
    }
}
