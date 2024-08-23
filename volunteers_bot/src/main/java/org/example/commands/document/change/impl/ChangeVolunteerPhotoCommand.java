package org.example.commands.document.change.impl;

import org.example.commands.document.change.ChangeDocumentCommand;
import org.example.enums.EConversation;
import org.example.enums.EDocument;
import org.example.services.ConversationService;
import org.example.services.DocumentService;
import org.springframework.stereotype.Component;

@Component
public class ChangeVolunteerPhotoCommand extends ChangeDocumentCommand {
    private static final String COMMAND_MODIFIER = "change_volunteer_photo";
    private static final String COMMAND_DESCRIPTION = "Change volunteer's photo command";
    private static final String EXCEPTION_MESSAGE_TEXT = "Сейчас вы не можете изменить вашу фотографию, так как предыдущая ещё находится на проверке. Дождитесь её окончания";
    private static final EDocument DOCUMENT_TYPE = EDocument.VOLUNTEER_PHOTO;
    private static final EConversation CONVERSATION_TYPE = EConversation.CHANGE_VOLUNTEER_PHOTO;

    public ChangeVolunteerPhotoCommand(
            ConversationService conversationService, DocumentService documentService
    ) {
        super(COMMAND_MODIFIER, COMMAND_DESCRIPTION, conversationService, documentService);
    }

    @Override
    protected String getExceptionMessageText() {
        return EXCEPTION_MESSAGE_TEXT;
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
