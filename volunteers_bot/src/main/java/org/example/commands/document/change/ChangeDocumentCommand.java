package org.example.commands.document.change;

import lombok.extern.slf4j.Slf4j;
import org.example.enums.EConversation;
import org.example.enums.EDocument;
import org.example.services.BotUserService;
import org.example.services.ConversationService;
import org.example.services.DocumentService;
import org.example.utils.MessageUtil;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;

@Slf4j
public abstract class ChangeDocumentCommand extends BotCommand {
    private final ConversationService conversationService;
    private final DocumentService documentService;

    public ChangeDocumentCommand(
            String commandModifier,
            String commandDescription,
            ConversationService conversationService,
            DocumentService documentService
    ) {
        super(commandModifier, commandDescription);
        this.conversationService = conversationService;
        this.documentService = documentService;
    }

    protected abstract String getExceptionMessageText();

    protected abstract EDocument getDocumentType();

    protected abstract EConversation getConversationType();

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] strings) {
        boolean mayChangeDocument = documentService.mayChangeDocument(chat.getId(), getDocumentType());
        if (mayChangeDocument) {
            conversationService.startConversation(chat.getId(), getConversationType(), absSender);
        } else {
            MessageUtil.sendMessageText(chat.getId(), getExceptionMessageText(), absSender);
        }
    }
}

