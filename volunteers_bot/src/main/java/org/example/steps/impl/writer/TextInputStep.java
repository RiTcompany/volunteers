package org.example.steps.impl.writer;

import lombok.RequiredArgsConstructor;
import org.example.dto.ResultDto;
import org.example.entities.ChatHash;
import org.example.enums.ERole;
import org.example.exceptions.EntityNotFoundException;
import org.example.services.BotMessageService;
import org.example.services.UserService;
import org.example.steps.InputStep;
import org.example.utils.MessageUtil;
import org.example.utils.ValidUtil;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.bots.AbsSender;

@Component
@RequiredArgsConstructor
public class TextInputStep extends InputStep {
    private final BotMessageService botMessageService;
    private final UserService userService;
    private static final String PREPARE_MESSAGE_TEXT = "Введите текст, который необходимо отобразить в сообщении";
    private static final String ANSWER_MESSAGE_TEXT = "Текст добавлен";

    @Override
    public void prepare(ChatHash chatHash, AbsSender sender) throws EntityNotFoundException {
        MessageUtil.sendMessageText(PREPARE_MESSAGE_TEXT, chatHash.getId(), sender);
    }

    @Override
    protected int finishStep(ChatHash chatHash, AbsSender sender, String data) throws EntityNotFoundException {
        saveMessageText(chatHash.getId(), data);
        MessageUtil.sendMessageText(ANSWER_MESSAGE_TEXT, chatHash.getId(), sender);
        return 0;
    }

    @Override
    protected ResultDto isValidData(String data) {
        if (ValidUtil.isLongBotMessage(data)) {
            String exceptionMessage = ValidUtil.getLongMessageExceptionText(ValidUtil.MAX_BOT_MESSAGE_LENGTH);
            return new ResultDto(false, exceptionMessage);
        }

        return new ResultDto(true);
    }

    private void saveMessageText(long chatId, String data) throws EntityNotFoundException {
        long userId = userService.getByChatIdAndRole(chatId, ERole.ROLE_WRITER).getId();
        botMessageService.saveText(userId, data);
    }
}
