package org.example.steps.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.pojo.dto.MessageDto;
import org.example.pojo.dto.ResultDto;
import org.example.pojo.entities.ChatHash;
import org.example.services.VolunteerService;
import org.example.steps.InputStep;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.bots.AbsSender;

@Slf4j
@Component
@RequiredArgsConstructor
public class EmailInputStep extends InputStep {
    private final VolunteerService volunteerService;
    private static final String PREPARE_MESSAGE_TEXT = "Введите ваш <b>Email</b>:";

    @Override
    public int execute(ChatHash chatHash, MessageDto messageDto, AbsSender sender) {
        String email = messageDto.getData();
        ResultDto result = setEmail(chatHash.getId(), email);
        if (result.isDone()) {
            finishStep(chatHash, sender, getAnswerMessageText(email));
            return 0;
        }

        return handleIllegalUserAction(messageDto, sender, result.getMessage());
    }

    @Override
    protected String getPREPARE_MESSAGE_TEXT() {
        return PREPARE_MESSAGE_TEXT;
    }

    private ResultDto setEmail(long chatId, String email) {
//        TODO : будем ли проверить верность email ???
//        Можно проверить по регулярке

        saveEmail(chatId, email);
        return new ResultDto(true);
    }

    private void saveEmail(long chatId, String email) {
//        Volunteer volunteer = volunteerService.getVolunteerByChatId(chatId);
//        volunteer.setEmail(email);
//        volunteerService.saveAndFlushVolunteer(volunteer);
    }

    private String getAnswerMessageText(String email) {
        return "Ваш email: <b>".concat(email).concat("</b>");
    }
}
