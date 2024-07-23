package org.example.steps.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.enums.EEducationStatus;
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
public class EducationStatusChoiceStep extends ChoiceStep {
    private final VolonteerService volonteerService;
    private static final String PREPARE_MESSAGE_TEXT = "Укажите, на какой <b>стадии получения образования</b> вы сейчас находитесь:";
    private final static List<ButtonDto> buttonDtoList;

    static {
        buttonDtoList = ButtonUtil.educationStatusButtonList();
    }

    @Override
    public int execute(ChatHash chatHash, MessageDto messageDto, AbsSender sender) {
        int stepIndex = super.execute(chatHash, messageDto, sender);
        if (stepIndex != 0) return stepIndex;

        String educationStatus = messageDto.getData();
        try {
            EEducationStatus eEducationStatus = EEducationStatus.valueOf(educationStatus);
            saveEducation(chatHash.getId(), eEducationStatus);
            finishStep(chatHash, sender, getAnswerMessageText(eEducationStatus.getEEducationStatusStr()));
            return 0;
        } catch (IllegalArgumentException e) {
            log.error("Chat ID={} Incorrect educationStatus choice: {}", chatHash.getId(), educationStatus);
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

    private void saveEducation(long chatId, EEducationStatus eEducationStatus) {
        Volonteer volonteer = volonteerService.getVolonteerByChatId(chatId);
        volonteer.setEducationStatus(eEducationStatus);
        volonteerService.saveAndFlushVolonteer(volonteer);
    }

    private String getAnswerMessageText(String answer) {
        return "Ваш ответ: <b>".concat(answer).concat("</b>");
    }
}
