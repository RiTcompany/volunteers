package org.example.steps.impl.volunteer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.exceptions.EntityNotFoundException;
import org.example.pojo.dto.MessageDto;
import org.example.pojo.dto.ResultDto;
import org.example.pojo.entities.ChatHash;
import org.example.pojo.entities.Volunteer;
import org.example.services.VolunteerService;
import org.example.steps.FileSendStep;
import org.example.utils.MessageUtil;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.PhotoSize;
import org.telegram.telegrambots.meta.bots.AbsSender;

import java.io.File;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class PhotoSendStep extends FileSendStep {
    private final VolunteerService volunteerService;
    private static final String PREPARE_MESSAGE_TEXT = "Отправьте ваше <b>фото</b> форматом 3x4 на нейтральном фоне:";
    private static final String ANSWER_MESSAGE_TEXT = "Ваш документ был отправлен на проверку. Ожидайте ответа. А пока можете продолжить регистрацию";
    private static final int MAX_PHOTO_SIZE_MB = 10;

    @Override
    public void prepare(ChatHash chatHash, AbsSender sender) {
        int messageId = MessageUtil.sendMessageText(getPrepareMessageText(), chatHash.getId(), sender);
        chatHash.setPrevBotMessageId(messageId);
    }

    @Override
    protected String getPrepareMessageText() {
        return PREPARE_MESSAGE_TEXT;
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

        int maxPhotoSize = MAX_PHOTO_SIZE_MB * 1024 * 1024;
        if (photoList.get(0).getFileSize() > maxPhotoSize) {
            return new ResultDto(false, "Размер фото не должен превышать ".concat(String.valueOf(MAX_PHOTO_SIZE_MB)).concat("MB"));
        }

        return new ResultDto(true);
    }

    @Override
    protected void saveFile(long chatId, MessageDto messageDto, AbsSender sender) throws EntityNotFoundException {
        File file = downloadPhoto(messageDto.getPhotoList(), sender);
        if (file != null) {
            Volunteer volunteer = volunteerService.getByChatId(chatId);
            volunteer.setPhotoPath(file.getPath());
            volunteerService.saveAndFlush(volunteer);
        }
//        TODO : добавить что-то, если вдруг файл не скачан
    }

    @Override
    protected String getAnswerMessageText() {
        return ANSWER_MESSAGE_TEXT;
    }

    private File downloadPhoto(List<PhotoSize> photoList, AbsSender sender) {
        for (int i = 1; i < photoList.size(); i++) {
            if (photoList.get(i).getFileSize() > MAX_PHOTO_SIZE_MB * 1024 * 1024) {
                return MessageUtil.downloadFile(photoList.get(i - 1).getFileId(), sender);
            }
        }

        return MessageUtil.downloadFile(photoList.get(photoList.size() - 1).getFileId(), sender);
    }
}
