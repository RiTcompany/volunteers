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
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.PhotoSize;
import org.telegram.telegrambots.meta.bots.AbsSender;

import java.io.File;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class PhotoSendStep extends FileSendStep {
    private final DocumentService documentService;
    private static final String PREPARE_MESSAGE_TEXT = "Отправьте ваше <b>фото</b> форматом 3x4 на нейтральном фоне:";
    private static final String ANSWER_MESSAGE_TEXT = "Ваше фото было отправлено на модерацию. Ожидайте ответа.";
    private static final int MAX_PHOTO_SIZE_MB = 10;

    @Override
    public void prepare(ChatHash chatHash, AbsSender sender) {
        Message message = MessageUtil.sendMessageText(chatHash.getId(), PREPARE_MESSAGE_TEXT, sender);
        int messageId = message != null ? message.getMessageId() : -1;
        chatHash.setPrevBotMessageId(messageId);
    }

    @Override
    protected int finishStep(ChatHash chatHash, AbsSender sender, String data) {
        sendFinishMessage(chatHash, sender, data);
        return 0;
    }

    @Override
    protected ResultDto isValidFile(MessageDto messageDto, AbsSender sender) {
        List<PhotoSize> photoList = messageDto.getPhotoList();
        if (photoList == null || photoList.isEmpty()) {
            return new ResultDto(false, "Вам необходимо отправить фото в ответном сообщении");
        }

        PhotoSize photo = photoList.get(0);
        if (photo.getFileSize() > MAX_PHOTO_SIZE_MB * 1024 * 1024) {
            return new ResultDto(false, "Размер фото не должен превышать ".concat(String.valueOf(MAX_PHOTO_SIZE_MB)).concat("MB"));
        }

        double proportion = (double) photo.getWidth() / photo.getHeight();
        if (!(0.7 < proportion && proportion < 0.8)) {
            return new ResultDto(false, "Формат фото должен быть 3x4");
        }

        return new ResultDto(true);
    }

    @Override
    protected File download(MessageDto messageDto, AbsSender sender) {
        List<PhotoSize> photoList = messageDto.getPhotoList();
        String fieldId = photoList.get(photoList.size() - 1).getFileId();
        for (int i = 1; i < photoList.size(); i++) {
            if (photoList.get(i).getFileSize() > MAX_PHOTO_SIZE_MB * 1024 * 1024) {
                fieldId = photoList.get(i - 1).getFileId();
                break;
            }
        }

        return MessageUtil.downloadFile(fieldId, sender);
    }

    @Override
    protected void saveDocument(long chatId, String path) {
        documentService.create(chatId, path, EDocument.VOLUNTEER_PHOTO);
    }

    @Override
    protected String getAnswerMessageText() {
        return ANSWER_MESSAGE_TEXT;
    }
}
