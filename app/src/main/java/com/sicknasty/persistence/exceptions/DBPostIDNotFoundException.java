package com.sicknasty.persistence.exceptions;

public class DBPostIDNotFoundException extends Exception {
    public DBPostIDNotFoundException(int id) {
        super("Post ID not found: " + id);
    }
}
