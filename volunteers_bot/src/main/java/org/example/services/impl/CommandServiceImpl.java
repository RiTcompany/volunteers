package org.example.services.impl;

import lombok.RequiredArgsConstructor;
import org.example.commands.InfoCommand;
import org.example.commands.RegisterCommand;
import org.example.services.CommandService;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.CommandRegistry;

@Service
@RequiredArgsConstructor
public class CommandServiceImpl implements CommandService {
    private final InfoCommand infoCommand;
    private final RegisterCommand registerCommand;

    public CommandRegistry registerCommands(String botName) {
        CommandRegistry commandRegistry = new CommandRegistry(true, () -> botName);
        commandRegistry.register(infoCommand);
        commandRegistry.register(registerCommand);
        return commandRegistry;
    }
}
