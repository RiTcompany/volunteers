package org.example.steps.impl.parent;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.dto.ResultDto;
import org.example.entities.ChatHash;
import org.example.exceptions.EntityNotFoundException;
import org.example.services.ParentService;
import org.example.steps.InputStep;
import org.example.utils.DateUtil;
import org.example.utils.StepUtil;
import org.example.utils.ValidUtil;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.bots.AbsSender;

@Slf4j
@Component
@RequiredArgsConstructor
public class ParentBirthdayInputStep extends InputStep {
    private final ParentService parentService;
    private static final String PREPARE_MESSAGE_TEXT = "Укажите вашу <b>дату рождения</b>:";

    @Override
    public void prepare(ChatHash chatHash, AbsSender sender) throws EntityNotFoundException {
        StepUtil.sendPrepareMessageOnlyText(chatHash, PREPARE_MESSAGE_TEXT, sender);
    }

    @Override
    protected ResultDto isValidData(String data) {
        return ValidUtil.isValidBirthday(data);
    }

    @Override
    protected int finishStep(ChatHash chatHash, AbsSender sender, String data) throws EntityNotFoundException {
        parentService.saveParentBirthday(chatHash.getId(), DateUtil.convertDate(data));
        sendFinishMessage(chatHash, sender, getAnswerMessageText(data));
        return 0;
    }

    private String getAnswerMessageText(String answer) {
        return "Ваша дата рождения: <b>".concat(answer).concat("</b>");
    }
}

