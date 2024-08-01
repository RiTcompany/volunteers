package org.example.steps.impl.volunteer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.dto.MessageDto;
import org.example.dto.ResultDto;
import org.example.entities.ChatHash;
import org.example.services.ChildDocumentService;
import org.example.steps.FileSendStep;
import org.example.utils.MessageUtil;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Document;
import org.telegram.telegrambots.meta.bots.AbsSender;

import java.io.File;


@Slf4j
@Component
@RequiredArgsConstructor
public class ChildDocumentSendStep extends FileSendStep {
    private final ChildDocumentService childDocumentService;
    private static final String PREPARE_MESSAGE_TEXT = """
            Вам необходимо согласие родителей. Для этого необходимо сделать следующие шаги:
                1) Скачайте и распечатайте документ.
                2) Передайте документ родителям для заполнения.
                3) Отсканируйте заполненный документ.
                4) Отправьте отсканированный документ в ответном сообщении
                P.S. Документ должен быть в формате .doc или .pdf""";
    private static final String ANSWER_MESSAGE_TEXT = "Ваш документ был отправлен на проверку. Ожидайте ответа. А пока можете продолжить регистрацию";
//    TODO : изменить путь на короткий
    private static final File FILE = new File("C:\\Users\\User\\IdeaProjects\\volunteers\\volunteers_bot\\src\\main\\resources\\static\\Согласие.pdf");
//    TODO : уточнить, какой макс размер файла должен быть
    private static final int MAX_DOCUMENT_SIZE_KB = 300;

    @Override
    public void prepare(ChatHash chatHash, AbsSender sender) {
        int messageId = MessageUtil.sendFile(chatHash.getId(), FILE, PREPARE_MESSAGE_TEXT, sender);
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
    protected void saveFile(long chatId, MessageDto messageDto, AbsSender sender) {
        File file = MessageUtil.downloadFile(messageDto.getDocument().getFileId(), sender);
        if (file != null) {
            childDocumentService.create(chatId, file.getPath());
        }
//        TODO : добавить что-то, если вдруг файл не скачан
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
}
