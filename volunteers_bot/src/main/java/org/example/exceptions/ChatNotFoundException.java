package org.example.exceptions;

public class ChatNotFoundException extends AbstractException {
    public ChatNotFoundException(String message, String userMessage) {
        super(message, userMessage);
    }
}
