package org.example.commands;

import org.example.entities.BotUser;
import org.example.enums.EConversation;
import org.example.enums.ERole;
import org.example.exceptions.EntityNotFoundException;
import org.example.services.BotMessageService;
import org.example.services.ConversationService;
import org.example.services.UserService;
import org.example.utils.MessageUtil;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;

@Component
public class SendBotMessageCommand extends BotCommand {
    private final ConversationService conversationService;
    private final UserService userService;
    private final BotMessageService botMessageService;

    public SendBotMessageCommand(
            ConversationService conversationService,
            UserService userService,
            BotMessageService botMessageService
    ) {
        super("send_message", "Send bot message to users");
        this.conversationService = conversationService;
        this.userService = userService;
        this.botMessageService = botMessageService;
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] arguments) {
        try {
            BotUser botUser = userService.getByChatIdAndRole(chat.getId(), ERole.ROLE_WRITER);
            botMessageService.create(botUser.getId());
            conversationService.startConversation(
                    chat.getId(), EConversation.SEND_BOT_MESSAGE, absSender
            );
        } catch (EntityNotFoundException e) {
            MessageUtil.sendMessageText("Такой команды не существует", chat.getId(), absSender);
        }
    }
}
