package org.example.steps.impl.volunteer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.dto.ButtonDto;
import org.example.dto.MessageDto;
import org.example.dto.ResultDto;
import org.example.entities.ChatHash;
import org.example.entities.Volunteer;
import org.example.enums.ECity;
import org.example.exceptions.EntityNotFoundException;
import org.example.mappers.KeyboardMapper;
import org.example.services.VolunteerService;
import org.example.steps.ChoiceStep;
import org.example.utils.StepUtil;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.bots.AbsSender;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class CityChoiceStep extends ChoiceStep {
    private final VolunteerService volunteerService;
    private final KeyboardMapper keyboardMapper;
    private static final String PREPARE_MESSAGE_TEXT = "Укажите ваш <b>город</b>:";

    @Override
    public void prepare(ChatHash chatHash, AbsSender sender) throws EntityNotFoundException {
        StepUtil.sendPrepareMessageWithInlineKeyBoard(
                chatHash,
                PREPARE_MESSAGE_TEXT,
                keyboardMapper.keyboardDto(chatHash, getCityButtonDtoList()),
                sender
        );
    }

    @Override
    protected ResultDto isValidData(MessageDto messageDto) {
        if (!isCallback(messageDto.getEMessage())) {
            return new ResultDto(false, EXCEPTION_MESSAGE_TEXT);
        }

        try {
            ECity.valueOf(messageDto.getData());
            return new ResultDto(true);
        } catch (IllegalArgumentException e) {
            return new ResultDto(false, EXCEPTION_MESSAGE_TEXT);
        }
    }

    @Override
    protected int finishStep(ChatHash chatHash, AbsSender sender, String data) throws EntityNotFoundException {
        ECity eCity = ECity.valueOf(data);
        sendFinishMessage(chatHash, sender, getAnswerMessageText(eCity.getString()));
        if (ECity.OTHER.equals(eCity)) {
            return 0;
        }

        saveDefaultCity(chatHash);
        return 1;
    }

    private void saveDefaultCity(ChatHash chatHash) throws EntityNotFoundException {
        Volunteer volunteer = volunteerService.getByChatId(chatHash.getId());
        volunteer.setCity(ECity.SPB.getString());
        volunteerService.saveAndFlush(volunteer);
    }

    private String getAnswerMessageText(String answer) {
        return "Ваш город: <b>".concat(answer).concat("</b>");
    }

    private List<ButtonDto> getCityButtonDtoList() {
        ECity[] eCityArray = ECity.values();
        List<ButtonDto> buttonDtoList = new ArrayList<>();
        for (int i = 0; i < eCityArray.length; i++) {
            buttonDtoList.add(new ButtonDto(eCityArray[i].toString(), eCityArray[i].getString(), i));
        }

        return buttonDtoList;
    }
}
