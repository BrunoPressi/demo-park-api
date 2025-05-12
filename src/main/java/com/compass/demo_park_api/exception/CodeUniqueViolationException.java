package com.compass.demo_park_api.exception;

import lombok.Getter;

@Getter
public class CodeUniqueViolationException extends RuntimeException{

    private String code;

    /*public CodeUniqueViolationException(String msg) {
        super(msg);
    }*/

    public CodeUniqueViolationException(String code) {
        this.code = code;
    }

}
