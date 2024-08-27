package org.example.exceptions;

public class EventNotFoundException extends RuntimeException {
    public EventNotFoundException(String message) {
        super("Not found event ID = ".concat(message));
    }
}
