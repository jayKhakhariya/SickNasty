package com.sicknasty.persistence.exceptions;

public class DBPostIDExistsException extends Exception {
    public DBPostIDExistsException(int id) {
        super("Post ID already exists in the database: " + id);
    }
}
