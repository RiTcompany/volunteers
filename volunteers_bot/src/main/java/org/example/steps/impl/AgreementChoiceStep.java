package org.example.steps.impl;

import lombok.extern.slf4j.Slf4j;
import org.example.enums.EAgreement;
import org.example.pojo.dto.ButtonDto;
import org.example.pojo.dto.MessageDto;
import org.example.pojo.entities.ChatHash;
import org.example.steps.ChoiceStep;
import org.example.utils.ButtonUtil;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.bots.AbsSender;

import java.util.List;

@Slf4j
@Component
public class AgreementChoiceStep extends ChoiceStep {
    private static final String PREPARE_MESSAGE_TEXT = """
            На данном шаге от вас требуются следующие шаги:
                1) Ознакомьтесь с нашей политикой конфиденциальности: https://волонтёрыпобеды.рф/policy
                2) Нажмите кнопку ДА для соглашения с данной политикой, а так же для получения уведомлений через бота""";
    private static final String ANSWER_MESSAGE_TEXT = "Ваше согласие принято";
    private final static List<ButtonDto> buttonDtoList;

    static {
        buttonDtoList = ButtonUtil.agrrementButtonList();
    }

    @Override
    public int execute(ChatHash chatHash, MessageDto messageDto, AbsSender sender) {
        int stepIndex = super.execute(chatHash, messageDto, sender);
        if (stepIndex != 0) return stepIndex;

        String response = messageDto.getData();
        if (isPositiveResponse(response)) {
            finishStep(chatHash, sender, ANSWER_MESSAGE_TEXT);
            return 0;
        }

        log.error("Chat ID={} Incorrect response choice: {}", chatHash.getId(), response);
        return handleIllegalUserAction(messageDto, sender, EXCEPTION_MESSAGE_TEXT);
    }

    @Override
    protected String getPREPARE_MESSAGE_TEXT() {
        return PREPARE_MESSAGE_TEXT;
    }

    @Override
    protected void setButtonList() {
        setButtonDtoList(buttonDtoList);
    }

    private boolean isPositiveResponse(String response) {
        return EAgreement.YES.toString().equals(response);
    }
}
