package org.example.exceptions;

public class CommandException extends AbstractException {
    public CommandException() {
        super(
                "Попытка вызвать команду во время действующего диалога",
                "Вы не можете ввести другую команду, пока не завершите данный диалог"
        );
    }
}
