package org.example.commands;

import org.example.utils.MessageUtil;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;

@Component
public class StartCommand extends BotCommand {
    private final String MESSAGE = """
            Привет, дорогой друг!\s
            Я – бот Санкт-Петербургского регионального отделения ВОД «Волонтёры Победы» 🕊
                        
            Я помогу тебе:
            ✅ стать Волонтёром Победы
            ✅ зарегистрироваться на наши мероприятия\s
                        
            Ниже мы попросим тебя ответить на несколько вопросов, но не пугайся. Данные потребуется ввести всего один раз — в дальнейшем информация будет загружаться автоматически.
                        
            Давай начнём!""";

    public StartCommand() {
        super("start", "Start command");
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] strings) {
        MessageUtil.sendMessageText(chat.getId(), MESSAGE, absSender);
    }
}
