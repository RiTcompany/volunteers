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
public class ReasonInputStep extends InputStep {
    private final VolonteerService volonteerService;
    private static final String PREPARE_MESSAGE_TEXT = "Расскажите, почему вы решили стать волонтёром";
    private static final String ANSWER_MESSAGE_TEXT = "Спасибо за ваш ответ";

    @Override
    public int execute(ChatHash chatHash, MessageDto messageDto, AbsSender sender) {
        String reason = messageDto.getData();
        ResultDto result = setReason(chatHash.getId(), reason);
        if (result.isDone()) {
            finishStep(chatHash, sender, ANSWER_MESSAGE_TEXT);
            return 0;
        }

        return handleIllegalUserAction(messageDto, sender, result.getMessage());
    }

    @Override
    protected String getPREPARE_MESSAGE_TEXT() {
        return PREPARE_MESSAGE_TEXT;
    }

    private ResultDto setReason(long chatId, String reason) {
//        TODO : нужно ли ограничивать ответ по длине? Вдруг нам закинуть письмо на 10000000000000000 символов
        saveReason(chatId, reason);
        return new ResultDto(true);
    }

    private void saveReason(long chatId, String reason) {
//        Volonteer volonteer = volonteerService.getVolonteerByChatId(chatId);
//        volonteer.setReason(reason);
//        volonteerService.saveAndFlushVolonteer(volonteer);
    }
}
