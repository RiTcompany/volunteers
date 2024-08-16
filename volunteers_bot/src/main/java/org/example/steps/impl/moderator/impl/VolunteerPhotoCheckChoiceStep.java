package org.example.steps.impl.moderator.impl;

import org.example.enums.EDocument;
import org.example.mappers.KeyboardMapper;
import org.example.services.BotUserService;
import org.example.services.DocumentService;
import org.example.steps.impl.moderator.DocumentCheckChoiceStep;
import org.springframework.stereotype.Component;

@Component
public class VolunteerPhotoCheckChoiceStep extends DocumentCheckChoiceStep {
    private static final String PREPARE_MESSAGE_TEXT = "Проверьте фото. Если оно удовлетворяет всем критериям - нажмите ДА, иначе - НЕТ";
    private static final String ACCEPT_MESSAGE_TEXT = "Модератор принял ваше фото";
    private static final EDocument DOCUMENT_TYPE = EDocument.VOLUNTEER_PHOTO;

    public VolunteerPhotoCheckChoiceStep(
            DocumentService documentService, KeyboardMapper keyboardMapper, BotUserService botUserService
    ) {
        super(documentService, keyboardMapper, botUserService);
    }

    @Override
    protected String getPrepareMessageText() {
        return PREPARE_MESSAGE_TEXT;
    }

    @Override
    protected String getAcceptMessageText() {
        return ACCEPT_MESSAGE_TEXT;
    }

    @Override
    protected EDocument getDocumentType() {
        return DOCUMENT_TYPE;
    }
}
