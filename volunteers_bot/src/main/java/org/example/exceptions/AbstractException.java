package org.example.exceptions;

import lombok.Getter;

@Getter
public abstract class AbstractException extends RuntimeException {
    protected String userMessage;

    public AbstractException(String message, String userMessage) {
        super(message);
        this.userMessage = userMessage;
    }
}
