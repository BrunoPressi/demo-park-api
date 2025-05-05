package com.compass.demo_park_api.exception;

public class CodeUniqueViolationException extends RuntimeException{

    public CodeUniqueViolationException(String msg) {
        super(msg);
    }

}
