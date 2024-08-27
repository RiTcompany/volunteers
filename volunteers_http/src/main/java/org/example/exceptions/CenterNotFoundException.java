package org.example.exceptions;

public class CenterNotFoundException extends RuntimeException {
    public CenterNotFoundException(String message) {
        super("Not found center ID = ".concat(message));
    }
}
