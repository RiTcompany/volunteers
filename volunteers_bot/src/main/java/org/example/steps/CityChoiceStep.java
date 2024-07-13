package org.example.steps;

import lombok.RequiredArgsConstructor;
import org.example.enums.ECity;
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

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@RequiredArgsConstructor
public class CityChoiceStep extends ConversationStep {
    private final VolonteerRepository volonteerRepository;
    private final String PREPARE_MESSAGE_TEXT = "Укажите ваш город: ";

    public void prepare(ChatHash chatHash, AbsSender sender) {
        SendMessage sendMessage = KeyboardUtil.createKeyboardBuilder(
                chatHash.getId(), getButtonList(), PREPARE_MESSAGE_TEXT
        ).build();
        int messageId = MessageUtil.sendMessage(sendMessage, sender);
        chatHash.setLastMessageId(messageId);
    }

    public EConversationStep execute(ChatHash chatHash, MessageDto messageDto, AbsSender sender) {
//        try {
//            switch (messageDto.getData()) {  // TODO : вынести в enum + разобрать ветвление
//                case "NOT_SPB" -> {
//                    return executeOtherCity(chatHash, sender);
//                }
//                case "SPB" -> {
//                    return executeDefaultCity(chatHash, messageDto, sender);
//                }
//                default -> {
//                    return executeIllegalWay(messageDto, sender);
//                }
//            }
//        }

        switch (messageDto.getData()) {  // TODO : вынести в enum + разобрать ветвление
            case "NOT_SPB" -> {
                return executeOtherCity(chatHash, sender);
            }
            case "SPB" -> {
                return executeDefaultCity(chatHash, messageDto, sender);
            }
            default -> {
                return executeIllegalWay(messageDto, sender);
            }
        }
    }

    private EConversationStep executeOtherCity(ChatHash chatHash, AbsSender sender) {
        finishStep(chatHash, sender);
        return eConversationStepList.get(0);
    }

    private EConversationStep executeDefaultCity(ChatHash chatHash, MessageDto messageDto, AbsSender sender) {
        saveDefaultCity(messageDto.getChatId());
        finishStep(chatHash, sender);
        return eConversationStepList.get(1);
    }

    private EConversationStep executeIllegalWay(MessageDto messageDto, AbsSender sender) {
        MessageUtil.sendMessage(
                "Выберите один из выше предложенных вариантов", messageDto.getChatId(), sender
        );
        return eConversationStepList.get(2);
    }

    private void saveDefaultCity(long chatId) {
        Volonteer volonteer = volonteerRepository.findByChatId(chatId).orElseGet(Volonteer::new);
        volonteer.setCity(ECity.SPB.getCityName());
        volonteerRepository.saveAndFlush(volonteer);
    }

    private void finishStep(ChatHash chatHash, AbsSender sender) {
        long chatId = chatHash.getId();
        int keyBoardMessageId = chatHash.getLastMessageId();
        KeyboardUtil.cleanKeyboard(chatId, keyBoardMessageId, sender);
    }

    private List<ButtonDto> getButtonList() {
        return List.of(
                new ButtonDto(ECity.SPB.getCityName(), ECity.SPB.toString(), 0),
                new ButtonDto(ECity.NOT_SPB.getCityName(), ECity.NOT_SPB.toString(), 1)
        );
    }
}
