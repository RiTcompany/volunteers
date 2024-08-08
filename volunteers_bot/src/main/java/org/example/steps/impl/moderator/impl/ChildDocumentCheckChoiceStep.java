package org.example.steps.impl.moderator.impl;

import org.example.enums.EDocument;
import org.example.mappers.KeyboardMapper;
import org.example.services.DocumentService;
import org.example.services.UserService;
import org.example.steps.impl.moderator.DocumentCheckChoiceStep;
import org.springframework.stereotype.Component;

@Component
public class ChildDocumentCheckChoiceStep extends DocumentCheckChoiceStep {
    private static final String PREPARE_MESSAGE_TEXT = "Проверьте документ. Если он удовлетворяет всем критериям - нажмите ДА, иначе - НЕТ";
    private static final String ACCEPT_MESSAGE_TEXT = "Модератор принял ваш документ";
    private static final EDocument DOCUMENT_TYPE = EDocument.CHILD_DOCUMENT;

    public ChildDocumentCheckChoiceStep(
            DocumentService documentService, KeyboardMapper keyboardMapper, UserService userService
    ) {
        super(documentService, keyboardMapper, userService);
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
