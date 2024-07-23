package org.example.steps.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.pojo.dto.MessageDto;
import org.example.pojo.dto.ResultDto;
import org.example.pojo.entities.ChatHash;
import org.example.pojo.entities.Volunteer;
import org.example.services.VolunteerService;
import org.example.steps.InputStep;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.bots.AbsSender;

@Slf4j
@Component
@RequiredArgsConstructor
public class EducationInstitutionInputStep extends InputStep {
    private final VolunteerService volunteerService;
    private static final String PREPARE_MESSAGE_TEXT = "Введите ваше <b>учебное заведение</b>:";

    @Override
    public int execute(ChatHash chatHash, MessageDto messageDto, AbsSender sender) {
        String educationInstitution = messageDto.getData();
        ResultDto result = setEducationInstitution(chatHash.getId(), educationInstitution);
        if (result.isDone()) {
            finishStep(chatHash, sender, getAnswerMessageText(educationInstitution));
            return 0;
        }

        return handleIllegalUserAction(messageDto, sender, result.getMessage());
    }

    @Override
    protected String getPREPARE_MESSAGE_TEXT() {
        return PREPARE_MESSAGE_TEXT;
    }

    private ResultDto setEducationInstitution(long chatId, String educationInstitution) {
        if (!isValidEducationInstitution(educationInstitution)) {
            log.error("Chat ID={} incorrect education institution: {}", chatId, educationInstitution);
            return new ResultDto(false, "Такого учебного заведения не существует");
        }

        saveEducationInstitution(chatId, educationInstitution);
        return new ResultDto(true);
    }

    private boolean isValidEducationInstitution(String educationInstitution) {
        return true;
    } // TODO : Сделать какую-нибудь проверку

    private void saveEducationInstitution(long chatId, String educationInstitution) {
        Volunteer volunteer = volunteerService.getVolunteerByChatId(chatId);
        volunteer.setEducationInstitution(educationInstitution);
        volunteerService.saveAndFlushVolunteer(volunteer);
    }

    private String getAnswerMessageText(String answer) {
        return "Ваше учебное заведение: <b>".concat(answer).concat("</b>");
    }
}
