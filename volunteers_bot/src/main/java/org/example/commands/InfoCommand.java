package org.example.commands;

import org.example.utils.MessageUtil;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;

@Component
public class InfoCommand extends BotCommand {
    private final String MESSAGE = "Hello"; // TODO : add information list

    public InfoCommand() {
        super("info", "Information command");
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] strings) {
        MessageUtil.sendMessage(MESSAGE, chat.getId(), absSender);
    }
}
