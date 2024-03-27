package org.webatrio.backend.events.exceptions;

public class EventAlreadyExistException extends Exception {
    public EventAlreadyExistException(String message) {
        super(message);
    }
}
