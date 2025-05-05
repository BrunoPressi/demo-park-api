package com.compass.demo_park_api.exception;

public class CodeNotFoundException extends RuntimeException{

    public CodeNotFoundException(String msg) {
        super(msg);
    }

}
