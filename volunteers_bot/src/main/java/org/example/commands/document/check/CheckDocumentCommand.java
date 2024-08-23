package org.example.commands.document.check;

import lombok.extern.slf4j.Slf4j;
import org.example.entities.BotUser;
import org.example.entities.DocumentToCheck;
import org.example.enums.EConversation;
import org.example.enums.EDocument;
import org.example.enums.ERole;
import org.example.exceptions.AbstractException;
import org.example.exceptions.EntityNotFoundException;
import org.example.services.BotUserService;
import org.example.services.ConversationService;
import org.example.services.DocumentService;
import org.example.utils.MessageUtil;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;

@Slf4j
public abstract class CheckDocumentCommand extends BotCommand {
    private final ConversationService conversationService;
    private final BotUserService botUserService;
    private final DocumentService documentService;

    public CheckDocumentCommand(
            String commandModifier,
            String commandDescription,
            ConversationService conversationService,
            BotUserService botUserService,
            DocumentService documentService
    ) {
        super(commandModifier, commandDescription);
        this.conversationService = conversationService;
        this.botUserService = botUserService;
        this.documentService = documentService;
    }

    protected abstract String getNoDocsMessageText();

    protected abstract EDocument getDocumentType();

    protected abstract EConversation getConversationType();

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] strings) {
        try {
            DocumentToCheck documentToCheck = documentService.getToCheck(getDocumentType());

            if (documentToCheck != null) {
                BotUser botUser = botUserService.getByChatIdAndRole(chat.getId(), ERole.ROLE_MODERATOR);
                documentService.setModerator(documentToCheck, botUser.getId());
                conversationService.startConversation(chat.getId(), getConversationType(), absSender);
            } else {
                MessageUtil.sendMessageText(chat.getId(), getNoDocsMessageText(), absSender);
            }
        } catch (EntityNotFoundException e) {
            log.error(e.getMessage());
            MessageUtil.sendMessageText(chat.getId(), "Недостаточно прав", absSender);
        } catch (AbstractException e) {
            log.error(e.getMessage());
            MessageUtil.sendMessageText(chat.getId(), "Что-то пошло не так, обратитесь в поддержку", absSender);
        }
    }
}
