package org.example.services;

import org.springframework.stereotype.Service;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.CommandRegistry;

@Service
public interface CommandService {
    CommandRegistry registerCommands(String botName);
}
