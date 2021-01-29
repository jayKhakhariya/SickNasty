package com.sicknasty.persistence.exceptions;

public class DBUsernameExistsException extends Exception {
    public DBUsernameExistsException(String username) {
        super("User already exists: " + username);
    }
}
