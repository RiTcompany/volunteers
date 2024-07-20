package org.example.steps.impl;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.enums.EConversationStep;
import org.example.enums.EGender;
import org.example.pojo.dto.ButtonDto;
import org.example.pojo.dto.MessageDto;
import org.example.pojo.entities.ChatHash;
import org.example.pojo.entities.Volonteer;
import org.example.services.VolonteerService;
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
@RequiredArgsConstructor
public class GenderChoiceStep extends ChoiceStep {
    private final VolonteerService volonteerService;
    @Getter(AccessLevel.PROTECTED)
    private static final String PREPARE_MESSAGE_TEXT = "Укажите ваш <b>пол</b>: ";
    private static final String ANSWER_MESSAGE_TEXT_TEMPLATE = "Ваш пол: <b>";
    private final static List<ButtonDto> buttonDtoList;

    static {
        buttonDtoList = ButtonUtil.genderButtonList();
    }

    @Override
    public EConversationStep execute(ChatHash chatHash, MessageDto messageDto, AbsSender sender) {
        EConversationStep eConversationStep = super.execute(chatHash, messageDto, sender);
        if (eConversationStep != null) return eConversationStep;

        String gender = messageDto.getData();
        try {
            EGender eGender = EGender.valueOf(gender);
            saveGender(chatHash.getId(), eGender);
            finishStep(chatHash, sender, ANSWER_MESSAGE_TEXT_TEMPLATE.concat(eGender.getGenderStr()).concat("</b>"));
            return eConversationStepList.get(0);
        } catch (IllegalArgumentException e) {
            log.error("Chat ID={} Incorrect gender choice: {}", chatHash.getId(), gender);
            return handleIllegalUserAction(chatHash, messageDto, sender, EXCEPTION_MESSAGE_TEXT);
        }
    }

    @Override
    protected void setButtonList(long chatId) {
        setButtonDtoList(buttonDtoList);
    }

    private void saveGender(long chatId, EGender eGender) {
        Volonteer volonteer = volonteerService.getVolonteerByChatId(chatId);
        volonteer.setGender(eGender);
        volonteerService.saveAndFlushVolonteer(volonteer);
    }
}
