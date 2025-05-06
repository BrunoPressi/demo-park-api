package com.compass.demo_park_api.exception;

public class CpfNotFoundException extends RuntimeException {

    public CpfNotFoundException(String msg) {
        super(msg);
    }

}
