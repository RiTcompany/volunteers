package org.example.steps.impl.moderator.impl;

import org.example.enums.EDocument;
import org.example.services.BotUserService;
import org.example.services.DocumentService;
import org.example.steps.impl.moderator.FailDocumentMessageInputStep;
import org.springframework.stereotype.Component;

@Component
public class ChildDocumentFailMessageInputStep extends FailDocumentMessageInputStep {
    private static final String PREPARE_MESSAGE_TEXT = "Укажите <b>комментарий</b> почему вы отклонили данный документ. Это сообщение будет отправлено кандидату";
    private static final String FAIL_MESSAGE_TEXT = """
            Модератор не принял ваш документ.
            Пожалуйста прочтите комментарий, учтите все поправки и введите команду /change_child_document
            Комментарий:
            """;
    private static final EDocument DOCUMENT_TYPE = EDocument.CHILD_DOCUMENT;

    public ChildDocumentFailMessageInputStep(
            DocumentService documentService, BotUserService botUserService
    ) {
        super(documentService, botUserService);
    }

    @Override
    protected String getPrepareMessageText() {
        return PREPARE_MESSAGE_TEXT;
    }

    @Override
    protected String getFailMessageText() {
        return FAIL_MESSAGE_TEXT;
    }

    @Override
    protected EDocument getDocumentType() {
        return DOCUMENT_TYPE;
    }
}
