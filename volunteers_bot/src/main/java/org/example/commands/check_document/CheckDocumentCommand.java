package org.example.commands.check_document;

import org.example.entities.BotUser;
import org.example.entities.DocumentToCheck;
import org.example.enums.EConversation;
import org.example.enums.EDocument;
import org.example.enums.ERole;
import org.example.exceptions.EntityNotFoundException;
import org.example.services.ConversationService;
import org.example.services.DocumentService;
import org.example.services.UserService;
import org.example.utils.MessageUtil;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;

public abstract class CheckDocumentCommand extends BotCommand {
    private final ConversationService conversationService;
    private final UserService userService;
    private final DocumentService documentService;

    public CheckDocumentCommand(
            String commandModifier,
            String commandDescription,
            ConversationService conversationService,
            UserService userService,
            DocumentService documentService
    ) {
        super(commandModifier, commandDescription);
        this.conversationService = conversationService;
        this.userService = userService;
        this.documentService = documentService;
    }

    protected abstract String getNoDocsMessageText();

    protected abstract EDocument getDocumentType();

    protected abstract EConversation getConversationType();

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] strings) {
        try {
            BotUser botUser = userService.getByChatIdAndRole(chat.getId(), ERole.ROLE_MODERATOR);
            DocumentToCheck documentToCheck = documentService.getToCheck(getDocumentType());

            if (documentToCheck != null) {
                documentService.update(documentToCheck, botUser.getId());
                conversationService.startConversation(chat.getId(), getConversationType(), absSender);
            } else {
                MessageUtil.sendMessageText(getNoDocsMessageText(), chat.getId(), absSender);
            }
        } catch (EntityNotFoundException e) {
            MessageUtil.sendMessageText("Нет доступа", chat.getId(), absSender);
        }
    }
}
