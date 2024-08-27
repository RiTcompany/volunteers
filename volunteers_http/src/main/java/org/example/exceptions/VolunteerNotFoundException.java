package org.example.exceptions;

public class VolunteerNotFoundException extends RuntimeException {
    public VolunteerNotFoundException(String message) {
        super("Not found volunteer ID = ".concat(message));
    }
}
