package org.example.steps.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.enums.EEducationStatus;
import org.example.pojo.dto.ButtonDto;
import org.example.pojo.dto.MessageDto;
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
    private EEducationStatus eEducationStatus;

    @Override
    public void prepare(ChatHash chatHash, AbsSender sender) {
        collectEEducationStatus(chatHash.getId());
        super.prepare(chatHash, sender);
    }

    @Override
    public int execute(ChatHash chatHash, MessageDto messageDto, AbsSender sender) {
        collectEEducationStatus(chatHash.getId());
        int stepIndex = super.execute(chatHash, messageDto, sender);
        if (stepIndex != 0) return stepIndex;

        String educationInstitution = messageDto.getData();
        if (isValidEducationInstitution(educationInstitution, eEducationStatus)) {
            return switch (educationInstitution) {
                case EducationUtil.OTHER_ANSWER -> executeOtherChoice(chatHash, sender);
                default -> executeDefaultChoice(chatHash, sender, educationInstitution);
            };
        } else {
            log.error("Chat ID={} Incorrect gender choice: {}", chatHash.getId(), educationInstitution);
            return handleIllegalUserAction(messageDto, sender, EXCEPTION_MESSAGE_TEXT);
        }
    }

    @Override
    protected void setButtonList() {
        setButtonDtoList(getButtonDtoList(eEducationStatus));
    }

    @Override
    protected String getPREPARE_MESSAGE_TEXT() {
        return PREPARE_MESSAGE_TEXT;
    }

    private boolean isValidEducationInstitution(String educationInstitution, EEducationStatus eEducationStatus) {
        List<String> educationInstitutionList = EducationUtil.getEducationInstitutionList(eEducationStatus);
        return educationInstitutionList.contains(educationInstitution);
    }

    private int executeOtherChoice(ChatHash chatHash, AbsSender sender) {
        finishStep(chatHash, sender, getAnswerMessageText("ожидается..."));
        return 0;
    }

    private int executeDefaultChoice(ChatHash chatHash, AbsSender sender, String educationInstitution) {
        finishStep(chatHash, sender, getAnswerMessageText(educationInstitution));
        saveEducationInstitution(chatHash.getId(), educationInstitution);
        return 1;
    }

    private void saveEducationInstitution(long chatId, String educationInstitution) {
        Volunteer volunteer = volunteerService.getVolunteerByChatId(chatId);
        volunteer.setEducationInstitution(educationInstitution);
        volunteerService.saveAndFlushVolunteer(volunteer);
    }

    private List<ButtonDto> getButtonDtoList(EEducationStatus eEducationStatus) {
        return ButtonUtil.educationInstitutionButtonList(eEducationStatus);
    }

    private void collectEEducationStatus(long chatId) {
        if (eEducationStatus == null) {
            eEducationStatus = volunteerService.getVolunteerByChatId(chatId).getEducationStatus();
        }
    }

    private String getAnswerMessageText(String answer) {
        return "Ваше учебное заведение: <b>".concat(answer).concat("</b>");
    }
}
