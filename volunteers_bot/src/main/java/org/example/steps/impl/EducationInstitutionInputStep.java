package org.example.steps.impl;

import lombok.extern.slf4j.Slf4j;
import org.example.enums.EConversationStep;
import org.example.pojo.dto.MessageDto;
import org.example.pojo.entities.ChatHash;
import org.example.pojo.entities.Volonteer;
import org.example.repositories.VolonteerRepository;
import org.example.steps.InputStep;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.bots.AbsSender;

@Slf4j
@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class EducationInstitutionInputStep extends InputStep {
    private static final String PREPARE_MESSAGE_TEXT = "Введите ваше <b>учебное заведение</b>: ";
    private static final String ANSWER_MESSAGE_TEXT_TEMPLATE = "Ваше учебное заведение: <b>";
    private static final String EXCEPTION_MESSAGE_TEXT = "Технические шоколадки";

    public EducationInstitutionInputStep(VolonteerRepository volonteerRepository) {
        super(volonteerRepository, PREPARE_MESSAGE_TEXT);
    }

    @Override
    public EConversationStep execute(ChatHash chatHash, MessageDto messageDto, AbsSender sender) {
        String educationInstitution = messageDto.getData();
        boolean isEducationInstitutionSaved = setEducationInstitution(chatHash, educationInstitution);
        if (isEducationInstitutionSaved) {
            finishStep(chatHash, messageDto, sender, ANSWER_MESSAGE_TEXT_TEMPLATE.concat(educationInstitution).concat("</b>"));
            return eConversationStepList.get(0);
        }

        return handleIllegalUserAction(chatHash, messageDto, sender, EXCEPTION_MESSAGE_TEXT);
    }

    private boolean setEducationInstitution(ChatHash chatHash, String educationInstitution) {
        if (!isValidEducationInstitution(educationInstitution)) {
            log.error("Chat ID={} incorrect education institution: {}", chatHash.getId(), educationInstitution);
            return false;
        }

        Volonteer volonteer = getVolonteer(chatHash);
        volonteer.setEducationInstitution(educationInstitution);
        saveAndFlushVolonteer(volonteer);
        return true;
    }

    private boolean isValidEducationInstitution(String educationInstitution) {
        return true;
    } // TODO : Сделать какую-нибудь проверку
}
