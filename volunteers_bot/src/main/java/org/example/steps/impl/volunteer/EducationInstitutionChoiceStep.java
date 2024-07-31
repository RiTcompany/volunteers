package org.example.steps.impl.volunteer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.dto.ButtonDto;
import org.example.dto.MessageDto;
import org.example.dto.ResultDto;
import org.example.entities.ChatHash;
import org.example.entities.EducationInstitution;
import org.example.entities.Volunteer;
import org.example.enums.EEducationInstitution;
import org.example.enums.EEducationStatus;
import org.example.exceptions.EntityNotFoundException;
import org.example.repositories.EducationInstitutionRepository;
import org.example.services.VolunteerService;
import org.example.steps.ChoiceStep;
import org.example.utils.ButtonUtil;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.bots.AbsSender;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class EducationInstitutionChoiceStep extends ChoiceStep {
    private final VolunteerService volunteerService;
    private final EducationInstitutionRepository educationInstitutionRepository;
    private EEducationInstitution educationInstitution;
    private static final String PREPARE_MESSAGE_TEXT = "Укажите ваше <b>учебное заведение</b>:";
    private static final Map<EEducationStatus, EEducationInstitution> educationStatusToInstitution = new HashMap<>() {{
        put(EEducationStatus.STUDY_SCHOOL, EEducationInstitution.SCHOOL);
        put(EEducationStatus.STUDY_UNIVERSITY, EEducationInstitution.UNIVERSITY);
        put(EEducationStatus.FINISHED_UNIVERSITY, EEducationInstitution.UNIVERSITY);
        put(EEducationStatus.STUDY_SECONDARY_PROFESSIONAL, EEducationInstitution.SECONDARY_PROFESSIONAL);
        put(EEducationStatus.FINISHED_SECONDARY_PROFESSIONAL, EEducationInstitution.SECONDARY_PROFESSIONAL);
    }};

    @Override
    public void prepare(ChatHash chatHash, AbsSender sender) throws EntityNotFoundException {
        educationInstitution = getEEducationInstitution(chatHash.getId());
        setButtonList(educationInstitution);
        super.prepare(chatHash, sender);
    }

    @Override
    public int execute(ChatHash chatHash, MessageDto messageDto, AbsSender sender) throws EntityNotFoundException {
        educationInstitution = getEEducationInstitution(chatHash.getId());
        setButtonList(educationInstitution);
        return super.execute(chatHash, messageDto, sender);
    }

    @Override
    protected String getPrepareMessageText() {
        return PREPARE_MESSAGE_TEXT;
    }

    @Override
    protected ResultDto isValidData(String data) {
        if (!isCorrectAnswer(data)) {
            return new ResultDto(false, EXCEPTION_MESSAGE_TEXT);
        }

        return new ResultDto(true);
    }

    @Override
    protected int finishStep(ChatHash chatHash, AbsSender sender, String data) throws EntityNotFoundException {
        sendFinishMessage(chatHash, sender, getAnswerMessageText(data));
        if (ButtonUtil.OTHER_CHOICE.equals(data)) {
            return 0;
        }

        saveEducationInstitution(chatHash.getId(), data);
        return 1;
    }

    private void setButtonList(EEducationInstitution eEducationInstitution) {
        List<EducationInstitution> educationInstitutionList =
                educationInstitutionRepository.findAllByType(eEducationInstitution);
        setButtonDtoList(getButtonList(educationInstitutionList));
    }

    private EEducationInstitution getEEducationInstitution(long chatId) throws EntityNotFoundException {
        EEducationStatus eEducationStatus = volunteerService.getByChatId(chatId).getEducationStatus();
        return educationStatusToInstitution.get(eEducationStatus);
    }

    private void saveEducationInstitution(long chatId, String educationInstitution) throws EntityNotFoundException {
        Volunteer volunteer = volunteerService.getByChatId(chatId);
        volunteer.setEducationInstitution(educationInstitution);
        volunteerService.saveAndFlush(volunteer);
    }

    private String getAnswerMessageText(String answer) {
        return "Ваше учебное заведение: <b>".concat(answer).concat("</b>");
    }

    private List<ButtonDto> getButtonList(List<EducationInstitution> educationInstitutionList) {
        List<ButtonDto> buttonDtoList = new ArrayList<>();
        for (int i = 0; i < educationInstitutionList.size(); i++) {
            String name = educationInstitutionList.get(i).getId().toString();
            buttonDtoList.add(new ButtonDto(name, name, i));
        }

        ButtonUtil.addOtherChoice(buttonDtoList);
        return buttonDtoList;
    }

    private boolean isCorrectAnswer(String data) {
        boolean isOtherChoice = ButtonUtil.OTHER_CHOICE.equals(data);
//        boolean isCorrectChoice = educationInstitutionRepository.existsByNameAndType(data, educationInstitution);
        boolean isCorrectChoice = educationInstitutionRepository.existsByIdAndType(Integer.valueOf(data), educationInstitution);
        return isOtherChoice || isCorrectChoice;
    } // TODO: Нужно решить, что делать со слишком длинными надписями на кнопках
}
