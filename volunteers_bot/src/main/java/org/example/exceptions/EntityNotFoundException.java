package org.example.exceptions;

public class EntityNotFoundException extends AbstractException {
    public EntityNotFoundException(String message, String userMessage) {
        super(message, userMessage);
    }
}
