package org.example.steps.impl.writer;

import lombok.RequiredArgsConstructor;
import org.example.dto.ResultDto;
import org.example.entities.BotMessage;
import org.example.entities.BotMessageButton;
import org.example.entities.ChatHash;
import org.example.enums.ERole;
import org.example.exceptions.EntityNotFoundException;
import org.example.services.BotMessageButtonService;
import org.example.services.BotMessageService;
import org.example.services.UserService;
import org.example.steps.InputStep;
import org.example.utils.MessageUtil;
import org.example.utils.ValidUtil;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.bots.AbsSender;

@Component
@RequiredArgsConstructor
public class ButtonInputStep extends InputStep {
    private final BotMessageButtonService botMessageButtonService;
    private final BotMessageService botMessageService;
    private final UserService userService;
    private static final String PREPARE_MESSAGE_TEXT = """
            1) Введите текст для кнопки
            2) Введите перенос на другую строку
            3) Введите ссылку""";
    private static final String ANSWER_MESSAGE_TEXT = "Текст добавлен";

    @Override
    public void prepare(ChatHash chatHash, AbsSender sender) throws EntityNotFoundException {
        MessageUtil.sendMessageText(PREPARE_MESSAGE_TEXT, chatHash.getId(), sender);
    }

    @Override
    protected int finishStep(ChatHash chatHash, AbsSender sender, String data) throws EntityNotFoundException {
        MessageUtil.sendMessageText(ANSWER_MESSAGE_TEXT, chatHash.getId(), sender);
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

        return new ResultDto(true);
    }

    @Override
    protected void saveData(long chatId, String data) throws EntityNotFoundException {
        long userId = userService.getByChatIdAndRole(chatId, ERole.ROLE_WRITER).getId();
        BotMessage botMessage = botMessageService.getProcessedMessageByUserId(userId);

        String[] dataPartArray = data.split("\n");
        String buttonName = dataPartArray[0];
        String buttonLink = dataPartArray[1];

        botMessageButtonService.create(botMessage.getId(), buttonName, buttonLink);
    }
}
