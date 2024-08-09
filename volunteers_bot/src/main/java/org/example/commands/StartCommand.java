package org.example.commands;

import org.example.utils.MessageUtil;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;

@Component
public class StartCommand extends BotCommand {
    private final String MESSAGE = "Здравствуй, дорогой друг! Мы рады, что ты захотел стать частью команды \"Волонтёры Победы\" в Санкт-Петербурге. Для регистрации в системе - перейди в панель \"меню\" и выбери \"зарегистрироваться волонтеру\".";

    public StartCommand() {
        super("start", "Start command");
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] strings) {
        MessageUtil.sendMessageText(chat.getId(), MESSAGE, absSender);
    }
}
