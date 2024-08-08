package org.example.commands.check_document;

import org.example.enums.EConversation;
import org.example.enums.EDocument;
import org.example.services.ConversationService;
import org.example.services.DocumentService;
import org.example.services.UserService;
import org.springframework.stereotype.Component;

@Component
public class CheckVolunteerPhotoCommand extends CheckDocumentCommand {
    private static final String COMMAND_MODIFIER = "check_volunteer_photo";
    private static final String COMMAND_DESCRIPTION = "Check volunteer's photo command";
    private static final String NO_DOCS_MESSAGE_TEXT = "Сейчас нет фото для проверки";
    private static final EDocument DOCUMENT_TYPE = EDocument.VOLUNTEER_PHOTO;
    private static final EConversation CONVERSATION_TYPE = EConversation.CHECK_VOLUNTEER_PHOTO;

    public CheckVolunteerPhotoCommand(
            ConversationService conversationService,
            UserService userService,
            DocumentService documentService
    ) {
        super(COMMAND_MODIFIER, COMMAND_DESCRIPTION, conversationService, userService, documentService);
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