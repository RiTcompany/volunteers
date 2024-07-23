package org.example.steps.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.pojo.dto.MessageDto;
import org.example.pojo.dto.ResultDto;
import org.example.pojo.entities.ChatHash;
import org.example.services.VolonteerService;
import org.example.steps.InputStep;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.bots.AbsSender;

@Slf4j
@Component
@RequiredArgsConstructor
public class VolonteerIdInputStep extends InputStep {
    private final VolonteerService volonteerService;
    private static final String PREPARE_MESSAGE_TEXT = "С сайта https://волонтёрыпобеды.рф введите ваш <b>ID</b>:";

    @Override
    public int execute(ChatHash chatHash, MessageDto messageDto, AbsSender sender) {
        String volonteerId = messageDto.getData();
        ResultDto result = setVolonteerId(chatHash.getId(), volonteerId);
        if (result.isDone()) {
            finishStep(chatHash, sender, getAnswerMessageText(volonteerId));
            return 0;
        }

        return handleIllegalUserAction(messageDto, sender, result.getMessage());
    }

    @Override
    protected String getPREPARE_MESSAGE_TEXT() {
        return PREPARE_MESSAGE_TEXT;
    }

    private ResultDto setVolonteerId(long chatId, String email) {
//        TODO : будем ли проверить верность id ???

        saveVolonteerId(chatId, email);
        return new ResultDto(true);
    }

    private void saveVolonteerId(long chatId, String volonteerId) {
//        Volonteer volonteer = volonteerService.getVolonteerByChatId(chatId);
//        volonteer.setVolonteerId(volonteerId);
//        volonteerService.saveAndFlushVolonteer(volonteer);
    }

    private String getAnswerMessageText(String id) {
        return "Ваш ID: <b>".concat(id).concat("</b>");
    }
}
