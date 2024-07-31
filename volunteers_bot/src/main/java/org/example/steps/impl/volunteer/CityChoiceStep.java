package org.example.steps.impl.volunteer;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.dto.ButtonDto;
import org.example.enums.ECity;
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
public class CityChoiceStep extends ChoiceStep {
    private final VolunteerService volunteerService;
    private static final String PREPARE_MESSAGE_TEXT = "Укажите ваш <b>город</b>:";

    @PostConstruct
    public void init() {
        setButtonDtoList(getCityButtonDtoList());
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

    private List<ButtonDto> getCityButtonDtoList() {
        ECity[] eCityArray = ECity.values();
        List<ButtonDto> buttonDtoList = new ArrayList<>();
        for (int i = 0; i < eCityArray.length; i++) {
            buttonDtoList.add(new ButtonDto(eCityArray[i].toString(), eCityArray[i].getCityStr(), i));
        }

        return buttonDtoList;
    }
}
