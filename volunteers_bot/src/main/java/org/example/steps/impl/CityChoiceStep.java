package org.example.steps.impl;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.enums.ECity;
import org.example.enums.EConversationStep;
import org.example.pojo.dto.ButtonDto;
import org.example.pojo.dto.MessageDto;
import org.example.pojo.entities.ChatHash;
import org.example.pojo.entities.Volonteer;
import org.example.services.VolonteerService;
import org.example.steps.ChoiceStep;
import org.example.utils.ButtonUtil;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.bots.AbsSender;

import java.util.List;

@Slf4j
@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@RequiredArgsConstructor
public class CityChoiceStep extends ChoiceStep {
    private final VolonteerService volonteerService;
    @Getter(AccessLevel.PROTECTED)
    private static final String PREPARE_MESSAGE_TEXT = "Укажите ваш <b>город</b>: ";
    private static final String DEFAULT_CITY_MESSAGE_TEXT = "Ваш город: <b>".concat(ECity.SPB.getCityStr()).concat("</b>");
    private static final String INPUT_CITY_MESSAGE_TEXT = "Ваш город: ожидается...";
    private final static List<ButtonDto> buttonDtoList;

    static {
        buttonDtoList = ButtonUtil.cityButtonList();
    }

    @Override
    public EConversationStep execute(ChatHash chatHash, MessageDto messageDto, AbsSender sender) {
        EConversationStep eConversationStep = super.execute(chatHash, messageDto, sender);
        if (eConversationStep != null) return eConversationStep;

        String city = messageDto.getData();
        try {
            ECity eCity = ECity.valueOf(city);
            return switch (eCity) {
                case OTHER -> executeOtherCity(chatHash, sender);
                case SPB -> executeDefaultCity(chatHash, sender);
            };
        } catch (IllegalArgumentException e) {
            log.error("Chat ID={} Incorrect city choice: {}", chatHash.getId(), city);
            return handleIllegalUserAction(chatHash, messageDto, sender, EXCEPTION_MESSAGE_TEXT);
        }
    }

    @Override
    protected void setButtonList(long chatId) {
        setButtonDtoList(buttonDtoList);
    }

    private EConversationStep executeOtherCity(ChatHash chatHash, AbsSender sender) {
        finishStep(chatHash, sender, INPUT_CITY_MESSAGE_TEXT);
        return eConversationStepList.get(0);
    }

    private EConversationStep executeDefaultCity(ChatHash chatHash, AbsSender sender) {
        saveDefaultCity(chatHash);
        finishStep(chatHash, sender, DEFAULT_CITY_MESSAGE_TEXT);
        return eConversationStepList.get(1);
    }

    private void saveDefaultCity(ChatHash chatHash) {
        Volonteer volonteer = volonteerService.getVolonteerByChatId(chatHash.getId());
        volonteer.setCity(ECity.SPB.getCityStr());
        volonteerService.saveAndFlushVolonteer(volonteer);
    }
}
