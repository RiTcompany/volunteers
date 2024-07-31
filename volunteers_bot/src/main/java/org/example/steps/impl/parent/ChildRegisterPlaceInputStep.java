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
public class ChildRegisterPlaceInputStep extends InputStep {
    private final ParentService parentService;
    private static final String PREPARE_MESSAGE_TEXT = "Укажите <b>место регистрации</b> вашего ребёнка:";

    @Override
    protected String getPrepareMessageText() {
        return PREPARE_MESSAGE_TEXT;
    }

    @Override
    protected ResultDto isValidData(String data) {
        return new ResultDto(true); // TODO : валидация на длину
    }

    @Override
    protected void saveData(long chatId, String registerPlace) throws EntityNotFoundException {
        Parent parent = parentService.getByChatId(chatId);
        parent.setChildRegisterPlace(registerPlace);
        parentService.saveAndFlush(parent);
    }

    @Override
    protected int finishStep(ChatHash chatHash, AbsSender sender, String data) {
        sendFinishMessage(chatHash, sender, getAnswerMessageText(data));
        return 0;
    }

    private String getAnswerMessageText(String answer) {
        return "Место регистрации вашего ребёнка: <b>".concat(answer).concat("</b>");
    }
}