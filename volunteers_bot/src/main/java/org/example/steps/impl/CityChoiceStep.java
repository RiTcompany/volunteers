package org.example.steps.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.enums.ECity;
import org.example.pojo.dto.ButtonDto;
import org.example.pojo.dto.MessageDto;
import org.example.pojo.entities.ChatHash;
import org.example.pojo.entities.Volonteer;
import org.example.services.VolonteerService;
import org.example.steps.ChoiceStep;
import org.example.utils.ButtonUtil;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.bots.AbsSender;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class CityChoiceStep extends ChoiceStep {
    private final VolonteerService volonteerService;
    private static final String PREPARE_MESSAGE_TEXT = "Укажите ваш <b>город</b>:";
    private final static List<ButtonDto> buttonDtoList;

    static {
        buttonDtoList = ButtonUtil.cityButtonList();
    }

    @Override
    public int execute(ChatHash chatHash, MessageDto messageDto, AbsSender sender) {
        int stepIndex = super.execute(chatHash, messageDto, sender);
        if (stepIndex != 0) return stepIndex;

        String city = messageDto.getData();
        try {
            ECity eCity = ECity.valueOf(city);
            return switch (eCity) {
                case OTHER -> executeOtherCity(chatHash, sender);
                default -> executeDefaultCity(chatHash, sender, eCity);
            };
        } catch (IllegalArgumentException e) {
            log.error("Chat ID={} Incorrect city choice: {}", chatHash.getId(), city);
            return handleIllegalUserAction(messageDto, sender, EXCEPTION_MESSAGE_TEXT);
        }
    }

    @Override
    protected void setButtonList() {
        setButtonDtoList(buttonDtoList);
    }

    @Override
    protected String getPREPARE_MESSAGE_TEXT() {
        return CityChoiceStep.PREPARE_MESSAGE_TEXT;
    }

    private int executeOtherCity(ChatHash chatHash, AbsSender sender) {
        finishStep(chatHash, sender, getAnswerMessageText("ожидается..."));
        return 0;
    }

    private int executeDefaultCity(ChatHash chatHash, AbsSender sender, ECity eCity) {
        saveDefaultCity(chatHash);
        finishStep(chatHash, sender, getAnswerMessageText(eCity.getCityStr()));
        return 1;
    }

    private void saveDefaultCity(ChatHash chatHash) {
        Volonteer volonteer = volonteerService.getVolonteerByChatId(chatHash.getId());
        volonteer.setCity(ECity.SPB.getCityStr());
        volonteerService.saveAndFlushVolonteer(volonteer);
    }

    private String getAnswerMessageText(String answer) {
        return "Ваш город: <b>".concat(answer).concat("</b>");
    }
}
