package org.example.steps.impl.writer;

import lombok.RequiredArgsConstructor;
import org.example.dto.ResultDto;
import org.example.entities.ChatHash;
import org.example.enums.ERole;
import org.example.exceptions.EntityNotFoundException;
import org.example.services.BotMessageService;
import org.example.services.BotUserService;
import org.example.steps.InputStep;
import org.example.utils.MessageUtil;
import org.example.utils.ValidUtil;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.bots.AbsSender;

@Component
@RequiredArgsConstructor
public class ButtonInputStep extends InputStep {
    private final BotMessageService botMessageService;
    private final BotUserService botUserService;
    private static final String PREPARE_MESSAGE_TEXT = """
            1) Введите текст для кнопки
            2) Введите перенос на другую строку
            3) Введите ссылку""";
    private static final String ANSWER_MESSAGE_TEXT = "Кнопка добавлена";

    @Override
    public void prepare(ChatHash chatHash, AbsSender sender) throws EntityNotFoundException {
        MessageUtil.sendMessageText(chatHash.getId(), PREPARE_MESSAGE_TEXT, sender);
    }

    @Override
    protected int finishStep(ChatHash chatHash, AbsSender sender, String data) throws EntityNotFoundException {
        createButton(chatHash.getId(), data);
        MessageUtil.sendMessageText(chatHash.getId(), ANSWER_MESSAGE_TEXT, sender);
        return 0;
    }

    @Override
    protected ResultDto isValidData(String data) {
        String[] dataPartArray = data.split("\n");

        if (dataPartArray.length != 2) {
            return new ResultDto(false, "Вы неверно ввели данные\n".concat(PREPARE_MESSAGE_TEXT));
        }

        if (ValidUtil.isLongButtonText(dataPartArray[0].length())) {
            return new ResultDto(false, ValidUtil.getLongMessageExceptionText(ValidUtil.MAX_BOT_MESSAGE_LENGTH));
        }

        if (!ValidUtil.isValidURL(dataPartArray[1])) {
            return new ResultDto(false, "Некорректная ссылка\n".concat(PREPARE_MESSAGE_TEXT));
        }

        return new ResultDto(true);
    }

    private void createButton(long chatId, String data) throws EntityNotFoundException {
        long userId = botUserService.getByChatIdAndRole(chatId, ERole.ROLE_WRITER).getId();

        String[] dataPartArray = data.split("\n");
        String buttonName = dataPartArray[0];
        String buttonLink = dataPartArray[1];

        botMessageService.createButton(userId, buttonName, buttonLink);
    }
}
