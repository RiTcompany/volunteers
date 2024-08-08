package org.example.steps.impl.parent;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.dto.ResultDto;
import org.example.entities.ChatHash;
import org.example.exceptions.EntityNotFoundException;
import org.example.services.ParentService;
import org.example.steps.InputStep;
import org.example.utils.StepUtil;
import org.example.utils.ValidUtil;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.bots.AbsSender;

@Slf4j
@Component
@RequiredArgsConstructor
public class ChildRegisterPlaceInputStep extends InputStep {
    private final ParentService parentService;
    private static final String PREPARE_MESSAGE_TEXT = "Укажите <b>место регистрации</b> вашего ребёнка:";
    private static final int MAX_FULL_NAME_LENGTH = 511;

    @Override
    public void prepare(ChatHash chatHash, AbsSender sender) throws EntityNotFoundException {
        StepUtil.sendPrepareMessageOnlyText(chatHash, PREPARE_MESSAGE_TEXT, sender);
    }

    @Override
    protected ResultDto isValidData(String data) {
        if (ValidUtil.isLongRegisterPlace(data)) {
            return new ResultDto(false, "Слишком длинные данные. Введи сокращённую версию");
        }

        return new ResultDto(true);
    }

    @Override
    protected int finishStep(ChatHash chatHash, AbsSender sender, String data) throws EntityNotFoundException {
        parentService.saveChildRegisterPlace(chatHash.getId(), data);
        sendFinishMessage(chatHash, sender, getAnswerMessageText(data));
        return 0;
    }

    private String getAnswerMessageText(String answer) {
        return "Место регистрации вашего ребёнка: <b>".concat(answer).concat("</b>");
    }
}
