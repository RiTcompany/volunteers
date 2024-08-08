package org.example.steps.impl.writer;

import lombok.RequiredArgsConstructor;
import org.example.dto.MessageDto;
import org.example.dto.ResultDto;
import org.example.entities.ChatHash;
import org.example.enums.EYesNo;
import org.example.exceptions.EntityNotFoundException;
import org.example.mappers.KeyboardMapper;
import org.example.steps.ChoiceStep;
import org.example.utils.ButtonUtil;
import org.example.utils.StepUtil;
import org.example.utils.ValidUtil;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.bots.AbsSender;

@Component
@RequiredArgsConstructor
public class ButtonAddChoiceStep extends ChoiceStep {
    private final KeyboardMapper keyboardMapper;
    private static final String PREPARE_MESSAGE_TEXT = "Вы хотите добавить кнопку-ссылку?";

    @Override
    protected ResultDto isValidData(MessageDto messageDto) throws EntityNotFoundException {
        return ValidUtil.isValidYesNoChoice(messageDto, EXCEPTION_MESSAGE_TEXT);
    }

    @Override
    public void prepare(ChatHash chatHash, AbsSender sender) throws EntityNotFoundException {
        StepUtil.sendPrepareMessageWithInlineKeyBoard(
                chatHash,
                PREPARE_MESSAGE_TEXT,
                keyboardMapper.keyboardDto(chatHash, ButtonUtil.yesNoButtonList()),
                sender
        );
    }

    @Override
    protected int finishStep(ChatHash chatHash, AbsSender sender, String data) throws EntityNotFoundException {
        if (EYesNo.YES.toString().equals(data)) {
            return 0;
        }

        return 1;
    }
}
