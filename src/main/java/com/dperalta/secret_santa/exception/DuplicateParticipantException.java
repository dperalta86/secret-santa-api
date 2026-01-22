package com.dperalta.secret_santa.exception;

public class DuplicateParticipantException extends RuntimeException {
    public DuplicateParticipantException(String email) {
        super("Participant with email " + email + " already exists in this draw");
    }
}