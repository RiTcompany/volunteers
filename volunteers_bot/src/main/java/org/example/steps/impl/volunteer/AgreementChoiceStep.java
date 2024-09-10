package org.example.steps.impl.volunteer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.dto.MessageDto;
import org.example.dto.ResultDto;
import org.example.entities.ChatHash;
import org.example.enums.EMessage;
import org.example.mappers.KeyboardMapper;
import org.example.steps.ChoiceStep;
import org.example.utils.ButtonUtil;
import org.example.utils.StepUtil;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.bots.AbsSender;

@Slf4j
@Component
@RequiredArgsConstructor
public class AgreementChoiceStep extends ChoiceStep {
    private final KeyboardMapper keyboardMapper;
    private static final String PREPARE_MESSAGE_TEXT = """
            На данном шаге от вас требуются следующие шаги:
                1) Ознакомьтесь с нашей политикой конфиденциальности: https://волонтёрыпобеды.рф/policy
                2) Нажмите кнопку ОК для соглашения с данной политикой, а также для получения уведомлений через бота""";
    private static final String ANSWER_MESSAGE_TEXT = "Ваше согласие принято";

    @Override
    public void prepare(ChatHash chatHash, AbsSender sender) {
        StepUtil.sendPrepareMessageWithInlineKeyBoard(
                chatHash,
                PREPARE_MESSAGE_TEXT,
                keyboardMapper.keyboardDto(chatHash, ButtonUtil.okButtonList()),
                sender
        );
    }

    @Override
    protected ResultDto isValidData(MessageDto messageDto) {
        if (!isValidData(messageDto.getEMessage(), messageDto.getData())) {
            return new ResultDto(false, EXCEPTION_MESSAGE_TEXT);
        }

        return new ResultDto(true);
    }

    @Override
    protected int finishStep(ChatHash chatHash, AbsSender sender, String data) {
        sendFinishMessage(chatHash, sender, ANSWER_MESSAGE_TEXT);
        return 0;
    }

    private boolean isValidData(EMessage eMessage, String data) {
        boolean isCallback = isCallback(eMessage);
        boolean isCorrectChoice = ButtonUtil.OK_ANSWER.equals(data);
        return isCallback && isCorrectChoice;
    }
}
