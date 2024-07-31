package org.example.services.impl;

import lombok.RequiredArgsConstructor;
import org.example.commands.ChildDocumentCheckCommand;
import org.example.commands.InfoCommand;
import org.example.commands.ParentRegisterCommand;
import org.example.commands.StartCommand;
import org.example.commands.VolunteerRegisterCommand;
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
    private final ChildDocumentCheckCommand childDocumentCheckCommand;

    public CommandRegistry registerCommands(String botName) {
        CommandRegistry commandRegistry = new CommandRegistry(true, () -> botName);
        commandRegistry.register(startCommand);
        commandRegistry.register(infoCommand);
        commandRegistry.register(volunteerRegisterCommand);
        commandRegistry.register(parentRegisterCommand);
        commandRegistry.register(childDocumentCheckCommand);
        return commandRegistry;
    }
}
