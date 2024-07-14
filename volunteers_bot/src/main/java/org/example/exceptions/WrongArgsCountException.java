package org.example.exceptions;

public class WrongArgsCountException extends AbstractException {
    public WrongArgsCountException(String message, String userMessage) {
        super(message, userMessage);
    }
}
