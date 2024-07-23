package org.example.steps.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.pojo.dto.MessageDto;
import org.example.pojo.dto.ResultDto;
import org.example.pojo.entities.ChatHash;
import org.example.pojo.entities.Volonteer;
import org.example.services.VolonteerService;
import org.example.steps.InputStep;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.bots.AbsSender;

@Slf4j
@Component
@RequiredArgsConstructor
public class FullNameInputStep extends InputStep {
    private final VolonteerService volonteerService;
    private static final String PREPARE_MESSAGE_TEXT = "Введите ваше <b>ФИО</b> полностью:";
    private static final int MAX_FULL_NAME_LENGTH = 100;

    @Override
    public int execute(ChatHash chatHash, MessageDto messageDto, AbsSender sender) {
        String fullName = messageDto.getData();
        ResultDto result = setFullName(chatHash.getId(), fullName);
        if (result.isDone()) {
            finishStep(chatHash, sender, getAnswerMessageText(fullName));
            return 0;
        }

        return handleIllegalUserAction(messageDto, sender, result.getMessage());
    }

    @Override
    protected String getPREPARE_MESSAGE_TEXT() {
        return PREPARE_MESSAGE_TEXT;
    }

    private ResultDto setFullName(long chatId, String fullName) {
        if (isLongFullName(fullName)) {
            log.error("Chat ID={} incorrect full name: {}", chatId, fullName);
            return new ResultDto(false, "Ваше ФИО слишком длинное, по нашим данным такого существовать не может");
        }

        saveFullName(chatId, fullName);
        return new ResultDto(true);
    }

    private boolean isLongFullName(String fullName) {
        return fullName.length() > MAX_FULL_NAME_LENGTH;
    }

    // TODO : возможно стоит добавить проверка на 1 или 2 пробела (если нет отчества)

    private void saveFullName(long chatId, String fullName) {
        Volonteer volonteer = volonteerService.getVolonteerByChatId(chatId);
        volonteer.setFullName(fullName);
        volonteerService.saveAndFlushVolonteer(volonteer);
    }

    private String getAnswerMessageText(String answer) {
        return "Ваше ФИО: <b>".concat(answer).concat("</b>");
    }
}
