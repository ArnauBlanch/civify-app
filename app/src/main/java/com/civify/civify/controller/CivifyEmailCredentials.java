package com.civify.civify.controller;

class CivifyEmailCredentials {

    final String email;
    final String password;

    public CivifyEmailCredentials(String emailCredential, String hashedPassword) {
        email = emailCredential;
        password = hashedPassword;
    }
}
