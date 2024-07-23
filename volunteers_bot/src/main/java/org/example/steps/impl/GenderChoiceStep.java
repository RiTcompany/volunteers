package org.example.steps.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.enums.EGender;
import org.example.pojo.dto.ButtonDto;
import org.example.pojo.dto.MessageDto;
import org.example.pojo.entities.ChatHash;
import org.example.pojo.entities.Volonteer;
import org.example.services.VolonteerService;
import org.example.steps.ChoiceStep;
import org.example.utils.ButtonUtil;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.bots.AbsSender;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class GenderChoiceStep extends ChoiceStep {
    private final VolonteerService volonteerService;
    private static final String PREPARE_MESSAGE_TEXT = "Укажите ваш <b>пол</b>:";
    private final static List<ButtonDto> buttonDtoList;

    static {
        buttonDtoList = ButtonUtil.genderButtonList();
    }

    @Override
    public int execute(ChatHash chatHash, MessageDto messageDto, AbsSender sender) {
        int stepIndex = super.execute(chatHash, messageDto, sender);
        if (stepIndex != 0) return stepIndex;

        String gender = messageDto.getData();
        try {
            EGender eGender = EGender.valueOf(gender);
            saveGender(chatHash.getId(), eGender);
            finishStep(chatHash, sender, getAnswerMessageText(eGender.getGenderStr()));
            return 0;
        } catch (IllegalArgumentException e) {
            log.error("Chat ID={} Incorrect gender choice: {}", chatHash.getId(), gender);
            return handleIllegalUserAction(messageDto, sender, EXCEPTION_MESSAGE_TEXT);
        }
    }

    @Override
    protected void setButtonList() {
        setButtonDtoList(buttonDtoList);
    }

    @Override
    protected String getPREPARE_MESSAGE_TEXT() {
        return PREPARE_MESSAGE_TEXT;
    }

    private void saveGender(long chatId, EGender eGender) {
        Volonteer volonteer = volonteerService.getVolonteerByChatId(chatId);
        volonteer.setGender(eGender);
        volonteerService.saveAndFlushVolonteer(volonteer);
    }

    private String getAnswerMessageText(String answer) {
        return "Ваш пол: <b>".concat(answer).concat("</b>");
    }
}
