package com.sicknasty.persistence.exceptions;

public class DBPageNameNotFoundException extends Exception {
    public DBPageNameNotFoundException(String name) {
        super("Page name not found: " + name);
    }
}
