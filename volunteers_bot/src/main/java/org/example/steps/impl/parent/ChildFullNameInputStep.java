package org.example.steps.impl.parent;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.dto.ResultDto;
import org.example.entities.ChatHash;
import org.example.entities.Parent;
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
public class ChildFullNameInputStep extends InputStep {
    private final ParentService parentService;
    private static final String PREPARE_MESSAGE_TEXT = "Введите <b>ФИО</b> вашего ребёнка полностью:";

    @Override
    public void prepare(ChatHash chatHash, AbsSender sender) throws EntityNotFoundException {
        StepUtil.sendPrepareMessage(chatHash, PREPARE_MESSAGE_TEXT, sender);
    }
    @Override
    protected ResultDto isValidData(String data) {
        return ValidUtil.isValidFullName(data);
    }

    @Override
    protected void saveData(long chatId, String fullName) throws EntityNotFoundException {
        Parent parent = parentService.getByChatId(chatId);
        parent.setChildFullName(fullName);
        parentService.saveAndFlush(parent);
    }

    @Override
    protected int finishStep(ChatHash chatHash, AbsSender sender, String data) {
        sendFinishMessage(chatHash, sender, getAnswerMessageText(data));
        return 0;
    }

    private String getAnswerMessageText(String answer) {
        return "ФИО вашего ребёнка: <b>".concat(answer).concat("</b>");
    }
}