package org.example.exceptions;

public class CommandException extends AbstractException {
    public CommandException(String message, String userMessage) {
        super(message, userMessage);
    }
}
