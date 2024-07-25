package org.example.steps.impl.volunteer;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.example.enums.EAgreement;
import org.example.pojo.dto.ResultDto;
import org.example.pojo.entities.ChatHash;
import org.example.steps.ChoiceStep;
import org.example.utils.ButtonUtil;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.bots.AbsSender;

@Slf4j
@Component
// TODO : не вызывается кнопка ДА
public class AgreementChoiceStep extends ChoiceStep {
    private static final String PREPARE_MESSAGE_TEXT = """
            На данном шаге от вас требуются следующие шаги:
                1) Ознакомьтесь с нашей политикой конфиденциальности: https://волонтёрыпобеды.рф/policy
                2) Нажмите кнопку ДА для соглашения с данной политикой, а так же для получения уведомлений через бота""";
    private static final String ANSWER_MESSAGE_TEXT = "Ваше согласие принято";

    @PostConstruct
    public void init() {
        setButtonDtoList(ButtonUtil.agrrementButtonList());
    }

    @Override
    protected String getPrepareMessageText() {
        return PREPARE_MESSAGE_TEXT;
    }

    @Override
    protected ResultDto isValidData(String data) {
        if (!EAgreement.YES.toString().equals(data)) {
            return new ResultDto(false, EXCEPTION_MESSAGE_TEXT);
        }

        return new ResultDto(true);
    }

    @Override
    protected int finishStep(ChatHash chatHash, AbsSender sender, String data) {
        sendFinishMessage(chatHash, sender, ANSWER_MESSAGE_TEXT);
        return 0;
    }
}
