package org.example.services.impl;

import lombok.RequiredArgsConstructor;
import org.example.commands.InfoCommand;
import org.example.commands.SendBotMessageCommand;
import org.example.commands.StartCommand;
import org.example.commands.check_document.CheckChildDocumentCommand;
import org.example.commands.check_document.CheckVolunteerPhotoCommand;
import org.example.commands.register.ParentRegisterCommand;
import org.example.commands.register.VolunteerRegisterCommand;
import org.example.services.CommandService;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.CommandRegistry;

@Service
@RequiredArgsConstructor
public class CommandServiceImpl implements CommandService {
    private final StartCommand startCommand;
    private final InfoCommand infoCommand;
    private final VolunteerRegisterCommand volunteerRegisterCommand;
    private final ParentRegisterCommand parentRegisterCommand;
    private final CheckChildDocumentCommand checkChildDocumentCommand;
    private final CheckVolunteerPhotoCommand checkVolunteerPhotoCommand;
    private final SendBotMessageCommand sendBotMessageCommand;

    public CommandRegistry registerCommands(String botName) {
        CommandRegistry commandRegistry = new CommandRegistry(true, () -> botName);
        commandRegistry.register(startCommand);
        commandRegistry.register(infoCommand);
        commandRegistry.register(volunteerRegisterCommand);
        commandRegistry.register(parentRegisterCommand);
        commandRegistry.register(checkChildDocumentCommand);
        commandRegistry.register(checkVolunteerPhotoCommand);
        commandRegistry.register(sendBotMessageCommand);
        return commandRegistry;
    }
}
