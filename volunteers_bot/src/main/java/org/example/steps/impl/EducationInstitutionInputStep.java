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
public class EducationInstitutionInputStep extends InputStep {
    private final VolonteerService volonteerService;
    @Getter(AccessLevel.PROTECTED)
    private static final String PREPARE_MESSAGE_TEXT = "Введите ваше <b>учебное заведение</b>: ";
    private static final String ANSWER_MESSAGE_TEXT_TEMPLATE = "Ваше учебное заведение: <b>";
    private static final String EXCEPTION_MESSAGE_TEXT = "Технические шоколадки";

    @Override
    public EConversationStep execute(ChatHash chatHash, MessageDto messageDto, AbsSender sender) {
        String educationInstitution = messageDto.getData();
        boolean isEducationInstitutionSaved = setEducationInstitution(chatHash.getId(), educationInstitution);
        if (isEducationInstitutionSaved) {
            finishStep(chatHash, sender, ANSWER_MESSAGE_TEXT_TEMPLATE.concat(educationInstitution).concat("</b>"));
            return eConversationStepList.get(0);
        }

        return handleIllegalUserAction(chatHash, messageDto, sender, EXCEPTION_MESSAGE_TEXT);
    }

    private boolean setEducationInstitution(long chatId, String educationInstitution) {
        if (!isValidEducationInstitution(educationInstitution)) {
            log.error("Chat ID={} incorrect education institution: {}", chatId, educationInstitution);
            return false;
        }

        Volonteer volonteer = volonteerService.getVolonteerByChatId(chatId);
        volonteer.setEducationInstitution(educationInstitution);
        volonteerService.saveAndFlushVolonteer(volonteer);
        return true;
    }

    private boolean isValidEducationInstitution(String educationInstitution) {
        return true;
    } // TODO : Сделать какую-нибудь проверку
}
