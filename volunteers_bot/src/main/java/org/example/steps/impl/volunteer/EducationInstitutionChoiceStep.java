package org.example.steps.impl.volunteer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.enums.EEducationStatus;
import org.example.exceptions.EntityNotFoundException;
import org.example.pojo.dto.ButtonDto;
import org.example.pojo.dto.ResultDto;
import org.example.pojo.entities.ChatHash;
import org.example.pojo.entities.Volunteer;
import org.example.services.VolunteerService;
import org.example.steps.ChoiceStep;
import org.example.utils.ButtonUtil;
import org.example.utils.EducationUtil;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.bots.AbsSender;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class EducationInstitutionChoiceStep extends ChoiceStep {
    private final VolunteerService volunteerService;
    private static final String PREPARE_MESSAGE_TEXT = "Укажите ваше <b>учебное заведение</b>:";

    @Override
    public void prepare(ChatHash chatHash, AbsSender sender) throws EntityNotFoundException {
        setButtonList(chatHash.getId());
        super.prepare(chatHash, sender);
    }

    @Override
    protected String getPrepareMessageText() {
        return PREPARE_MESSAGE_TEXT;
    }

    @Override
    protected ResultDto isValidData(String data) {
//        List<String> educationInstitutionList = EducationUtil.getEducationInstitutionList(getEEducationStatus());
//        if (!educationInstitutionList.contains(data)) {
//            return new ResultDto(false, EXCEPTION_MESSAGE_TEXT);
//        }
//        TODO : сделать проверку, когда решим, как хранить учебные заведения

        return new ResultDto(true);
    }

    @Override
    protected int finishStep(ChatHash chatHash, AbsSender sender, String data) throws EntityNotFoundException {
        sendFinishMessage(chatHash, sender, getAnswerMessageText(data));
        if (EducationUtil.OTHER_ANSWER.equals(data)) {
            return 0;
        }

        saveEducationInstitution(chatHash.getId(), data);
        return 1;
    }

    protected void setButtonList(long chatId) throws EntityNotFoundException {
        EEducationStatus eEducationStatus = volunteerService.getByChatId(chatId).getEducationStatus();
        List<ButtonDto> buttonDtoList = ButtonUtil.educationInstitutionButtonList(eEducationStatus);
        setButtonDtoList(buttonDtoList);
    }

    private void saveEducationInstitution(long chatId, String educationInstitution) throws EntityNotFoundException {
        Volunteer volunteer = volunteerService.getByChatId(chatId);
        volunteer.setEducationInstitution(educationInstitution);
        volunteerService.saveAndFlush(volunteer);
    }

    private String getAnswerMessageText(String answer) {
        return "Ваше учебное заведение: <b>".concat(answer).concat("</b>");
    }
}
