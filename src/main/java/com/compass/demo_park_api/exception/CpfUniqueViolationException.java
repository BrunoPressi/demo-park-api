package com.compass.demo_park_api.exception;

public class CpfUniqueViolationException extends RuntimeException {

    public CpfUniqueViolationException(String msg) {
        super(msg);
    }

}
