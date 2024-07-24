package org.example.steps.impl.parent;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.pojo.dto.ResultDto;
import org.example.pojo.entities.ChatHash;
import org.example.pojo.entities.Parent;
import org.example.services.ParentService;
import org.example.steps.InputStep;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.bots.AbsSender;

@Slf4j
@Component
@RequiredArgsConstructor
public class ParentFullNameInputStep extends InputStep {
    private final ParentService parentService;
    private static final String PREPARE_MESSAGE_TEXT = "Введите ваше <b>ФИО</b> полностью:";
    private static final int MAX_FULL_NAME_LENGTH = 100;

    @Override
    protected String getPrepareMessageText() {
        return PREPARE_MESSAGE_TEXT;
    }

    @Override
    protected ResultDto isValidData(String data) {
        if (isLongFullName(data)) {
            return new ResultDto(false, "Ваше ФИО слишком длинное, по нашим данным такого существовать не может");
        }

        return new ResultDto(true);
    }

    @Override
    protected void saveData(long chatId, String fullName) {
        Parent parent = parentService.getByChatId(chatId);
        parent.setFullName(fullName);
        parentService.saveAndFlush(parent);
    }

    @Override
    protected int finishStep(ChatHash chatHash, AbsSender sender, String data) {
        saveData(chatHash.getId(), data);
        cleanPreviousMessage(chatHash, sender, getAnswerMessageText(data));
        return 0;
    }

    private String getAnswerMessageText(String answer) {
        return "Ваше ФИО: <b>".concat(answer).concat("</b>");
    }

    private boolean isLongFullName(String fullName) {
        // TODO : возможно стоит добавить проверка на 1 или 2 пробела (если нет отчества)
        return fullName.length() > MAX_FULL_NAME_LENGTH;
    }
}