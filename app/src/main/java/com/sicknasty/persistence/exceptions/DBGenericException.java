package com.sicknasty.persistence.exceptions;

public class DBGenericException extends RuntimeException {
    public DBGenericException(Exception e) {
        super(e);
    }
}
