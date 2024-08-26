package org.example.steps.impl.volunteer;

import lombok.RequiredArgsConstructor;
import org.example.dto.ButtonDto;
import org.example.dto.KeyboardDto;
import org.example.dto.MessageDto;
import org.example.dto.ResultDto;
import org.example.entities.ChatHash;
import org.example.entities.EducationInstitution;
import org.example.entities.SpbDistrict;
import org.example.enums.EMessage;
import org.example.exceptions.AbstractException;
import org.example.exceptions.EntityNotFoundException;
import org.example.mappers.KeyboardMapper;
import org.example.repositories.SpbDistrictRepository;
import org.example.services.VolunteerService;
import org.example.steps.ChoiceStep;
import org.example.utils.ButtonUtil;
import org.example.utils.StepUtil;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.bots.AbsSender;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class SpbDistrictChoiceStep extends ChoiceStep {
    private final VolunteerService volunteerService;
    private final SpbDistrictRepository spbDistrictRepository;
    private final KeyboardMapper keyboardMapper;
    private static final String PREPARE_MESSAGE_TEXT = "В каком <b>районе Санкт-Петербурга</b> вы бы предпочли заниматься волонтёрской деятельностью в первую очередь:";

    @Override
    public void prepare(ChatHash chatHash, AbsSender sender) throws AbstractException {
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
        if (!isCorrectAnswer(messageDto.getData(), messageDto.getEMessage())) {
            return new ResultDto(false, EXCEPTION_MESSAGE_TEXT);
        }

        return new ResultDto(true);
    }

    @Override
    protected int finishStep(ChatHash chatHash, AbsSender sender, String data) throws AbstractException {
        sendFinishMessage(chatHash, sender, getAnswerMessageText(data));
        volunteerService.saveSpbDistrict(chatHash.getId(), data);
        return 0;
    }

    private String getAnswerMessageText(String answer) {
        return "Ваш район: <b>".concat(answer).concat("</b>");
    }

    private KeyboardDto keyboardDto(ChatHash chatHash) {
        return keyboardMapper.keyboardDto(chatHash, getButtonList(
                spbDistrictRepository.findAll().stream().map(SpbDistrict::getName).toList()
        ));
    }

    private List<ButtonDto> getButtonList(List<String> spbDistrictNameList) {
        List<ButtonDto> buttonDtoList = new ArrayList<>();
        for (String name : spbDistrictNameList) {
            buttonDtoList.add(new ButtonDto(name, name));
        }

        return buttonDtoList;
    }

    private boolean isCorrectAnswer(String data, EMessage eMessage) throws EntityNotFoundException {
        boolean isCallback = isCallback(eMessage);
        boolean isCorrectChoice = spbDistrictRepository.existsByName(data);
        return isCallback && isCorrectChoice;
    }
}
