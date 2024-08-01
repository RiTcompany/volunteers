package org.example.exceptions;

public class EntityNotFoundException extends AbstractException {
    public EntityNotFoundException(String message) {
        super(message, "Что-то пошло не так, пожалуйста обратитесь в поддержку");
    }
}
