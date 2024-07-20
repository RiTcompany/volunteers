package org.example.steps.impl;

import lombok.extern.slf4j.Slf4j;
import org.example.enums.EConversationStep;
import org.example.pojo.dto.MessageDto;
import org.example.pojo.entities.ChatHash;
import org.example.steps.ConversationStep;
import org.example.utils.MessageUtil;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Document;
import org.telegram.telegrambots.meta.bots.AbsSender;

import java.io.File;


@Slf4j
@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ChildDocumentStep extends ConversationStep {
    private static final String PREPARE_MESSAGE_TEXT = new StringBuilder()
            .append("Вам необходимо согласие родителей. Для этого необходимо сделать следующие шаги:\n")
            .append("\t1) Скачайте и распечатайте документ.\n")
            .append("\t2) Передайте документ родителям для заполнения.\n")
            .append("\t3) Отсканируйте заполненный документ.\n")
            .append("\t4) Отправьте отсканированный документ в ответном сообщении")
            .toString();
    private static final String ANSWER_MESSAGE_TEXT = "Ваш документ был отправлен на проверку. Ожидайте ответа. А пока можете продолжить регистрацию";
    private static final String EXCEPTION_MESSAGE_TEXT = "Вам необходимо отправить документ в ответном сообщении";
    private static final File FILE = new File("C:\\Users\\User\\IdeaProjects\\volunteers\\volunteers_bot\\src\\main\\resources\\static\\Согласие.pdf");
    private static final long MAX_DOCUMENT_SIZE = 300 * 1024;

    @Override
    public void prepare(ChatHash chatHash, AbsSender sender) {
        int messageId = MessageUtil.sendDocument(chatHash.getId(), FILE, PREPARE_MESSAGE_TEXT, sender);
        chatHash.setPrevBotMessageId(messageId);
    }

    @Override
    public EConversationStep execute(ChatHash chatHash, MessageDto messageDto, AbsSender sender) {
        boolean isDocumentDownloaded = downloadDocument(messageDto.getDocument(), sender);
        if (isDocumentDownloaded) {
            finishStep(chatHash, sender, ANSWER_MESSAGE_TEXT);
            return eConversationStepList.get(0);
        }

        return handleIllegalUserAction(chatHash, messageDto, sender, EXCEPTION_MESSAGE_TEXT);
    }

    private boolean downloadDocument(Document document, AbsSender sender) {
        if (document == null || document.getFileSize() > MAX_DOCUMENT_SIZE) {
            return false;
        }

        File file = MessageUtil.downloadDocument(document, sender);
        if (file == null) {
            return false;
        }

        log.info(file.getAbsoluteFile().toString());
        return true;
    }
}
