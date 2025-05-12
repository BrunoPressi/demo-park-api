package com.compass.demo_park_api.exception;

import lombok.Getter;

@Getter
public class CodeNotFoundException extends RuntimeException{

    private String code;

    /*public CodeNotFoundException(String msg) {
        super(msg);
    }*/

    public CodeNotFoundException(String code) {
        this.code = code;
    }
}
