package org.example.steps.impl.volunteer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.dto.MessageDto;
import org.example.dto.ResultDto;
import org.example.entities.ChatHash;
import org.example.enums.EDocument;
import org.example.services.DocumentService;
import org.example.steps.FileSendStep;
import org.example.utils.MessageUtil;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Document;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.bots.AbsSender;

import java.io.File;


@Slf4j
@Component
@RequiredArgsConstructor
public class ChildDocumentSendStep extends FileSendStep {
    private final DocumentService documentService;
    private static final String PREPARE_MESSAGE_TEXT = """
            Вам необходимо согласие родителей. Для этого необходимо сделать следующие шаги:
                1) Скачайте и распечатайте документ.
                2) Передайте документ родителям для заполнения.
                3) Отсканируйте заполненный документ.
                4) Отправьте отсканированный документ в ответном сообщении
                P.S. Документ должен быть в формате .doc или .pdf""";
    private static final String ANSWER_MESSAGE_TEXT = "Ваш документ был отправлен на проверку. Ожидайте ответа. А пока можете продолжить регистрацию";
    private static final File FILE = new File("volunteers_bot/src/main/resources/static/Согласие.pdf");
    private static final long MAX_DOCUMENT_SIZE_KB = getMaxDocumentSize();

    @Override
    public void prepare(ChatHash chatHash, AbsSender sender) {
        Message message = MessageUtil.sendFile(chatHash.getId(), FILE, PREPARE_MESSAGE_TEXT, sender);
        int messageId = message != null ? message.getMessageId() : -1;
        chatHash.setPrevBotMessageId(messageId);
    }

    @Override
    protected ResultDto isValidFile(MessageDto messageDto, AbsSender sender) {
        Document document = messageDto.getDocument();

        if (document == null) {
            return new ResultDto(false, "Вам необходимо отправить документ в ответном сообщении");
        }

        if (document.getFileSize() > MAX_DOCUMENT_SIZE_KB * 1024) {
            return new ResultDto(false, "Размер документа не должен превышать ".concat(String.valueOf(MAX_DOCUMENT_SIZE_KB)).concat("KB"));
        }

        if (!document.getFileName().endsWith(".doc") && !document.getFileName().endsWith(".pdf")) {
            return new ResultDto(false, "Формат документа должен быть в формате .doc или .pdf");
        }

        return new ResultDto(true);
    }

    @Override
    protected int finishStep(ChatHash chatHash, AbsSender sender, String data) {
        sendFinishMessage(chatHash, sender, data);
        return 0;
    }

    @Override
    protected String getAnswerMessageText() {
        return ANSWER_MESSAGE_TEXT;
    }

    @Override
    protected File download(MessageDto messageDto, AbsSender sender) {
        return MessageUtil.downloadFile(messageDto.getDocument().getFileId(), sender);
    }

    @Override
    protected void saveDocument(long chatId, String path) {
        documentService.create(chatId, path, EDocument.CHILD_DOCUMENT);
    }

    private static long getMaxDocumentSize() {
        long fileSizeKb = FILE.length() / 1024;
        return (fileSizeKb * 2 / 100 + 1) * 100;
    }
}
