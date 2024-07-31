package org.example.steps.impl.volunteer;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.dto.ButtonDto;
import org.example.enums.EGender;
import org.example.exceptions.EntityNotFoundException;
import org.example.dto.ResultDto;
import org.example.entities.ChatHash;
import org.example.entities.Volunteer;
import org.example.services.VolunteerService;
import org.example.steps.ChoiceStep;
import org.example.utils.ButtonUtil;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.bots.AbsSender;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class GenderChoiceStep extends ChoiceStep {
    private final VolunteerService volunteerService;
    private static final String PREPARE_MESSAGE_TEXT = "Укажите ваш <b>пол</b>:";

    @PostConstruct
    public void init() {
        setButtonDtoList(getGenderButtonDtoList());
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
        sendFinishMessage(chatHash, sender, getAnswerMessageText(eGender.getGender()));
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

    private List<ButtonDto> getGenderButtonDtoList() {
        EGender[] eGenderArray = EGender.values();
        List<ButtonDto> buttonDtoList = new ArrayList<>();
        for (int i = 0; i < eGenderArray.length; i++) {
            buttonDtoList.add(new ButtonDto(eGenderArray[i].toString(), eGenderArray[i].getGender(), i));
        }

        return buttonDtoList;
    }
}
