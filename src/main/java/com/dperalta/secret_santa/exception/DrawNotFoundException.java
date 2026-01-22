package com.dperalta.secret_santa.exception;

public class DrawNotFoundException extends RuntimeException {
    public DrawNotFoundException(String code) {
        super("Draw not found with code: " + code);
    }
}
