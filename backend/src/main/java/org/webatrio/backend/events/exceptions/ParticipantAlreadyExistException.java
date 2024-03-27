package org.webatrio.backend.events.exceptions;

public class ParticipantAlreadyExistException extends Exception {
    public ParticipantAlreadyExistException(String message) {
        super(message);
    }
}
