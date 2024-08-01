package org.example.commands;

import org.example.entities.ChildDocument;
import org.example.entities.Moderator;
import org.example.enums.ECheckDocumentStatus;
import org.example.enums.EConversation;
import org.example.exceptions.EntityNotFoundException;
import org.example.services.ChildDocumentService;
import org.example.services.ConversationService;
import org.example.services.ModeratorService;
import org.example.utils.MessageUtil;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;

@Component
public class ChildDocumentCheckCommand extends BotCommand {
    private final ConversationService conversationService;
    private final ModeratorService moderatorService;
    private final ChildDocumentService childDocumentService;
    private static final String NO_DOCS_MESSAGE_TEXT = "Сейчас нет документов для проверки";

    public ChildDocumentCheckCommand(
            ConversationService conversationService,
            ModeratorService moderatorService,
            ChildDocumentService childDocumentService) {
        super("check_document", "Check child's document command");
        this.conversationService = conversationService;
        this.moderatorService = moderatorService;
        this.childDocumentService = childDocumentService;
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] strings) {
        if (moderatorService.existsByChatId(chat.getId())) {
            ChildDocument document = childDocumentService.getToCheck();
            if (document != null) {
                startConversation(chat.getId(), document, absSender);
            } else {
                MessageUtil.sendMessageText(NO_DOCS_MESSAGE_TEXT, chat.getId(), absSender);
            }
        } else {
            MessageUtil.sendMessageText("Такой команды не существует", chat.getId(), absSender);
        }
    }

    private void updateDocument(long chatId, ChildDocument document) throws EntityNotFoundException {
        Moderator moderator = moderatorService.getModeratorByChatId(chatId);

        document.setModeratorId(moderator.getId());
        document.setStatus(ECheckDocumentStatus.CHECKING);
        childDocumentService.saveAndFlush(document);
    }

    private void startConversation(long chatId, ChildDocument document, AbsSender absSender) {
        try {
            updateDocument(chatId, document);
            conversationService.startConversation(chatId, EConversation.CHECK_DOCUMENT, absSender);
        } catch (EntityNotFoundException e) {
            MessageUtil.sendMessageText(e.getUserMessage(), chatId, absSender);
        }
    }
}
