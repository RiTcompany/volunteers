package org.example.steps.impl.volunteer;

import lombok.RequiredArgsConstructor;
import org.example.dto.MessageDto;
import org.example.dto.ResultDto;
import org.example.entities.ChatHash;
import org.example.enums.EYesNo;
import org.example.exceptions.AbstractException;
import org.example.exceptions.EntityNotFoundException;
import org.example.mappers.KeyboardMapper;
import org.example.services.VolunteerService;
import org.example.steps.ChoiceStep;
import org.example.utils.ButtonUtil;
import org.example.utils.StepUtil;
import org.example.utils.ValidUtil;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.bots.AbsSender;

@Component
@RequiredArgsConstructor
public class AnorakExistChoiceStep extends ChoiceStep {
    private final KeyboardMapper keyboardMapper;
    private final VolunteerService volunteerService;
    private static final String PREPARE_MESSAGE_TEXT = "У вас есть анорак?";

    @Override
    public void prepare(ChatHash chatHash, AbsSender sender) throws AbstractException {
        StepUtil.sendPrepareMessageWithInlineKeyBoard(
                chatHash,
                PREPARE_MESSAGE_TEXT,
                keyboardMapper.keyboardDto(chatHash, ButtonUtil.yesNoButtonList()),
                sender
        );
    }

    @Override
    protected ResultDto isValidData(MessageDto messageDto) throws EntityNotFoundException {
        return ValidUtil.isValidYesNoChoice(messageDto, EXCEPTION_MESSAGE_TEXT);
    }


    @Override
    protected int finishStep(ChatHash chatHash, AbsSender sender, String data) throws AbstractException {
        if (EYesNo.NO.toString().equals(data)) {
            volunteerService.saveAnorakExists(chatHash.getId(), false);
            return 0;
        }

        volunteerService.saveAnorakExists(chatHash.getId(), true);
        return 1;
    }
}
