package com.dperalta.secret_santa.exception;

public class InsufficientParticipantsException extends RuntimeException {
    public InsufficientParticipantsException() {
        super("At least 3 participants are required for a secret santa draw");
    }
}