package org.example.steps.impl.volunteer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
public class VolunteerIdInputStep extends InputStep {
    private final VolunteerService volunteerService;
    private static final String PREPARE_MESSAGE_TEXT = "С сайта https://волонтёрыпобеды.рф введите ваш <b>ID</b>:";

    @Override
    protected String getPrepareMessageText() {
        return PREPARE_MESSAGE_TEXT;
    }

    @Override
    protected ResultDto isValidData(String data) {
//        TODO : будем ли проверить верность id ???
        return new ResultDto(true);
    }

    @Override
    protected void saveData(long chatId, String data) {
        Volunteer volunteer = volunteerService.getByChatId(chatId);
        volunteer.setVolunteerId(data);
        volunteerService.saveAndFlush(volunteer);
    }

    @Override
    protected int finishStep(ChatHash chatHash, AbsSender sender, String data) {
        saveData(chatHash.getId(), data);
        cleanPreviousMessage(chatHash, sender, getAnswerMessageText(data));
        return 0;
    }

    private String getAnswerMessageText(String id) {
        return "Ваш ID: <b>".concat(id).concat("</b>");
    }
}
