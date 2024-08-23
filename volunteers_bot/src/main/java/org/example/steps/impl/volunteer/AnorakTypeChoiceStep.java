package org.example.steps.impl.volunteer;

import lombok.RequiredArgsConstructor;
import org.example.dto.ButtonDto;
import org.example.dto.MessageDto;
import org.example.dto.ResultDto;
import org.example.entities.ChatHash;
import org.example.enums.EAnorak;
import org.example.enums.ECity;
import org.example.exceptions.AbstractException;
import org.example.exceptions.EntityNotFoundException;
import org.example.mappers.KeyboardMapper;
import org.example.services.VolunteerService;
import org.example.steps.ChoiceStep;
import org.example.utils.StepUtil;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.bots.AbsSender;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class AnorakTypeChoiceStep extends ChoiceStep {
    private final KeyboardMapper keyboardMapper;
    private final VolunteerService volunteerService;
    private static final String PREPARE_MESSAGE_TEXT = "Выберите необходимый вам тип анорака";

    @Override
    public void prepare(ChatHash chatHash, AbsSender sender) throws AbstractException {
        StepUtil.sendPrepareMessageWithInlineKeyBoard(
                chatHash,
                PREPARE_MESSAGE_TEXT,
                keyboardMapper.keyboardDto(chatHash, getAnorakTypeButtonDtoList()),
                sender
        );
    }

    @Override
    protected ResultDto isValidData(MessageDto messageDto) throws EntityNotFoundException {
        if (!isCallback(messageDto.getEMessage())) {
            return new ResultDto(false, EXCEPTION_MESSAGE_TEXT);
        }

        try {
            EAnorak.valueOf(messageDto.getData());
            return new ResultDto(true);
        } catch (IllegalArgumentException ignored) {
        }

        return new ResultDto(false, EXCEPTION_MESSAGE_TEXT);
    }

    @Override
    protected int finishStep(ChatHash chatHash, AbsSender sender, String data) throws AbstractException {
        EAnorak eAnorak = EAnorak.valueOf(data);
        sendFinishMessage(chatHash, sender, getAnswerMessageText(eAnorak.getValue()));
        volunteerService.saveAnorakType(chatHash.getId(), eAnorak);
        return 0;
    }

    private String getAnswerMessageText(String answer) {
        return "У вас есть анорак: <b>".concat(answer.toLowerCase()).concat("</b>");
    }

    private List<ButtonDto> getAnorakTypeButtonDtoList() {
        EAnorak[] eAnorakArray = EAnorak.values();
        List<ButtonDto> buttonDtoList = new ArrayList<>();
        for (EAnorak eAnorak : eAnorakArray) {
            buttonDtoList.add(new ButtonDto(eAnorak.toString(), eAnorak.getValue()));
        }

        return buttonDtoList;
    }
}
