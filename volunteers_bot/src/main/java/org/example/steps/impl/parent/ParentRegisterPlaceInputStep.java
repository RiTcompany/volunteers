package org.example.steps.impl.parent;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.exceptions.EntityNotFoundException;
import org.example.dto.ResultDto;
import org.example.entities.ChatHash;
import org.example.entities.Parent;
import org.example.services.ParentService;
import org.example.steps.InputStep;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.bots.AbsSender;

@Slf4j
@Component
@RequiredArgsConstructor
public class ParentRegisterPlaceInputStep extends InputStep {
    private final ParentService parentService;
    private static final String PREPARE_MESSAGE_TEXT = "Укажите ваше <b>место регистрации</b>:";

    @Override
    protected String getPrepareMessageText() {
        return PREPARE_MESSAGE_TEXT;
    }

    @Override
    protected ResultDto isValidData(String data) {
//        TODO : возможно нужна какая-то валидация
        return new ResultDto(true);
    }

    @Override
    protected void saveData(long chatId, String registerPlace) throws EntityNotFoundException {
        Parent parent = parentService.getByChatId(chatId);
        parent.setRegisterPlace(registerPlace);
        parentService.saveAndFlush(parent);
    }

    @Override
    protected int finishStep(ChatHash chatHash, AbsSender sender, String data) {
        sendFinishMessage(chatHash, sender, getAnswerMessageText(data));
        return 0;
    }

    private String getAnswerMessageText(String answer) {
        return "Ваше место регистрации: <b>".concat(answer).concat("</b>");
    }
}
