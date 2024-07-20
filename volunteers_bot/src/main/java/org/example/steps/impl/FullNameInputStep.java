package org.example.steps.impl;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.enums.EConversationStep;
import org.example.pojo.dto.MessageDto;
import org.example.pojo.entities.ChatHash;
import org.example.pojo.entities.Volonteer;
import org.example.services.VolonteerService;
import org.example.steps.InputStep;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.bots.AbsSender;

@Slf4j
@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@RequiredArgsConstructor
public class FullNameInputStep extends InputStep {
    private final VolonteerService volonteerService;
    @Getter(AccessLevel.PROTECTED)
    private static final String PREPARE_MESSAGE_TEXT = "Введите ваше <b>ФИО</b> полностью: ";
    private static final String ANSWER_MESSAGE_TEXT_TEMPLATE = "Ваше ФИО: <b>";
    private static final String EXCEPTION_MESSAGE_TEXT = "Ваше ФИО некорректно. Причина: слишком длинные данные";
    private static final int MAX_FULL_NAME_LENGTH = 100;

    @Override
    public EConversationStep execute(ChatHash chatHash, MessageDto messageDto, AbsSender sender) {
        String fullName = messageDto.getData();
        boolean citySaved = setFullName(chatHash.getId(), fullName);
        if (citySaved) {
            finishStep(chatHash, sender, ANSWER_MESSAGE_TEXT_TEMPLATE.concat(fullName).concat("</b>"));
            return eConversationStepList.get(0);
        }

        return handleIllegalUserAction(chatHash, messageDto, sender, EXCEPTION_MESSAGE_TEXT);
    }

    private boolean setFullName(long chatId, String fullName) {
        if (!isValidFullName(fullName)) {
            log.error("Chat ID={} incorrect full name: {}", chatId, fullName);
            return false;
        }

        Volonteer volonteer = volonteerService.getVolonteerByChatId(chatId);
        volonteer.setFullName(fullName);
        volonteerService.saveAndFlushVolonteer(volonteer);
        return true;
    }

    private boolean isValidFullName(String fullName) {
        return fullName.length() < MAX_FULL_NAME_LENGTH;
    } // TODO : возможно стоит добавить проверка на 1 или 2 пробела (если нет отчества)
}
