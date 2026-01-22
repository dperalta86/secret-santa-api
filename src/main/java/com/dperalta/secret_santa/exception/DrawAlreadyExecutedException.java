package com.dperalta.secret_santa.exception;

public class DrawAlreadyExecutedException extends RuntimeException {
    public DrawAlreadyExecutedException(String code) {
        super("Draw with code " + code + " has already been executed");
    }
}