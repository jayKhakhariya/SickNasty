package com.sicknasty.persistence.exceptions;

public class DBUsernameNotFoundException extends Exception {
    public DBUsernameNotFoundException(String username) {
        super("User not found: " + username);
    }
}
