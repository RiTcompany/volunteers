package org.example.steps.impl.volunteer;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.enums.EGender;
import org.example.exceptions.EntityNotFoundException;
import org.example.pojo.dto.ResultDto;
import org.example.pojo.entities.ChatHash;
import org.example.pojo.entities.Volunteer;
import org.example.services.VolunteerService;
import org.example.steps.ChoiceStep;
import org.example.utils.ButtonUtil;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.bots.AbsSender;

@Slf4j
@Component
@RequiredArgsConstructor
public class GenderChoiceStep extends ChoiceStep {
    private final VolunteerService volunteerService;
    private static final String PREPARE_MESSAGE_TEXT = "Укажите ваш <b>пол</b>:";

    @PostConstruct
    public void init() {
        setButtonDtoList(ButtonUtil.genderButtonList());
    }

    @Override
    protected String getPrepareMessageText() {
        return PREPARE_MESSAGE_TEXT;
    }

    @Override
    protected ResultDto isValidData(String data) {
        try {
            EGender.valueOf(data);
            return new ResultDto(true);
        } catch (IllegalArgumentException e) {
            return new ResultDto(false, EXCEPTION_MESSAGE_TEXT);
        }
    }

    @Override
    protected int finishStep(ChatHash chatHash, AbsSender sender, String data) throws EntityNotFoundException {
        EGender eGender = EGender.valueOf(data);
        sendFinishMessage(chatHash, sender, getAnswerMessageText(eGender.getGenderStr()));
        saveGender(chatHash.getId(), eGender);
        return 0;
    }

    private void saveGender(long chatId, EGender eGender) throws EntityNotFoundException {
        Volunteer volunteer = volunteerService.getByChatId(chatId);
        volunteer.setGender(eGender);
        volunteerService.saveAndFlush(volunteer);
    }

    private String getAnswerMessageText(String answer) {
        return "Ваш пол: <b>".concat(answer).concat("</b>");
    }
}
