package com.civify.civify.service;

import com.civify.civify.model.User;

@SuppressWarnings("ALL")
public interface UserService {

    void registerUser(User user);

    boolean checkUnusedUsername(String username);

    boolean checkUnusedEmail(String email);
}
