package org.example.steps.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.pojo.dto.MessageDto;
import org.example.pojo.dto.ResultDto;
import org.example.pojo.entities.ChatHash;
import org.example.services.VolonteerService;
import org.example.steps.InputStep;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.bots.AbsSender;

@Slf4j
@Component
@RequiredArgsConstructor
public class VkInputStep extends InputStep {
    private final VolonteerService volonteerService;
    private static final String PREPARE_MESSAGE_TEXT = "Введите ссылку на ваш <b>Профиль ВКонтакте</b>:";

    @Override
    public int execute(ChatHash chatHash, MessageDto messageDto, AbsSender sender) {
        String vk = messageDto.getData();
        ResultDto result = setVk(chatHash.getId(), vk);
        if (result.isDone()) {
            finishStep(chatHash, sender, getAnswerMessageText(vk));
            return 0;
        }

        return handleIllegalUserAction(messageDto, sender, result.getMessage());
    }

    @Override
    protected String getPREPARE_MESSAGE_TEXT() {
        return PREPARE_MESSAGE_TEXT;
    }

    private ResultDto setVk(long chatId, String reason) {
//        TODO : будем ли проверить верность ссылки ???
//        Можно проверить по http запросу, можно просто по регулярке

        saveVk(chatId, reason);
        return new ResultDto(true);
    }

    private void saveVk(long chatId, String vk) {
//        Volonteer volonteer = volonteerService.getVolonteerByChatId(chatId);
//        volonteer.setVk(vk);
//        volonteerService.saveAndFlushVolonteer(volonteer);
    }

    private String getAnswerMessageText(String vk) {
        return "Ваша ссылка: <b>".concat(vk).concat("</b>");
    }
}
