package com.sicknasty.persistence.exceptions;

public class DBUserAlreadyFollowingException extends Exception {
    public DBUserAlreadyFollowingException(String username, String pageName) {
        super("User '" + username + "' is already following '" + pageName + "'");
    }
}
