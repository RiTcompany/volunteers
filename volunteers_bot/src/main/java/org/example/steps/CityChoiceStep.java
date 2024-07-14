package org.example.steps;

import lombok.extern.slf4j.Slf4j;
import org.example.enums.ECity;
import org.example.mappers.ButtonMapper;
import org.example.pojo.dto.ButtonDto;
import org.example.pojo.dto.MessageDto;
import org.example.pojo.entities.ChatHash;
import org.example.pojo.entities.Volonteer;
import org.example.enums.EConversationStep;
import org.example.repositories.VolonteerRepository;
import org.example.utils.KeyboardUtil;
import org.example.utils.MessageUtil;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.bots.AbsSender;

import java.util.List;

@Slf4j
@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class CityChoiceStep extends VolonteerConversationStep {
    private final ButtonMapper buttonMapper;
    private final String PREPARE_MESSAGE_TEXT = "Укажите ваш город: ";
    private final String DEFAULT_CITY_MESSAGE_TEXT = "Ваш город: ".concat(ECity.SPB.getCityName());
    private final String INPUT_CITY_MESSAGE_TEXT = "Ваш город: ожидается...";
    private final String EXCEPTION_MESSAGE_TEXT = "Выберите один из выше предложенных вариантов";

    public CityChoiceStep(VolonteerRepository volonteerRepository, ButtonMapper buttonMapper) {
        super(volonteerRepository);
        this.buttonMapper = buttonMapper;
    }

    public void prepare(ChatHash chatHash, AbsSender sender) {
        SendMessage sendMessage = KeyboardUtil.createKeyboardBuilder(
                        chatHash.getId(),
                        getButtonList(),
                        PREPARE_MESSAGE_TEXT
                ).build();
        int messageId = MessageUtil.sendMessage(sendMessage, sender);
        chatHash.setPrevBotMessageId(messageId);
    }

    public EConversationStep execute(ChatHash chatHash, MessageDto messageDto, AbsSender sender) {
        String city = messageDto.getData();
        try {
            ECity eCity = ECity.valueOf(city);
            return switch (eCity) {
                case NOT_SPB -> executeOtherCity(chatHash, messageDto, sender);
                case SPB -> executeDefaultCity(chatHash, messageDto, sender);
            };
        } catch (IllegalArgumentException e) {
            log.error("Incorrect city choice: ".concat(city));
            return executeIllegalWay(chatHash, messageDto, sender, EXCEPTION_MESSAGE_TEXT);
        }
    }

    private EConversationStep executeOtherCity(ChatHash chatHash, MessageDto messageDto, AbsSender sender) {
        finishStep(chatHash, messageDto, sender, INPUT_CITY_MESSAGE_TEXT);
        return eConversationStepList.get(0);
    }

    private EConversationStep executeDefaultCity(ChatHash chatHash, MessageDto messageDto, AbsSender sender) {
        saveDefaultCity(messageDto.getChatId());
        finishStep(chatHash, messageDto, sender, DEFAULT_CITY_MESSAGE_TEXT);
        return eConversationStepList.get(1);
    }

    private void saveDefaultCity(long chatId) {
        Volonteer volonteer = findVolonteerByChatId(chatId);
        volonteer.setCity(ECity.SPB.getCityName());
        saveAndFlushVolonteer(volonteer);
    }

    private List<ButtonDto> getButtonList() {
        return List.of(
                buttonMapper.buttonDto(ECity.SPB, 0),
                buttonMapper.buttonDto(ECity.NOT_SPB, 1)
        );
    }
}
