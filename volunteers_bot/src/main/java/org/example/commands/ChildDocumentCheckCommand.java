package org.example.commands;

import org.example.entities.BotUser;
import org.example.entities.ChildDocument;
import org.example.enums.EConversation;
import org.example.enums.ERole;
import org.example.exceptions.EntityNotFoundException;
import org.example.services.ChildDocumentService;
import org.example.services.ConversationService;
import org.example.services.UserService;
import org.example.utils.MessageUtil;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;

@Component
public class ChildDocumentCheckCommand extends BotCommand {
    private final ConversationService conversationService;
    private final UserService userService;
    private final ChildDocumentService childDocumentService;
    private static final String NO_DOCS_MESSAGE_TEXT = "Сейчас нет документов для проверки";

    public ChildDocumentCheckCommand(
            ConversationService conversationService,
            UserService userService,
            ChildDocumentService childDocumentService) {
        super("check_document", "Check child's document command");
        this.conversationService = conversationService;
        this.userService = userService;
        this.childDocumentService = childDocumentService;
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] strings) {
        try {
            BotUser botUser = userService.getByChatIdAndRole(chat.getId(), ERole.ROLE_WRITER);
            ChildDocument document = childDocumentService.getToCheck();
            if (document != null) {
                childDocumentService.update(document, botUser.getId());
                conversationService.startConversation(chat.getId(), EConversation.CHECK_DOCUMENT, absSender);
            } else {
                MessageUtil.sendMessageText(NO_DOCS_MESSAGE_TEXT, chat.getId(), absSender);
            }
        } catch (EntityNotFoundException e) {
            MessageUtil.sendMessageText("Такой команды не существует", chat.getId(), absSender);
        }
    }
}
