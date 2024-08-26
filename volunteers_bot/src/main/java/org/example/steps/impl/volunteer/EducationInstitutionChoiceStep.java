package org.example.steps.impl.volunteer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.dto.ButtonDto;
import org.example.dto.KeyboardDto;
import org.example.dto.MessageDto;
import org.example.dto.ResultDto;
import org.example.entities.ChatHash;
import org.example.entities.EducationInstitution;
import org.example.enums.EEducationInstitution;
import org.example.enums.EEducationStatus;
import org.example.enums.EMessage;
import org.example.exceptions.EntityNotFoundException;
import org.example.mappers.KeyboardMapper;
import org.example.repositories.EducationInstitutionRepository;
import org.example.services.VolunteerService;
import org.example.steps.ChoiceStep;
import org.example.utils.ButtonUtil;
import org.example.utils.EducationUtil;
import org.example.utils.StepUtil;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.bots.AbsSender;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class EducationInstitutionChoiceStep extends ChoiceStep {
    private final VolunteerService volunteerService;
    private final EducationInstitutionRepository educationInstitutionRepository;
    private final KeyboardMapper keyboardMapper;
    private static final String PREPARE_MESSAGE_TEXT = "Укажите ваше <b>учебное заведение</b>:";

    @Override
    public void prepare(ChatHash chatHash, AbsSender sender) throws EntityNotFoundException {
        StepUtil.sendPrepareMessageWithPageableKeyBoard(
                chatHash, PREPARE_MESSAGE_TEXT, keyboardDto(chatHash), sender
        );
    }

    @Override
    public int execute(ChatHash chatHash, MessageDto messageDto, AbsSender sender) throws EntityNotFoundException {
        KeyboardDto keyboardDto = keyboardDto(chatHash);
        if (StepUtil.isMovePageAction(chatHash, messageDto, keyboardDto, sender)) {
            return -1;
        }

        return super.execute(chatHash, messageDto, sender);
    }

    @Override
    protected ResultDto isValidData(MessageDto messageDto) throws EntityNotFoundException {
        if (!isCorrectAnswer(messageDto.getData(), messageDto.getChatId(), messageDto.getEMessage())) {
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

        volunteerService.saveEducationInstitution(chatHash.getId(), data);

        if (hasSpeciality(chatHash.getId())) {
            return 1;
        }

        return 2;
    }

    private EEducationInstitution getType(long chatId) throws EntityNotFoundException {
        EEducationStatus eEducationStatus = volunteerService.getByChatId(chatId).getEducationStatus();
        return EducationUtil.getInstitutionType(eEducationStatus);
    }

    private String getAnswerMessageText(String answer) {
        return "Ваше учебное заведение: <b>".concat(answer).concat("</b>");
    }

    private KeyboardDto keyboardDto(ChatHash chatHash) {
        return keyboardMapper.keyboardDto(chatHash, getButtonList(
                educationInstitutionRepository.findAllByType(getType(chatHash.getId()))
        ));
    }

    private List<ButtonDto> getButtonList(List<EducationInstitution> educationInstitutionList) {
        List<ButtonDto> buttonDtoList = new ArrayList<>();
        for (EducationInstitution educationInstitution : educationInstitutionList) {
            String name = educationInstitution.getShortName();
            buttonDtoList.add(new ButtonDto(name, name));
        }

        ButtonUtil.addOtherChoice(buttonDtoList);
        return buttonDtoList;
    }

    private boolean isCorrectAnswer(String data, long chatId, EMessage eMessage) throws EntityNotFoundException {
        boolean isCallback = isCallback(eMessage);
        boolean isOtherChoice = ButtonUtil.OTHER_CHOICE.equals(data);
        boolean isCorrectChoice = educationInstitutionRepository.existsByShortNameAndType(data, getType(chatId));
        return isCallback && (isOtherChoice || isCorrectChoice);
    }

    private boolean hasSpeciality(long chatId) {
        EEducationStatus educationStatus = volunteerService.getByChatId(chatId).getEducationStatus();
        boolean finishSecondaryProfessional = EEducationStatus.FINISHED_SECONDARY_PROFESSIONAL.equals(educationStatus);
        boolean finishUniversity = EEducationStatus.FINISHED_UNIVERSITY.equals(educationStatus);
        return finishSecondaryProfessional || finishUniversity;
    }
}
