package org.example.steps.impl.volunteer;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.dto.ButtonDto;
import org.example.enums.EEducationStatus;
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
public class EducationStatusChoiceStep extends ChoiceStep {
    private final VolunteerService volunteerService;
    private static final String PREPARE_MESSAGE_TEXT = "Укажите, на какой <b>стадии получения образования</b> вы сейчас находитесь:";

    @PostConstruct
    public void init() {
        setButtonDtoList(getEducationStatusButtonDtoList());
    }

    @Override
    protected String getPrepareMessageText() {
        return PREPARE_MESSAGE_TEXT;
    }

    @Override
    protected ResultDto isValidData(String data) {
        try {
            EEducationStatus.valueOf(data);
            return new ResultDto(true);
        } catch (IllegalArgumentException e) {
            return new ResultDto(false, EXCEPTION_MESSAGE_TEXT);
        }
    }

    @Override
    protected int finishStep(ChatHash chatHash, AbsSender sender, String data) throws EntityNotFoundException {
        EEducationStatus eEducationStatus = EEducationStatus.valueOf(data);
        saveEducation(chatHash.getId(), eEducationStatus);
        sendFinishMessage(chatHash, sender, getAnswerMessageText(eEducationStatus.getEEducationStatusStr()));
        return 0;
    }

    private void saveEducation(long chatId, EEducationStatus eEducationStatus) throws EntityNotFoundException {
        Volunteer volunteer = volunteerService.getByChatId(chatId);
        volunteer.setEducationStatus(eEducationStatus);
        volunteerService.saveAndFlush(volunteer);
    }

    private String getAnswerMessageText(String answer) {
        return "Ваш ответ: <b>".concat(answer).concat("</b>");
    }

    private List<ButtonDto> getEducationStatusButtonDtoList() {
        EEducationStatus[] eEducationStatusArray = EEducationStatus.values();
        List<ButtonDto> buttonDtoList = new ArrayList<>();
        for (int i = 0; i < eEducationStatusArray.length; i++) {
            buttonDtoList.add(new ButtonDto(
                    eEducationStatusArray[i].toString(),
                    eEducationStatusArray[i].getEEducationStatusStr(),
                    i
            ));
        }

        return buttonDtoList;
    }
}
