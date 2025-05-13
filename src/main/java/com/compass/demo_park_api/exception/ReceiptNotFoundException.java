package com.compass.demo_park_api.exception;

public class ReceiptNotFoundException extends RuntimeException {

    private String receipt;

    /*public ReceiptNotFoundException(String msg) {
        super(msg);
    }*/

    public ReceiptNotFoundException(String receipt) {
        this.receipt = receipt;
    }

}
