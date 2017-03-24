package com.civify.civify.service;

import com.civify.civify.model.User;

public class IUserService implements UserService {

    @Override
    public void registerUser(User user) {

    }

    @Override
    public boolean checkUnusedUsername(String username) {
        return false;
    }

    @Override
    public boolean checkUnusedEmail(String email) {
        return false;
    }
}
