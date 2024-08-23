package org.example.commands.document.check.impl;

import org.example.commands.document.check.CheckDocumentCommand;
import org.example.enums.EConversation;
import org.example.enums.EDocument;
import org.example.services.BotUserService;
import org.example.services.ConversationService;
import org.example.services.DocumentService;
import org.springframework.stereotype.Component;

@Component
public class CheckChildDocumentCommand extends CheckDocumentCommand {
    private static final String COMMAND_MODIFIER = "check_child_document";
    private static final String COMMAND_DESCRIPTION = "Check child's document command";
    private static final String NO_DOCS_MESSAGE_TEXT = "Сейчас нет документов для проверки";
    private static final EDocument DOCUMENT_TYPE = EDocument.CHILD_DOCUMENT;
    private static final EConversation CONVERSATION_TYPE = EConversation.CHECK_CHILD_DOCUMENT;

    public CheckChildDocumentCommand(
            ConversationService conversationService,
            BotUserService botUserService,
            DocumentService documentService
    ) {
        super(COMMAND_MODIFIER, COMMAND_DESCRIPTION, conversationService, botUserService, documentService);
    }

    @Override
    protected String getNoDocsMessageText() {
        return NO_DOCS_MESSAGE_TEXT;
    }

    @Override
    protected EDocument getDocumentType() {
        return DOCUMENT_TYPE;
    }

    @Override
    protected EConversation getConversationType() {
        return CONVERSATION_TYPE;
    }
}
