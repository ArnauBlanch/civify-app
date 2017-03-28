package com.civify.civify.controller;

class CivifyUsernameCredentials {

    final String username;
    final String password;

    public CivifyUsernameCredentials(String usernameCredential, String hashedPassword) {
        username = usernameCredential;
        password = hashedPassword;
    }
}
