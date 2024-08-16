package org.example.commands;

import lombok.extern.slf4j.Slf4j;
import org.example.entities.BotUser;
import org.example.enums.EConversation;
import org.example.enums.ERole;
import org.example.exceptions.EntityNotFoundException;
import org.example.services.BotMessageService;
import org.example.services.BotUserService;
import org.example.services.ConversationService;
import org.example.utils.MessageUtil;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;

@Slf4j
@Component
public class SendBotMessageCommand extends BotCommand {
    private final ConversationService conversationService;
    private final BotUserService botUserService;
    private final BotMessageService botMessageService;

    public SendBotMessageCommand(
            ConversationService conversationService,
            BotUserService botUserService,
            BotMessageService botMessageService
    ) {
        super("send_message", "Send bot message to users");
        this.conversationService = conversationService;
        this.botUserService = botUserService;
        this.botMessageService = botMessageService;
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] arguments) {
        try {
            BotUser botUser = botUserService.getByChatIdAndRole(chat.getId(), ERole.ROLE_WRITER);
            botMessageService.create(botUser.getId());
            conversationService.startConversation(
                    chat.getId(), EConversation.SEND_BOT_MESSAGE, absSender
            );
        } catch (EntityNotFoundException e) {
            log.error(e.getMessage());
            MessageUtil.sendMessageText(chat.getId(), "Недостаточно прав", absSender);
        }
    }
}
