package org.example.exceptions;

public class DocumentNotFoundException extends RuntimeException {
    public DocumentNotFoundException(String message) {
        super("Not found document ID = ".concat(message));
    }
}
