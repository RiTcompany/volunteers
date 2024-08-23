package org.example.steps.impl.moderator.impl;

import org.example.enums.EDocument;
import org.example.services.BotUserService;
import org.example.services.DocumentService;
import org.example.steps.impl.moderator.FailDocumentMessageInputStep;
import org.springframework.stereotype.Component;

@Component
public class VolunteerPhotoFailMessageInputStep extends FailDocumentMessageInputStep {
    private static final String PREPARE_MESSAGE_TEXT = "Укажите <b>комментарий</b> почему вы отклонили данное фото. Это сообщение будет отправлено кандидату";
    private static final String FAIL_MESSAGE_TEXT = """
            Модератор не принял ваше фото.
            Пожалуйста прочтите комментарий, учтите все поправки и введите команду /change_volunteer_photo
            Комментарий:
            """;
    private static final EDocument DOCUMENT_TYPE = EDocument.VOLUNTEER_PHOTO;

    public VolunteerPhotoFailMessageInputStep(
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
