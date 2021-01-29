package com.sicknasty.persistence.exceptions;

public class DBPageNameExistsException extends Exception {
    public DBPageNameExistsException(String name) {
        super("Page name already exists: " + name);
    }
}
