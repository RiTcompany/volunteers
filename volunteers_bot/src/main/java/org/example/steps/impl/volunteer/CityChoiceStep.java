package org.example.steps.impl.volunteer;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.enums.ECity;
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
public class CityChoiceStep extends ChoiceStep {
    private final VolunteerService volunteerService;
    private static final String PREPARE_MESSAGE_TEXT = "Укажите ваш <b>город</b>:";

    @PostConstruct
    public void init() {
        setButtonDtoList(ButtonUtil.cityButtonList());
    }

    @Override
    protected String getPrepareMessageText() {
        return CityChoiceStep.PREPARE_MESSAGE_TEXT;
    }

    @Override
    protected ResultDto isValidData(String data) {
        try {
            ECity.valueOf(data);
            return new ResultDto(true);
        } catch (IllegalArgumentException e) {
            return new ResultDto(false, EXCEPTION_MESSAGE_TEXT);
        }
    }

    @Override
    protected int finishStep(ChatHash chatHash, AbsSender sender, String data) throws EntityNotFoundException {
        ECity eCity = ECity.valueOf(data);
        sendFinishMessage(chatHash, sender, getAnswerMessageText(eCity.getCityStr()));
        if (ECity.OTHER.equals(eCity)) {
            return 0;
        }

        saveDefaultCity(chatHash);
        return 1;
    }

    private void saveDefaultCity(ChatHash chatHash) throws EntityNotFoundException {
        Volunteer volunteer = volunteerService.getByChatId(chatHash.getId());
        volunteer.setCity(ECity.SPB.getCityStr());
        volunteerService.saveAndFlush(volunteer);
    }

    private String getAnswerMessageText(String answer) {
        return "Ваш город: <b>".concat(answer).concat("</b>");
    }
}
