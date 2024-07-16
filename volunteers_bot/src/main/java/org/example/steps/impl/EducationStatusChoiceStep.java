package org.example.steps.impl;

import lombok.extern.slf4j.Slf4j;
import org.example.enums.EConversationStep;
import org.example.enums.EEducationStatus;
import org.example.mappers.KeyboardMapper;
import org.example.pojo.dto.ButtonDto;
import org.example.pojo.dto.MessageDto;
import org.example.pojo.entities.ChatHash;
import org.example.pojo.entities.Volonteer;
import org.example.repositories.VolonteerRepository;
import org.example.steps.ChoiceStep;
import org.example.utils.ButtonUtil;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.bots.AbsSender;

import java.util.List;

@Slf4j
@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class EducationStatusChoiceStep extends ChoiceStep {
    private static final String PREPARE_MESSAGE_TEXT = "Укажите, на какой <b>стадии получения образования</b> вы сейчас находитесь: ";
    private static final String ANSWER_MESSAGE_TEXT_TEMPLATE = "Ваш ответ: <b>";
    private final static List<ButtonDto> buttonDtoList;

    static {
        buttonDtoList = ButtonUtil.educationStatusButtonList();
    }

    public EducationStatusChoiceStep(VolonteerRepository volonteerRepository, KeyboardMapper keyboardMapper) {
        super(volonteerRepository, PREPARE_MESSAGE_TEXT, keyboardMapper);
    }

    @Override
    public EConversationStep execute(ChatHash chatHash, MessageDto messageDto, AbsSender sender) {
        EConversationStep eConversationStep = super.execute(chatHash, messageDto, sender);
        if (eConversationStep != null) return eConversationStep;

        String educationStatus = messageDto.getData();
        try {
            EEducationStatus eEducationStatus = EEducationStatus.valueOf(educationStatus);
            saveEducation(chatHash, eEducationStatus);
            finishStep(chatHash, messageDto, sender, ANSWER_MESSAGE_TEXT_TEMPLATE.concat(eEducationStatus.getEEducationStatusStr()).concat("</b>"));
            return eConversationStepList.get(0);
        } catch (IllegalArgumentException e) {
            log.error("Chat ID={} Incorrect educationStatus choice: {}", chatHash.getId(), educationStatus);
            return handleIllegalUserAction(chatHash, messageDto, sender, EXCEPTION_MESSAGE_TEXT);
        }
    }

    @Override
    protected void setButtonList(ChatHash chatHash) {
        setButtonDtoList(buttonDtoList);
    }

    private void saveEducation(ChatHash chatHash, EEducationStatus eEducationStatus) {
        Volonteer volonteer = getVolonteer(chatHash);
        volonteer.setEducationStatus(eEducationStatus);
        saveAndFlushVolonteer(volonteer);
    }
}
