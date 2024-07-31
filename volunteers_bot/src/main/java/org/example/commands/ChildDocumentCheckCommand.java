package org.example.commands;

import org.example.enums.EConversation;
import org.example.repositories.ModeratorRepository;
import org.example.services.ConversationService;
import org.example.utils.MessageUtil;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;

@Component
public class ChildDocumentCheckCommand extends BotCommand {
    private final ConversationService conversationService;
    private final ModeratorRepository moderatorRepository;

    public ChildDocumentCheckCommand(
            ConversationService conversationService,
            ModeratorRepository moderatorRepository
    ) {
        super("check_document", "Check child's document command");
        this.conversationService = conversationService;
        this.moderatorRepository = moderatorRepository;
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] strings) {
        if (moderatorRepository.existsByChatId(chat.getId())) {
            conversationService.startConversation(chat.getId(), EConversation.CHECK_DOCUMENT, absSender);
        } else {
            MessageUtil.sendMessageText("Такой команды не существует", chat.getId(), absSender);
        }
    }
}
